package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turn {
    private Feedback feedback;

    public Turn() {
        this.feedback = null;
    }

    public Feedback getFeedback() { return feedback; }

    public Feedback takeGuess(String word, String guess) {
        if (guess == null || word == null) throw new IllegalArgumentException("Word and Guess cannot be null");
        List<Mark> marks;
        if (isGuessAndWordSameLength(word, guess)){
            marks = generateMarks(word, guess);
        }else{
            marks = generateInvalidMarks(guess);
        }
        this.feedback = new Feedback(guess, marks);
        return this.feedback;
    }

    private boolean isGuessAndWordSameLength(String word, String guess) {
        return word.length() == guess.length();
    }

    private List<Mark> generateMarks(String guess, String word) {
        List<Mark> marks = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        for (int index = 0; index < wordChars.length; index++){
            Mark mark = calculateMark(word, index, guess);
            marks.add(index, mark);
        }
        return marks;
    }

    private Mark calculateMark(String word, int index, String guess) {
        char[] guessChars = guess.toCharArray();
        if (isCharOnIndex(word, index, guessChars[index])){
            return Mark.CORRECT;
        }else if(isCharacterContainedWord(word, guessChars[index])){
            return Mark.PRESENT;
        }else {
            return Mark.ABSENT;
        }
    }

    private boolean isCharOnIndex(String word, int index, char character) {
        return word.charAt(index) == character;
    }

    private boolean isCharacterContainedWord(String word, char character) {
        return word.contains(String.valueOf(character));
    }

    private List<Mark> generateInvalidMarks(String guess) {
        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < guess.toCharArray().length; i++){
            marks.add(Mark.INVALID);
        }
        return marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return Objects.equals(feedback, turn.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedback);
    }
}
