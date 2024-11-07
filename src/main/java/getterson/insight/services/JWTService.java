package getterson.insight.services;

import getterson.insight.dtos.TokenResult;
import getterson.insight.entities.TokenEntity;
import getterson.insight.exceptions.InvalidTokenException;
import getterson.insight.repositories.TokenRepository;
import getterson.insight.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

@Service
public class JWTService {
    private final TokenRepository tokenRepository;

    public JWTService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.saveAndFlush(tokenEntity);
    }

    public TokenResult getToken(String token) throws InvalidTokenException {
        Optional<TokenEntity> tokenEntityOptional = tokenRepository.findByToken(token);

        if (tokenEntityOptional.isPresent()) {
            TokenEntity tokenEntity = tokenEntityOptional.get();
            return new TokenResult(tokenEntity, isExpired(tokenEntity));
        }

        throw new InvalidTokenException("Token inválido.");
    }

    public boolean isExpired(TokenEntity tokenEntity) {
        return tokenEntity.getExpirationDate().isBefore(Instant.now());
    }
}
