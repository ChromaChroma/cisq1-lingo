package nl.hu.cisq1.lingo.trainer.presentation.controller;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Turn;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/games")
    public ResponseEntity<GameResponse> startNewGame() {
        Game game = this.trainerService.startNewGame();
        GameResponse response = new GameResponse();
        response.id = game.getId();
        response.points = game.getScore().getPoints();
        response.roundsPlayed = game.getScore().getRoundsPlayed();
        response.wordLength = game.getWordLength().currentLength();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/games/{gameId}/rounds")
    public ResponseEntity<Void> startNewRound( @PathVariable UUID gameId) throws NotFoundException {
        this.trainerService.startNewRound(gameId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/games/{gameId}/guess")
    public ResponseEntity<Hint> guessWord( @PathVariable UUID gameId, @RequestBody String guess) throws NotFoundException {
        Hint hint = this.trainerService.guessWord(gameId, guess);
        return new ResponseEntity<>(hint, HttpStatus.OK);
    }

    @GetMapping("/games/{gameId}/turn")
    public ResponseEntity<Turn> getCurrentTurn( @PathVariable UUID gameId) throws NotFoundException {
        Turn turn = this.trainerService.getCurrentTurn(gameId);
        return new ResponseEntity<>(turn, HttpStatus.OK);
    }

    @GetMapping("/games/{gameId}/hint")
    public ResponseEntity<Hint> getLatestHint( @PathVariable UUID gameId) throws NotFoundException {
        Hint hint = this.trainerService.getLatestHint(gameId);
        return new ResponseEntity<>(hint, HttpStatus.OK);
    }
}
