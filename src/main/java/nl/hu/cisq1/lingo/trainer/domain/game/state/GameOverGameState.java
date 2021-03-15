package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.Turn;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

public class GameOverGameState implements GameState{
    @Override
    public Round startNewRound(String word, Game game) {
        throw new IllegalGameStateException("Cannot start round when game is over");
    }

    @Override
    public Turn getCurrentTurn(Game game) {
        throw new IllegalGameStateException("Cannot get current turn when game is over");
    }

    @Override
    public Feedback guessWord(Game game, String guess) {
        throw new IllegalGameStateException("Cannot guess word when game is over");
    }

    @Override
    public Hint latestHint(Game game) {
        throw new IllegalGameStateException("Cannot get latest hint when game is over");
    }
}
