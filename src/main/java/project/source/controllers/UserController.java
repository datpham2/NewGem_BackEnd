package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.UserDTO;
import project.source.models.entities.User;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.MailService;
import project.source.services.implement.UserService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "Operations related to users")
public class UserController {
    UserService userService;
    MailService mailService;

    @Operation(
            method = "POST",
            summary = "Add new user",
            description = "Send a request via this API to create a new user"
    )
    @PostMapping("addUser")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
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
                .data(createdUser)
                .build();


        if (createdUser != null) {
            sendValidationEmail(createdUser);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            method = "GET",
            summary = "Get the user by id",
            description = "Send a request via this API to get the user with path variable id"
    )
    @GetMapping("getUser/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable long id) {
        User user = userService.getUserById(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(UserDTO.fromUser(user))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            method = "GET",
            summary = "Get all users",
            description = "Send a request via this API to retrieve all users"
    )
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

    @Operation(
            method = "PUT",
            summary = "Update user",
            description = "Send a request via this API to update user"
    )
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

    @Operation(
            method = "PATCH",
            summary = "Change status of user",
            description = "Send a request via this API to change the status of the user"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("changeStatus/{id}")
    public ResponseEntity<ApiResponse> updateUserStatus(@PathVariable Long id) {
        userService.changeStatus(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User status updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            method = "PATCH",
            summary = "Disable user",
            description = "Send a request via this API to disable user"
    )
    @PatchMapping("disableUser/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id) {
        userService.disableUser(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User disabled successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Confirm user", description = "Send a request via this API to confirm user")
    @GetMapping("/confirm/{userId}")
    public ResponseEntity<String> confirm(@Min(1) @PathVariable long userId, @RequestParam String verifyCode, HttpServletResponse response) throws IOException {
        log.info("Confirm user, userId={}, verifyCode={}", userId, verifyCode);
        try {
            userService.confirmUser(userId, verifyCode);
            return ResponseEntity.accepted().body("User has confirmed successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.badRequest().body("Confirm was failed");
        } finally {
            response.sendRedirect("http://localhost:3000/");
        }
    }



    private void sendValidationEmail(User user) throws MessagingException {
        String verifyCode = "12345";
        long userId = user.getId();
        String activationUrl = "http://localhost:8080/user/confirm/" + userId + "?verifyCode=" + verifyCode;

        mailService.sendEmail(
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                "confirm-email",
                activationUrl,
                verifyCode,
                "Account activation"
        );
    }
}
