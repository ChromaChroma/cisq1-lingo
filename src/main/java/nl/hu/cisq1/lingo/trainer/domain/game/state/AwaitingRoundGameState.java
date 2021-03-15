package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.Turn;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

public class AwaitingRoundGameState implements GameState {
    @Override
    public Round startNewRound(String word, Game game) {
        Round round = Round.of(word);
        game.getRounds().add(round);
        game.setState(new ActiveGameState());
        return round;
    }

    @Override
    public Turn getCurrentTurn(Game game) {
        throw new IllegalGameStateException("Cannot get the current turn when the game is waiting to start a new round");
    }

    @Override
    public Feedback guessWord(Game game, String guess) {
        throw new IllegalGameStateException("Cannot guess a word when the game is waiting to start a new round");
    }

    @Override
    public Hint latestHint(Game game) {
        throw new IllegalGameStateException("Cannot get a latest hint when the game is waiting to start a new round");
    }
}
