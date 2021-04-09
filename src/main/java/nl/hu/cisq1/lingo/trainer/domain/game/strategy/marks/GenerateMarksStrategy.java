package nl.hu.cisq1.lingo.trainer.domain.game.strategy.marks;

import nl.hu.cisq1.lingo.trainer.domain.Mark;

import java.util.List;

public interface GenerateMarksStrategy {
    List<Mark> generateMarks(String word, String guess);
}
