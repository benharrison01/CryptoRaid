package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;

/**
 * This component uses a collection of components to tie together how the player should behave.
 * Essentially, this class is 'the player'.
 */
public class PlayerBehaviour extends GameComponent {

    protected CharacterMovement characterMovement;
    protected Input input;
    protected PlayerCollider playerCollider;
    protected Audio audioComponent;
    protected int undetectableTimer = 0;
    protected GameMapWrapper gameMapWrapper;
    protected Inventory inventory;
    protected int score;
    protected boolean isUndetectable = false;
    private final int DEFAULT_TIMER_LIMIT = 5;
    private final int DEFAULT_STUNNED_TIME = 3;

    /**
     * The constructor for the behaviour component of a player.
     * @param characterMovement: The component which controls the movement of the player.
     * @param input: The input component which this PlayerBehaviour component reads.
     * @param playerCollider: The collision system belonging to this PlayerBehaviour component.
     * @param audioComponent: The audio system belonging to this PlayerBehaviour component.
     * @param gameMapWrapper: The information about the game map.
     */
    public PlayerBehaviour(CharacterMovement characterMovement, Input input, PlayerCollider playerCollider,
                           Audio audioComponent, GameMapWrapper gameMapWrapper) {
        this.characterMovement = characterMovement;
        this.input = input;
        this.playerCollider = playerCollider;
        this.audioComponent = audioComponent;
        this.gameMapWrapper = gameMapWrapper;
        this.inventory = GameSettings.getInventory();
    }

    /**
     * The constructor for the behaviour component of a remote player in an online game.
     * @param playerCollider: The collision system belonging to this PlayerBehaviour component.
     * @param characterMovement: The component which controls the movement of the player.
     */
    public PlayerBehaviour(PlayerCollider playerCollider, CharacterMovement characterMovement) {
        this.characterMovement = characterMovement;
        this.playerCollider = playerCollider;
        this.inventory = new Inventory();
        this.gameMapWrapper = characterMovement.getGameMapWrapper();
    }

     /**
     * Sets the sprites as images from the predefined file location and passes them
     * to the CharacterAnimator component.
     */
    public SpriteSets setSprites() {
        SpriteSets spriteSets = new SpriteSets();
        try {
            final String FILESTR = "images/player/player_%s.png";

            Image[] rightStrips = new Image[4];
            rightStrips[0] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
            rightStrips[1] = ResourceManager.getImage(String.format(FILESTR, "E_2"));
            rightStrips[2] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
            rightStrips[3] = ResourceManager.getImage(String.format(FILESTR, "E_4"));

            Image[] leftStrips = new Image[4];
            leftStrips[0] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
            leftStrips[1] = ResourceManager.getImage(String.format(FILESTR, "W_2"));
            leftStrips[2] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
            leftStrips[3] = ResourceManager.getImage(String.format(FILESTR, "W_4"));

            Image[] upStrips = new Image[4];
            upStrips[0] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
            upStrips[1] = ResourceManager.getImage(String.format(FILESTR, "N_2"));
            upStrips[2] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
            upStrips[3] = ResourceManager.getImage(String.format(FILESTR, "N_4"));

            Image[] downStrips = new Image[4];
            downStrips[0] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
            downStrips[1] = ResourceManager.getImage(String.format(FILESTR, "S_2"));
            downStrips[2] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
            downStrips[3] = ResourceManager.getImage(String.format(FILESTR, "S_4"));

            spriteSets.setUpStrips(upStrips);
            spriteSets.setDownStrips(downStrips);
            spriteSets.setLeftStrips(leftStrips);
            spriteSets.setRightStrips(rightStrips);
        }
        catch (Exception ignored) {
            //for testing purposes where images do not need to be set up.
        }
        return spriteSets;
    }

    /**
     * Getter method.
     * @return boolean isUndetectable.
     */
    public boolean isUndetectable() {
        return isUndetectable;
    }

