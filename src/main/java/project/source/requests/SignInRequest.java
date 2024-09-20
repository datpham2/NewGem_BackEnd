package project.source.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class SignInRequest {
    @NotBlank(message = "username must be not blank")
    private String username;

    @NotBlank(message = "username must be not blank")
    private String password;
}
