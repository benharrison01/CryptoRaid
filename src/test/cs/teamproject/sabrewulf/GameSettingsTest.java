package test.cs.teamproject.sabrewulf;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;
import uob.cs.teamproject.sabrewulf.Game;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.components.Inventory;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.ui.selectors.WINDOWSIZE;

import static org.junit.jupiter.api.Assertions.*;

public class GameSettingsTest extends Application {

    Stage mainStage;

    @Test
    public void testClassSetup() {
        assertNull(GameSettings.getDifficulty());
        assertNull(GameSettings.getGameMode());
        assertEquals(0.0, GameSettings.getFxVolume());
        assertEquals(0.0, GameSettings.getMusicVolume());
        assertNull(GameSettings.getWindowSize());
        assertNull(GameSettings.getModel());
        assertEquals(0, GameSettings.getNumPlayers());
        assertEquals(0, GameSettings.getScreenWidth());
        assertEquals(0, GameSettings.getScreenHeight());
        assertNull(GameSettings.getGameStateOwner());
        assertNull(GameSettings.getUsername());
        assertNull(GameSettings.getInventory());
    }

    @Test
    public void testInitialise() {
        Game game = new Game();
        GameSettings.initialise();
        GameSettings.setGameStateOwner(game);
        assertEquals(game, GameSettings.getGameStateOwner());
        assertEquals(0.2, GameSettings.getMusicVolume());
        assertEquals(0.2, GameSettings.getFxVolume());
        assertEquals(1, GameSettings.getNumPlayers());
        assertEquals(WINDOWSIZE.WINDOWED, GameSettings.getWindowSize());
        assertEquals("", GameSettings.getUsername());
        assertEquals(0, GameSettings.getScreenHeight());
        assertEquals(0, GameSettings.getScreenWidth());
        assertEquals(true, GameSettings.getPauseMenuDisabled());
    }

    @Test
    public void testSetGameMode() {
        GameSettings.setGameMode(null);
        assertNull(GameSettings.getGameMode());
        GameSettings.setGameMode(MODE.SINGLEPLAYER);
        assertEquals(MODE.SINGLEPLAYER, GameSettings.getGameMode());
        GameSettings.setGameMode(MODE.MULTIPLAYER);
        assertEquals(MODE.MULTIPLAYER, GameSettings.getGameMode());
        GameSettings.setGameMode(MODE.SINGLEPLAYER);
        assertEquals(MODE.SINGLEPLAYER, GameSettings.getGameMode());
        GameSettings.setGameMode(null);
    }

    @Test
    public void testSetDifficulty() {
        GameSettings.setDifficulty(null);
        assertNull(GameSettings.getDifficulty());
        GameSettings.setDifficulty(DIFFICULTY.EASY);
        assertEquals(DIFFICULTY.EASY, GameSettings.getDifficulty());
        GameSettings.setDifficulty(DIFFICULTY.MEDIUM);
        assertEquals(DIFFICULTY.MEDIUM, GameSettings.getDifficulty());
        GameSettings.setDifficulty(DIFFICULTY.HARD);
        assertEquals(DIFFICULTY.HARD, GameSettings.getDifficulty());
        GameSettings.setDifficulty(null);
    }

    @Test
    public void testSetModel() {
        GameSettings.setModel(null);
        assertNull(GameSettings.getModel());
        GameSettings.setModel(MODEL.SERVER);
        assertEquals(MODEL.SERVER, GameSettings.getModel());
        GameSettings.setModel(MODEL.CLIENT);
        assertEquals(MODEL.CLIENT, GameSettings.getModel());
        GameSettings.setModel(MODEL.SERVER);
        assertEquals(MODEL.SERVER, GameSettings.getModel());
        GameSettings.setModel(null);
        assertNull(GameSettings.getModel());
    }

    @Test
    public void testSetWindowSize() {
        GameSettings.setWindowSize(null);
        assertNull(GameSettings.getWindowSize());
        GameSettings.setWindowSize(WINDOWSIZE.WINDOWED);
        assertEquals(WINDOWSIZE.WINDOWED, GameSettings.getWindowSize());
        GameSettings.setWindowSize(WINDOWSIZE.FULLSCREEN);
        assertEquals(WINDOWSIZE.FULLSCREEN, GameSettings.getWindowSize());
        GameSettings.setWindowSize(WINDOWSIZE.WINDOWED);
        assertEquals(WINDOWSIZE.WINDOWED, GameSettings.getWindowSize());
        GameSettings.setWindowSize(null);
        assertNull(GameSettings.getWindowSize());
    }

