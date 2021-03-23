package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.InvalidHintException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "hint")
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ElementCollection
    @CollectionTable(
            name = "hint_character",
            joinColumns = @JoinColumn(name = "hint_id")
    )
    @Column(name = "sequence_character")
    private List<Character> hintSequence;

    static Hint of(Hint previousHint, String word, List<Mark> marks) {
        if (previousHint == null) {
            previousHint = new Hint(List.of(' ', ' ', ' ', ' ', ' '));
        }
        checkWordAndHintSameSize(word, previousHint);
        checkWordAndMarksSameSize(word, marks);
        return new Hint(createHintCharachters(word, previousHint, marks));
    }

    private static void checkWordAndHintSameSize(String word, Hint previousHint) {
        if (word.length() != previousHint.hintSequence.size()) throw new InvalidHintException("Previous hint and marks arent the same size");
    }

    private static void checkWordAndMarksSameSize(String word,  List<Mark> marks) {
        if (word.length() != marks.size()) throw new InvalidHintException("Word and marks arent the same size");
    }

    private static List<Character> createHintCharachters(String word, Hint previousHint, List<Mark> marks) {
        List<Character> resChars = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        for (int i = 0; i < previousHint.hintSequence.size(); i ++) {
            String charString = previousHint.hintSequence.get(i).toString();
            if (charString.matches("[a-zA-Z]+\\.?")){
                resChars.add(i, previousHint.hintSequence.get(i));
            }else if (marks.get(i) == Mark.CORRECT){
                resChars.add(wordChars[i]);
            } else {
                resChars.add('.');
            }
        }
        return resChars;
    }

    public Hint() { }
    public Hint(List<Character> hint) {
        this.hintSequence = hint;
    }

    public List<Character> getHintSequence() { return hintSequence; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint1 = (Hint) o;
        return Objects.equals(hintSequence, hint1.hintSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hintSequence);
    }
}
