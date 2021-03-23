package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.WordLengthStrategy;

import javax.persistence.AttributeConverter;

public class WordLengthConverter  implements AttributeConverter<WordLengthStrategy, Integer> {
    @Override
    public Integer convertToDatabaseColumn(WordLengthStrategy wordLengthStrategy) {
        if (wordLengthStrategy!= null) return wordLengthStrategy.currentLength();
        throw new NullPointerException();
    }

    @Override
    public WordLengthStrategy convertToEntityAttribute(Integer wordLengthStrategy) {
        return new DefaultWordLengthStrategy(wordLengthStrategy);
    }
}
