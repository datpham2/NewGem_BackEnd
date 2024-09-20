package project.source.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.source.models.enums.TokenType;

@Service
public interface IJwtService {
    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String generateResetToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails user);
}
