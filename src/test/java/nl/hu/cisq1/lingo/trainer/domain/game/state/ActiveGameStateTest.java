package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.Score;
import nl.hu.cisq1.lingo.trainer.domain.Turn;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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
        GameState state = new ActiveGameState();
        assertThrows(
                IllegalGameStateException.class,
                () -> state.startNewRound("woord", game)
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
    @DisplayName("Throw when get latest hint with no existing round")
    void throwOnGetHintWithNoROund() {
        Game game = new Game(
                UUID.randomUUID(),
                Score.empty(),
                new ActiveGameState(),
                new ArrayList<>(),
                new DefaultWordLengthStrategy(5)
        );
        assertThrows(
                NotFoundException.class,
                () -> new ActiveGameState().latestHint(game)

        );
    }

    @Test
    @DisplayName("guess word correct")
    void gameActiveStateGuessWordCorrect() {
        assertDoesNotThrow(
                () -> {
                    Hint hint = new ActiveGameState().guessWord(game, "woord");
                    assertNotNull(hint);
                }
        );
    }

    private static Stream<Arguments> provideWrongGuesses() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("09876"),
                Arguments.of("12345"),
                Arguments.of("-1")
        );
    }
    @ParameterizedTest
    @MethodSource("provideWrongGuesses")
    @DisplayName("Guess with invalid strings and throw")
    void guessWithInvalidStrings(String guess) {
        GameState state = new ActiveGameState();
        assertThrows(
                IllegalArgumentException.class,
                () -> state.guessWord(game, guess)
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
        GameState state = new ActiveGameState();
        assertThrows(
                IllegalArgumentException.class,
                () -> state.guessWord(game, null)
        );
    }
}