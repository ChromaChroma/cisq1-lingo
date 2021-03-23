package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActiveGameStateTest {
    private static Game game;

    @BeforeEach
    void setup() {
        List<Round> rounds = new ArrayList<>();
        rounds.add(Round.of("woord"));
        game = new Game(
                UUID.randomUUID(),
                Score.empty(),
                new ActiveGameState(),
                rounds,
                new DefaultWordLengthStrategy(5)
        );
    }

    @Test
    @DisplayName("active round throws when trying to create a new round")
    void startNewRoundThrows() {
        assertThrows(
                IllegalGameStateException.class,
                () -> new ActiveGameState().startNewRound("woord", game)
        );
    }

    @Test
    @DisplayName("get current turn")
    void gameActiveStateGetCurrentTurn() {
        assertDoesNotThrow(
                () -> {
                    Turn turn = new ActiveGameState().getCurrentTurn(game);
                    assertNull(turn.getFeedback());
                }
        );
    }

    @Test
    @DisplayName("get latest hint")
    void gameActiveStateGetLatestHint() {
        assertDoesNotThrow(
                () -> {
                    Hint hint = new ActiveGameState().latestHint(game);
                    assertNotNull(hint);
                }
        );
    }

    @Test
    @DisplayName("guess word correct")
    void gameActiveStateGuessWordCorrect() {

        assertDoesNotThrow(
                () -> {
                    Feedback feedback = new ActiveGameState().guessWord(game, "woord");
                    assertNotNull(feedback);
                }
        );
    }

    @Test
    @DisplayName("guess word wrong five times, then when game over throw")
    void gameActiveStateGuessWordWrongFiveTimes() {

        assertDoesNotThrow(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        new ActiveGameState().guessWord(game, "wrong");
                    }
                }
        );
        assertThrows(
                IllegalGameStateException.class,
                () -> game.guessWord( "wrong")
        );
    }

    @Test
    @DisplayName("guess word null")
    void gameActiveStateGuessWordNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ActiveGameState().guessWord(game, null)
        );
    }
}