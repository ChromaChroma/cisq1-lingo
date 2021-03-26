package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.IllegalRoundStateException;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Table(name = "round")
public class Round {
    private static final int AMOUNT_OF_ATTEMPTS = 5;

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
        return new Round(
                word,
                IntStream.range(0, AMOUNT_OF_ATTEMPTS)
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), turn -> new Turn()))
        );
    }

    public Round() { }
    public Round(String word, Map<Integer, Turn> turns) {
        this.word = word;
        this.turns = new TreeMap<>(turns);
        this.state = RoundState.ACTIVE;
    }

    public Turn getCurrentTurn() {
        return turns.values().stream()
                .filter(turn -> turn.getFeedback() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalRoundStateException("Round is not active anymore"));
    }

    public Feedback takeGuess(String guess) {
        Turn turn = getCurrentTurn();
        Feedback feedback = turn.takeGuess(word, guess);
        checkRoundState(turn);
        return feedback;
    }

    private void checkRoundState(Turn turn) {
        if (turn.getFeedback().isWordGuessed()) {
            this.state = RoundState.WON;
        } else if (!hasTurnsLeft()) {
            this.state = RoundState.LOST;
        }
    }

    private boolean hasTurnsLeft() {
        return turns.values().stream()
                .anyMatch(turn -> turn.getFeedback() == null);
    }

    public Hint getLatestHint() {
        Hint hint = new Hint(buildInitialHintList());
        for (Turn turn : turns.values()) {
            if (turn.getFeedback() != null) {
                hint = turn.getFeedback().giveHint(hint);
            }
        }
        return hint;
    }

    private List<Character> buildInitialHintList() {
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < word.toCharArray().length; i++) {
            if (i == 0) chars.add(i, word.charAt(i));
            else chars.add('.');
        }
        return chars;
    }

    public Integer lengthOfWord() {
        return word.length();
    }

    public Map<Integer, Turn> getTurns() {
        return turns;
    }

    public RoundState getState() {
        return state;
    }

    public void setState(RoundState state) {
        this.state = state;
    }
}
