package project.source.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import project.source.models.enums.UserStatus;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;
import project.source.respones.PageResponse;
import project.source.respones.UserDetailResponse;

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
