package project.source.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;


@Service
public interface IUserService {
    UserDetailsService userDetailsService();

    User addUser(UserDTO userDTO);

    User getUserById(long userId);

    void emailExisted(String email);

    void usernameExisted(String username);

    void phoneExisted(String phone);

    void existed(UserDTO userDTO);

    UserDTO updateUser(long userId, UserDTO userDTO);

    void changeStatus(long userId);

    void deleteUser(long userId);

    Page<User> getAllUsers(PageRequest request);

    void saveUser(User user);
}
