package uob.cs.teamproject.sabrewulf.leaderboards;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents one entry into a leaderboard.
 */
public class LeaderboardElement {

    private String name;
    private Integer score;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private String time;

    /**
     * Constructor for a LeaderboardElement.
     * @param name: name of player.
     * @param score: score player achieved.
     * @param time: the date and time the score was achieved.
     */
    public LeaderboardElement(String name, int score, LocalDateTime time) {
        this.name = name;
        this.score = score;
        if (time == null) {
            this.time = "Not recorded";
        }
        else {
            this.time = dtf.format(time);
        }
    }

    /**
     *
     * @return name of player belonging to this leaderboard element.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return score achieved belonging to this leaderboard element.
     */
    public Integer getScore() {
        return score;
    }

    /**
     *
     * @return time that this entry was achieved.
     */
    public String getTime() {
        return time;
    }
}
