package test.cs.teamproject.sabrewulf.achievements;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.achievements.AchievementElement;
import uob.cs.teamproject.sabrewulf.achievements.AchievementTracker;
import uob.cs.teamproject.sabrewulf.leaderboards.LeaderboardElement;
import uob.cs.teamproject.sabrewulf.leaderboards.StoredLeaderboard;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AchievementTrackerTest {

    @Test
    public void achievementsCombination1() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(0,0,0);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(0, 0, 0);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)1000000, (long)1000000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(0, achievementTracker.getNumAchievements(), "No achievements should have been awarded");
    }

    @Test
    public void achievementsCombination2() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(5,12,30);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(5, 12, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)1000000, (long)120000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(16, achievementTracker.getNumAchievements(), "No achievements should have been awarded");

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED INVISIBILITY POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED INVISIBILITY POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 10 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 5 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 3 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 2 MINUTES"));

    }

    @Test
    public void achievementsCombination3() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(75,1,300);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(70, 420, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)240000, (long)100000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(28, achievementTracker.getNumAchievements(), "28 achievements should have been awarded");

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 25 TIMES"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 50 TIMES"));
        assertEquals(true, achievementsList.get("USED INVISIBILITY POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 25 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 50 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 100 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 100 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY IN UNDER 10 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY IN UNDER 5 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 10 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 5 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 3 MINUTES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY IN UNDER 2 MINUTES"));

    }

    @Test
    public void testGetAchievementElements() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(56,2,38);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(70, 420, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)1000000, (long)1000000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(45, achievementTracker.getAchievementElements().size(), "getAchievementElements should " +
                "return a list of 45 AchievementElement objects");

    }

    @Test
    public void testUpdateAchievements() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(0,0,0);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(70, 420, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)1000000, (long)1000000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(12, achievementTracker.getNumAchievements(), "12 achievements should have been " +
                "awarded prior to the updated values being inputted");

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 100 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 25 TIMES"));

        powerUpsUsed = initialisePowerUpsUsed(75,1,300);

        achievementTracker.updateAchievements(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, false);

        assertEquals(22, achievementTracker.getNumAchievements(), "22 achievements should have been " +
                "awarded after the updated values were inputted");

        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 25 TIMES"));
        assertEquals(true, achievementsList.get("USED SPEEDUP POWERUP 50 TIMES"));
        assertEquals(true, achievementsList.get("USED INVISIBILITY POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 1 TIME"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 10 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 25 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 50 TIMES"));
        assertEquals(true, achievementsList.get("USED ADDLIFE POWERUP 100 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN EASY DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 25 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 50 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN MEDIUM DIFFICULTY 100 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 1 TIME"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 10 TIMES"));
        assertEquals(true, achievementsList.get("COMPLETED GAME IN HARD DIFFICULTY 25 TIMES"));

    }

    @Test
    public void testTrackFinishedGameUsingNoPowerUps() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(75,1,300);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(70, 420, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)240000, (long)100000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                true, false);

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("FINISHED A GAME WITHOUT USING ANY POWERUPS"),
                "achievement of finishing a game without using any powerups should've been awarded");

    }

    @Test
    public void testTrackCompletedGameUndetected() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(75,1,300);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(670, 420, 30);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)240000, (long)100000, (long)1000000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                false, true);

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("FINISHED A GAME WITHOUT BEING DETECTED"),
                "achievement of finishing a game without being detected should've been awarded");

    }

    @Test
    public void testEarnedAllAchievements() {

        Map<Cell.PowerUpType, Integer> powerUpsUsed = initialisePowerUpsUsed(1000,1000,1000);
        Map<DIFFICULTY, Integer> gamesCompleted = initialiseGamesCompleted(1000, 1000, 1000);
        Map<DIFFICULTY, Long> fastestTimeMillis = initialiseFastestTimeMillis((long)1000, (long)1000, (long)1000);

        AchievementTracker achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                true, true);

        HashMap<String, Boolean> achievementsList = achievementTracker.getAchievementsList();

        assertEquals(true, achievementsList.get("EARNED ALL ACHIEVEMENTS"),
                "achievement of earning all achievements should've been awarded");

    }

    public Map<Cell.PowerUpType, Integer> initialisePowerUpsUsed(int speedUpNo, int invisibilityNo, int addLifeNo) {
        Map<Cell.PowerUpType, Integer> powerUpsUsed = new HashMap<>();
        powerUpsUsed.put(Cell.PowerUpType.SPEEDUP, speedUpNo);
        powerUpsUsed.put(Cell.PowerUpType.INVISIBILITY, invisibilityNo);
        powerUpsUsed.put(Cell.PowerUpType.ADDLIFE, addLifeNo);

        return powerUpsUsed;
    }

    public Map<DIFFICULTY, Integer> initialiseGamesCompleted(int easyCompletedNo, int mediumCompletedNo,
                                                             int hardCompletedNo) {
        Map<DIFFICULTY, Integer> gamesCompleted = new HashMap<>();
        gamesCompleted.put(DIFFICULTY.EASY, easyCompletedNo);
        gamesCompleted.put(DIFFICULTY.MEDIUM, mediumCompletedNo);
        gamesCompleted.put(DIFFICULTY.HARD, hardCompletedNo);

        return gamesCompleted;
    }

    public Map<DIFFICULTY, Long> initialiseFastestTimeMillis(Long easyFastestTime, Long mediumFastestTime,
                                                             Long hardFastestTime) {
        Map<DIFFICULTY, Long> fastestTimeMillis = new HashMap<>();
        fastestTimeMillis.put(DIFFICULTY.EASY, easyFastestTime);
        fastestTimeMillis.put(DIFFICULTY.MEDIUM, mediumFastestTime);
        fastestTimeMillis.put(DIFFICULTY.HARD, hardFastestTime);

        return fastestTimeMillis;
    }

}
