package nl.hu.cisq1.lingo.trainer.data.converter;

import nl.hu.cisq1.lingo.trainer.domain.game.state.ActiveGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.AwaitingRoundGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameOverGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameState;

import javax.persistence.AttributeConverter;

public class GameStateConverter implements AttributeConverter<GameState, String> {
    @Override
    public String convertToDatabaseColumn(GameState state) {
        if (state != null) return state.getClass().getSimpleName().toUpperCase();
        throw new NullPointerException();
    }

    @Override
    public GameState convertToEntityAttribute(String state) {
        if (state == null) return new GameOverGameState();
        return switch (state.toUpperCase()) {
            case "ACTIVEGAMESTATE" -> new ActiveGameState();
            case "AWAITINGROUNDGAMESTATE" -> new AwaitingRoundGameState();
            case "GAMEOVERGAMESTATE" -> new GameOverGameState();
            default -> new GameOverGameState();
        };
    }
}
