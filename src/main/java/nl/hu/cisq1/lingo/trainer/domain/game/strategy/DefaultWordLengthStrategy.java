package nl.hu.cisq1.lingo.trainer.domain.game.strategy;

import java.util.Map;

public class DefaultWordLengthStrategy implements WordLengthStrategy{
    private Map<Integer, Integer> wordLengths;
    private Integer currentWordLength;

    public DefaultWordLengthStrategy(){
        this(5);
    }
    public DefaultWordLengthStrategy(Integer initialLength) {
        this.wordLengths = Map.of(
                5, 6,
                6, 7,
                7, 5
        );
        if (initialLength == null || initialLength < 5 || initialLength > 7) this.currentWordLength = 5;
        else this.currentWordLength = initialLength;
    }

    @Override
    public Integer next() {
        this.currentWordLength = wordLengths.get(currentWordLength);
        return this.currentWordLength;
    }

    @Override
    public Integer currentLength() {
        return this.currentWordLength;
    }
}
