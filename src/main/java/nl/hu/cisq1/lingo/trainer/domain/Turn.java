package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Mark> marks = chooseMarksGeneration(word, guess);
        this.feedback = new Feedback(guess, marks);
        return this.feedback;
    }

    private List<Mark> chooseMarksGeneration(String word, String guess) {
        if (isGuessAndWordSameLength(word, guess)) return generateMarks(word, guess);
        return generateInvalidMarks(guess);
    }

    private boolean isGuessAndWordSameLength(String word, String guess) {
        return word.length() == guess.length();
    }

    private List<Mark> generateMarks(String word, String guess) {
        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < word.toCharArray().length; i++) marks.add(Mark.ABSENT);
        char[] guessArray = guess.toCharArray();
        for (int i = 0; i < guessArray.length; i++) {
            int characterIndex = word.indexOf(guessArray[i]);
            if (characterIndex == i) {
                marks.set(i, Mark.CORRECT);
                word = word.replaceFirst(String.valueOf(guessArray[i]), ".");
            }
        }
        for (int i = 0; i < guessArray.length; i++) {
            int characterIndex = word.indexOf(guessArray[i]);
            if (characterIndex != -1 && marks.get(i) != Mark.CORRECT) {
                marks.set(i, Mark.PRESENT);
                word = word.replaceFirst(String.valueOf(guessArray[i]), ".");
            }
        }
        return marks;
    }

    private List<Mark> generateInvalidMarks(String guess) {
        return IntStream.range(0, guess.length())
                .boxed()
                .map(integer -> Mark.INVALID)
                .collect(Collectors.toList());
    }
}
