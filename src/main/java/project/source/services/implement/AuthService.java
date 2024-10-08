package project.source.services.implement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.source.exceptions.InvalidDataException;
import project.source.models.entities.Token;
import project.source.models.entities.User;
import project.source.models.enums.TokenType;


import project.source.requests.RefreshRequest;
import project.source.requests.ResetPasswordRequest;
import project.source.requests.SignInRequest;
import project.source.respones.TokenResponse;
import project.source.services.IAuthService;

import static org.springframework.http.HttpHeaders.REFERER;



@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService {
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    UserService userService;
    JwtService jwtService;

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        User user = userService.getByUsername(signInRequest.getUsername());
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new InvalidDataException("Wrong password");
        }

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.save(Token.builder().username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .userId(user.getId())
                .role(user.getRole().getName())
                .build();
    }


    public TokenResponse refreshToken(RefreshRequest refreshRequest) {
        final String refreshToken = refreshRequest.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        User user = userService.getByUsername(userName);
        log.info(user.getUsername());
        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allowed access with this token");
        }

        String accessToken = jwtService.generateToken(user);

        tokenService.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }


    public void removeToken(RefreshRequest refreshRequest) {
        final String refreshToken = refreshRequest.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }

        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        tokenService.delete(userName);
    }

    public String forgotPassword(String username) {
        User user = userService.getByUsername(username);
        String resetToken = jwtService.generateResetToken(user);
        tokenService.save(Token.builder()
                .username(user.getUsername())
                .resetToken(resetToken)
                .build());
        return resetToken;
    }


    public void changePassword(ResetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }
        User user = validateToken(request.getResetKey());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);
    }


    private User validateToken(String token) {
        String userName = jwtService.extractUsername(token, TokenType.RESET_TOKEN);
        User user = userService.getByUsername(userName);
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }
        return user;
    }
}
