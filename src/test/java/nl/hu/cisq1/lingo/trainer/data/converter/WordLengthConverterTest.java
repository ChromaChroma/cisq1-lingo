package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.WordLengthStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WordLengthConverterTest {
    private static Stream<Arguments> provideInitialWordLengthStrategies() {
        return Stream.of(
                Arguments.of(new DefaultWordLengthStrategy())
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialWordLengthStrategies")
    @DisplayName("Convert word length strategy into db column")
    void assertInputIsCorrectWordLength(WordLengthStrategy lengthStrategy) {
        String strategy = new WordLengthConverter().convertToDatabaseColumn(lengthStrategy);
        assertEquals(lengthStrategy.getClass().getSimpleName().toUpperCase(), strategy);
    }

    @Test
    @DisplayName("null input throws null pointer exception")
    void nullInputThrowsException() {
        WordLengthConverter converter = new WordLengthConverter();
        assertThrows(
                NullPointerException.class,
                () -> converter.convertToDatabaseColumn(null)
        );
    }

    private static Stream<Arguments> provideInitialLengths() {
        return Stream.of(
                Arguments.of("DEFAULTWORDLENGTHSTRATEGY", DefaultWordLengthStrategy.class),
                Arguments.of("SOMERANDOMTEXTBUTNOTANEXISTINGSTRATEGY", DefaultWordLengthStrategy.class)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialLengths")
    @DisplayName("Convert db column length into word length strategy")
    void dbColumnToStrategy(String strategyName, Class<WordLengthStrategy> expectedStrategy) {
        WordLengthStrategy lengthStrategy = new WordLengthConverter().convertToEntityAttribute(strategyName);
        assertSame(lengthStrategy.getClass(), expectedStrategy);
    }
}