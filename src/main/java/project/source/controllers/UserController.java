package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.UserDTO;
import project.source.models.entities.User;
import project.source.respones.ApiResponse;
import project.source.services.UserService;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("addUser")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Validation error");

            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(400)
                    .message(errorMessage)
                    .build());
        }

        User createdUser = userService.addUser(userDTO);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(createdUser)
                .build();
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("getUser/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(UserDTO.fromUser(user))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable long id, @Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Validation error");

            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(errorMessage)
                    .build());
        }

        UserDTO updatedUser = userService.updateUser(id, userDTO);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully")
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
