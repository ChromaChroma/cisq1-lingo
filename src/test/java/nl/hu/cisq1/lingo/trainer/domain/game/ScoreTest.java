package nl.hu.cisq1.lingo.trainer.domain.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    static Stream<Arguments> provideCorrectScoreArgs() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(999999, 0),
                Arguments.of(0, 999999),
                Arguments.of(999999, 999999)
        );
    }
    @ParameterizedTest
    @MethodSource("provideCorrectScoreArgs")
    @DisplayName("Create valid Score")
    void createValidScore(Integer points, Integer roundsPlayed) {
        assertDoesNotThrow(() -> new Score(points,roundsPlayed));
    }

    static Stream<Arguments> provideInvalidScoreArgs() {
        return Stream.of(
                Arguments.of(-1, 0),
                Arguments.of(0, -1),
                Arguments.of(-1, 999999),
                Arguments.of(999999, -1),
                Arguments.of(-999999, 0),
                Arguments.of(0, -999999),
                Arguments.of(-999999, 999999),
                Arguments.of(999999, -999999)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInvalidScoreArgs")
    @DisplayName("Throws on invalid Score args")
    void throwInvalidScoreArgs(Integer points, Integer roundsPlayed) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Score(points,roundsPlayed)
        );
    }

    @Test
    @DisplayName("Score.empty() creates valid score with 0 points and 0 roundsPlayed")
    void scoreEmptyValid() {
        assertDoesNotThrow(

                () -> {
                    Score score = Score.empty();
                    assertEquals(0, score.getPoints());
                    assertEquals(0, score.getRoundsPlayed());
                }

        );

    }


    static Stream<Arguments> providePointInput() {
        return Stream.of(
                Arguments.of(List.of(0, 0, 0, 0)),
                Arguments.of(List.of(1, 1, 1, 1)),
                Arguments.of(List.of(999999999, 999999999, 999999999, 999999999))
        );
    }
    @ParameterizedTest
    @MethodSource("providePointInput")
    @DisplayName("Add points to score")
    void addPointsToScore(List<Integer> points) {
        Score score = Score.empty();
        Integer controllInteger = 0;
        for (Integer integer : points){
            controllInteger += integer;
            assertDoesNotThrow(() -> score.increasePoints(integer));
            assertEquals(controllInteger, score.getPoints());
        }
    }

    static Stream<Arguments> provideInvalidScorePointArgs() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(-999999)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInvalidScorePointArgs")
    @DisplayName("Add invalid points to score")
    void addInvalidPointsToScore(Integer points) {
        Score score = Score.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> score.increasePoints(points)
        );
    }

    @Test
    @DisplayName("Increase rounds played")
    void increaseRoundsPlayed() {
        Score score = Score.empty();
        assertDoesNotThrow(score::increaseRoundsPlayed);
        assertEquals(1, score.getRoundsPlayed());
    }
}