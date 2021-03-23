package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
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
                () -> game.startNewRound("woord")
        );
    }

    @Test
    @DisplayName("get turn, find active round in list over completed rounds and throw")
    void gameActiveAndThrowWhenFindCurrentRoundInEmptyList() {
        for(Round round : game.getRounds()) {
            round.setState(RoundState.LOST);
        }
        assertThrows(
                NotFoundException.class,
                game::getCurrentTurn
        );
    }

    @Test
    @DisplayName("get current turn")
    void gameActiveStateGetCurrentTurn() {
        assertDoesNotThrow(
                () -> {
                    Turn turn = game.getCurrentTurn();
                    assertNull(turn.getFeedback());
                }
        );
    }

    @Test
    @DisplayName("get latest hint")
    void gameActiveStateGetLatestHint() {
        assertDoesNotThrow(
                () -> {
                    Hint hint = game.latestHint();
                    assertNotNull(hint);
                }
        );
    }

    @Test
    @DisplayName("guess word correct")
    void gameActiveStateGuessWordCorrect() {

        assertDoesNotThrow(
                () -> {
                    Feedback feedback = game.guessWord("woord");
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
                        game.guessWord("wrong");
                    }
                }
        );
        assertThrows(
                IllegalGameStateException.class,
                () -> game.guessWord("wrong")
        );
    }

    @Test
    @DisplayName("guess word null")
    void gameActiveStateGuessWordNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> game.guessWord(null)
        );
    }
}