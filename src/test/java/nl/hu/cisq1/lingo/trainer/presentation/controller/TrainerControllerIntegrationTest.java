package nl.hu.cisq1.lingo.trainer.presentation.controller;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.data.repository.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.domain.game.state.GameOverGameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerService service;

    @Autowired
    private SpringGameRepository repository;

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }

    @Test
    @DisplayName("Start a new game")
    void startGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games");

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.points").value(0))
                .andExpect(jsonPath("$.roundsPlayed").value(0));

    }

    @Test
    @DisplayName("Get game")
    void getGame() throws Exception {
        Game game = service.startNewGame();
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.points").value(0))
                .andExpect(jsonPath("$.roundsPlayed").value(0));
    }

    @Test
    @DisplayName("Get game after a round won")
    void getGameAfterRoundWin() throws Exception {
        Game game = Game.createDefault();
        game.startNewRound("woord");
        game.guessWord("woord");
        game = this.repository.save(game);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.points").value(25))
                .andExpect(jsonPath("$.roundsPlayed").value(1));
    }

    @Test
    @DisplayName("Get game when there is no game with id")
    void getGameWhenNonExistent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}", UUID.randomUUID());

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Start a new round")
    void startNewRound() throws Exception {
        Game game = service.startNewGame();
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/rounds", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hint").exists());
    }

    @Test
    @DisplayName("409 when start a new round if already an active round")
    void startNewRoundWhenAlreadyActive() throws Exception {
        Game game = service.startNewGame();
        service.startNewRound(game.getId());
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/rounds", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Guess the word")
    void guessWord() throws Exception {
        Game game = service.startNewGame();
        service.startNewRound(game.getId());
        String word = "woord";
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/guess", game.getId())
                .content(word);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hint").exists())
                .andExpect(jsonPath("$.guess").value(word));
    }

    @ParameterizedTest
    @MethodSource("provideWrongGuesses")
    @DisplayName("Guess with invalid words")
    void guessInvalidWord(String guess) throws Exception {
        Game game = service.startNewGame();
        service.startNewRound(game.getId());
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/guess", game.getId())
                .content(guess);

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }
    private static Stream<Arguments> provideWrongGuesses() {
        return Stream.of(
                Arguments.of("woor"),
                Arguments.of(""),
                Arguments.of("09876"),
                Arguments.of("12345"),
                Arguments.of("-1"),
                Arguments.of("wooorrrdddd")
        );
    }

    @Test
    @DisplayName("409 when trying to guess word before round started")
    void guessWordWhenRoundNotActive() throws Exception {
        Game game = service.startNewGame();
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/guess", game.getId())
                .content("woord");

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("409 when trying to guess word when game over")
    void guessWordWhenGameOver() throws Exception {
        Game game = Game.createDefault();
        game.setState(new GameOverGameState());
        this.repository.save(game);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/trainer/games/{gameId}/guess", game.getId())
                .content("woord");

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Request the latest hint")
    void getLatestHint() throws Exception {
        Game game = service.startNewGame();
        service.startNewRound(game.getId());
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}/hint", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hint").exists());
    }

    @Test
    @DisplayName("409 if requesting the latest hint when no active round")
    void getLatestHintWhenNoActiveRound() throws Exception {
        Game game = service.startNewGame();
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}/hint", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }
    @Test
    @DisplayName("409 if requesting the latest hint when game over")
    void getLatestHintWhenGameOver() throws Exception {
        Game game = Game.createDefault();
        game.setState(new GameOverGameState());
        this.repository.save(game);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/trainer/games/{gameId}/hint", game.getId());

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }
}