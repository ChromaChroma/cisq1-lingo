package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.IllegalRoundStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Round {
    private RoundState state;
    private String word;
    private Map<Integer, Turn> turns;

    public static Round of(String word) {
        Map<Integer, Turn> turns = new TreeMap<>();
        for (int turnIndex = 1; turnIndex <= 5; turnIndex++) {
            turns.put(turnIndex, new Turn());
        }
        return new Round(word, turns);
    }

    public Round(String word, Map<Integer, Turn> turns)  {
        this.word = word;
        this.turns = new TreeMap<>(turns);
        this.state = RoundState.ACTIVE;
    }

    public Turn getCurrentTurn() {
        if (this.state.equals(RoundState.ACTIVE)) {
            for (Map.Entry<Integer, Turn> entry : turns.entrySet()) {
                if (entry.getValue().getFeedback() == null) return entry.getValue();
            }
        }
        throw new IllegalRoundStateException("Round is not active anymore");
    }

    public Feedback takeGuess(String guess) {
        Turn turn = getCurrentTurn();
        Feedback feedback = turn.takeGuess(word, guess);
        checkRoundState(turn);
        return feedback;
    }

    private void checkRoundState(Turn turn) {
        if (turn.getFeedback().isWordGuessed()) this.state = RoundState.WON;
        if (!turn.getFeedback().isWordGuessed() && !hasTurnsLeft()) this.state = RoundState.LOST;
    }

    private boolean hasTurnsLeft() {
        for (Map.Entry<Integer, Turn> entry : turns.entrySet()) {
            if (entry.getValue().getFeedback() == null) return true;
        }
        return false;
    }

    public Hint getLastHint() {
        Integer lastHintTurnIndex = null;
        Hint hint = new Hint(buildInitialHintList());
        for (Map.Entry<Integer, Turn> entry : turns.entrySet()) {
            if (entry.getValue().getFeedback() != null) {
                hint = entry.getValue().getFeedback().giveHint(hint);
                lastHintTurnIndex = entry.getKey();
            }
        }
        if (lastHintTurnIndex == null) return hint;
        return this.turns.get(lastHintTurnIndex).getFeedback().giveHint(hint);
    }

    private List<Character> buildInitialHintList() {
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i <  word.toCharArray().length; i++){
            if (i == 0) chars.add(i, word.charAt(i));
            else chars.add('.');
        }
        return chars;
    }

    public Map<Integer, Turn> getTurns() { return turns; }

    public RoundState getState() { return state; }

    public void setState(RoundState state) { this.state = state; }
}
