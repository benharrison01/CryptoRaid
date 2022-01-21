package uob.cs.teamproject.sabrewulf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import uob.cs.teamproject.sabrewulf.components.Inventory;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.ui.selectors.WINDOWSIZE;

/** Game settings class stores all data values associated with the setup of the current game. All fields
 *  and methods are static so can be accessed directly without needing to access an object of this class.
 */
public class GameSettings {

    private static MODE gameMode;
    private static DIFFICULTY difficulty;
    private static MODEL model;
    private static WINDOWSIZE windowSize;
    private static final DoubleProperty musicVolume = new SimpleDoubleProperty();
    private static final DoubleProperty fxVolume = new SimpleDoubleProperty();
    private static int numPlayers;
    private static int screenWidth;
    private static int screenHeight;
    private static GameStateOwner gameStateOwner;
    private static String username;
    private static Inventory inventory;
    private static Boolean pauseMenuDisabled;

    /** Initialise the values for the game settings
     */
    public static void initialise() {
        GameSettings.musicVolume.set(0.2);
        GameSettings.fxVolume.set(0.2);
        GameSettings.numPlayers = 1;
        GameSettings.windowSize = WINDOWSIZE.WINDOWED;
        GameSettings.username = "";
        GameSettings.pauseMenuDisabled = true;
    }

    /** Reset values of game settings variables when returning to the main menu at the end of a game
     */
    public static void onReturnToMenu() {
        GameSettings.gameMode = MODE.SINGLEPLAYER;
        GameSettings.difficulty = DIFFICULTY.EASY;
        GameSettings.model = MODEL.SERVER;
        GameSettings.numPlayers = 1;
        GameSettings.username = "";
        GameSettings.inventory = null;
        GameSettings.pauseMenuDisabled = true;
    }

    /** Set value of game mode (single player or multiplayer)
     * @param gameMode - New value of game mode
     */
    public static void setGameMode(MODE gameMode) {
        GameSettings.gameMode = gameMode;
    }

    /** Set value of difficulty (easy, medium or hard)
     * @param difficulty - New value of game difficulty
     */
    public static void setDifficulty(DIFFICULTY difficulty) {
        GameSettings.difficulty = difficulty;
    }

    /** Set value of network model (server or client)
     * @param model - New value of network model
     */
    public static void setModel(MODEL model) {
        GameSettings.model = model;
    }

    /** Set value of window size (windowed or full screen)
     * @param windowSize - New value of window size
     */
    public static void setWindowSize(WINDOWSIZE windowSize) {
        GameSettings.windowSize = windowSize;
    }

    /** Set music volume value - only accepts value between 0.0 and 1.0
     * @param musicVolume - New music volume value
     */
    public static void setMusicVolume(double musicVolume) {
        if (volValid(musicVolume)) {
            GameSettings.musicVolume.set(musicVolume);
        }
    }

    /** Set FX volume - only accepts value between 0.0 and 1.0
     * @param fxVolume - New FX volume value
     */
    public static void setFxVolume(double fxVolume) {
        if (volValid(fxVolume)) {
            GameSettings.fxVolume.set(fxVolume);
        }
    }

    /** Set the number of game players (only accepts values between 1 and 4)
     * @param numPlayers - Number of players
     */
    public static void setNumPlayers(int numPlayers) {
        if (numPlayers < 5 && numPlayers > 0) {
            GameSettings.numPlayers = numPlayers;
        }
    }

    /** Set the width dimension of the player's screen
     * @param screenWidth - Width of player's screen
     */
    public static void setScreenWidth(int screenWidth) {
        GameSettings.screenWidth = screenWidth;
    }

    /** Set the height dimension of the player's screen
     * @param screenHeight - Height of player's screen
     */
    public static void setScreenHeight(int screenHeight) {
        GameSettings.screenHeight = screenHeight;
    }

    /** Store the object which manages the overall state of the game
     * @param gameStateOwner - the object which manages the game state
     */
    public static void setGameStateOwner(GameStateOwner gameStateOwner) {
        GameSettings.gameStateOwner = gameStateOwner;
    }

    /** Set the username chosen by the player
     * @param username - Username chosen by player
     */
    public static void setUsername(String username) {
        GameSettings.username = username;
    }

    /** Set the inventory for the player
     * @param inventory - Inventory for the player
     */
    public static void  setInventory(Inventory inventory) { GameSettings.inventory = inventory; }

    /** Set whether the menu should be disabled
     * @param disablePauseMenu - Boolean whether menu should be active
     */
    public static void setPauseMenuDisabled(Boolean disablePauseMenu) {
        GameSettings.pauseMenuDisabled = disablePauseMenu;
    }

    /** @return Current value of game mode
     */
    public static MODE getGameMode() {
        return gameMode;
    }

    /** @return Current value of game difficulty
     */
    public static DIFFICULTY getDifficulty() {
        return difficulty;
    }

    /** @return Current value of network model
     */
    public static MODEL getModel() {
        return model;
    }

    /** @return Current value of window size
     */
    public static WINDOWSIZE getWindowSize() {
        return windowSize;
    }

    /** @return Current background music volume
     */
    public static double getMusicVolume() {
        return musicVolume.get();
    }

    /** @return Current FX volume
     */
    public static double getFxVolume() {
        return fxVolume.get();
    }

    /** @return Current number of players selected
     */
    public static int getNumPlayers() {
        return numPlayers;
    }

    /** @return Returns the width of the player's screen
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /** @return Return's the height of the player's screen
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /** @return Return an interface for managing the game state (e.g. pausing, unpausing, returning to start menu)
     */
    public static GameStateOwner getGameStateOwner() {
        return gameStateOwner;
    }

    /** @return Return the player's username
     */
    public static String getUsername() {
        return username;
    }

    /** @return Return the inventory for the player
     */
    public static Inventory getInventory() {
        return inventory;
    }

    /** @return Return whether the menu should be disabled
     */
    public static Boolean getPauseMenuDisabled() {
        return pauseMenuDisabled;
    }

    /** Boolean method to check whether a given value is a valid volume (between 0.0 and 1.0)
     * @param vol - Volume value to be verified
     * @return Whether volume is valid
     */
    private static boolean volValid(double vol) {
        return vol >= 0 && vol <= 1.0;
    }

    /** @return Returns the volume property of the music volume
     */
    public static DoubleProperty volumeProperty() {
        return musicVolume;
    }

    /** @return Returns the volume property of the FX volume
     */
    public static DoubleProperty fxVolumeProperty() {
        return fxVolume;
    }
}
