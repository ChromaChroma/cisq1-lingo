package nl.hu.cisq1.lingo.trainer.data.repository;

import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringGameRepository extends JpaRepository<Game, UUID> {
}
