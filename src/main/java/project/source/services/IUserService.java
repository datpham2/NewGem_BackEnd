package project.source.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.source.models.entities.Role;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;
import project.source.models.enums.Status;


@Service
public interface IUserService {
    UserDetailsService userDetailsService();

    User addUser(UserDTO userDTO);

    void confirmUser(Long userId, String verifyCode);

    User getUserById(long userId);

    void emailExisted(String email);

    void usernameExisted(String username);

    void phoneExisted(String phone);

    void existed(UserDTO userDTO);

    UserDTO updateUser(long userId, UserDTO userDTO);

    void changeStatus(long userId);

    void disableUser(long userId);

    Page<User> getAllUsers(PageRequest request);

    void saveUser(User user);

    Page<User> getActiveUsersWithoutAdmins(PageRequest request);

    Page<User> getInactiveUsersWithoutAdmins(PageRequest request);
}
