package nl.hu.cisq1.lingo.trainer.domain.game;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.*;
import nl.hu.cisq1.lingo.trainer.domain.game.state.AwaitingRoundGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private UUID id;
    private GameState state;
    private List<Round> rounds;
    private Score score;

    public static Game create() {
        return new Game(
                UUID.randomUUID(),
                Score.empty(),
                new ArrayList<>()
        );
    }

    public Game(UUID id, Score score, List<Round> rounds) {
        this.id = id;
        this.score = score;
        this.rounds = rounds;
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
}
