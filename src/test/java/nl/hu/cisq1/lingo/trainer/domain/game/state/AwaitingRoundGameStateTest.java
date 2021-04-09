package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.Score;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.word.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AwaitingRoundGameStateTest {
    private static Game game;

    @BeforeEach
    void setup() {
        game = new Game(
                UUID.randomUUID(),
                Score.empty(),
                new AwaitingRoundGameState(),
                new ArrayList<>(),
                new DefaultWordLengthStrategy()
        );
    }

    @Test
    @DisplayName("Create new round when trying to create a new round")
    void startNewRound() {

        assertDoesNotThrow(
                () -> {
                    new AwaitingRoundGameState().startNewRound("woord", game);
                    assertNotNull(game.getCurrentRound());
                }
        );

    }

    @Test
    @DisplayName("get latest hint throws")
    void getLatestHintThrows() {
        GameState state = new AwaitingRoundGameState();
        assertThrows(
                IllegalGameStateException.class,
                () -> state.latestHint(game)
        );
    }

    @Test
    @DisplayName("guess word throws")
    void guessWordThrows() {
        GameState state = new AwaitingRoundGameState();
        assertThrows(
                IllegalGameStateException.class,
                () -> state.guessWord(game, "woord")
        );
    }
}