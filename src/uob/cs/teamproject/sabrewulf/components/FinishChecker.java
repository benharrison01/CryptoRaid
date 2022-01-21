package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.GameWorld;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.ui.scene.FinalScoreScene;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;

/** Finish Checker class verifies whether the game is complete by checking to see whether any of the following
 *  conditions have been met:
 *  - No lives left (SP)
 *  - No coins left
 */
public class FinishChecker extends GameComponent {
    private final GameWorld gameWorld;
    private final NetworkSystem networkSystem;

    /** Constructor creates a new instance of finish checker which takes two parameters
     * @param gameWorld - Game world instance
     * @param networkSystem - Network system
     */
    public FinishChecker(GameWorld gameWorld, NetworkSystem networkSystem) {
        this.gameWorld = gameWorld;
        this.networkSystem = networkSystem;
    }

    /** Method to carry out the actual check to see whether the specified conditions have been met for the game to
     *  be complete, and then calls either the complete game method or the handle finish method in the network system
     */
    public void checkFinish() {
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
            if (GameSettings.getInventory().getNumberOfLives() == 0) {
                GameSettings.getGameStateOwner().completeGame(FinalScoreScene.COMPLETIONTYPE.OUTOFLIVES);
            }
            if (gameWorld.getNumCoins() == 0) {
                GameSettings.getGameStateOwner().completeGame(FinalScoreScene.COMPLETIONTYPE.SUCCESS);
            }
        } else {
            if (gameWorld.getNumCoins() == 0) {
                networkSystem.handleFinish();
            }
        }
    }

    /** The update method for this game component.
     * @param t - the timestamp of the current frame given in nanoseconds.
     */
    @Override
    public void update(long t) {
        checkFinish();
    }
}
