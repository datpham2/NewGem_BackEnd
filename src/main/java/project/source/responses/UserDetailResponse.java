package project.source.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.source.enums.Gender;
import project.source.enums.UserStatus;

import java.util.Date;

@Getter
@Builder
public class UserDetailResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Date dateOfBirth;

    private Gender gender;

    private String username;

    private String type;

    private UserStatus status;
}
