package nl.hu.cisq1.lingo.trainer.domain.game.state;

import nl.hu.cisq1.lingo.trainer.domain.feedback.Hint;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.round.Round;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

public class AwaitingRoundGameState implements GameState {
    @Override
    public Round startNewRound(String word, Game game) {
        Round round = Round.of(word.toLowerCase());
        game.getRounds().add(round);
        game.setState(new ActiveGameState());
        return round;
    }

    @Override
    public Hint guessWord(Game game, String guess) {
        throw new IllegalGameStateException("Cannot guess a word when the game is waiting to start a new round");
    }

    @Override
    public Hint latestHint(Game game) {
        throw new IllegalGameStateException("Cannot get a latest hint when the game is waiting to start a new round");
    }
}
