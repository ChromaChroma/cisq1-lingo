package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.repository.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.RoundState;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private static final SpringGameRepository mockRepository = mock(SpringGameRepository.class);
    private static final WordService mockWordService = mock(WordService.class);
    private static Game game;

    @BeforeAll
    static void beforeAll() {
        when(mockWordService.provideRandomWord(5))
                .thenReturn("woord");
        when(mockWordService.provideRandomWord(6))
                .thenReturn("waarde");
        when(mockWordService.provideRandomWord(7))
                .thenReturn("woorden");
    }

    @BeforeEach
    void setup() {
        game = Game.create();
        when(mockRepository.findById(any()))
                .thenReturn(Optional.of(game));
    }

    @AfterEach
    void tearDown() {
        Mockito.clearInvocations(mockWordService, mockRepository);
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
    void startNewRound() throws NotFoundException {
        UUID uuid = game.getId();
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        Round round = trainerService.startNewRound(uuid);

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockWordService, times(1)).provideRandomWord(5);
        verify(mockRepository, times(1)).save(any(Game.class));
        assertEquals(List.of('w','.','.','.','.'), round.getLatestHint().getHintSequence());
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Start a second round, correct")
    void startNewSecondRound() throws NotFoundException {
        game.startNewRound("woord");
        game.guessWord("woord");
        UUID uuid = game.getId();
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        Round round = trainerService.startNewRound(uuid);

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockWordService, times(1)).provideRandomWord(6);
        verify(mockRepository, times(1)).save(any(Game.class));
        assertEquals(List.of('w','.','.','.','.','.'), round.getLatestHint().getHintSequence());
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Start a third round, correct")
    void startNewThirdRound() throws NotFoundException {
        game.startNewRound("woord");
        game.guessWord("woord");
        game.startNewRound("waarde");
        game.guessWord("waarde");
        UUID uuid = game.getId();
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        Round round = trainerService.startNewRound(uuid);

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockWordService, times(1)).provideRandomWord(7);
        verify(mockRepository, times(1)).save(any(Game.class));
        assertEquals(List.of('w','.','.','.','.','.','.'), round.getLatestHint().getHintSequence());
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Start a next round and return to first length(5), correct")
    void startNewRoundAndStartsBackAtFive() throws NotFoundException {
        game.startNewRound("woord");
        game.guessWord("woord");
        game.startNewRound("waarde");
        game.guessWord("waarde");
        game.startNewRound("woorden");
        game.guessWord("woorden");
        UUID uuid = game.getId();
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        Round round = trainerService.startNewRound(uuid);

        verify(mockRepository, times(1)).findById(uuid);
        verify(mockWordService, times(1)).provideRandomWord(5);
        verify(mockRepository, times(1)).save(any(Game.class));
        assertEquals(List.of('w','.','.','.','.'), round.getLatestHint().getHintSequence());
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Throw when start a new round on non existing game")
    void startNewRoundOnNonExisting() {
        UUID uuid = game.getId();
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
        UUID uuid = game.getId();
        game.startNewRound("woord");
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
        UUID uuid = game.getId();
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
    @DisplayName("Get latest hint")
    void getLatestHint() {
        UUID uuid = game.getId();
        game.startNewRound("woord");
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertDoesNotThrow(
                () -> trainerService.getLatestHint(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }

    @Test
    @DisplayName("Throw when trying to get latest hint on non existing game")
    void getLatestHintInvalidGameIDThrows() {
        UUID uuid = game.getId();
        when(mockRepository.findById(any()))
                .thenReturn(Optional.empty());
        TrainerService trainerService = new TrainerService(mockRepository, mockWordService);

        assertThrows(
                NotFoundException.class,
                () -> trainerService.getLatestHint(uuid)
        );

        verify(mockRepository, times(1)).findById(uuid);
    }
}