    /**
     * Getter method.
     * @return this.inventory - The inventory belonging to this player, which contains keys, powerups and coins.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Setter method used in junit tests to assign an inventory to a player when not in a fully set up game.
     * @param inventory: The inventory to assign.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Performs the following actions:
     * Checks if the player is undetectable and sets the behaviour appropriately.
     * Checks if the player is invisible and sets the behaviour appropriately.
     * Checks if the player has a speed boost and sets the behaviour appropriately.
     * Checks if the player is inactive and sets the behaviour appropriately.
     * Updates the score.
     * @param t: The current time in nanoseconds.
     */
    @Override
    public void update(long t) {

        if (undetectableTimer > 0) {
            undetectableTimer--;
            if (undetectableTimer == 0) {
                setUndetectable(false);
                inventory.setInvisibility(false);
                Cell cell = gameMapWrapper.coordsToCell((int) characterMovement.getPosX(), (int) characterMovement.getPosY());
            }
        }

        //similar to when caught but player can still move
        int invisibilityTimer = inventory.getInvisibilityTimer();
        if (invisibilityTimer > 0) {
            inventory.setInvisibilityTimer(invisibilityTimer - 1);
            if (inventory.getInvisibilityTimer() == 0) { //time limit has expired
                playerCollider.setTagData(
                        PlayerColliderData.removePowerUp(playerCollider.getTagData(), Cell.PowerUpType.INVISIBILITY));
                inventory.setInvisibility(false);
            }
        }

        int speedBoostTimer = inventory.getSpeedBoostTimer();
        if (speedBoostTimer > 0) {
            inventory.setSpeedBoostTimer(speedBoostTimer - 1);
            if (inventory.getSpeedBoostTimer() == 0) {
                characterMovement.setSpeedBoost(0); //this function is the useful one
                inventory.setBoost(false);
            }
        }

        int inactiveTimer = inventory.getInactiveTimer();
        if (inactiveTimer > 0) {
            inventory.setInactiveTimer(inactiveTimer - 1);
            if (inventory.getInactiveTimer() == 0) {
                inventory.setIsInactive(false);
            }
        }

        inventory.updateScore();
        if (inventory.getScore() != score) {
            this.score = inventory.getScore();
        }

    }

    /**
     * Makes the player temporarily inactive so they cannot be detected by enemies or interact with any
     * coins/ powerups/ keys. This is to be used when the player is caught to allow them to tactically
     * move to a new location before starting again. This will only be needed in single player mode.
     * @param t: Duration of inactivity in seconds (approximately).
     */
    protected void giveTemporaryInactivity(int t) {
        setUndetectable(true);
        inventory.setInactiveTimer(t*60); //60 fps on average.
        inventory.setIsInactive(true);
    }

    /**
     * Makes the player temporarily inactive so they cannot be detected by enemies or interact with any
     * coins/ powerups/ keys. This is to be used when the player is caught to allow them to tactically
     * move to a new location before starting again. This will only be needed in single player mode.
     * The default duration of inactivity is 5 seconds (approximately).
     */
    protected void giveTemporaryInactivity() {
        giveTemporaryInactivity(DEFAULT_STUNNED_TIME);
    }

    /**
     * Makes the player temporarily invisible so they cannot be detected by enemies but can still interact
     * with coins/ powerups/ keys. This is to be used when the player activates an invisibility powerup.
     * @param t: Duration of invisibility in seconds (approximately).
     */
    protected void giveTemporaryInvisibility(int t) {
        inventory.setInvisibilityTimer(t*60); //60 frames per second, t is in seconds
        inventory.setInvisibility(true);
        try {
            playerCollider.setTagData(
                    PlayerColliderData.addPowerUp(playerCollider.getTagData(), Cell.PowerUpType.INVISIBILITY));
        }
        catch (Exception ignored) {
            //for junit testing player classes where a collision system is not set up
        }
    }

    /**
     * Makes the player temporarily faster for an in-game advantage. This is to be used when the
     * player activates a speed boost powerup.
     * @param t: Duration of the speed boost in seconds (approximately).
     */
    protected void giveTemporarySpeedBoost(int t) {
        inventory.setSpeedBoostTimer(t*60);
        inventory.setBoost(true);
        characterMovement.setSpeedBoost(3);
    }

    /**
     * Makes the player temporarily invisible so they cannot be detected by enemies but can still interact
     * with coins/ powerups/ keys. This is to be used when the player activates an invisibility powerup.
     * The default duration of invisibility is 5 seconds (approximately).
     */
    protected void giveTemporaryInvisibility() {
        giveTemporaryInvisibility(DEFAULT_TIMER_LIMIT);
    }

    /**
     * Makes the player temporarily faster for an in-game advantage. This is to be used when the
     * player activates a speed boost powerup.
     * The duration of the speed boost is 5 seconds (approximately).
     */
    protected void giveTemporarySpeedBoost() {
        giveTemporarySpeedBoost(DEFAULT_TIMER_LIMIT);
    }

    /**
     * A method to be called when the player is caught by an enemy such that the player is punished.
     */
    protected void penalisePlayer() {
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
            inventory.removeLife();
        }
    }

    /**
     * To be called when a player is caught so that they cannot be detected by enemies, nor interact with coins, keys,
     * lives or powerups.
     * @param b: Boolean value of is undetectable or is not undetectable.
     */
    private void setUndetectable(Boolean b) {
        if (b) {
            undetectableTimer = DEFAULT_STUNNED_TIME * 60;
        }
        playerCollider.setTagData(
                PlayerColliderData.setCaptured(playerCollider.getTagData(), b));
        isUndetectable = b;
    }
}
