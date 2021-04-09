package nl.hu.cisq1.lingo.trainer.domain.turn.strategy.marks;

import nl.hu.cisq1.lingo.trainer.domain.Mark;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultGenerateMarksStrategy implements GenerateMarksStrategy{
    @Override
    public List<Mark> generateMarks(String word, String guess) {
        if (word.length() == guess.length()) return generateValidMarks(word, guess);
        return generateInvalidMarks(guess);
    }

    private List<Mark> generateValidMarks(String word, String guess) {
        List<Mark> marks = setAbsentMarks(word);
        word = setCorrectMarks(word, guess, marks);
        setPresentMarks(word, guess, marks);
        return marks;
    }

    private List<Mark> setAbsentMarks(String word) {
        return word.chars()
                .mapToObj(integer -> Mark.ABSENT)
                .collect(Collectors.toList());
    }

    private String setCorrectMarks(String word, String guess, List<Mark> marks) {
        for (int i = 0; i < guess.length(); i++) {
            if (word.indexOf(guess.charAt(i)) == i) {
                marks.set(i, Mark.CORRECT);
                word = word.replaceFirst(String.valueOf(guess.charAt(i)), ".");
            }
        }
        return word;
    }

    private void setPresentMarks(String word, String guess, List<Mark> marks) {
        for (int i = 0; i < guess.length(); i++) {
            if (word.indexOf(guess.charAt(i)) != -1 && marks.get(i) != Mark.CORRECT) {
                marks.set(i, Mark.PRESENT);
                word = word.replaceFirst(String.valueOf(guess.charAt(i)), ".");
            }
        }
    }

    private List<Mark> generateInvalidMarks(String guess) {
        return IntStream.range(0, guess.length())
                .boxed()
                .map(integer -> Mark.INVALID)
                .collect(Collectors.toList());
    }
}
