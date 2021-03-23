package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.repository.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private static SpringGameRepository mockRepository;
    private static WordService mockWordService;

    @BeforeEach
    void setup() {
        mockRepository = mock(SpringGameRepository.class);
        mockWordService = mock(WordService.class);
    }

    @Test
    @DisplayName("Start new game, correct")
    void startNewGame() {
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(trainerService::startNewGame);

        verify(mockRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("Start a new round, correct")
    void startNewRound() {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.of(Game.create()));
        when(mockWordService.provideRandomWord(5))
                .thenReturn("woord");
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(
                () -> trainerService.startNewRound(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockWordService, times(1)).provideRandomWord(5);
        verify(mockRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("Throw when start a new round on non existing game")
    void startNewRoundOnNonExisting() {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.empty());
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertThrows(
                NotFoundException.class,
                () -> trainerService.startNewRound(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }

    @Test
    @DisplayName("Guess word")
    void guessWord() {
        UUID uuid = UUID.randomUUID();
        Game game = Game.create();
        game.startNewRound("woord");
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.of(game));
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(
                () -> trainerService.guessWord(uuid, "woord")
        );

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("Throw when guess word on non existing game")
    void guessWordInvalidGameIDThrows() {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.empty());
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertThrows(
                NotFoundException.class,
                () -> trainerService.guessWord(uuid, "woord")
        );

        verify(mockRepository, times(1)).findById(uuid);
    }

    @Test
    @DisplayName("Get current turn")
    void getCurrentTurn() {
        UUID uuid = UUID.randomUUID();
        Game game = Game.create();
        game.startNewRound("woord");
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.of(game));
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(
                () -> trainerService.getCurrentTurn(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }

    @Test
    @DisplayName("Throw when trying to get current turn on non existing game")
    void getCurrentTurnInvalidGameIDThrows() {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.empty());
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertThrows(
                NotFoundException.class,
                () -> trainerService.getCurrentTurn(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }


    @Test
    @DisplayName("Get latest hint")
    void getLatestHint() {
        UUID uuid = UUID.randomUUID();
        Game game = Game.create();
        game.startNewRound("woord");
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.of(game));
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(
                () -> trainerService.getLatestHint(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }

    @Test
    @DisplayName("Throw when trying to get latest hint on non existing game")
    void getLatestHintInvalidGameIDThrows() {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.findById(uuid))
                .thenReturn(Optional.empty());
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertThrows(
                NotFoundException.class,
                () -> trainerService.getLatestHint(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }
}