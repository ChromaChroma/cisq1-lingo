package nl.hu.cisq1.lingo.trainer.domain.game;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.converter.GameStateConverter;
import nl.hu.cisq1.lingo.trainer.data.converter.WordLengthConverter;
import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.state.AwaitingRoundGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameState;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.WordLengthStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Transient
    private Score score;

    @Column(name = "game_state")
    @Convert(converter = GameStateConverter.class)
    private GameState state;

    @Column(name = "word_length")
    @Convert(converter = WordLengthConverter.class)
    private WordLengthStrategy wordLength;

    @Column(name = "game_state")
    @Transient
    private List<Round> rounds;

    public static Game create() {
        return new Game(
                UUID.randomUUID(),
                Score.empty(),
                new ArrayList<>(),
                new DefaultWordLengthStrategy()
        );
    }

    public Game(UUID id, Score score, List<Round> rounds, WordLengthStrategy wordLength) {
        this.id = id;
        this.score = score;
        this.rounds = rounds;
        this.wordLength = wordLength;
        this.state = new AwaitingRoundGameState();
    }

    public Round startNewRound(String word) {
        return state.startNewRound(word, this);
    }

    public Feedback guessWord(String guess) throws NotFoundException {
        return state.guessWord(this, guess);
    }

    public Hint latestHint() throws NotFoundException {
        return state.latestHint(this);
    }

    public Turn getCurrentTurn() throws NotFoundException {
        return state.getCurrentTurn(this);
    }

    public UUID getId() { return id; }

    public Score getScore() { return score; }

    public List<Round> getRounds() { return rounds; }

    public void setState(GameState state) { this.state = state; }

    public WordLengthStrategy getWordLength() { return wordLength; }

    public void setWordLength(WordLengthStrategy wordLength) { this.wordLength = wordLength; }
}
