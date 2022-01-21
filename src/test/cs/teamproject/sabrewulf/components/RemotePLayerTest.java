package test.cs.teamproject.sabrewulf.components;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.components.CharacterAnimator;
import uob.cs.teamproject.sabrewulf.components.CharacterMovement;
import uob.cs.teamproject.sabrewulf.components.PlayerCollider;
import uob.cs.teamproject.sabrewulf.components.RemotePlayer;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.rendering.ResizeableCanvas;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.util.Transform;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;

public class RemotePLayerTest {

    @Test
    public void errorCorrectionTest() throws IOException, UsernameUnavailableException, ClassNotFoundException, InterruptedException {
        GameSettings.setGameMode(MODE.SINGLEPLAYER);
        NetworkSystem networkSystem = new NetworkSystem();
        networkSystem.initiateNetworkSystem(1,"test1","127.0.0.1", "55000");
        networkSystem.start();

        networkSystem.getClient().sendCoordinates(CharacterMovement.Direction.LEFT, CharacterMovement.Direction.DOWN,
                false, CharacterMovement.Direction.DOWN, 130.0, 100.0, false, false);
        networkSystem.update();

        Transform transform = new Transform(10.0,10.0,10.0,10.0);
        Renderer renderer = new Renderer(new ResizeableCanvas(1080,1080));
        CharacterAnimator characterAnimator = new CharacterAnimator(renderer,transform);
        CharacterMovement characterMovement = new CharacterMovement(characterAnimator, transform, null);
        PlayerCollider playerCollider = new PlayerCollider(new CollisionSystem(),transform);
        RemotePlayer remotePlayer = new RemotePlayer(transform,characterAnimator,playerCollider,characterMovement,
                0,null,networkSystem.getClient().getDataStorage());

        remotePlayer.update(1);
        assertEquals(130.0,transform.position.x, 0.01);
        assertEquals(100.0,transform.position.y, 0.01);

        networkSystem.closeClientSocket();
        networkSystem.closeServerSocket();
        GameSettings.setGameMode(null);
    }


}
