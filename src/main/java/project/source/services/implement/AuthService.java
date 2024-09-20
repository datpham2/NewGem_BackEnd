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

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.save(Token.builder().username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }


    public TokenResponse refreshToken(HttpServletRequest request) {


        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        var user = userService.getByUsername(userName);
        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
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
                .build();
    }

    public String removeToken(HttpServletRequest request) {


        final String token = request.getHeader(REFERER);
        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("Token must be not blank");
        }

        final String userName = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        tokenService.delete(userName);

        return "Removed!";
    }

    public String forgotPassword(String username) {

        User user = userService.getByUsername(username);

        String resetToken = jwtService.generateResetToken(user);

        tokenService.save(Token.builder().username(user.getUsername()).resetToken(resetToken).build());

        String confirmLink = String.format("curl --location 'http://localhost:8080/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);
        return resetToken;
    }

    public String resetPassword(String secretKey) {

        var user = validateToken(secretKey);

        tokenService.getByUsername(user.getUsername());

        return "Reset";
    }

    public String changePassword(ResetPasswordRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }

        User user = validateToken(request.getSecretKey());


        user.setPassword(passwordEncoder.encode(request.getPassword()));
        long id = userService.saveUser(user);

        log.info(String.valueOf(id));

        return "Changed";
    }


    private User validateToken(String token) {
        var userName = jwtService.extractUsername(token, TokenType.RESET_TOKEN);

        var user = userService.getByUsername(userName);
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        return user;
    }
}