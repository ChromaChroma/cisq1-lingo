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

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(Mark. ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT),
                        new Hint(List.of('w', '.', '.', '.', '.')),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', 'd')),
                        new Hint(List.of('w', '.', '.', '.', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', 'd')),
                        new Hint(List.of('w', '.', 'o', '.', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.')),
                        new Hint(List.of('w', 'o', 'o', 'r', 'd'))
                )
        );
    }

    @Test
    @DisplayName("Hint.of() makes only first letter visible if invalid")
    void hintFirstLetterVisibleWhenInvalid() {
        Hint hint = Hint.of(new Hint(List.of('w', 'o', '.', '.', '.')), "woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(hint.toString().contains(List.of('w', 'o', '.', '.', '.').toString()));
    }


    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Hint.of() creates correct hint objects")
    void hintOfCreatesCorrectHint(String guess, List<Mark> marks, Hint previousHint, Hint expectedHint) {
        Hint hint = Hint.of(previousHint, guess, marks);
        assertEquals(expectedHint, hint);
    }

    static Stream<Arguments> provideUnequalLengthsHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woorde",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.', '.'))
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideUnequalLengthsHintExamples")
    @DisplayName("Hint.of() throws error if length of word is not equal to length of marks")
    void unequalLengthWordMarksHint(String guess, List<Mark> marks, Hint previousHint) {
        assertThrows(
                InvalidHintException.class,
                () -> Hint.of(previousHint, guess, marks)
        );
    }

    static Stream<Arguments> provideEqualLengthsHintExamples() {
        return Stream.of(
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woorde",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woorden",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', '.', '.', '.'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID),
                        new Hint(List.of('w', '.', '.', '.', '.'))
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideEqualLengthsHintExamples")
    @DisplayName("Hint.of() does not throws error if length of word is equal to length of marks")
    void equalLengthWordMarksHint(String guess, List<Mark> marks, Hint previousHint) {
        assertDoesNotThrow(
                () -> Hint.of(previousHint, guess, marks)
        );
    }
}