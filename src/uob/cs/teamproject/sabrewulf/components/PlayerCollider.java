package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * Manages interactions between a player and the collision system. When the player collides with a wall, this class
 * handles how to move back out of the wall. Handling collisions with other entities is delegated to another class
 * which provides a {@link PlayerCollider.CollisionHandler} instance.
 */
public class PlayerCollider extends GameComponent {

    private final Transform transform;
    private final BoxCollider collider;
    private final Subscriber<BoxCollider> colliderSub;

    private CollisionHandler handler;

    /* the most recent position of the transform which wasn't colliding with anything */
    private double lastPosX;
    private double lastPosY;

    /* the previous values of lastPosX and lastPosY */
    private double lastLastPosX;
    private double lastLastPosY;

    /* for tracking any enemies are currently chasing the player */
    private int enemiesDetecting;
    private int enemiesChasing;

    /* for tracking whether the player is entering a door */
    private boolean insideDoor;
    private boolean alreadyInsideDoor;

    /**
     * @param collisionSystem the {@link CollisionSystem} to use
     * @param transform the {@link Transform} which describes the position and size of the player
     */
    public PlayerCollider(CollisionSystem collisionSystem, Transform transform) {
        this.transform = transform;
        this.handler = null;

        lastPosX = 0;
        lastPosY = 0;
        lastLastPosX = 0;
        lastLastPosY = 0;

        enemiesDetecting = 0;
        enemiesChasing = 0;

        insideDoor = false;
        alreadyInsideDoor = false;

        collider = new BoxCollider(transform, ColliderTag.PLAYER, PlayerColliderData.INITIAL_VALUE) {
            @Override
            public void onCollision(ColliderTag tag, int tagData, XYPair posOfOther, XYPair overlapSize) {
                if (handler == null) return;
                switch (tag) {
                    case WALL: /* the player is colliding with a wall or door */
                        boolean collideWithWall = false;
                        if (tagData == 0) { /* wall isn't a door */
                            collideWithWall = true;
                        } else { /* wall is a door */
                            Cell.KeyType requiredKey = Cell.KeyType.values()[tagData - 1];
                            if (!PlayerColliderData.hasKey(collider.getTagData(), requiredKey)) {
                                collideWithWall = true;
                            } else {
                                insideDoor = true;
                            }
                        }
                        if (collideWithWall) {
                            handleWallCollision(overlapSize);
                        }
                        break;
                    case ENEMY: /* the player is colliding with an enemy */
                        handler.onEnemyCollision();
                        break;
                    case ENEMY_VIEW: /* the player is within an enemy's view */
                        enemiesDetecting++;
                        break;
                    case COIN: /* the player is colliding with a coin */
                        handler.onCoinCollision();
                        break;
                    case KEY: /* the player is colliding with a key */
                        handler.onKeyCollision(Cell.KeyType.values()[tagData]);
                        break;
                    case POWERUP: /* the player is colliding with a power-up */
                        handler.onPowerUpCollision(Cell.PowerUpType.values()[tagData]);
                    default:
                        break;
                }
            }
        };
        colliderSub = collisionSystem.addBoxCollider(collider);
    }

    /**
     * See {@link GameComponent#start()}.
     */
    @Override
    public void start() {
        updateLastPos();
    }

    /**
     * See {@link GameComponent#update(long)}.
     * @param t a timestamp for the current frame in nanoseconds
     */
    @Override
    public void update(long t) {
        updateLastPos();
        updateBeingChased();
        updateInsideDoor();
    }

    /**
     * See {@link GameComponent#remove()}.
     */
    @Override
    public void remove() {
        colliderSub.unsubscribe();
    }

    /**
     * Set the tag data of the player's collider. This is used to convey the state of the player to colliding entities.
     * @param tagData the value to use for the tag data
     */
    public void setTagData(int tagData) {
        collider.setTagData(tagData);
    }

