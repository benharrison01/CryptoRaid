package uob.cs.teamproject.sabrewulf.achievements;

import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The statistics tracker records data about the user's single player games by noting events which occur during these
 * games. It can read and write these statistics to a JSON file so that they persist between games.
 */
public class StatisticsTracker {

    private boolean fileHasBeenRead;
    private final String path;

    /* the statistics being tracked */
    private final Map<Cell.PowerUpType, Integer> powerUpsUsed;
    private final Map<DIFFICULTY, Integer> gamesCompleted;
    private final Map<DIFFICULTY, Long> fastestTimeMillis; // (milliseconds)
    private boolean hasUsedPowerUpsThisGame;
    private boolean everCompletedGameWithoutPowerUps;
    private boolean hasBeenDetectedThisGame;
    private boolean everCompletedGameUndetected;

    /* objects / data structures for achievements and whether player has achieved them */
    private final AchievementTracker achievementTracker;

    /**
     * Constructor for object
     * @param path the file path to read from
     */
    public StatisticsTracker(String path) {
        this.path = path;
        fileHasBeenRead = false;
        powerUpsUsed = new HashMap<>();
        gamesCompleted = new HashMap<>();
        fastestTimeMillis = new HashMap<>();
        readStatsFromFile();
        achievementTracker = new AchievementTracker(powerUpsUsed, gamesCompleted, fastestTimeMillis,
                everCompletedGameWithoutPowerUps, everCompletedGameUndetected);
    }

    public ArrayList<AchievementElement> getAchievementElements() { return achievementTracker.getAchievementElements(); }

