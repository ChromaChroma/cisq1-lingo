package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.WordLengthStrategy;

import javax.persistence.AttributeConverter;

public class WordLengthConverter  implements AttributeConverter<WordLengthStrategy, String> {
    @Override
    public String convertToDatabaseColumn(WordLengthStrategy wordLengthStrategy) {
        return wordLengthStrategy.getClass().getSimpleName().toUpperCase();
    }

    @Override
    public WordLengthStrategy convertToEntityAttribute(String wordLengthStrategy) {
        return switch (wordLengthStrategy.toUpperCase()) {
            case "DEFAULTWORDLENGTHSTRATEGY" -> new DefaultWordLengthStrategy();
            default -> new DefaultWordLengthStrategy();
        };

    }
}
