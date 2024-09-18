package project.source.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.source.models.enums.UserStatus;
import project.source.models.entities.User;
import project.source.dtos.UserDTO;
import project.source.respones.PageResponse;



@Service
public interface UserService {
    UserDetailsService userDetailsService();

    User addUser(UserDTO request);

    User getUser(long userId);

    UserDTO updateUser(long userId, UserDTO request);

    UserDTO changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);
}
