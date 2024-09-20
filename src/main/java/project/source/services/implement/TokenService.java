package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Token;
import project.source.repositories.TokenRepository;



@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TokenService {
    TokenRepository tokenRepository;

    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Token not found for username: " + username));
    }

    public long save(Token token) {
        return tokenRepository.findByUsername(token.getUsername())
                .map(existingToken -> {
                    existingToken.setAccessToken(token.getAccessToken());
                    existingToken.setRefreshToken(token.getRefreshToken());
                    return tokenRepository.save(existingToken).getId();
                })
                .orElseGet(() -> tokenRepository.save(token).getId());
    }

    public void delete(String username) {
        if (tokenRepository.existsByUsername(username)) {
            tokenRepository.deleteByUsername(username);
        } else {
            throw new NotFoundException("Token not found for username: " + username);
        }
    }
}