    /**
     * Read the player's statistics from a file.
     */
    public void readStatsFromFile() {
        String json = "";
        try {
            File file = new File(path);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                json = json + myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        try {
            json = json.substring(2,json.length()-2);
            String lifePowerUp = json.split(",")[0];
            lifePowerUp = lifePowerUp.replaceAll("\"health\":","");
            lifePowerUp = lifePowerUp.substring(1,lifePowerUp.length()-1);
            powerUpsUsed.put(Cell.PowerUpType.ADDLIFE, Integer.valueOf(lifePowerUp));

            String invisibilityPowerUp = json.split(",")[1];
            invisibilityPowerUp = invisibilityPowerUp.replaceAll("\"invisibility\":","");
            invisibilityPowerUp = invisibilityPowerUp.substring(1,invisibilityPowerUp.length()-1);
            powerUpsUsed.put(Cell.PowerUpType.INVISIBILITY, Integer.valueOf(invisibilityPowerUp));

            String speedPowerUp = json.split(",")[2];
            speedPowerUp = speedPowerUp.replaceAll("\"speedup\":","");
            speedPowerUp = speedPowerUp.substring(1,speedPowerUp.length()-1);
            powerUpsUsed.put(Cell.PowerUpType.SPEEDUP, Integer.valueOf(speedPowerUp));

            String easyCompleted = json.split(",")[3];
            easyCompleted = easyCompleted.replaceAll("\"easyCompleted\":","");
            easyCompleted = easyCompleted.substring(1,easyCompleted.length()-1);
            gamesCompleted.put(DIFFICULTY.EASY, Integer.valueOf(easyCompleted));

            String mediumCompleted = json.split(",")[4];
            mediumCompleted = mediumCompleted.replaceAll("\"mediumCompleted\":","");
            mediumCompleted = mediumCompleted.substring(1,mediumCompleted.length()-1);
            gamesCompleted.put(DIFFICULTY.MEDIUM, Integer.valueOf(mediumCompleted));

            String hardCompleted = json.split(",")[5];
            hardCompleted = hardCompleted.replaceAll("\"hardCompleted\":","");
            hardCompleted = hardCompleted.substring(1,hardCompleted.length()-1);
            gamesCompleted.put(DIFFICULTY.HARD, Integer.valueOf(hardCompleted));

            String easyFastest = json.split(",")[6];
            easyFastest = easyFastest.replaceAll("\"easyFastest\":","");
            easyFastest = easyFastest.substring(1,easyFastest.length()-1);
            fastestTimeMillis.put(DIFFICULTY.EASY, Long.valueOf(easyFastest));

            String mediumFastest = json.split(",")[7];
            mediumFastest = mediumFastest.replaceAll("\"mediumFastest\":","");
            mediumFastest = mediumFastest.substring(1,mediumFastest.length()-1);
            fastestTimeMillis.put(DIFFICULTY.MEDIUM, Long.valueOf(mediumFastest));

            String hardFastest = json.split(",")[8];
            hardFastest = hardFastest.replaceAll("\"hardFastest\":","");
            hardFastest = hardFastest.substring(1,hardFastest.length()-1);
            fastestTimeMillis.put(DIFFICULTY.HARD, Long.valueOf(hardFastest));

            String withoutPowerUps = json.split(",")[9];
            withoutPowerUps = withoutPowerUps.replaceAll("\"withoutPowerUps\":","");
            withoutPowerUps = withoutPowerUps.substring(1,withoutPowerUps.length()-1);
            everCompletedGameWithoutPowerUps = Boolean.valueOf(withoutPowerUps);

            String withoutDetected = json.split(",")[10];
            withoutDetected = withoutDetected.replaceAll("\"withoutDetected\":","");
            withoutDetected = withoutDetected.substring(1,withoutDetected.length()-1);
            everCompletedGameUndetected = Boolean.valueOf(withoutDetected);
        }
        catch (Exception e) {
            powerUpsUsed.putIfAbsent(Cell.PowerUpType.ADDLIFE, 0);
            powerUpsUsed.putIfAbsent(Cell.PowerUpType.INVISIBILITY, 0);
            powerUpsUsed.putIfAbsent(Cell.PowerUpType.SPEEDUP, 0);
            gamesCompleted.putIfAbsent(DIFFICULTY.EASY, 0);
            gamesCompleted.putIfAbsent(DIFFICULTY.MEDIUM, 0);
            gamesCompleted.putIfAbsent(DIFFICULTY.HARD, 0);
            fastestTimeMillis.putIfAbsent(DIFFICULTY.EASY, 1000000000L);
            fastestTimeMillis.putIfAbsent(DIFFICULTY.MEDIUM, 1000000000L);
            fastestTimeMillis.putIfAbsent(DIFFICULTY.HARD, 1000000000L);
            everCompletedGameWithoutPowerUps = false;
            everCompletedGameUndetected = false;
        }
        fileHasBeenRead = true;
    }

    /**
     * Save the player's statistics to a file.
     * @param path the file path to write to
     */
    public void writeStatsToFile(String path) {
        String json = "[";
        json = json + "{"
                + toJSON("health", powerUpsUsed.get(Cell.PowerUpType.ADDLIFE)) + ","
                + toJSON("invisibility", powerUpsUsed.get(Cell.PowerUpType.INVISIBILITY)) + ","
                + toJSON("speedup", powerUpsUsed.get(Cell.PowerUpType.SPEEDUP)) + ","
                + toJSON("easyCompleted", gamesCompleted.get(DIFFICULTY.EASY)) + ","
                + toJSON("mediumCompleted", gamesCompleted.get(DIFFICULTY.MEDIUM)) + ","
                + toJSON("hardCompleted", gamesCompleted.get(DIFFICULTY.HARD)) + ","
                + toJSON("easyFastest", fastestTimeMillis.get(DIFFICULTY.EASY).toString()) + ","
                + toJSON("mediumFastest", fastestTimeMillis.get(DIFFICULTY.MEDIUM).toString()) + ","
                + toJSON("hardFastest", fastestTimeMillis.get(DIFFICULTY.HARD).toString()) + ","
                + toJSON("withoutPowerUps", everCompletedGameWithoutPowerUps) + ","
                + toJSON("withoutDetected", everCompletedGameUndetected) + "},";
        json = json.substring(0, json.length()-1);
        json = json + "]";
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Inform the achievement system that a game has started.
     */
    public void noteGameStarted() {
        hasUsedPowerUpsThisGame = false;
        hasBeenDetectedThisGame = false;
    }

    /**
     * Inform the achievement system that the player has used a powerup.
     * @param type the type of powerup which was used
     */
    public void notePowerUpUsed(Cell.PowerUpType type) {
        assert fileHasBeenRead;
        if(powerUpsUsed.get(type) != null) {
            powerUpsUsed.put(type, powerUpsUsed.get(type) + 1);
        } else {
            powerUpsUsed.put(type, 1);
        }
        hasUsedPowerUpsThisGame = true;
    }

    /**
     * Inform the achievement system that the player has been detected by an enemy.
     */
    public void noteDetected() {
        assert fileHasBeenRead;
        hasBeenDetectedThisGame = true;
    }

    /**
     * Inform the achievement system that a game has been finished.
     * @param difficulty the difficulty of the finished game
     * @param timeTakenMillis the duration of the finished game in milliseconds
     */
    public void noteGameFinished(DIFFICULTY difficulty, long timeTakenMillis) {
        assert fileHasBeenRead;

        if(gamesCompleted.get(difficulty) != null) {
            gamesCompleted.put(difficulty, gamesCompleted.get(difficulty) + 1);
        } else {
            gamesCompleted.put(difficulty, 1);
        }

        long currentBestTime;

        if(fastestTimeMillis.get(difficulty) != null) {
            currentBestTime = fastestTimeMillis.get(difficulty);
        } else {
            currentBestTime = 0;
        }
        if (timeTakenMillis < currentBestTime) {
            fastestTimeMillis.put(difficulty, timeTakenMillis);
        }

        if (!everCompletedGameWithoutPowerUps && !hasUsedPowerUpsThisGame) {
            everCompletedGameWithoutPowerUps = true;
        }

        if (!everCompletedGameUndetected && !hasBeenDetectedThisGame) {
            everCompletedGameUndetected = true;
        }
    }

    /**
     * Use to get an up-to-date set of achievements awarded to the player.
     * @return achievements, set of achievements which the player has been awarded using the latest game statistics
     */
    public Set<String> getAwardedAchievements() {
        Set<String> awardedAchievements = new HashSet<>();

        /* for each achievement */
        for (Map.Entry<String, Boolean> achievement : achievementTracker.getAchievementsList().entrySet()) {
            if(achievement.getValue()) { /* if achievement has been awarded */
                awardedAchievements.add(achievement.getKey());
            }
        }
        return awardedAchievements;
    }

    /**
     * Once a game has finished, load in updated game statistics to {@link AchievementTracker} and track if any new
     * achievements should be awarded to the player.
     */
    public void updateAchievements() { achievementTracker.updateAchievements(powerUpsUsed, gamesCompleted, fastestTimeMillis,
            everCompletedGameWithoutPowerUps, everCompletedGameUndetected); }

    /** Helper function which converts a field into a JSON format.
     * @param fieldName: name of the field.
     * @param value: name of the value.
     * @return "fieldName":"value"
     */
    private String toJSON(String fieldName, Object value) {
        return "\"" + fieldName +"\":" + "\"" + value.toString() + "\"";
    }
}