package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.source.models.enums.Gender;
import project.source.models.enums.UserStatus;

import java.time.LocalDate;
import java.util.*;



@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseEntity<Long> implements UserDetails {
    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "phone", unique = true)
    String phone;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "password")
    String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    UserStatus status;

//    @ManyToOne
//    @JoinColumn(name = "role_id", nullable = false)
//    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return role != null ? List.of(() -> role.getName().name()) : List.of();
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(status);
    }
}
