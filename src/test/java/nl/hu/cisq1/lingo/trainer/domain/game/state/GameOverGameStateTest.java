package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.Score;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameOverGameStateTest {
    private static Game game;

    @BeforeEach
    void setup() {
        game = new Game(
                UUID.randomUUID(),
                Score.empty(),
                new GameOverGameState(),
                new ArrayList<>(),
                new DefaultWordLengthStrategy(5)
        );
    }

    @Test
    @DisplayName("throws when trying to create a new round")
    void startNewRoundThrows() {
        assertThrows(
                IllegalGameStateException.class,
                () -> new GameOverGameState().startNewRound("woord", game)
        );
    }

    @Test
    @DisplayName("get current turn throws")
    void getCurrentTurnThrows() {
        assertThrows(
                IllegalGameStateException.class,
                () -> new GameOverGameState().getCurrentTurn(game)
        );
    }

    @Test
    @DisplayName("get latest hint throws")
    void getLatestHintThrows() {
        assertThrows(
                IllegalGameStateException.class,
                () -> new GameOverGameState().latestHint(game)
        );
    }

    @Test
    @DisplayName("guess word throws")
    void guessWordThrows() {
        assertThrows(
                IllegalGameStateException.class,
                () -> new GameOverGameState().guessWord(game, "woord")
        );
    }
}