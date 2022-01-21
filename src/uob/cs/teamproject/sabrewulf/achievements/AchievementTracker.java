package uob.cs.teamproject.sabrewulf.achievements;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.map.Cell.PowerUpType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The AchievementTracker works in hand in hand with the {@link StatisticsTracker}. It receives data on game progress
 * by the player from the {@link StatisticsTracker}, and then processes which achievements should be awarded to the
 * player following on from this. Only single player gameplay is logged for achievements.
 */
public class AchievementTracker {

    private Map<Cell.PowerUpType, Integer> powerUpsUsed;
    private Map<DIFFICULTY, Integer> gamesCompleted;
    private Map<DIFFICULTY, Long> fastestTimeMillis; // (milliseconds)
    private boolean everCompletedGameWithoutPowerUps;
    private boolean everCompletedGameUndetected;
    private ArrayList<Integer> thresholdSet1;
    private ArrayList<Integer> thresholdSet2;
    private ArrayList<AchievementElement> achievementElements;
    private HashMap<String, Boolean> achievementsList;

    /**
     * Load entire history of player's single player gameplay which is needed to process which achievements should be
     * awarded to the player.
     * @param powerUpsUsed how many of each type of {@link PowerUpType} the player has used
     * @param gamesCompleted how many games of each type of {@link DIFFICULTY} the player has completed
     * @param fastestTimeMillis the fastest time for each type of {@link DIFFICULTY} the player has completed a game in
     * @param everCompletedGameWithoutPowerUps whether a player has ever completed a game without using powerups
     * @param everCompletedGameUndetected whether a player has ever completed a game without being detected by a bot
     */
    public AchievementTracker(Map<Cell.PowerUpType, Integer> powerUpsUsed, Map<DIFFICULTY, Integer> gamesCompleted,
                              Map<DIFFICULTY, Long> fastestTimeMillis,
                              boolean everCompletedGameWithoutPowerUps,
                              boolean everCompletedGameUndetected) {

        this.powerUpsUsed = powerUpsUsed;
        this.gamesCompleted = gamesCompleted;
        this.fastestTimeMillis = fastestTimeMillis;
        this.everCompletedGameWithoutPowerUps = everCompletedGameWithoutPowerUps;
        this.everCompletedGameUndetected = everCompletedGameUndetected;
        this.achievementsList = new HashMap<String, Boolean>();
        this.achievementElements = new ArrayList<AchievementElement>();

        thresholdSet1 = new ArrayList<>(Arrays.asList(1,10,25,50,100));
        thresholdSet2 = new ArrayList<>(Arrays.asList(10,5,3,2));

        setupAchievementsList();
        trackAllAchievements();
    }

    public HashMap<String, Boolean> getAchievementsList() { return achievementsList; }

    public ArrayList<AchievementElement> getAchievementElements() { return achievementElements; }

    /**
     *  Once a game has finished, load in updated game stats and track achievements.
     */
    public void updateAchievements(Map<Cell.PowerUpType, Integer> powerUpsUsed, Map<DIFFICULTY, Integer> gamesCompleted,
                                   Map<DIFFICULTY, Long> fastestTimeMillis, boolean everCompletedGameWithoutPowerUps,
                                   boolean everCompletedGameUndetected) {

        this.powerUpsUsed = powerUpsUsed;
        this.gamesCompleted = gamesCompleted;
        this.fastestTimeMillis = fastestTimeMillis;
        this.everCompletedGameWithoutPowerUps = everCompletedGameWithoutPowerUps;
        this.everCompletedGameUndetected = everCompletedGameUndetected;

        trackAllAchievements();

    }

    /**
     * Check for every achievement related to using a certain {@link PowerUpType} N times whether it should be awarded
     * to the player.
     * @param powerUpType which {@link PowerUpType} to check for relevant achievements to award against
     */
    public void trackUsedXPowerUpNTimes(Cell.PowerUpType powerUpType) {

        Integer usedPowerUpNTimes = powerUpsUsed.get(powerUpType);

        for (Integer threshold : thresholdSet1) {
            if (usedPowerUpNTimes >= threshold) {
                if (threshold == 1) {
                    if (!achievementsList.get("USED " + powerUpType.toString() + " POWERUP " + threshold + " TIME")) {
                        achievementsList.replace("USED " + powerUpType.toString() + " POWERUP " + threshold + " TIME",
                                Boolean.TRUE);
                        int index = ((powerUpType.ordinal() * 5) + thresholdSet1.indexOf(threshold));
                        AchievementElement newElement = achievementElements.get(index);
                        newElement.setAchieved(true);
                        achievementElements.set(index, newElement);
                    }
                }
                else if (!achievementsList.get("USED " + powerUpType.toString() + " POWERUP " + threshold + " TIMES")) {
                    achievementsList.replace("USED " + powerUpType.toString() + " POWERUP " + threshold + " TIMES",
                            Boolean.TRUE);
                    int index = ((powerUpType.ordinal() * 5) + thresholdSet1.indexOf(threshold));
                    AchievementElement newElement = achievementElements.get(index);
                    newElement.setAchieved(true);
                    achievementElements.set(index, newElement);
                }
            } else { break; }
        }
    }

