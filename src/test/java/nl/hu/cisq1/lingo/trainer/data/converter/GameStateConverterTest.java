package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.state.ActiveGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.AwaitingRoundGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameOverGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameStateConverterTest {
    private static Stream<Arguments> provideGameStates() {
        return Stream.of(
                Arguments.of(new ActiveGameState(), "ACTIVEGAMESTATE"),
                Arguments.of(new AwaitingRoundGameState(), "AWAITINGROUNDGAMESTATE"),
                Arguments.of(new GameOverGameState(), "GAMEOVERGAMESTATE")

        );
    }
    @ParameterizedTest
    @MethodSource("provideGameStates")
    @DisplayName("Convert game state into db column string")
    void assertInputIsCorrectWordLength(GameState gameState, String dbState) {
        String state = new GameStateConverter().convertToDatabaseColumn(gameState);
        assertEquals(dbState, state);
    }

    @Test
    @DisplayName("null input throws null pointer exception")
    void nullInputThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> new GameStateConverter().convertToDatabaseColumn(null)
        );
    }

    private static Stream<Arguments> provideInitialLengths() {
        return Stream.of(
                Arguments.of("ACTIVEGAMESTATE", ActiveGameState.class),
                Arguments.of("AWAITINGROUNDGAMESTATE", AwaitingRoundGameState.class),
                Arguments.of("GAMEOVERGAMESTATE", GameOverGameState.class),
                Arguments.of("activegamestate", ActiveGameState.class),
                Arguments.of("awaitingroundgamestate", AwaitingRoundGameState.class),
                Arguments.of("gameovergamestate", GameOverGameState.class),
                Arguments.of(null, GameOverGameState.class),
                Arguments.of("", GameOverGameState.class)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInitialLengths")
    @DisplayName("Convert db column length into word length strategy")
    void dbColumnToStrategy(String dbState, Class<GameState> expectedClass) {
        GameState state = new GameStateConverter().convertToEntityAttribute(dbState);
        assertEquals(expectedClass.getName(), state.getClass().getName());
    }
}