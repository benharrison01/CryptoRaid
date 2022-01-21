package test.cs.teamproject.sabrewulf.achievements;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class StatisticsTrackerTest {

    @Test
    public void testStatisticsTrackerOperation() {

        StatisticsTracker statisticsTracker = new StatisticsTracker("src/test/cs/teamproject/sabrewulf/achievements/statistics_tracker_test_1.json");

        statisticsTracker.noteGameStarted();

        statisticsTracker.notePowerUpUsed(Cell.PowerUpType.SPEEDUP);
        statisticsTracker.notePowerUpUsed(Cell.PowerUpType.ADDLIFE);
        statisticsTracker.notePowerUpUsed(Cell.PowerUpType.INVISIBILITY);
        statisticsTracker.noteDetected();
        statisticsTracker.noteGameFinished(DIFFICULTY.EASY, 250000);

        statisticsTracker.writeStatsToFile("src/test/cs/teamproject/sabrewulf/achievements/statistics_tracker_test_2.json");

        File file2Object = new
                File("src/test/cs/teamproject/sabrewulf/achievements/statistics_tracker_test_2.json");
        File file3Object = new
                File("src/test/cs/teamproject/sabrewulf/achievements/statistics_tracker_test_3.json");

        try {
            byte[] file2 = Files.readAllBytes(file2Object.toPath());
            byte[] file3 = Files.readAllBytes(file3Object.toPath());
            assertEquals(true, Arrays.equals(file2, file3));
        } catch (IOException e) {
            e.printStackTrace();
        }

        statisticsTracker.updateAchievements();

        Set<String> awardedAchievements = statisticsTracker.getAwardedAchievements();

        awardedAchievements.remove("COMPLETED GAME IN EASY DIFFICULTY 1 TIME");
        awardedAchievements.remove("COMPLETED GAME IN EASY DIFFICULTY 10 TIMES");
        awardedAchievements.remove("COMPLETED GAME IN MEDIUM DIFFICULTY 1 TIME");
        awardedAchievements.remove("COMPLETED GAME IN HARD DIFFICULTY 1 TIME");
        awardedAchievements.remove("USED SPEEDUP POWERUP 1 TIME");
        awardedAchievements.remove("USED SPEEDUP POWERUP 10 TIMES");
        awardedAchievements.remove("USED ADDLIFE POWERUP 1 TIME");
        awardedAchievements.remove("USED ADDLIFE POWERUP 10 TIMES");
        awardedAchievements.remove("USED INVISIBILITY POWERUP 1 TIME");
        awardedAchievements.remove("USED INVISIBILITY POWERUP 10 TIMES");
        awardedAchievements.remove("COMPLETED GAME IN EASY DIFFICULTY IN UNDER 10 MINUTES");
        awardedAchievements.remove("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 10 MINUTES");
        awardedAchievements.remove("COMPLETED GAME IN HARD DIFFICULTY IN UNDER 10 MINUTES");

        assertEquals(0, awardedAchievements.size());

    }
}