    /**
     * Get the tag data of the player's collider. This is used to convey the state of the player to colliding entities.
     * @return the player's collider's tag data
     */
    public int getTagData() {
        return collider.getTagData();
    }

    /**
     * Provide a {@link CollisionHandler} instance to define behaviour for different types of collision.
     * @param handler the {@link CollisionHandler} instance to use
     */
    public void setHandler(PlayerCollider.CollisionHandler handler) {
        this.handler = handler;
    }

    /* update the record of the last known position, and the one before that */
    private void updateLastPos() {
        lastLastPosX = lastPosX;
        lastLastPosY = lastPosY;
        lastPosX = transform.position.x;
        lastPosY = transform.position.y;
    }

    /* determine whether a chase is beginning or ending based on whether the player is currently detected */
    private void updateBeingChased() {
        if (enemiesChasing != enemiesDetecting) {
            if (enemiesDetecting > enemiesChasing) {
                handler.onChaseIncrease(enemiesDetecting);
            } else if (enemiesDetecting < enemiesChasing) {
                handler.onChaseDecrease(enemiesDetecting);
            }
            enemiesChasing = enemiesDetecting;
        }
        /* set enemiesDetecting back to 0 for the next frame */
        enemiesDetecting = 0;
    }

    /* determine whether the player has just entered a door */
    private void updateInsideDoor() {
        if (!alreadyInsideDoor && insideDoor) {
            handler.onDoorEnter();
            alreadyInsideDoor = true;
        } else if (alreadyInsideDoor && !insideDoor) {
            alreadyInsideDoor = false;
        }
        /* set insideDoor back to false for the next frame */
        insideDoor = false;
    }

    /* move the transform back to the last non-colliding X or Y (or both) coordinate(s) */
    private void handleWallCollision(XYPair overlapSize) {
        final double CORNER_THRESHOLD = 5; /* maximum difference between X and Y overlap for it to count as a corner */
        boolean revertX=false, revertY=false;

        if (overlapSize.x - overlapSize.y > CORNER_THRESHOLD) {
            /* overlap rectangle is wide, so move to previous Y coord */
            revertY = true;

        } else if (overlapSize.y - overlapSize.x > CORNER_THRESHOLD) {
            /* overlap rectangle is tall, so move to previous X coord */
            revertX = true;

        } else {
            /* overlap rectangle is roughly square, so move back in both X and Y */
            revertX = true;
            revertY = true;
        }

        if (revertX) {
            /* move back to the last non-colliding X coordinate */
            transform.position.x = lastPosX;
            lastPosX = lastLastPosX;
        }

        if (revertY) {
            /* move back to the last non-colliding Y coordinate */
            transform.position.y = lastPosY;
            lastPosY = lastLastPosY;
        }

    }


    /** An interface for other classes to define how collisions should be handled. */
    public interface CollisionHandler {

        /** This hook is called when the player collides with an enemy */
        void onEnemyCollision();

        /**
         * This hook is called when the number of enemies chasing the player increases.
         * It is called at most once per update tick, so not necessarily once for every new enemy.
         * @param numOfEnemiesChasing the new number of enemies chasing the player
         */
        void onChaseIncrease(int numOfEnemiesChasing);

        /**
         * This hook is called when the number of enemies chasing the player decreases.
         * It is called at most once per update tick, so not necessarily once for every new enemy.
         * @param numOfEnemiesChasing the new number of enemies chasing the player
         */
        void onChaseDecrease(int numOfEnemiesChasing);

        /** This hook is called once when the player enters a door */
        void onDoorEnter();

        /** This hook is called when the player collides with a coin */
        void onCoinCollision();

        /**
         * This hook is called when the player collides with a key.
         * @param keyType the {@Cell.KeyType} describing what type of key was collided with
         */
        void onKeyCollision(Cell.KeyType keyType);

        /**
         * This hook is called when the player collides with a power-up.
         * @param powerUpType the {@Cell.PowerUpType} describing what type of power-up was collided with
         */
        void onPowerUpCollision(Cell.PowerUpType powerUpType);
    }
}