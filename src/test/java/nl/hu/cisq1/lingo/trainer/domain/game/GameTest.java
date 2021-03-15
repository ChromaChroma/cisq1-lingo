package nl.hu.cisq1.lingo.trainer.domain.game;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.RoundState;
import nl.hu.cisq1.lingo.trainer.domain.Score;
import nl.hu.cisq1.lingo.trainer.exception.IllegalGameStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    @DisplayName("Game constructor with correct args")
    void correctConstructor() {
        assertDoesNotThrow(
                () -> new Game(
                        UUID.randomUUID(),
                        Score.empty(),
                        new ArrayList<>()
                )
        );
    }

    @Test
    @DisplayName("Game.create() with correct args")
    void correctCreateConstructor() {
        assertDoesNotThrow(
                Game::create
        );
    }

    @Test
    @DisplayName("Test get score returns correct score")
    void getGameScore() {
        Score score = new Score(5, 2);
        Game game = new Game( UUID.randomUUID(), score, new ArrayList<>());
        assertEquals(score, game.getScore());
    }

    @Test
    @DisplayName("Test get id returns correct uuid")
    void getGameId() {
        UUID uuid = UUID.randomUUID();
        Game game = new Game( uuid, Score.empty(), new ArrayList<>());
        assertEquals(uuid, game.getId());
    }

    //Game Before Round Start (Awaiting round state)

    @Test
    @DisplayName("Game round not started, make guess, throws invalid game state exception")
    void throwIfGuessBeforeRoundStart() {
        Game game = Game.create();
        assertThrows(
                IllegalGameStateException.class,
                () -> game.guessWord("")
        );
    }

    @Test
    @DisplayName("Game round not started, get latest hint, throws invalid game state exception")
    void throwIfGetLatestHintBeforeRoundStart() {
        Game game = Game.create();
        assertThrows(
                IllegalGameStateException.class,
                game::latestHint
        );
    }

    @Test
    @DisplayName("Game round not started, get latest hint, throws invalid game state exception")
    void throwIfGetCurrentRoundBeforeRoundStart() {
        Game game = Game.create();
        assertThrows(
                IllegalGameStateException.class,
                game::getCurrentTurn
        );
    }

    //Game Before Round Start (Active round state)

    @Test
    @DisplayName("Game Active, try to start new round and throw")
    void gameActiveAndStartRound() {
        Game game = Game.create();
        game.startNewRound("woord");
        assertThrows(
                IllegalGameStateException.class,
                () -> game.startNewRound("woord")
        );
    }

    @Test
    @DisplayName("Game Active, get turn, find active round in list over completed rounds and throw")
    void gameActiveAndThrowWhenFindCurrentRoundInEmptyList() {
        Game game = Game.create();
        game.startNewRound("woord");
        for(Round round : game.getRounds()) {
            round.setState(RoundState.LOST);
        }
        assertThrows(
                NotFoundException.class,
                game::getCurrentTurn
        );
    }

    @Test
    @DisplayName("Start game Round")
    void doesntThrowIfGetCurrentRoundBeforeRoundStart() {
        Game game = Game.create();
        assertDoesNotThrow(
                () -> game.startNewRound("woord")
        );
    }

    @Test
    @DisplayName("Game round started, do game methods")
    void gameRoundStartedDoGameMethods() {
        Game game = Game.create();
        game.startNewRound("woord");
        assertDoesNotThrow(
                () -> {
                    game.getCurrentTurn();
                    game.latestHint();
                    game.guessWord("woord");
                }
        );
    }

    //game over state tests

    @Test
    @DisplayName("Game Lost, try to start new round and throw")
    void gameOverAndStartRound() throws NotFoundException {
        Game game = Game.create();
        game.startNewRound("woord");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        assertThrows(
                IllegalGameStateException.class,
                () -> game.startNewRound("woord")
        );
    }

    @Test
    @DisplayName("Game Lost, getCurrentTurn and throw")
    void gameOverAndGetCurrentTurn() throws NotFoundException {
        Game game = Game.create();
        game.startNewRound("woord");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        assertThrows(
                IllegalGameStateException.class,
                game::getCurrentTurn
        );
    }

    @Test
    @DisplayName("Game Lost, get latest hint and throw")
    void gameOverAndGetHint() throws NotFoundException {
        Game game = Game.create();
        game.startNewRound("woord");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        assertThrows(
                IllegalGameStateException.class,
                game::latestHint
        );
    }

    @Test
    @DisplayName("Game Lost, guess word and throw")
    void gameOverAndguessWord() throws NotFoundException {
        Game game = Game.create();
        game.startNewRound("woord");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        game.guessWord("ttttt");
        assertThrows(
                IllegalGameStateException.class,
                () -> game.guessWord("woord")
        );
    }
}