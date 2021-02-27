package nl.hu.cisq1.lingo.trainer.domain;

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
    private  Turn turn= new Turn();

    @BeforeEach
    void setUp() {
        this.turn = new Turn();
    }

    static Stream<Arguments> provideTurnGuesses() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        "woord"
                ),Arguments.of(
                        "woord",
                        "marge"
                ),Arguments.of(
                        "woord",
                        "waden"
                ),Arguments.of(
                        "woorde",
                        "woorde"
                ),Arguments.of(
                        "woorde",
                        "marges"
                ),Arguments.of(
                        "woorde",
                        "wadden"
                ),Arguments.of(
                        "woorden",
                        "woorden"
                ),Arguments.of(
                        "woorden",
                        "magiers"
                ),Arguments.of(
                        "woorden",
                        "waddens"
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideTurnGuesses")
    @DisplayName("Make correct and non invalid guesses")
    void makeNonInvalidGuesses(String word, String guess) {
        turn.takeGuess(word, guess);
        assertNotNull(turn.getFeedback());
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
        turn.takeGuess(word, guess);
        assertEquals(
                turn.getFeedback().marks,
                expected
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