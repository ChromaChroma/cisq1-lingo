package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.strategy.word.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.word.WordLengthStrategy;

import javax.persistence.AttributeConverter;

public class WordLengthConverter  implements AttributeConverter<WordLengthStrategy, String> {
    @Override
    public String convertToDatabaseColumn(WordLengthStrategy wordLengthStrategy) {
        return wordLengthStrategy.getClass().getSimpleName().toUpperCase();
    }

    @Override
    public WordLengthStrategy convertToEntityAttribute(String wordLengthStrategy) {
        //No if or switch case, because DefaultWordLengthStrategy is the only current implementation
        return new DefaultWordLengthStrategy();
    }
}
