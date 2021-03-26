package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.IllegalRoundStateException;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "round")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private RoundState state;

    @Column(name = "word")
    private String word;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "round_turn_mapping",
            joinColumns = {@JoinColumn(name = "round_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "turn_id", referencedColumnName = "id")})
    private Map<Integer, Turn> turns;

    public static Round of(String word) {
        Map<Integer, Turn> turns = new TreeMap<>();
        for (int turnIndex = 1; turnIndex <= 5; turnIndex++) {
            turns.put(turnIndex, new Turn());
        }
        return new Round(word, turns);
    }

    public Round() { }
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

    public Hint getLatestHint() {
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

    public Integer lengthOfWord() {
        return word.length();
    }

    public Map<Integer, Turn> getTurns() { return turns; }

    public RoundState getState() { return state; }

    public void setState(RoundState state) { this.state = state; }
}
