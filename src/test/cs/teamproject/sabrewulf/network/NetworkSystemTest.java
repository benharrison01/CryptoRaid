package test.cs.teamproject.sabrewulf.network;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import uob.cs.teamproject.sabrewulf.Game;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.components.CharacterMovement;
import uob.cs.teamproject.sabrewulf.components.Inventory;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NetworkSystemTest {

    private NetworkSystem networkSystem;

    @After
    public void cleanUp(){
        GameSettings.setGameMode(null);
        GameSettings.setModel(null);
        networkSystem.closeServerSocket();
        networkSystem.closeClientSocket();
        assertNull(networkSystem.getClient());
        assertNull(networkSystem.getServer());
    }

    @Test
    public void initiateNetworkSystemTest() throws SocketException {
        networkSystem = new NetworkSystem();
        GameSettings.setGameMode(MODE.SINGLEPLAYER);

        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55000");

        assertNotNull(networkSystem.getClient());
        assertNotNull(networkSystem.getServer());
    }

    @Test(expected = SocketException.class)
    public void initiateNetworkSystemFailTest() throws SocketException {
        GameSettings.setGameMode(MODE.MULTIPLAYER);
        GameSettings.setModel(MODEL.SERVER);
        networkSystem = new NetworkSystem();
        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55000");

        NetworkSystem networkSystem2 = new NetworkSystem();
        networkSystem2.initiateNetworkSystem(1,"test2","127.0.0.1", "55000");
    }

    @Test
    public void startAndUpdateTest() throws IOException, UsernameUnavailableException, ClassNotFoundException, InterruptedException {
        networkSystem = new NetworkSystem();
        GameSettings.setGameMode(MODE.SINGLEPLAYER);

        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55000");

        assertNotNull(networkSystem.getClient());
        assertNotNull(networkSystem.getServer());

        assertNull(networkSystem.getClient().getDataStorage().getCellGrid());
        assertNull(networkSystem.getClient().getDataStorage().getSpawnAt()[0]);

        networkSystem.start();

        assertNotNull(networkSystem.getClient().getDataStorage().getCellGrid());
        assertNotNull(networkSystem.getClient().getDataStorage().getSpawnAt()[0]);

        networkSystem.getClient().sendCoordinates(CharacterMovement.Direction.LEFT, CharacterMovement.Direction.DOWN,
                false, CharacterMovement.Direction.DOWN, 130.0, 100.0, false, false);

        networkSystem.update();
        CharacterMovement.Direction[] directions = networkSystem.getClient().getDataStorage().getPlayerDirections();
        assertEquals(CharacterMovement.Direction.LEFT,directions[0]);
        assertEquals(CharacterMovement.Direction.DOWN,directions[1]);
        assertEquals(CharacterMovement.Direction.DOWN,networkSystem.getClient().getDataStorage().getPlayerIsFacing(0));
        assertFalse(networkSystem.getClient().getDataStorage().getPlayerIsMoving(0));
        assertFalse(networkSystem.getClient().getDataStorage().getInvisibilityBoost(0));
        assertFalse(networkSystem.getClient().getDataStorage().getSpeedBoost(0));
        assertEquals(130.0,networkSystem.getClient().getDataStorage().getTransformCheck(0).x,0.01);
        assertEquals(100.0,networkSystem.getClient().getDataStorage().getTransformCheck(0).y,0.01);
    }

    @Test
    public void restartTest() throws IOException, UsernameUnavailableException, ClassNotFoundException, InterruptedException {
        networkSystem = new NetworkSystem();
        GameSettings.setGameMode(MODE.SINGLEPLAYER);

        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55000");
        networkSystem.start();

        networkSystem.getClient().getDataStorage().setUsername("testRestart");

        networkSystem.restart();

        List<String> usernames = networkSystem.getServer().getUsernames();

        assertEquals("testRestart",usernames.get(0));
    }

    @Test
    public void getScoresTest() throws IOException, UsernameUnavailableException, ClassNotFoundException, InterruptedException {
        networkSystem = new NetworkSystem();
        GameSettings.setGameMode(MODE.SINGLEPLAYER);
        Inventory inventory = new Inventory();
        GameSettings.setInventory(inventory);
        GameSettings.getInventory().addCoin();

        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55001");
        networkSystem.start();

        HashMap<String,Integer> scores = networkSystem.getScores();

        assertTrue(scores.containsKey("test1"));
        assertTrue(scores.containsValue(10));

        GameSettings.setInventory(null);
    }

    @Test
    public void clientAndServerTest() throws IOException, UsernameUnavailableException, ClassNotFoundException, InterruptedException {
        GameSettings.setGameMode(MODE.MULTIPLAYER);
        GameSettings.setModel(MODEL.SERVER);
        networkSystem = new NetworkSystem();
        networkSystem.initiateNetworkSystem(2,"test1","127.0.0.1", "55000");

        GameSettings.setModel(MODEL.CLIENT);
        NetworkSystem networkSystem2 = new NetworkSystem();
        networkSystem2.initiateNetworkSystem(2,"test2","127.0.0.1", "55000");

        networkSystem.start();
        networkSystem2.start();

        assertEquals("test1",networkSystem.getServer().getUsernames().get(0));
        assertEquals("test2",networkSystem.getServer().getUsernames().get(1));

        networkSystem.getClient().sendCoordinates(CharacterMovement.Direction.LEFT, CharacterMovement.Direction.DOWN,
                false, CharacterMovement.Direction.DOWN, 130.0, 100.0, false, false);

        networkSystem2.getClient().sendCoordinates(CharacterMovement.Direction.RIGHT, CharacterMovement.Direction.UP,
                true, CharacterMovement.Direction.UP, 0.0, 10.0, false, false);

        networkSystem.update();
        networkSystem2.update();

        CharacterMovement.Direction[] directions = networkSystem.getClient().getDataStorage().getPlayerDirections();
        assertEquals(CharacterMovement.Direction.LEFT,directions[0]);
        assertEquals(CharacterMovement.Direction.DOWN,directions[1]);
        assertEquals(CharacterMovement.Direction.RIGHT,directions[2]);
        assertEquals(CharacterMovement.Direction.UP,directions[3]);
        assertEquals(CharacterMovement.Direction.DOWN,networkSystem.getClient().getDataStorage().getPlayerIsFacing(0));
        assertEquals(CharacterMovement.Direction.UP,networkSystem.getClient().getDataStorage().getPlayerIsFacing(1));
        assertFalse(networkSystem.getClient().getDataStorage().getPlayerIsMoving(0));
        assertTrue(networkSystem.getClient().getDataStorage().getPlayerIsMoving(1));
        assertFalse(networkSystem.getClient().getDataStorage().getInvisibilityBoost(0));
        assertFalse(networkSystem.getClient().getDataStorage().getSpeedBoost(0));
        assertFalse(networkSystem.getClient().getDataStorage().getInvisibilityBoost(1));
        assertFalse(networkSystem.getClient().getDataStorage().getSpeedBoost(1));
        assertEquals(130.0,networkSystem.getClient().getDataStorage().getTransformCheck(0).x,0.01);
        assertEquals(100.0,networkSystem.getClient().getDataStorage().getTransformCheck(0).y,0.01);

        CharacterMovement.Direction[] directions2 = networkSystem2.getClient().getDataStorage().getPlayerDirections();
        assertEquals(CharacterMovement.Direction.LEFT,directions2[2]);
        assertEquals(CharacterMovement.Direction.DOWN,directions2[3]);
        assertEquals(CharacterMovement.Direction.RIGHT,directions2[0]);
        assertEquals(CharacterMovement.Direction.UP,directions2[1]);
        assertEquals(CharacterMovement.Direction.DOWN,networkSystem2.getClient().getDataStorage().getPlayerIsFacing(1));
        assertEquals(CharacterMovement.Direction.UP,networkSystem2.getClient().getDataStorage().getPlayerIsFacing(0));
        assertFalse(networkSystem2.getClient().getDataStorage().getPlayerIsMoving(1));
        assertTrue(networkSystem2.getClient().getDataStorage().getPlayerIsMoving(0));
        assertFalse(networkSystem2.getClient().getDataStorage().getInvisibilityBoost(1));
        assertFalse(networkSystem2.getClient().getDataStorage().getSpeedBoost(1));
        assertFalse(networkSystem2.getClient().getDataStorage().getInvisibilityBoost(0));
        assertFalse(networkSystem2.getClient().getDataStorage().getSpeedBoost(0));
        assertEquals(0.0,networkSystem2.getClient().getDataStorage().getTransformCheck(0).x,0.01);
        assertEquals(10.0,networkSystem2.getClient().getDataStorage().getTransformCheck(0).y,0.01);

        assertNull(networkSystem2.getServer());
        networkSystem2.closeClientSocket();
    }
}
