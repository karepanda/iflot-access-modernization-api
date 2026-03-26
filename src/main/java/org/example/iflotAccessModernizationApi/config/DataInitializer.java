package org.example.iflotAccessModernizationApi.config;

import jakarta.transaction.Transactional;
import org.example.iflotAccessModernizationApi.entity.Role;
import org.example.iflotAccessModernizationApi.entity.User;
import org.example.iflotAccessModernizationApi.repository.RoleRepository;
import org.example.iflotAccessModernizationApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args)  {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User("admin", "admin@iflot.local","admin123",true);

            adminUser.addRole(adminRole);
            adminUser.addRole(userRole);

            userRepository.save(adminUser);
            
            log.info("Seed data created: admin user + ADMIN/USER roles");
        }else {
            log.info("Admin user already exists, skipping seed data creation");
        }

        userRepository.findByUsernameWithRoles("admin").ifPresent(user -> {
            String roles = user.getRoles().stream()
                    .map(Role::getName)
                    .sorted()
                    .collect(Collectors.joining(", "));

            log.info("Persistence check OK -> user='{}', roles=[{}], active{}",
                    user.getUsername(),
                    roles,
                    user.isActive());
        });
    }
}
