package org.example.iflotAccessModernizationApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "app_users",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_app_user_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_app_user_email", columnNames = "email")
    }
)
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 225)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_user_roles_user")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_user_roles_role")
            )
    )
    private Set<Role> roles = new LinkedHashSet<>();

    public User(String username, String email, String password, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

}
