package nl.hu.cisq1.lingo.trainer.domain.feedback;

import nl.hu.cisq1.lingo.trainer.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

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
                        new Hint(List.of('w', '.', '*', '*', 'd'))
                ),
                Arguments.of(
                        "woord",
                        List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT),
                        new Hint(List.of('w', '.', '.', '.', 'd')),
                        new Hint(List.of('w', '.', 'o', '*', 'd'))
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
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark. CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Guess is invalid if guess length is not same as word length")
    void guessIsInvalid() {
        List<Mark> marks = List.of(Mark.CORRECT);
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", marks)
        );
    }

    @Test
    @DisplayName("Guess is not invalid if guess length is same as word length")
    void guessIsNotInvalid() {
        assertDoesNotThrow(
                () -> new Feedback("woord", List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT))
        );
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Hint given by feedback is correct hint")
    void correctHint(String guess, List<Mark> marks, Hint previousHint, Hint expectedHint) {
        Feedback feedback = new Feedback(guess, marks);
        assertEquals(expectedHint, feedback.giveHint(previousHint));
    }
}