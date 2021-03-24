package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.WordLengthStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordLengthConverterTest {
    private static Stream<Arguments> provideInitialWordLengthStrategies() {
        return Stream.of(
                Arguments.of(new DefaultWordLengthStrategy(5)),
                Arguments.of(new DefaultWordLengthStrategy(6)),
                Arguments.of(new DefaultWordLengthStrategy(7)),
                Arguments.of(new DefaultWordLengthStrategy(null)),
                Arguments.of(new DefaultWordLengthStrategy(4)),
                Arguments.of(new DefaultWordLengthStrategy(0)),
                Arguments.of(new DefaultWordLengthStrategy(-1)),
                Arguments.of(new DefaultWordLengthStrategy(8)),
                Arguments.of(new DefaultWordLengthStrategy(100))
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialWordLengthStrategies")
    @DisplayName("Convert word length strategy into db column")
    void assertInputIsCorrectWordLength(WordLengthStrategy lengthStrategy) {
        Integer length = new WordLengthConverter().convertToDatabaseColumn(lengthStrategy);
        assertEquals(lengthStrategy.currentLength(), length);
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
                Arguments.of(5, 5),
                Arguments.of(6, 6),
                Arguments.of(7, 7),
                Arguments.of(null, 5),
                Arguments.of(4, 5),
                Arguments.of(0, 5),
                Arguments.of(-1, 5),
                Arguments.of(8, 5),
                Arguments.of(100, 5)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialLengths")
    @DisplayName("Convert db column length into word length strategy")
    void dbColumnToStrategy(Integer length, Integer expected) {
        WordLengthStrategy lengthStrategy = new WordLengthConverter().convertToEntityAttribute(length);
        assertEquals(expected, lengthStrategy.currentLength());
    }
}