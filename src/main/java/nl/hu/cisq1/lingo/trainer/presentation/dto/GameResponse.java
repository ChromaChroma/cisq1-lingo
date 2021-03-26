package nl.hu.cisq1.lingo.trainer.presentation.dto;

import java.util.UUID;

public class GameResponse {
    private final UUID id;
    private final int points;
    private final int roundsPlayed;

    public GameResponse(UUID id, int points, int roundsPlayed) {
        this.id = id;
        this.points = points;
        this.roundsPlayed = roundsPlayed;
    }

    public UUID getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }
}
