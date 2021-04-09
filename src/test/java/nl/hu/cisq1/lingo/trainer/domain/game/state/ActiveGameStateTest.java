package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.feedback.Hint;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.Score;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.word.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.round.Round;
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
                new DefaultWordLengthStrategy()
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
    void throwOnGetHintWithNoRound() {
        Game game = new Game(
                UUID.randomUUID(),
                Score.empty(),
                new ActiveGameState(),
                new ArrayList<>(),
                new DefaultWordLengthStrategy()
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

    @Test
    @DisplayName("guess word correct and points and rounds increase")
    void gameActiveStateGuessWordCorrectAndScoreIncrease() throws NotFoundException {
        int points = game.getScore().getPoints();
        int rounds = game.getScore().getRoundsPlayed();

        new ActiveGameState().guessWord(game, "woord");

        Score resScore = game.getScore();
        assertTrue(points < resScore.getPoints());
        assertEquals(rounds + 1, resScore.getRoundsPlayed());
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
    void gameActiveStateGuessWordWrongFiveTimesAndThrowsOnSixth() throws NotFoundException {
        for (int i = 0; i < 5; i++) {
            new ActiveGameState().guessWord(game, "wrong");
        }

        assertThrows(
                IllegalGameStateException.class,
                () -> game.guessWord("wrong")
        );
    }

    @Test
    @DisplayName("guess word wrong five times, then when game over throw")
    void gameActiveStateGuessWordWrongFiveTimesAndRoundsPlayedIncreaseAndScoreIsSame() throws NotFoundException {
        int rounds = game.getScore().getRoundsPlayed();
        for (int i = 0; i < 5; i++) {
            new ActiveGameState().guessWord(game, "wrong");
        }
        Score resScore = game.getScore();
        assertEquals(0, resScore.getPoints());
        assertEquals(rounds + 1, resScore.getRoundsPlayed());
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