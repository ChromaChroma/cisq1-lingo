package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.Turn;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;

public interface GameState {
    Round startNewRound(String word, Game game);
    Turn getCurrentTurn(Game game) throws NotFoundException;
    Feedback guessWord(Game game, String guess) throws NotFoundException;
    Hint latestHint(Game game) throws NotFoundException;
}
