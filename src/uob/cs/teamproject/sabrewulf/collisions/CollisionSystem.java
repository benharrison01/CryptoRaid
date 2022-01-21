package uob.cs.teamproject.sabrewulf.collisions;

import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.components.PlayerColliderData;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Subscription;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * The collision system is responsible for tracking colliders and informing them when they collide with each other.
 * It checks for the 4 types of collision which can happen in our game:
 *  - a player colliding with a wall: the player needs to be notified
 *  - a player colliding with an item (coin, key, etc.): both the player and the item need to be notified
 *  - an enemy colliding with a player: the player needs to be notified
 *  - a player entering the 'detection range' of an enemy: both the player and the enemy need to be notified
 */
public class CollisionSystem {

    private final Subscription<BoxCollider> staticColliders;
    private final Subscription<BoxCollider> playerColliders;
    private final Subscription<BoxCollider> enemyColliders;
    private final Subscription<DistanceTrigger> enemyViewTriggers;

    /**
     * The constructor for the collision system.
     */
    public CollisionSystem() {
        staticColliders = new Subscription<>();
        playerColliders = new Subscription<>();
        enemyColliders = new Subscription<>();
        enemyViewTriggers = new Subscription<>();
    }

    /**
     * The update method for the collision system.
     */
    public void update() {

        /* for each player collider */
        for (BoxCollider player : playerColliders) {

            /* check collision with walls */
            for (BoxCollider wall : staticColliders) {
                if (wall.getTag() == ColliderTag.WALL) {
                    if (closeToPlayer(player, wall)) {
                        XYPair overlap = player.getOverlapSize(wall);
                        if (overlap != null) {
                            /* notify the player */
                            player.onCollision(wall.getTag(), wall.getTagData(), wall.getPos(), overlap);
                        }
                    }
                }
            }

            /* all other collisions require that the player isn't in the 'captured' state */
            if (!PlayerColliderData.isCaptured(player.getTagData())) {

                /* check for detection by enemies, if the player isn't invisible */
                if (!PlayerColliderData.hasPowerUp(player.getTagData(), Cell.PowerUpType.INVISIBILITY)) {
                    for (DistanceTrigger trigger : enemyViewTriggers) {
                        if (trigger.isWithinRange(player.getPos())) {
                            /* notify the player */
                            player.onCollision(trigger.getTag(), trigger.getTagData(), trigger.getPos(),
                                    new XYPair(0, 0)); // 'trigger' isn't a box so overlap isn't relevant
                            /* notify the enemy */
                            trigger.onTrigger(player.getPos());
                        }
                    }
                }

                /* check collision with items */
                for (BoxCollider other : staticColliders) {
                    if (other.getTag() != ColliderTag.WALL) {
                        if (closeToPlayer(player, other)) {
                            XYPair overlap = player.getOverlapSize(other);
                            if (overlap != null) {
                                /* notify the player */
                                player.onCollision(other.getTag(), other.getTagData(), other.getPos(), overlap);
                                /* notify the item */
                                other.onCollision(player.getTag(), player.getTagData(), player.getPos(), overlap);
                            }
                        }
                    }
                }

                /* check collision with enemies */
                for (BoxCollider enemy : enemyColliders) {
                    if (closeToPlayer(player, enemy)) {
                        XYPair overlap = player.getOverlapSize(enemy);
                        if (overlap != null) {
                            /* notify the player */
                            player.onCollision(enemy.getTag(), enemy.getTagData(), enemy.getPos(), overlap);
                        }
                    }
                }

            }
        }
    }

    /**
     * @param col a {@link BoxCollider} to add to the collision system
     * @return a {@link Subscriber<BoxCollider>} instance for the added {@link BoxCollider}
     */
    public Subscriber<BoxCollider> addBoxCollider(BoxCollider col) {
        Subscriber<BoxCollider> sub = new Subscriber<>(col);
        switch (col.getTag()) {
            case PLAYER:
                playerColliders.addSubscriber(sub);
                break;
            case ENEMY:
                enemyColliders.addSubscriber(sub);
                break;
            default:
                staticColliders.addSubscriber(sub);
                break;
        }
        return sub;
    }

    /**
     * @param trig a {@link DistanceTrigger} to add to the collision system
     * @return a {@link Subscriber<DistanceTrigger>} instance for the added {@link DistanceTrigger}
     */
    public Subscriber<DistanceTrigger> addDistanceTrigger(DistanceTrigger trig) {
        Subscriber<DistanceTrigger> sub = new Subscriber<>(trig);
        enemyViewTriggers.addSubscriber(sub);
        return sub;
    }

    /* Return true iff the 'other' collider is close enough to the player to check collision between them. */
    private boolean closeToPlayer(BoxCollider player, BoxCollider other) {

        /* the 'other' collider is close enough iff it is within a square of side length
         * CLOSE_TO_PLAYER_DISTANCE * 2, centred at the player */
        final double CLOSE_TO_PLAYER_DISTANCE = 100;

        XYPair playerPos = player.getPos();
        XYPair otherPos = other.getPos();
        return (Math.abs(playerPos.x - otherPos.x) < CLOSE_TO_PLAYER_DISTANCE
                && Math.abs(playerPos.y - otherPos.y) < CLOSE_TO_PLAYER_DISTANCE);
    }
}