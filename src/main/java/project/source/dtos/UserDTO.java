package project.source.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import project.source.models.entities.Role;
import project.source.models.entities.User;
import project.source.models.enums.Gender;
import project.source.models.enums.UserStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


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


//    @PhoneNumber(message = "phone invalid format")
    String phone;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    //@Pattern(regexp = "^male|female|other$", message = "gender must be one in {male, female, other}")
//    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    Gender gender;

    @NotBlank(message = "username must be not null")
    String username;

    @NotNull(message = "password must be not null")
    @Size(min = 5, message = "password must be have at least 5 characters")
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
