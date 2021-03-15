package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.InvalidFeedbackException;

import java.util.List;

public class Feedback {
    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        if (attempt.length() != marks.size()) throw new InvalidFeedbackException("Guess and marks lengths do not match");
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return marks.stream()
                .allMatch( mark -> mark == Mark.CORRECT);
    }

    public Hint giveHint(Hint previousHint) {
        return Hint.of(previousHint, attempt, marks);
    }

    public List<Mark> getMarks() { return marks; }
}
