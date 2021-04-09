package nl.hu.cisq1.lingo.trainer.domain.turn;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Mark;
import nl.hu.cisq1.lingo.trainer.domain.turn.strategy.marks.DefaultGenerateMarksStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "turn")
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private Feedback feedback;

    public Turn() {
    }

    public Turn(Feedback feedback) {
        this.feedback = feedback;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public Feedback takeGuess(String word, String guess) {
        if (guess == null || word == null) throw new IllegalArgumentException("Word and Guess cannot be null");
        List<Mark> marks = new DefaultGenerateMarksStrategy().generateMarks(word, guess);
        this.feedback = new Feedback(guess, marks);
        return this.feedback;
    }
}
