package nl.hu.cisq1.lingo.trainer.domain.game.strategy.score;

import nl.hu.cisq1.lingo.trainer.domain.round.Round;

public class DefaultCalculateScoreStrategy implements CalculateScoreStrategy {
    private static final int INITIAL_MULTIPLIER = 5;
    private static final int INITIAL_ADDITION = 5;
    private static final int MAX_ROUNDS = 5;

    @Override
    public int calculateScore(Round round) {
        int attempts = calculateAttemptsMade(round);
        return INITIAL_MULTIPLIER * (MAX_ROUNDS - attempts) + INITIAL_ADDITION;
    }

    private int calculateAttemptsMade(Round round) {
        return (int) round.getTurns()
                .values()
                .stream()
                .filter(turn -> turn.getFeedback() != null)
                .count();
    }
}