    /**
     * Check for every achievement related to completing a game in a certain {@link DIFFICULTY} N times whether it
     * should be awarded to the player.
     * @param difficulty which {@link DIFFICULTY} to check for relevant achievements to award against
     */
    public void trackCompletedGameInXDifficultyNTimes(DIFFICULTY difficulty) {

        Integer completedGameNTimes = gamesCompleted.get(difficulty);

        for (Integer threshold : thresholdSet1) {
            if (completedGameNTimes >= threshold) {
                if (threshold == 1) {
                    if (!achievementsList.get("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY " + threshold +
                            " TIME")) {
                        achievementsList.replace("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY " + threshold +
                                " TIME", Boolean.TRUE);
                        int index = (15 + (difficulty.ordinal() * 5) + thresholdSet1.indexOf(threshold));
                        AchievementElement newElement = achievementElements.get(index);
                        newElement.setAchieved(true);
                        achievementElements.set(index, newElement);
                    }
                } else if (!achievementsList.get("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY " + threshold +
                            " TIMES")) {
                        achievementsList.replace("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY " + threshold +
                                " TIMES", Boolean.TRUE);
                        int index = (15 + (difficulty.ordinal() * 5) + thresholdSet1.indexOf(threshold));
                        AchievementElement newElement = achievementElements.get(index);
                        newElement.setAchieved(true);
                        achievementElements.set(index, newElement);
                } else {
                        break;
                }
            }
        }
    }

