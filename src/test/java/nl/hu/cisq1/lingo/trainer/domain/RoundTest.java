package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.IllegalRoundStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    @Test
    @DisplayName("new Round() creates active round")
    void newRoundIsAnActiveRound() {
        Round round = new Round("woord", new HashMap<>());
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Round.of() creates active round")
    void roundOfCreatesAnActiveRound() {
        Round round = Round.of("woord");
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Round.of() creates a round with no guesses made and thus no feedback")
    void roundOfCreatesRoundWithoutAnyMadeGuesses() {
        Round round = Round.of("woord");
        assert round.getTurns() != null;
        for (Map.Entry<Integer, Turn> entry : round.getTurns().entrySet()) {
            assertNull(entry.getValue().getFeedback());
        }
    }

    static Stream<Arguments> provideCorrectRoundGuesses() {
        Turn turn = new Turn();
        turn.takeGuess("woord", "teste");
        return Stream.of(
                Arguments.of(
                        "woord",
                        Map.of( 1, new Turn(),
                                2, new Turn(),
                                3, new Turn(),
                                4, new Turn(),
                                5, new Turn())
                ),
                Arguments.of(
                        "woord",
                        Map.of(
                                1, turn,
                                2, new Turn(),
                                3, new Turn(),
                                4, new Turn(),
                                5, new Turn()
                )
                ),Arguments.of(
                        "woord",
                                Map.of(
                                        1, turn,
                                        2, turn,
                                        3, turn,
                                        4, turn,
                                        5, new Turn())
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideCorrectRoundGuesses")
    @DisplayName("Take correct guesses when there are guess attempts left")
    void takeCorrectGuess(String word, Map<Integer, Turn> turns) {
        Round round = new Round(word, turns);
        round.takeGuess(word);
        assertSame(RoundState.WON, round.getState());
    }

    @Test
    @DisplayName("Take correct guess at fifth attempt is WON")
    void takeFifthCorrectGuess() {
        Round round = Round.of("woord");
        round.takeGuess("wursd");
        round.takeGuess("wwwww");
        round.takeGuess("sdsdd");
        round.takeGuess("noper");
        round.takeGuess("woord");
        assertSame(RoundState.WON, round.getState());
    }

    @Test
    @DisplayName("Take wrong guess at fifth attempt is LOST")
    void takeCorrectGuess() {
        Round round = Round.of("woord");
        round.takeGuess("wursd");
        round.takeGuess("wwwww");
        round.takeGuess("sdsdd");
        round.takeGuess("noper");
        round.takeGuess("wffff");
        assertSame(RoundState.LOST, round.getState());
    }

    @Test
    @DisplayName("Take guess when round is Lost")
    void guessWhenRoundIsLost() {
        String word = "woord";
        Round round = new Round(word, Map.of());
        round.setState(RoundState.LOST);

        assertThrows(
                IllegalRoundStateException.class,
                () -> round.takeGuess("invalid")
        );
    }

    @Test
    @DisplayName("Take guess when round is Won")
    void guessWhenRoundIsWon() {
        String word = "woord";
        Round round = new Round(word, Map.of());
        round.setState(RoundState.WON);

        assertThrows(
                IllegalRoundStateException.class,
                () -> round.takeGuess("invalid")
        );
    }

    @Test
    @DisplayName("if round is already guess attemps 5 times throw error")
    void guessWhenRoundAttemptsAreOver() {
        Turn turn = new Turn();
        turn.takeGuess("woord", "teste");
        String word = "woord";
        Round round = new Round(word, Map.of(
                1, turn,
                2, turn,
                3, turn,
                4, turn,
                5, turn
        ));
        assertThrows(
                IllegalRoundStateException.class,
                () -> round.takeGuess("invalid")
        );
    }

    @Test
    @DisplayName("getLastHint returns hint of the most recent guess")
    void getLastHintReturnsMostRecentHint() {
        Round round = Round.of("woord");
        round.takeGuess("wursd");
        assertEquals(List.of('w', '.', '.', '.', 'd'), round.getLastHint().getHint());
    }

    @Test
    @DisplayName("getLastHint returns null when no guesses made")
    void getLastHintWithNoGuesses() {
        Round round = Round.of("woord");
        assertEquals(List.of('w', '.', '.', '.', '.'), round.getLastHint().getHint());
    }

    @Test
    @DisplayName("getLastHint returns the last turn if all turns have been guessed")
    void getLastHintAndFiveGuessesHaveBeenMadeReturnsFifthGuess() {
        Round round = Round.of("woord");
        round.takeGuess("wursd");
        round.takeGuess("wwwww");
        round.takeGuess("sdsdd");
        round.takeGuess("noper");
        round.takeGuess("yikes");
        assertEquals(List.of('w', 'o', '.', '.', 'd'), round.getLastHint().getHint());
    }

    static Stream<Arguments> provideCurrentRoundExamples() {
        Turn turn = new Turn();
        turn.takeGuess("woord", "wursd");
        return Stream.of(
                Arguments.of(
                        1,
                        new Round("woord",
                                Map.of( 1, new Turn(),
                                    3, new Turn(),
                                    4, new Turn(),
                                    5, new Turn()
                                )
                        )
                ),
                Arguments.of(
                        2,
                        new Round("woord",
                            Map.of(
                            1, turn,
                            2, new Turn(),
                            3, new Turn(),
                            4, new Turn(),
                            5, new Turn()
                            )
                        )
                ),Arguments.of(
                        5,
                        new Round("woord",
                            Map.of(
                                1, turn,
                                2, turn,
                                3, turn,
                                4, turn,
                                5, new Turn()
                            )
                        )
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideCurrentRoundExamples")
    @DisplayName("getCurrentRound returns most recent turn with no word guess done yet in the round")
    void getCurrentTurnInRound(int turnIndex, Round round) {
        Turn currentTurn = round.getTurns().get(turnIndex);
        assertEquals(currentTurn, round.getCurrentTurn());
    }

    static Stream<Arguments> provideWordForWordLengthExamples() {
        return Stream.of(
                Arguments.of("woord"),
                Arguments.of("woorde"),
                Arguments.of("woorden")
                );
    }
    @ParameterizedTest
    @MethodSource("provideWordForWordLengthExamples")
    @DisplayName("getCurrentRound returns most recent turn with no word guess done yet in the round")
    void getWordLength(String word) {
        Round round = Round.of(word);
        assertEquals(word.length(), round.wordLength());
    }
}