package nl.hu.cisq1.lingo.trainer.domain.turn.strategy.marks;

import nl.hu.cisq1.lingo.trainer.domain.Mark;

import java.util.List;

public interface GenerateMarksStrategy {
    List<Mark> generateMarks(String word, String guess);
}
