package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.repository.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Game startNewGame() {
        return this.gameRepository.save(Game.createDefault());
    }

    public Game findGameById(UUID id) throws NotFoundException {
        return this.gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("No game found with id '%s'", id)));
    }

    public Round startNewRound(UUID gameId) throws NotFoundException {
        Game game = findGameById(gameId);
        String word = this.wordService.provideRandomWord(game.nextWordLength());
        Round round = game.startNewRound(word);
        this.gameRepository.save(game);
        return round;
    }

    public Hint guessWord(UUID gameId, String guess) throws NotFoundException {
        Game game = findGameById(gameId);
        Hint hint = game.guessWord(guess);
        this.gameRepository.save(game);
        return hint;
    }

    public Hint getLatestHint(UUID gameId) throws NotFoundException {
        return findGameById(gameId).latestHint();
    }
}
