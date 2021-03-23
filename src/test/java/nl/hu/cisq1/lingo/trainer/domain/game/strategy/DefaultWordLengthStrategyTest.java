package nl.hu.cisq1.lingo.trainer.domain.game.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultWordLengthStrategyTest {
    private static Stream<Arguments> provideInitialWordLengths() {
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
    @MethodSource("provideInitialWordLengths")
    @DisplayName("5, 6, 7 will return their word length, other outside this range or null returns length of 5")
    void assertInputIsCorrectWordLength(Integer length, Integer expected) {
        WordLengthStrategy lengthStrategy = new DefaultWordLengthStrategy(length);
        assertEquals(expected, lengthStrategy.currentLength());
    }

    private static Stream<Arguments> provideInitialWordLengthsAndExpectedNextValue() {
        return Stream.of(
                Arguments.of(5, 6),
                Arguments.of(6, 7),
                Arguments.of(7, 5),
                Arguments.of(null, 6),
                Arguments.of(4, 6),
                Arguments.of(0, 6),
                Arguments.of(-1, 6),
                Arguments.of(8, 6),
                Arguments.of(100, 6)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialWordLengthsAndExpectedNextValue")
    @DisplayName("5, 6, 7 will return the next in turn word length, other outside this range or null returns length of 6 (first after default start value)")
    void assertNextMethodValue(Integer length, Integer expected) {
        WordLengthStrategy lengthStrategy = new DefaultWordLengthStrategy(length);
        lengthStrategy.next();
        assertEquals(expected, lengthStrategy.currentLength());
    }
}