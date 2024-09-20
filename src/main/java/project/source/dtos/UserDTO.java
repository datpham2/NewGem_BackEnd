package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import project.source.dtos.validations.EnumPattern;
import project.source.dtos.validations.PhoneNumber;
import project.source.models.entities.User;
import project.source.models.enums.Gender;
import project.source.models.enums.UserStatus;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank(message = "firstName must be not blank")
    String firstName;

    @NotBlank(message = "lastName must be not null")
    String lastName;

    @Email(message = "Email invalid format")
    String email;

    @PhoneNumber(message = "Phone invalid format")
    String phone;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @EnumPattern(name = "Gender", regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE or OTHER")
    Gender gender;

    @NotBlank(message = "username must be not blank")
    String username;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Password must not be null")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one number, and one special character.")
    String password;


    UserStatus status;

    public static User toUser(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.firstName)
                .lastName(userDTO.lastName)
                .phone(userDTO.phone)
                .email(userDTO.email)
                .username(userDTO.username)
                .password(userDTO.password)
                .dateOfBirth(userDTO.dateOfBirth)
                .status(userDTO.status)
                .gender(userDTO.gender)
                .build();
    }

    public static UserDTO fromUser(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(user.getStatus())
                .build();
    }
}