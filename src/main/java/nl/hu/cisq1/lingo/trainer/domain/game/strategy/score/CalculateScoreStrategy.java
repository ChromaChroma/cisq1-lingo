package nl.hu.cisq1.lingo.trainer.domain.game.strategy.score;

import nl.hu.cisq1.lingo.trainer.domain.round.Round;

public interface CalculateScoreStrategy {
    int calculateScore(Round round);
}
