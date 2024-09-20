package project.source.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.UserDTO;
import project.source.models.entities.User;
import project.source.models.enums.Status;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("addUser")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                            .message("Validation error")
                            .data(errors)
                    .build());
        }

        User createdUser = userService.addUser(userDTO);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(UserDTO.fromUser(createdUser))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @GetMapping("getAllUser")
    public ResponseEntity<ApiResponse> getAllUser(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
            @RequestParam(name = "size", defaultValue = "10") @Min(value = 1, message = "Size must be greater than 0") int size) {

        PageRequest request = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsers(request);

        Set<UserDTO> userDTOS = users.getContent().stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toSet());

        PageResponse<Set<UserDTO>> pageResponse = PageResponse.<Set<UserDTO>>builder()
                .pageNo(users.getNumber())
                .pageSize(users.getSize())
                .totalPage(users.getTotalPages())
                .items(userDTOS)
                .build();

        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Users retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(pageResponse)
                .build());
    }



    @PutMapping("updateUser/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable long id, @Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Validation error")
                    .data(errors)
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

    @PatchMapping("changeStatus/{id}")
    public ResponseEntity<ApiResponse> updateUserStatus(@PathVariable Long id) {
        userService.changeStatus(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User status updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User disabled successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
