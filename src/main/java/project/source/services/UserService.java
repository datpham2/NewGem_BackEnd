package project.source.services;

import lombok.RequiredArgsConstructor;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.User;
import project.source.repositories.UserRepository;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

//    public User getUserById(Long id) {
//        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
//    }


}
