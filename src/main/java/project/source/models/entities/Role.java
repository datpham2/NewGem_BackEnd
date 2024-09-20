package project.source.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity<Long> {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    RoleType name;


    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<User> users;

    public enum RoleType {
        ROLE_ADMIN, ROLE_USER, ROLE_HOTEL
    }
}
