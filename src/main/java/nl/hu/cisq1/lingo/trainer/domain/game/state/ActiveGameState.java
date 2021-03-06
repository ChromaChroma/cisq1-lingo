package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.feedback.Hint;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.Score;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.score.DefaultCalculateScoreStrategy;
import nl.hu.cisq1.lingo.trainer.domain.round.Round;
import nl.hu.cisq1.lingo.trainer.domain.round.RoundState;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

public class ActiveGameState implements GameState {
    @Override
    public Round startNewRound(String word, Game game) {
        throw new IllegalGameStateException("Cannot start new round when a round is already active");
    }


    @Override
    public Hint guessWord(Game game, String guess) throws NotFoundException {
        if (guess == null || !guess.matches("[a-zA-Z]+\\.?")) throw new IllegalArgumentException("Only letters allowed");
        Round currentRound = game.getCurrentRound();
        currentRound.takeGuess(guess.toLowerCase());
        if (currentRound.getState().equals(RoundState.LOST)) updateAndEndGameOnRoundLost(game);
        else if (currentRound.getState().equals(RoundState.WON)) updateGameOnRoundWin(game, currentRound);
        return currentRound.getLatestHint();
    }

    private void updateAndEndGameOnRoundLost(Game game) {
        game.getScore().increaseRoundsPlayed();
        game.setState(new GameOverGameState());
    }

    private void updateGameOnRoundWin(Game game, Round currentRound) {
        updateGameScore(game, currentRound);
        game.setState(new AwaitingRoundGameState());
    }

    private void updateGameScore(Game game, Round currentRound) {
        Score score = game.getScore();
        score.increaseRoundsPlayed();
        Integer winScore = new DefaultCalculateScoreStrategy().calculateScore(currentRound);
        score.increasePoints(winScore);
    }

    @Override
    public Hint latestHint(Game game) throws NotFoundException {
        return game.getCurrentRound().getLatestHint();
    }
}
