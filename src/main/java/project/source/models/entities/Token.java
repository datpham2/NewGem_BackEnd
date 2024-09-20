package project.source.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "token")
public class Token extends BaseEntity<Long> {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "username", unique = true)
    String username;

    @Column(name = "access_token")
    String accessToken;

    @Column(name = "refresh_token")
    String refreshToken;

    @Column(name = "reset_token")
    String resetToken;
}
