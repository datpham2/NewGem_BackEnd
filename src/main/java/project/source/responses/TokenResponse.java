package project.source.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String accessToken;

    private String refreshToken;

    private Long userId;
}
