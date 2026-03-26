package org.example.iflotAccessModernizationApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roles",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_name", columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new LinkedHashSet<>();

    public Role(String name) {
        this.name = name;
    }
}
