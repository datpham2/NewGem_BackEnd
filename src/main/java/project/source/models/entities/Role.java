package project.source.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="roles")
@Builder
public class Role extends BaseEntity<Long>{
    @Column(nullable = false)

    private String name;

    public static String ADMIN = "ROLE_ADMIN";
    public static String USER = "ROLE_USER";
    public static String HOTEL = "ROLE_HOTEL";
}