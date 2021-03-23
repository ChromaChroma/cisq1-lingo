package nl.hu.cisq1.lingo.trainer.domain.game;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.Score;
import nl.hu.cisq1.lingo.trainer.domain.game.state.ActiveGameState;
import nl.hu.cisq1.lingo.trainer.domain.game.strategy.DefaultWordLengthStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    @Test
    @DisplayName("Game wordLength correct")
    void gameWordLength() {
        Game game = Game.create();
        game.startNewRound("woord");
        assertEquals(5, game.getWordLength().currentLength());
    }

    @Test
    @DisplayName("Game wordLength ++ after correct guess")
    void gameWordLengthUp() throws NotFoundException {
        Game game = Game.create();
        
        game.startNewRound("woord");
        game.guessWord("woord");
        assertEquals(6, game.getWordLength().currentLength());

        game.startNewRound("woord");
        game.guessWord("woord");
        assertEquals(7, game.getWordLength().currentLength());

        game.startNewRound("woord");
        game.guessWord("woord");
        assertEquals(5, game.getWordLength().currentLength());
    }

    @Test
    @DisplayName("Game constructor with correct args")
    void correctConstructor() {
        assertDoesNotThrow(
                () -> new Game(
                        UUID.randomUUID(),
                        Score.empty(),
                        new ActiveGameState(),
                        new ArrayList<>(),
                        new DefaultWordLengthStrategy(5)
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
        Game game = new Game(UUID.randomUUID(), score, new ActiveGameState(), new ArrayList<>(), new DefaultWordLengthStrategy(5));
        assertEquals(score, game.getScore());
    }

    @Test
    @DisplayName("Test get id returns correct uuid")
    void getGameId() {
        UUID uuid = UUID.randomUUID();
        Game game = new Game(uuid, Score.empty(), new ActiveGameState(), new ArrayList<>(), new DefaultWordLengthStrategy(5));
        assertEquals(uuid, game.getId());
    }
}