package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.network.Client;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.util.Transform;

import java.util.ArrayList;

public class Player extends PlayerBehaviour{

    private CharacterMovement.Direction facing;
    private String thisKeyDown = "";
    private Transform transform;
    private int doorSoundDelay = 0;
    private final StatisticsTracker statisticsTracker;
    private final DataStorage dataStorage;
    private final Client client;

    /**
     * Constructor for PlayerBehaviour class.
     * Also contains the handlers for different types of collisions detected by its PlayerCollider component.
     *
     * @param transform         : The Transform component belonging to this class.
     * @param characterMovement : The CharacterMovement class belonging to this. PlayerBehaviour also assigns the correct sprites.
     * @param input             : The Input system class belonging to this.
     * @param playerCollider    : The PlayerCollider collision system class belonging to this.
     * @param statisticsTracker : The StatisticsTracker class belonging to this.
     * @param audioComponent    : The Audio system belonging to this.
     * @param client            : The Client class.
     * @param gameMapWrapper    : The information about the game map.
     */
    public Player(Transform transform, CharacterMovement characterMovement, Input input, PlayerCollider playerCollider,
                  StatisticsTracker statisticsTracker, Audio audioComponent, Client client, GameMapWrapper gameMapWrapper, DataStorage dataStorage) {
        super(characterMovement, input, playerCollider, audioComponent, gameMapWrapper);
        this.transform = transform;
        this.client = client;
        this.statisticsTracker = statisticsTracker;
        this.dataStorage = dataStorage;
        characterMovement.getCharacterAnimator().setPlayer(this);
        characterMovement.getCharacterAnimator().setSpriteSets(setSprites());
        try {
            this.playerCollider.setHandler(new PlayerCollider.CollisionHandler() {
                @Override
                public void onEnemyCollision() {
                    if (!inventory.hasInvisibility()) {
                        if (undetectableTimer == 0) {
                            Cell cell = gameMapWrapper.coordsToCell(
                                    (int) characterMovement.getPosX(), (int) characterMovement.getPosY());
                            giveTemporaryInactivity();
                            inventory.setInvisibility(true);
                            inventory.incrementHitCount();
                            audioComponent.playHitAudio();
                            penalisePlayer();
                        }
                    }
                }

                @Override
                public void onChaseIncrease(int numOfEnemiesChasing) {
                    audioComponent.playDetectedAudio();
                    inventory.setEnemiesChasingPlayer(numOfEnemiesChasing);
                    statisticsTracker.noteDetected();
                }

                @Override
                public void onChaseDecrease(int numOfEnemiesChasing) {
                    inventory.setEnemiesChasingPlayer(numOfEnemiesChasing);
                }

                @Override
                public void onDoorEnter() {
                    if (doorSoundDelay == 0) {
                        audioComponent.playThroughDoorAudio();
                        doorSoundDelay = 90;
                    }
                }

                @Override
                public void onCoinCollision() {
                    inventory.addCoin();
                    audioComponent.playCoinAudio();
                }

                @Override
                public void onKeyCollision(Cell.KeyType keyType) {
                    inventory.addKey(keyType);
                    audioComponent.playKeyAudio();
                    playerCollider.setTagData(PlayerColliderData.addKey(playerCollider.getTagData(), keyType));
                }

                @Override
                public void onPowerUpCollision(Cell.PowerUpType powerUpType) {
                    switch (powerUpType) {
                        case ADDLIFE:
                            inventory.addLife();
                            statisticsTracker.notePowerUpUsed(Cell.PowerUpType.ADDLIFE);
                            audioComponent.playHeartAudio();
                            break;
                        case SPEEDUP:
                            inventory.incrementNumSpeedBoostsWaiting();
                            audioComponent.playPowerupAudio();
                            break;
                        case INVISIBILITY:
                            inventory.incrementNumInvisibilitiesWaiting();
                            audioComponent.playPowerupAudio();
                            break;
                    }
                }
            });
        }
        catch (Exception e) {

        }
    }

    /**
     * The update function for the player class which performs
     * movement and actions belonging to the parent class.
     * @param t: The current time in nanoseconds.
     */
    public void update(long t){
        if (doorSoundDelay > 0) {
            doorSoundDelay -= 1;
        }

        ArrayList<String> inputList = input.getInputList();
        analyseInputs(input.getCurrentKeyDown(), inputList);

        if(GameSettings.getGameMode() != MODE.SINGLEPLAYER){
            CharacterMovement.Direction[] directions = dataStorage.getPlayerDirections();
            boolean isMoving = dataStorage.getPlayerIsMoving(0);
            CharacterMovement.Direction isFacing = dataStorage.getPlayerIsFacing(0);
            if (directions != null && isFacing != null) {
                if (directions[0] != null && directions[1] != null) {
                    characterMovement.changeDirection(directions[0], directions[1]);
                    characterMovement.setFacing(isFacing);
                    characterMovement.setIsMoving(isMoving);
                }
            }
        }

        super.update(t);
    }

