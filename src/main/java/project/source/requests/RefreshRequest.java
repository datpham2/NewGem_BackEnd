package project.source.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshRequest {
    @NotNull(message = "refresh token must not be empty")
    String refreshToken;
}
