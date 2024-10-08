package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.source.requests.RefreshRequest;
import project.source.requests.ResetPasswordRequest;
import project.source.requests.SignInRequest;
import project.source.respones.ApiResponse;
import project.source.respones.TokenResponse;
import project.source.services.implement.AuthService;


@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Operation related to security and authentication")
public class AuthController {
    AuthService authService;

    @Operation(
            method = "POST",
            summary = "Sign in",
            description = "Sign in to receive access token to use other operations and refresh token to get new access token after expiration"
    )
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authService.signIn(signInRequest), HttpStatus.ACCEPTED);
    }


    @Operation(
            method = "POST",
            summary = "Refresh token",
            description = "Post the refresh token in the body to get a new access token")
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshRequest), HttpStatus.ACCEPTED);
    }


    @Operation(
            method = "POST",
            summary = "Remove token for logout",
            description = "Post the refresh token to remove the token from the db")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> removeToken(@RequestBody RefreshRequest refreshRequest) {
        authService.removeToken(refreshRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Log out successfully")
                        .build());
    }


    @Operation(
            method = "POST",
            summary = "Reset token",
            description = "Post the username to receive the reset token to renew password")
    @PostMapping("/forgotpassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody String username) {
        String resetToken = authService.forgotPassword(username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Reset token createad successfull")
                        .data(resetToken)
                        .build());
    }


    @Operation(
            method = "POST",
            summary = "Change password",
            description = "Post the reset token as secret key, new password and confirm password to change password, password and confirm password must be the same")
    @PostMapping("/changepassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.ACCEPTED.value())
                        .message("Password changed successfully")
                        .build());
    }

}
