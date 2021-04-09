package nl.hu.cisq1.lingo.trainer.domain.game.strategy.marks;

import nl.hu.cisq1.lingo.trainer.domain.Mark;

import java.util.ArrayList;
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