    @Test
    public void testSetMusicVolume() {
        GameSettings.setMusicVolume(0.0);
        assertEquals(0.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(0.5);
        assertEquals(0.5, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(1.0);
        assertEquals(1.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(5.0);
        assertNotEquals(5.0, GameSettings.getMusicVolume());
        assertEquals(1.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(300);
        assertNotEquals(300, GameSettings.getMusicVolume());
        assertEquals(1.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(0.0);
        assertEquals(0.0, GameSettings.getMusicVolume());
    }

    @Test
    public void testSetFxVolume() {
        GameSettings.setFxVolume(0.0);
        assertEquals(0.0, GameSettings.getFxVolume());
        GameSettings.setFxVolume(0.5);
        assertEquals(0.5, GameSettings.getFxVolume());
        GameSettings.setFxVolume(1.0);
        assertEquals(1.0, GameSettings.getFxVolume());
        GameSettings.setFxVolume(5.0);
        assertNotEquals(5.0, GameSettings.getFxVolume());
        assertEquals(1.0, GameSettings.getFxVolume());
        GameSettings.setFxVolume(300);
        assertNotEquals(300, GameSettings.getFxVolume());
        assertEquals(1.0, GameSettings.getFxVolume());
        GameSettings.setFxVolume(0.0);
        assertEquals(0.0, GameSettings.getFxVolume());
    }

    @Test
    public void testSetNumPlayers() {
        GameSettings.setNumPlayers(1);
        assertEquals(1, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(2);
        assertEquals(2, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(3);
        assertEquals(3, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(4);
        assertEquals(4, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(0);
        assertNotEquals(0, GameSettings.getNumPlayers());
        assertEquals(4, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(5);
        assertNotEquals(5, GameSettings.getNumPlayers());
        assertEquals(4, GameSettings.getNumPlayers());
        GameSettings.setNumPlayers(1);
        assertEquals(1, GameSettings.getNumPlayers());
    }

    @Test
    public void testSetScreenWidth() {
        GameSettings.setScreenWidth(500);
        assertEquals(500, GameSettings.getScreenWidth());
        GameSettings.setScreenWidth(1000);
        assertEquals(1000, GameSettings.getScreenWidth());
        GameSettings.setScreenWidth(0);
        assertEquals(0, GameSettings.getScreenWidth());
    }

    @Test
    public void testSetScreenHeight() {
        GameSettings.setScreenHeight(500);
        assertEquals(500, GameSettings.getScreenHeight());
        GameSettings.setScreenHeight(1000);
        assertEquals(1000, GameSettings.getScreenHeight());
        GameSettings.setScreenHeight(0);
        assertEquals(0, GameSettings.getScreenHeight());
    }

    @Test
    public void testSetGame() {
        Game game1 = new Game();
        GameSettings.setGameStateOwner(game1);
        assertEquals(game1, GameSettings.getGameStateOwner());
        Game game2 = new Game();
        GameSettings.setGameStateOwner(game2);
        assertEquals(game2, GameSettings.getGameStateOwner());
        GameSettings.setGameStateOwner(null);
        assertNull(GameSettings.getGameStateOwner());
    }

    @Test
    public void testSetUsername() {
        GameSettings.setUsername("Player1");
        assertEquals("Player1", GameSettings.getUsername());
        GameSettings.setUsername("PLAYER123456");
        assertEquals("PLAYER123456", GameSettings.getUsername());
        GameSettings.setUsername("");
        assertEquals("", GameSettings.getUsername());
        GameSettings.setUsername(" ");
        assertEquals(" ", GameSettings.getUsername());
    }

    @Test
    public void testSetPauseMenuDisabled() {
        GameSettings.setPauseMenuDisabled(true);
        assertEquals(true, GameSettings.getPauseMenuDisabled());
        GameSettings.setPauseMenuDisabled(null);
        assertNull(GameSettings.getPauseMenuDisabled());
        GameSettings.setPauseMenuDisabled(false);
        assertEquals(false, GameSettings.getPauseMenuDisabled());
        GameSettings.setPauseMenuDisabled(true);
        assertEquals(true, GameSettings.getPauseMenuDisabled());
    }

    @Test
    public void testGetInventory() {
        Inventory inventory = new Inventory();
        GameSettings.setInventory(inventory);
        assertEquals(inventory, GameSettings.getInventory());
        Inventory inventory1 = new Inventory();
        GameSettings.setInventory(inventory1);
        assertEquals(inventory1, GameSettings.getInventory());
    }

    @Test
    public void testVolValid() {
        GameSettings.setMusicVolume(0.0);
        assertEquals(0.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(0.3);
        assertEquals(0.3, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(1.0);
        assertEquals(1.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(5.0);
        assertNotEquals(5.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(-10);
        assertNotEquals(-10, GameSettings.getMusicVolume());
        assertEquals(1.0, GameSettings.getMusicVolume());
        GameSettings.setMusicVolume(0.0);
        assertEquals(0.0, GameSettings.getMusicVolume());
    }

    @Test
    public void testVolumeProperty() {
        GameSettings.volumeProperty().set(1.0);
        assertEquals(1.0, GameSettings.volumeProperty().get());
        GameSettings.volumeProperty().set(100);
        assertEquals(100, GameSettings.volumeProperty().get());
        GameSettings.volumeProperty().set(0.0);
        assertEquals(0.0, GameSettings.volumeProperty().get());
    }

    @Test
    public void testFxVolumeProperty() {
        GameSettings.fxVolumeProperty().set(1.0);
        assertEquals(1.0, GameSettings.fxVolumeProperty().get());
        GameSettings.fxVolumeProperty().set(100);
        assertEquals(100, GameSettings.fxVolumeProperty().get());
        GameSettings.fxVolumeProperty().set(0.0);
        assertEquals(0.0, GameSettings.fxVolumeProperty().get());
    }

    @Test
    public void testReturnToMenu() {
        GameSettings.onReturnToMenu();
        assertEquals(MODE.SINGLEPLAYER, GameSettings.getGameMode());
        assertEquals(DIFFICULTY.EASY, GameSettings.getDifficulty());
        assertEquals(MODEL.SERVER, GameSettings.getModel());
        assertEquals(1, GameSettings.getNumPlayers());
        assertEquals("", GameSettings.getUsername());
        assertNull(GameSettings.getInventory());
        assertTrue(GameSettings.getPauseMenuDisabled());
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
    }
}
