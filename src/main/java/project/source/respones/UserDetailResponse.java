package project.source.respones;

import lombok.Builder;
import lombok.Getter;
import project.source.models.enums.Gender;
import project.source.models.enums.UserStatus;

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
