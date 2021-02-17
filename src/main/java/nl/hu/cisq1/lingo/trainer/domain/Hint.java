package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hint {
    private List<Character> hint;

    static Hint of(String word, List<Mark> marks) {
        List<Character> chars = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        chars.add(wordChars[0]);
        for (int i = 1; i < marks.size(); i ++) {
            if (marks.get(i) == Mark.CORRECT) chars.add(wordChars[i]);
            else chars.add('.');
        }
        return new Hint(chars);
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
