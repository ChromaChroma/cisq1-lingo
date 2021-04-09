package nl.hu.cisq1.lingo.trainer.domain.game.strategy.score;

import nl.hu.cisq1.lingo.trainer.domain.feedback.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.feedback.Mark;
import nl.hu.cisq1.lingo.trainer.domain.round.Round;
import nl.hu.cisq1.lingo.trainer.domain.turn.Turn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultCalculateScoreStrategyTest {
    @ParameterizedTest
    @MethodSource("provideCalculationArgs")
    @DisplayName("Calculate round score and expect result")
    void calculateScore_ReturnsResult(List<Feedback> feedbackList, int expectedScore) {
        List<Turn> turns = feedbackList.stream()
                .map(Turn::new)
                .collect(Collectors.toList());
        Map<Integer, Turn> turnMap = IntStream.range(0, turns.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, turns::get));
        Round round = new Round("", turnMap);

        int score = new DefaultCalculateScoreStrategy().calculateScore(round);

        assertEquals(expectedScore, score);
    }

    private static Stream<Arguments> provideCalculationArgs() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Feedback("woord",
                                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                                )
                        ),
                        25
                ),
                Arguments.of(
                        List.of(
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                                )
                        ),
                        20
                ),
                Arguments.of(
                        List.of(
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                                )
                        ),
                        15
                ),
                Arguments.of(
                        List.of(
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                                )
                        ),
                        10
                ),
                Arguments.of(
                        List.of(
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)
                                ),
                                new Feedback("woord",
                                        List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)
                                )
                        ),
                        5
                )
        );
    }
}