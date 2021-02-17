package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {
    @Test
    @DisplayName("Hint.of() makes only first letter visible if invalid")
    void hintFirstLetterVisibleWhenInvalid() {
        Hint hint = Hint.of("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(hint.toString().contains(List.of('w', '.', '.', '.', '.').toString()));
    }

    @Test
    @DisplayName("Hint.of() makes non-first letters not visible if invalid")
    void hintNonFirstLettersNotVisibleWhenInvalid() {
        Hint hint = Hint.of("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(!hint.toString().contains(List.of('.', 'o', 'o', 'r', 'd').toString()));
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', 'o', '.', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', 'o', 'o', 'r', 'd'))
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Hint.of() creates correct hint objects")
    void hintOfCreatesCorrectHint(String guess, List<Mark> marks, Hint expectedHint) {
        Hint hint = Hint.of(guess, marks);
        assertEquals(expectedHint, hint);
    }

    static Stream<Arguments> provideWrongHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT),
                        new Hint(List.of('.', 'o', 'o', 'r', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', 'o', 'o', 'r', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('.', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('x', 'i', 'x', 'f', 'x'))
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideWrongHintExamples")
    @DisplayName("Hint.of() does not creates wrong hint objects")
    void wrongHint(String guess, List<Mark> marks, Hint wrongHint) {
        Hint hint = Hint.of(guess, marks);
        assertNotEquals(wrongHint, hint);
    }

    static Stream<Arguments> provideUnequalLengthsHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of()
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT)
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideUnequalLengthsHintExamples")
    @DisplayName("Hint.of() throws error if length of word is not equal to length of marks")
    void unequalLengthWordMarksHint(String guess, List<Mark> marks) {
        assertThrows(
                InvalidHintException.class,
                () -> Hint.of(guess, marks)
        );
    }

    static Stream<Arguments> provideEqualLengthsHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woorde",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woorden",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideEqualLengthsHintExamples")
    @DisplayName("Hint.of() does not throws error if length of word is equal to length of marks")
    void equalLengthWordMarksHint(String guess, List<Mark> marks) {
        assertDoesNotThrow(
                () -> Hint.of(guess, marks)
        );
    }
}