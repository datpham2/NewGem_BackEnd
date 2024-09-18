package project.source.services;


import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Token;
import project.source.repositories.TokenRepository;

import java.util.Optional;


@Service
public record TokenService(TokenRepository tokenRepository) {
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Not found token"));
    }

    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        Token savedToken;
        if (optional.isEmpty()) {
            savedToken = tokenRepository.save(token);
        } else {
            Token existingToken = optional.get();
            existingToken.setAccessToken(token.getAccessToken());
            existingToken.setRefreshToken(token.getRefreshToken());
            savedToken = tokenRepository.save(existingToken);
        }
        return Math.toIntExact(savedToken.getId());
    }



    public void delete(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }
}
