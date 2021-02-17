package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackTest {

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
        Feedback feedback = new Feedback("woordd", List.of(Mark. CORRECT, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertFalse(feedback.isWordValid());
    }

    @Test
    @DisplayName("Guess is not invalid if guess length is same as word length")
    void guessIsNotInvalid() {
        Feedback feedback = new Feedback("woord", List.of(Mark. CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT));
        assertTrue(feedback.isWordValid());
    }
}