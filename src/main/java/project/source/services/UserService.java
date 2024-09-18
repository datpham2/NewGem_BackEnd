package project.source.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import project.source.enums.UserStatus;
import project.source.models.User;
import project.source.requests.UserDTO;
import project.source.responses.PageResponse;
import project.source.responses.UserDetailResponse;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    User getByUsername(String userName);

    long saveUser(UserDTO request);

    long saveUser(User user);

    void updateUser(long userId, UserDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    UserDetailResponse getUser(long userId);

    List<String> getAllRolesByUserId(long userId);

    User getUserByEmail(String email);
}