    /**
     * Reads the current key input(s) and changes the direction of the player appropriately.
     * @param currentKeyDown: The most recent key to be pressed.
     * @param inputList: A list of all the keys currently pressed. When the key
     *                   is released, it is removed from inputList.
     */
    public void analyseInputs(String currentKeyDown, ArrayList<String> inputList) {
        CharacterMovement.Direction dirX = CharacterMovement.Direction.NONE;
        CharacterMovement.Direction dirY = CharacterMovement.Direction.NONE; //reset directions
        boolean isMoving = false;
        boolean invisibilty = false;
        boolean speed = false;

        if (currentKeyDown.equals("DIGIT2") || currentKeyDown.equals("PERIOD")) {
            if (inventory.getNumSpeedBoostsWaiting() > 0 && !inventory.hasSpeedBoost()) {
                speed = true;
                giveTemporarySpeedBoost();
                if(GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                    statisticsTracker.notePowerUpUsed(Cell.PowerUpType.SPEEDUP);
                }
                inventory.decrementNumSpeedBoostsWaiting();
                try {
                    audioComponent.playGetSpeedBoostAudio();
                }
                catch (Exception ignored){
                    //for junit tests where audio cannot be played.
                }
            }
            else {
                if (!thisKeyDown.equals(currentKeyDown)) {
                    try {
                        audioComponent.playInvalidAudio();
                    }
                    catch (Exception ignored){
                        //for junit tests where audio cannot be played.
                    }
                }
            }
        }

        if (currentKeyDown.equals("DIGIT1") || currentKeyDown.equals("COMMA")) {
            if (inventory.getNumInvisibilitiesWaiting() > 0 && !inventory.hasInvisibility()) {
                invisibilty = true;
                giveTemporaryInvisibility();
                if(GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                    statisticsTracker.notePowerUpUsed(Cell.PowerUpType.INVISIBILITY);
                }
                inventory.decrementNumInvisibilitiesWaiting();
                try {
                    audioComponent.playGoingInvisibleAudio();
                }
                catch (Exception ignored){
                    //for junit tests where audio cannot be played.
                }
            }
            else {
                if (!thisKeyDown.equals(currentKeyDown)) {
                    try {
                        audioComponent.playInvalidAudio();
                     }
                    catch (Exception ignored){
                        //for junit tests where audio cannot be played.
                    }
                }
            }
        }
        thisKeyDown = currentKeyDown;

        if (currentKeyDown.equals("W") || currentKeyDown.equals("A") || currentKeyDown.equals("S") ||
                currentKeyDown.equals("D") || currentKeyDown.equals("UP") || currentKeyDown.equals("DOWN")
                || currentKeyDown.equals("LEFT") || currentKeyDown.equals("RIGHT")) {
            isMoving = false;
        }


        if (inputList.contains("W")||inputList.contains("UP")) {
            dirY = CharacterMovement.Direction.UP;
            isMoving = true;
        }
        else if (inputList.contains("S")||inputList.contains("DOWN")) {
            dirY = CharacterMovement.Direction.DOWN;
            isMoving = true;
        }
        else {
            dirY = CharacterMovement.Direction.NONE;
        }

        if (inputList.contains("A")||inputList.contains("LEFT")) {
            dirX = CharacterMovement.Direction.LEFT;
            isMoving = true;
        }
        else if (inputList.contains("D")||inputList.contains("RIGHT")) {
            dirX = CharacterMovement.Direction.RIGHT;
            isMoving = true;
        }
        else {
            dirX = CharacterMovement.Direction.NONE;
        }

        if (dirX == CharacterMovement.Direction.NONE && dirY == CharacterMovement.Direction.NONE) {
            isMoving = false;
        }

        if (currentKeyDown.equals("SHIFT")) {
            isMoving = false;
        }
        switch (currentKeyDown) {
            case "W":
            case "UP":
                facing = CharacterMovement.Direction.UP;
                break;
            case "A":
            case "LEFT":
                facing = CharacterMovement.Direction.LEFT;
                break;
            case "D":
            case "RIGHT":
                facing = CharacterMovement.Direction.RIGHT;
                break;
            case "S":
            case "DOWN":
                facing = CharacterMovement.Direction.DOWN;
                break;
        }

        try {
            this.client.sendCoordinates(dirX, dirY, isMoving, facing, transform.position.x, transform.position.y,
                    invisibilty, speed);
            if(GameSettings.getGameMode() == MODE.SINGLEPLAYER){
                characterMovement.changeDirection(dirX, dirY);
                characterMovement.setFacing(facing);
                characterMovement.setIsMoving(isMoving);
            }
        }
        catch (Exception ignored) {
            //for junit testing
            characterMovement.changeDirection(dirX, dirY);
            characterMovement.setFacing(facing);
        }
    }


}
