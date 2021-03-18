package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.InvalidHintException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hint {
    private List<Character> hint;

    static Hint of(Hint previousHint, String word, List<Mark> marks) {
        if (previousHint == null) {
            previousHint = new Hint(List.of(' ', ' ', ' ', ' ', ' '));
        }

        checkWordAndHintSameSize(word, previousHint);
        checkWordAndMarksSameSize(word, marks);
        return new Hint(createHintCharachters(word, previousHint, marks));


    }

    private static void checkWordAndHintSameSize(String word, Hint previousHint) {
        if (word.length() != previousHint.hint.size()) throw new InvalidHintException("Previous hint and marks arent the same size");
    }

    private static void checkWordAndMarksSameSize(String word,  List<Mark> marks) {
        if (word.length() != marks.size()) throw new InvalidHintException("Word and marks arent the same size");
    }

    private static List<Character> createHintCharachters(String word, Hint previousHint, List<Mark> marks) {
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
        return resChars;
    }

    public Hint(List<Character> hint) {
        this.hint = hint;
    }

    public List<Character> getHint() { return hint; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint1 = (Hint) o;
        return Objects.equals(hint, hint1.hint);
    }
}
