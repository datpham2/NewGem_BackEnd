package project.source.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.source.models.enums.UserStatus;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;
import project.source.requests.SignInRequest;
import project.source.respones.PageResponse;



@Service
public interface IUserService {
    UserDetailsService userDetailsService();

    User addUser(UserDTO userDTO);

    User getUser(long userId);

    void emailExisted(String email);

    void usernameExisted(String username);

    void phoneExisted(String phone);

    void existed(UserDTO userDTO);

    UserDTO updateUser(long userId, UserDTO userDTO);

    UserDTO changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    long saveUser(User user);
}