    /**
     * Check for every achievement related to completing a game of a certain {@link DIFFICULTY} in under a certain time
     * whether it should be awarded to the player.
     * @param difficulty which {@link DIFFICULTY} to check for relevant achievements to award against
     */
    public void trackCompletedGameInXDifficultyUnderNTime (DIFFICULTY difficulty){

        float completedGameInNMinutes = convertFromMillisToMins(fastestTimeMillis.get(difficulty));

        for (Integer threshold : thresholdSet2) {
            if (completedGameInNMinutes <= threshold) {
                if (!achievementsList.get("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY IN UNDER " +
                        threshold + " MINUTES")) {
                    achievementsList.replace("COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY IN UNDER " +
                            threshold + " MINUTES", Boolean.TRUE);
                    int index = (30 + (difficulty.ordinal() * 4) + thresholdSet2.indexOf(threshold));
                    AchievementElement newElement = achievementElements.get(index);
                    newElement.setAchieved(true);
                    achievementElements.set(index, newElement);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Check whether the achievement of ever completing a game without using any power ups should be awarded to the
     * player.
     */
    public void trackFinishedGameUsingNoPowerUps() {

        if (everCompletedGameWithoutPowerUps) {
            if (!achievementsList.get("FINISHED A GAME WITHOUT USING ANY POWERUPS")) {
                achievementsList.replace("FINISHED A GAME WITHOUT USING ANY POWERUPS", Boolean.TRUE);
                int index = 42;
                AchievementElement newElement = achievementElements.get(index);
                newElement.setAchieved(true);
                achievementElements.set(index, newElement);
            }
        }
    }

    /**
     * Check whether the achievement of ever completing a game without being detected by any bots should be awarded to
     * the player.
     */
    public void trackCompletedGameUndetected() {
        if (everCompletedGameUndetected) {
            if (!achievementsList.get("FINISHED A GAME WITHOUT BEING DETECTED")) {
                achievementsList.replace("FINISHED A GAME WITHOUT BEING DETECTED", Boolean.TRUE);
                int index = 43;
                AchievementElement newElement = achievementElements.get(index);
                newElement.setAchieved(true);
                achievementElements.set(index, newElement);
            }
        }
    }

    /**
     * Check whether the achievement of obtaining all achievements should be awarded to the player.
     */
    public void trackEarnedAllAchievements() {
        Boolean earnedAllAchievements = true;
        if (achievementElements.size() == 45) {
            for (int i = 0; i < 45; i++) {
                if(!achievementElements.get(i).getAchievement().equals("EARNED ALL ACHIEVEMENTS")) {
                    if (!achievementElements.get(i).getAchieved()) {
                        earnedAllAchievements = false;
                    }
                }
            }
            if (earnedAllAchievements) {
                if (!achievementsList.get("EARNED ALL ACHIEVEMENTS")) {
                    achievementsList.replace("EARNED ALL ACHIEVEMENTS", Boolean.TRUE);
                    int index = 44;
                    AchievementElement newElement = achievementElements.get(index);
                    newElement.setAchieved(true);
                    achievementElements.set(index, newElement);
                }
            }
        }
    }

    /**
     * Setup a list of all achievements and store whether the player has been awarded them.
     */
    private void setupAchievementsList() {

        AchievementElement newElement;
        String stringToAdd;
        Integer count = 0;

        for (Cell.PowerUpType powerUpType : Cell.PowerUpType.values()) {
            for(Integer threshold : thresholdSet1) {
                stringToAdd = "USED " + powerUpType.toString() + " POWERUP " + threshold;
                if(threshold == 1) {
                    stringToAdd = stringToAdd + " TIME";
                } else {
                    stringToAdd = stringToAdd + " TIMES";
                }
                achievementsList.put(stringToAdd, Boolean.FALSE);
                newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
                achievementElements.add(newElement);
            }
        }

        for (DIFFICULTY difficulty : DIFFICULTY.values()) {

            for(Integer threshold : thresholdSet1) {

                stringToAdd = "COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY " + threshold;
                if(threshold == 1) {
                    stringToAdd = stringToAdd + " TIME";
                } else {
                    stringToAdd = stringToAdd + " TIMES";
                }
                achievementsList.put(stringToAdd, Boolean.FALSE);
                newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
                achievementElements.add(newElement);
            }
        }

        for (DIFFICULTY difficulty : DIFFICULTY.values()) {
            for(Integer threshold : thresholdSet2) {
                stringToAdd = "COMPLETED GAME IN " + difficulty.toString() + " DIFFICULTY IN UNDER " + threshold
                        + " MINUTES";
                achievementsList.put(stringToAdd, Boolean.FALSE);
                newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
                achievementElements.add(newElement);
            }
        }

        stringToAdd = "FINISHED A GAME WITHOUT USING ANY POWERUPS";
        achievementsList.put(stringToAdd, Boolean.FALSE);
        newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
        achievementElements.add(newElement);
        stringToAdd = "FINISHED A GAME WITHOUT BEING DETECTED";
        achievementsList.put(stringToAdd, Boolean.FALSE);
        newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
        achievementElements.add(newElement);
        stringToAdd = "EARNED ALL ACHIEVEMENTS";
        achievementsList.put(stringToAdd, Boolean.FALSE);
        newElement = new AchievementElement(stringToAdd,Boolean.FALSE);
        achievementElements.add(newElement);
    }

    /**
     *  Call all achievement checking methods here to check them all in one go, check every achievement for whether
     *  the player is eligible to be awarded it.
     */
    private void trackAllAchievements() {

        for (Cell.PowerUpType powerUpType : Cell.PowerUpType.values()) {
            trackUsedXPowerUpNTimes(powerUpType);
        }

        for (DIFFICULTY difficulty : DIFFICULTY.values()) {
            trackCompletedGameInXDifficultyNTimes(difficulty);
            trackCompletedGameInXDifficultyUnderNTime(difficulty);
        }

        trackFinishedGameUsingNoPowerUps();
        trackCompletedGameUndetected();
        trackEarnedAllAchievements();

    }

    /**
     * Convert a time from milliseconds into minutes.
     * @param millisTime time in milliseconds
     * @return time converted into minutes
     */
    private float convertFromMillisToMins(Long millisTime) { return ((float) millisTime / (1000*60)); }

    public Integer getNumAchievements() {
        Integer sumAchievements = 0;
        for(AchievementElement achievement : achievementElements) {
            if(achievement.getAchieved()) { sumAchievements++; }
        }
        return sumAchievements;
    }
}