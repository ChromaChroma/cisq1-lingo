package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

import java.util.List;

public class ActiveGameState implements GameState {
    @Override
    public Round startNewRound(String word, Game game) {
        throw new IllegalGameStateException("Cannot start new round when a round is already active");
    }

    @Override
    public Turn getCurrentTurn(Game game) throws NotFoundException {
        return getCurrentRound(game.getRounds()).getCurrentTurn();
    }

    @Override
    public Feedback guessWord(Game game, String guess) throws NotFoundException {
        Round round = getCurrentRound(game.getRounds());
        Feedback feedback = round.takeGuess(guess);;
        if (round.getState().equals(RoundState.WON)) game.setState(new AwaitingRoundGameState());
        if (round.getState().equals(RoundState.LOST)) game.setState(new GameOverGameState());
        return feedback;
    }

    @Override
    public Hint latestHint(Game game) throws NotFoundException {
        return getCurrentRound(game.getRounds()).getLastHint();
    }

    private Round getCurrentRound(List<Round> rounds) throws NotFoundException {
        for (Round round : rounds) {
            if (round.getState().equals(RoundState.ACTIVE)) return round;
        }
        throw new NotFoundException("No active round found");
    }
}
