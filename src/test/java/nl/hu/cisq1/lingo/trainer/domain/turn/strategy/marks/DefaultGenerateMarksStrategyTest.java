package nl.hu.cisq1.lingo.trainer.domain.turn.strategy.marks;

import nl.hu.cisq1.lingo.trainer.domain.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultGenerateMarksStrategyTest {
    @ParameterizedTest
    @MethodSource("provideExamples")
    @DisplayName("Generated marks from word and guess should equals expected")
    void generatedMarks_ShouldEqualExpected(String word, String guess, List<Mark> expectedMarks) {
        List<Mark> marks = new DefaultGenerateMarksStrategy().generateMarks(word, guess);

        assertEquals(expectedMarks, marks);

    }
    private static Stream<Arguments> provideExamples() {
        return Stream.of(
                Arguments.of(
                        "whord",
                        "woord",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woord",
                        "woord",
                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woord",
                        "nnnnn",
                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                ),
                Arguments.of(
                        "woord",
                        "wnnnn",
                        List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                ),
                Arguments.of(
                        "woord",
                        "nnnnd",
                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT)
                ),
                Arguments.of(
                        "woord",
                        "ownnn",
                        List.of(Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                ),
                Arguments.of(
                        "woord",
                        "nwwnn",
                        List.of(Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                ),
                Arguments.of(
                        "woord",
                        "nonno",
                        List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT)
                ),
                Arguments.of(
                        "woord",
                        "oonnn",
                        List.of(Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                ),
                Arguments.of(
                        "woord",
                        "nnnn",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)
                ),
                Arguments.of(
                        "woord",
                        "n",
                        List.of(Mark.INVALID)
                ),
                Arguments.of(
                        "woord",
                        "nnnnnn",
                        List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)
                )
        );
    }

}