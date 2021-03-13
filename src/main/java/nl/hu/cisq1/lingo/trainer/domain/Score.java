package nl.hu.cisq1.lingo.trainer.domain;

public class Score {
    private Integer points;
    private Integer roundsPlayed;

    public static Score empty() {
        return new Score(0, 0);
    }

    public Score(Integer points, Integer roundsPlayed) {
        if (points < 0 || roundsPlayed < 0) throw new IllegalArgumentException("Points and playedRounds cannot be smaller than 0");
        this.points = points;
        this.roundsPlayed = roundsPlayed;
    }

    public Integer getPoints() { return points; }

    public Integer getRoundsPlayed() { return roundsPlayed; }

    public void increaseRoundsPlayed(){
        roundsPlayed += 1;
    }

    public void increasePoints(Integer points) {
        if (points < 0) throw new IllegalArgumentException("Points cannot be 0 or below");
        this.points += points;
    }
}
