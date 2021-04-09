package nl.hu.cisq1.lingo.trainer.domain.turn;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {
    private Turn turn= new Turn();

    @BeforeEach
    void setUp() {
        this.turn = new Turn(null);
    }

    static Stream<Arguments> provideTurnGuesses() {
        return Stream.of(
                Arguments.of(
                        "whord",
                        "woord",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),Arguments.of(
                        "woord",
                        "woord",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),Arguments.of(
                        "woord",
                        "marge",
                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)
                ),Arguments.of(
                        "woord",
                        "waden",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)
                ),Arguments.of(
                        "woorde",
                        "woorde",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),Arguments.of(
                        "woorde",
                        "marges",
                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT)
                ),Arguments.of(
                        "woorde",
                        "wadden",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT)
                ),Arguments.of(
                        "woorden",
                        "woorden",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),Arguments.of(
                        "woorden",
                        "magiers",
                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT)
                ),Arguments.of(
                        "woorden",
                        "waddens",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT)
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideTurnGuesses")
    @DisplayName("Make correct and non invalid guesses")
    void makeNonInvalidGuesses(String word, String guess, List<Mark> marks) {
        turn.takeGuess(word, guess);
        assertNotNull(turn.getFeedback());
        assertEquals(marks, turn.getFeedback().getMarks());
    }

    static Stream<Arguments> provideInvalidSizedTurnGuesses() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        "woorde",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)
                ),Arguments.of(
                        "woord",
                        "marg",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)
                ),Arguments.of(
                        "woord",
                        "",
                        List.of()
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideInvalidSizedTurnGuesses")
    @DisplayName("Invalid guesses return Turn with feedback with INVALID marks")
    void makeInvalidGuesses(String word, String guess, List<Mark> expected) {
        Feedback feedback = turn.takeGuess(word, guess);
        assertEquals(
                expected,
               feedback.getMarks()
        );
    }

    @Test
    @DisplayName("Make null as guess")
    void nullAsGuess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> turn.takeGuess("woord", null)
        );
    }

    @Test
    @DisplayName("Make guess while word is null")
    void nullAsWord() {
        assertThrows(
                IllegalArgumentException.class,
                () -> turn.takeGuess(null, "guess")
        );
    }
}