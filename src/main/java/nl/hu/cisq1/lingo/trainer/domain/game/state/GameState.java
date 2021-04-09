package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.feedback.Hint;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.round.Round;

public interface GameState {
    Round startNewRound(String word, Game game);
    Hint guessWord(Game game, String guess) throws NotFoundException;
    Hint latestHint(Game game) throws NotFoundException;
}
