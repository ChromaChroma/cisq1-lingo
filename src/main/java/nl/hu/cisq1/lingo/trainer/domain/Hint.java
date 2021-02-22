package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hint {
    private List<Character> hint;

    static Hint of(Hint previousHint, String word, List<Mark> marks) {
        checkHintAndMarksSize(previousHint, marks);
        checkWordAndMarksSize(word, marks);

        List<Character> resChars = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        for (int i = 0; i < previousHint.hint.size(); i ++) {
            String charString = previousHint.hint.get(i).toString();
            if (charString.matches("[a-zA-Z]+\\.?")){
                resChars.add(i, previousHint.hint.get(i));
            }else if (marks.get(i) == Mark.CORRECT){
                resChars.add(wordChars[i]);
            } else {
                resChars.add('.');
            }
        }
        return new Hint(resChars);
    }

    private static void checkHintAndMarksSize(Hint previousHint, List<Mark> marks) {
        if (previousHint.hint.size() != marks.size()) throw new InvalidHintException("Previous hint and marks arent the same size");
    }
    private static void checkWordAndMarksSize(String word, List<Mark> marks) {
        if (word.length() != marks.size()) throw new InvalidHintException("Word and marks arent the same size");
    }

    public Hint(List<Character> hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint1 = (Hint) o;
        return Objects.equals(hint, hint1.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hint);
    }

    @Override
    public String toString() {
        return "Hint{" +
                "hint=" + hint +
                '}';
    }
}
