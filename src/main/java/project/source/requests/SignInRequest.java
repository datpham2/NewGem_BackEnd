package project.source.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class SignInRequest {
    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "username must be not blank")
    private String password;
}
