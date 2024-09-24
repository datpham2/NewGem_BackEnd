package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.source.models.entities.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Token> findByUsername(String username);
}
