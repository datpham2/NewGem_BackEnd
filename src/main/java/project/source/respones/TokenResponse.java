package project.source.respones;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.Role;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    String accessToken;

    String refreshToken;

    String username;

    Long userId;

    String role;
}
