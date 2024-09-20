package project.source.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.source.models.enums.Status;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;
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

    UserDTO changeStatus(long userId);

    void deleteUser(long userId);

    Page<User> getAllUsers(PageRequest request);

    long saveUser(User user);
}
