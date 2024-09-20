package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.source.exceptions.ExistedException;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Role;
import project.source.models.entities.User;
import project.source.models.enums.Status;
//import project.source.repositories.RoleRepository;
import project.source.repositories.RoleRepository;
import project.source.repositories.UserRepository;
import project.source.dtos.UserDTO;
import project.source.respones.PageResponse;
import project.source.services.IUserService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User addUser(UserDTO userDTO) {
        existed(userDTO);
        User user = UserDTO.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = roleRepository.findByName(Role.USER).orElseThrow(() -> new NotFoundException("Role not found"));
        user.setRole(role);
        return userRepository.save(user);
    }


    @Override
    public User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }


    @Override
    public UserDTO updateUser(long userId, UserDTO userDTO) {
        User user = getUser(userId);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setGender(userDTO.getGender());
        user.setUsername(userDTO.getUsername());

        User updatedUser = userRepository.save(user);
        updatedUser.setPassword(null);
        return UserDTO.fromUser(updatedUser);
    }

    @Override
    public UserDTO changeStatus(long userId, Status status) {
        User user = getUser(userId);
        user.setStatus(status);

        User updatedUser = userRepository.save(user);
        updatedUser.setPassword(null);
        return UserDTO.fromUser(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    @Override
    public PageResponse<?> getAllUsers(int pageNo, int pageSize) {
        // Implement pagination logic if required
        return null; // Placeholder for now
    }

    @Override
    public long saveUser(User user) {
        userRepository.save(user);
        return user.getId();
    }


    @Override
    public void emailExisted(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            throw new ExistedException("Email existed");
        }
    }

    @Override
    public void usernameExisted(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            throw new ExistedException("Username existed");
        }
    }

    @Override
    public void phoneExisted(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        if (user.isPresent()){
            throw new ExistedException("Phone existed");
        }
    }

    @Override
    public void existed(UserDTO userDTO) {
        usernameExisted(userDTO.getUsername());
        phoneExisted(userDTO.getPhone());
        emailExisted(userDTO.getEmail());
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User not found with username: " + username)
        );
    }


}
