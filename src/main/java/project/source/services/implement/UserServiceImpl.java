package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.User;
import project.source.models.enums.UserStatus;
import project.source.repositories.UserRepository;
import project.source.dtos.UserDTO;
import project.source.respones.PageResponse;
import project.source.services.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User addUser(UserDTO userDTO) {
        User user = UserDTO.toUser(userDTO);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
        user.setPassword(userDTO.getPassword());
        user.setStatus(userDTO.getStatus());

        User updatedUser = userRepository.save(user);
        return UserDTO.fromUser(updatedUser);
    }

    @Override
    public UserDTO changeStatus(long userId, UserStatus status) {
        return null;
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
}
