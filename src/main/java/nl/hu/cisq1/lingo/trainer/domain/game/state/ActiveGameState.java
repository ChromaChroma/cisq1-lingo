package nl.hu.cisq1.lingo.trainer.domain.game.state;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;

import java.util.List;
import java.util.Map;

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
    public Hint guessWord(Game game, String guess) throws NotFoundException {
        if (guess == null || !guess.matches("[a-zA-Z]+\\.?")) throw new IllegalArgumentException("Only letters allowed");
        Round currentRound = getCurrentRound(game.getRounds());
        currentRound.takeGuess(guess.toLowerCase());
        if (currentRound.getState().equals(RoundState.LOST)) updateGameOnRoundLost(game);
        if (currentRound.getState().equals(RoundState.WON)) updateGameOnRoundWin(game, currentRound);
        return currentRound.getLatestHint();
    }

    private void updateGameOnRoundLost(Game game) {
        game.getScore().increaseRoundsPlayed();
        game.setState(new GameOverGameState());
    }

    private void updateGameOnRoundWin(Game game, Round currentRound) {
        updateGameWordLength(game);
        updateGameScore(game, currentRound);
        game.setState(new AwaitingRoundGameState());
    }

    private void updateGameWordLength(Game game) {
        game.getWordLength().next();
    }

    private void updateGameScore(Game game, Round currentRound) {
        Score score = game.getScore();
        score.increaseRoundsPlayed();
        Integer winScore = calculateRoundScore(currentRound);
        score.increasePoints(winScore);
    }

    private Integer calculateRoundScore(Round currentRound) {
        int initialMultiplier = 5;
        int initialAddition = 5;
        int maxRounds = 5;
        int attempts = 0;
        for (Map.Entry<Integer, Turn> entry : currentRound.getTurns().entrySet()) {
            if (entry.getValue().getFeedback() != null) attempts++;
        }
        return initialMultiplier*(maxRounds - attempts) + initialAddition;
    }

    @Override
    public Hint latestHint(Game game) throws NotFoundException {
        return getCurrentRound(game.getRounds()).getLatestHint();
    }

    private Round getCurrentRound(List<Round> rounds) throws NotFoundException {
        for (Round round : rounds) {
            if (round.getState().equals(RoundState.ACTIVE)) return round;
        }
        throw new NotFoundException("No active round found");
    }
}
