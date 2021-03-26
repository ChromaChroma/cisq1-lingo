package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

public class ActiveGameState implements GameState {
    @Override
    public Round startNewRound(String word, Game game) {
        throw new IllegalGameStateException("Cannot start new round when a round is already active");
    }

    @Override
    public Turn getCurrentTurn(Game game) throws NotFoundException {
        return game.getCurrentRound().getCurrentTurn();
    }

    @Override
    public Hint guessWord(Game game, String guess) throws NotFoundException {
        if (guess == null || !guess.matches("[a-zA-Z]+\\.?")) throw new IllegalArgumentException("Only letters allowed");
        Round currentRound = game.getCurrentRound();
        currentRound.takeGuess(guess.toLowerCase());
        if (currentRound.getState().equals(RoundState.LOST)) updateGameOnRoundLost(game);
        else if (currentRound.getState().equals(RoundState.WON)) updateGameOnRoundWin(game, currentRound);
        return currentRound.getLatestHint();
    }

    private void updateGameOnRoundLost(Game game) {
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
        Integer winScore = calculateRoundScore(currentRound);
        score.increasePoints(winScore);
    }

    private Integer calculateRoundScore(Round round) {
        int initialMultiplier = 5;
        int initialAddition = 5;
        int maxRounds = 5;
        int attempts = calculateAttemptsMade(round);
        return initialMultiplier * (maxRounds - attempts) + initialAddition;
    }

    private int calculateAttemptsMade(Round round) {
        return (int) round.getTurns()
                .values()
                .stream()
                .filter(turn -> turn.getFeedback() != null)
                .count();
    }

    @Override
    public Hint latestHint(Game game) throws NotFoundException {
        return game.getCurrentRound().getLatestHint();
    }
}
