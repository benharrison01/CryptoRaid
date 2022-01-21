package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameEntity;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * The RemotePlayer is responsible for replicating a player controlled by another user.
 */
public class RemotePlayer extends PlayerBehaviour {
    private final CharacterAnimator characterAnimator;
    private final int num;
    private final Transform transform;
    private final GameEntity entity;
    private final DataStorage dataStorage;

    /**
     * The constructor for the RemotePlayer class.
     * Also contains the handlers for different types of collisions detected by its PlayerCollider component.
     * @param transform The {@link Transform} component belonging to this character
     * @param characterAnimator The {@link CharacterAnimator} component belonging to this character
     * @param playerCollider The {@link PlayerCollider} component belonging to this character
     * @param characterMovement The {@link CharacterMovement} component belonging to this character
     * @param num The number the character is distinguished by
     * @param entity The {@link GameEntity} this character belongs to
     * @param dataStorage The DataStorage object where character directions received from the server are stored
     */
    public RemotePlayer(Transform transform, CharacterAnimator characterAnimator, PlayerCollider playerCollider,
                        CharacterMovement characterMovement, int num, GameEntity entity, DataStorage dataStorage) {
        super(playerCollider, characterMovement);
        this.num = num;
        this.transform = transform;
        this.characterAnimator = characterAnimator;
        this.entity = entity;
        this.dataStorage = dataStorage;
        characterAnimator.setRemotePlayer(this);
        characterAnimator.setSpriteSets(setSprites());
        this.playerCollider.setHandler(new PlayerCollider.CollisionHandler() {
            @Override
            public void onEnemyCollision() {
                if (!inventory.hasInvisibility()) {
                    if (undetectableTimer == 0) {
                        playerCollider.setTagData(
                                PlayerColliderData.setCaptured(playerCollider.getTagData(), true));
                        giveTemporaryInactivity();
                        inventory.setInvisibility(true);
                        inventory.incrementHitCount();
                        penalisePlayer();
                    }
                }
            }

            @Override
            public void onChaseIncrease(int numOfEnemiesChasing) {
                inventory.setEnemiesChasingPlayer(numOfEnemiesChasing);
            }

            @Override
            public void onChaseDecrease(int numOfEnemiesChasing) {
                inventory.setEnemiesChasingPlayer(numOfEnemiesChasing);
            }

            @Override
            public void onDoorEnter() {
            }

            @Override
            public void onCoinCollision() {
                inventory.addCoin();
            }

            @Override
            public void onKeyCollision(Cell.KeyType keyType) {
                inventory.addKey(keyType);
                playerCollider.setTagData(PlayerColliderData.addKey(playerCollider.getTagData(), keyType));
            }

            @Override
            public void onPowerUpCollision(Cell.PowerUpType powerUpType) {
                switch (powerUpType) {
                    case ADDLIFE:
                        break;
                    case SPEEDUP:
                        inventory.incrementNumSpeedBoostsWaiting();
                        break;
                    case INVISIBILITY:
                        //may need more implementation
                        inventory.incrementNumInvisibilitiesWaiting();
                        break;
                }
            }
        });
    }

    /**
     * The update method for the RemotePlayer class.
     * Removes the character if the player has left the game.
     * Checks if the character is using an invisibility or a speed boost.
     * Makes changes to the characterMovement according to the changes received from the server.
     * Checks if the RemotePlayer position is the same as the position of the Player which it is replicating.
     * Calls an update method of its parent class.
     * @param t: The current time in nanoseconds.
     */
    public void update(long t){
        if(dataStorage.getRemove().containsValue(num)){
            entity.remove();
            return;
        }

        if(dataStorage.getInvisibilityBoost(num)){
            giveTemporaryInvisibility();
            inventory.decrementNumInvisibilitiesWaiting();
        }

        if(dataStorage.getSpeedBoost(num)){
            giveTemporarySpeedBoost();
            inventory.decrementNumSpeedBoostsWaiting();
        }

        CharacterMovement.Direction[] directions = dataStorage.getPlayerDirections();
        boolean isMoving = dataStorage.getPlayerIsMoving(num);
        CharacterMovement.Direction isFacing = dataStorage.getPlayerIsFacing(num);
        if(directions != null && isFacing != null){
            if(directions[num*2] != null && directions[(num*2)+1] != null) {
                characterMovement.changeDirection(directions[num*2], directions[num*2+1]);
                characterMovement.setFacing(isFacing);
                characterMovement.setIsMoving(isMoving);
            }
        }

        XYPair tc = dataStorage.getTransformCheck(num);
        if(tc != null){
            if(transform.position.x != tc.x || transform.position.y != tc.y){
                characterAnimator.setCoords((int) tc.x, (int) tc.y);
            }
        }

        super.update(t);
    }
}
