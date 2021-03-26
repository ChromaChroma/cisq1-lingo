package nl.hu.cisq1.lingo.trainer.presentation.controller;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.game.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponse;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundResponse;
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
        GameResponse response = new GameResponse(
                game.getId(),
                game.getScore().getPoints(),
                game.getScore().getRoundsPlayed()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable UUID gameId) throws NotFoundException {
        Game game = this.trainerService.findGameById(gameId);
        GameResponse response = new GameResponse(
                game.getId(),
                game.getScore().getPoints(),
                game.getScore().getRoundsPlayed()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/games/{gameId}/rounds")
    public ResponseEntity<RoundResponse> startNewRound(@PathVariable UUID gameId) throws NotFoundException {
        Round round = this.trainerService.startNewRound(gameId);
        RoundResponse response = new RoundResponse();
        response.hint = round.getLatestHint().getHintSequence();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/games/{gameId}/guess")
    public ResponseEntity<RoundResponse> guessWord( @PathVariable UUID gameId, @RequestBody String guess) throws NotFoundException {
        Hint hint = this.trainerService.guessWord(gameId, guess);
        RoundResponse response = new RoundResponse();
        response.hint = hint.getHintSequence();
        response.guess = guess;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/games/{gameId}/hint")
    public ResponseEntity<RoundResponse> getLatestHint( @PathVariable UUID gameId) throws NotFoundException {
        Hint hint = this.trainerService.getLatestHint(gameId);
        RoundResponse response = new RoundResponse();
        response.hint = hint.getHintSequence();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
