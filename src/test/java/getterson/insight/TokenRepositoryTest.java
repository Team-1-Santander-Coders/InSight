package getterson.insight;

import getterson.insight.entities.TokenEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.entities.types.UserType;
import getterson.insight.repositories.TokenRepository;
import getterson.insight.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        tokenRepository.deleteAll();

        UserEntity user1 = userRepository.save(new UserEntity(1L, "teste1", "teste1", "56931515063", LocalDate.now(), "a@a.com", "12345678@Aa", "27999999999", UserType.FISICA));
        UserEntity user2 = userRepository.save(new UserEntity(2L, "teste1", "teste1", "54018341016", LocalDate.now(), "b@b.com", "12asdsda78@Aa", "27999529999", UserType.FISICA));

        TokenEntity token1 = new TokenEntity("abc123", Instant.now().plusSeconds(3600), user1);
        tokenRepository.save(token1);

        TokenEntity token2 = new TokenEntity("def456", Instant.now().plusSeconds(7200), user2);
        tokenRepository.save(token2);
    }

    @Test
    void tokenExiste_buscoPorToken_ObtenhoToken() {
        Optional<TokenEntity> resultado = tokenRepository.findByToken("abc123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getToken()).isEqualTo("abc123");
        assertThat(resultado.get().getUser().getId()).isEqualTo(1L);
    }

    @Test
    void tokenNaoExiste_buscoPorToken_NaoObtenhoToken() {
        Optional<TokenEntity> resultado = tokenRepository.findByToken("xyz789");

        assertThat(resultado).isNotPresent();
    }
}
