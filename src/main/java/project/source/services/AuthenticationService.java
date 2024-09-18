package project.source.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.source.models.enums.TokenType;
import project.source.exceptions.InvalidDataException;
import project.source.models.entities.Token;
import project.source.models.entities.User;
import project.source.requests.ResetPasswordRequest;
import project.source.requests.SignInRequest;
import project.source.respones.TokenResponse;

import static org.springframework.http.HttpHeaders.REFERER;
import static project.source.models.enums.TokenType.*;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;
    private final JwtService jwtService;

    public TokenResponse accessToken(SignInRequest signInRequest) {

        var user = userService.getByUsername(signInRequest.getUsername());
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        List<String> roles = userService.getAllRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword(), authorities));


        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);


        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

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


        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

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

        final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);

        tokenService.delete(userName);

        return "Removed!";
    }

    public String forgotPassword(String email) {
        User user = userService.getUserByEmail(email);

        String resetToken = jwtService.generateResetToken(user);


        tokenService.save(Token.builder().username(user.getUsername()).resetToken(resetToken).build());

        String confirmLink = String.format("curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);


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


        var user = validateToken(request.getSecretKey());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);

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
