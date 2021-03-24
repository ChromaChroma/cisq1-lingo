package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.repository.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {
    private Game game;

    @Autowired
    private TrainerService service;

    @Autowired
    private SpringGameRepository repository;

    @BeforeEach
    void setup() {
        this.game = this.repository.save(Game.create());
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }


    @Test
    @DisplayName("Start a new game")
    void startNewGame() {
        assertDoesNotThrow(service::startNewGame);
    }

    @Test
    @DisplayName("Start a new round then try again and throw")
    void startNewRound() {
        UUID gameId = this.game.getId();
        assertDoesNotThrow(
                () -> service.startNewRound(gameId)
        );
        assertThrows(
                RuntimeException.class,
                () -> service.startNewRound(gameId)
        );
    }

    @Test
    @DisplayName("Guess the word")
    void guessTheWord() {
        UUID gameId = this.game.getId();
        assertDoesNotThrow(() -> service.startNewRound(gameId));
        assertDoesNotThrow(() -> service.guessWord(gameId, "woord"));
    }

    @Test
    @DisplayName("Get the current turn")
    void getCurrentTurn() {
        UUID gameId = this.game.getId();
        assertDoesNotThrow(() -> service.startNewRound(gameId));
        assertDoesNotThrow(() -> service.getCurrentTurn(gameId));
    }

    @Test
    @DisplayName("Get the latest hint")
    void getLatestHint() {
        UUID gameId = this.game.getId();
        assertDoesNotThrow(() -> service.startNewRound(gameId));
        assertDoesNotThrow(() -> service.getLatestHint(gameId));
    }
}