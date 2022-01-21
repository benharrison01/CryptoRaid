package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.collisions.DistanceTrigger;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * Manages how an enemy detects players, and creates a {@link BoxCollider} to enable the enemy to collide with players.
 */
public class EnemyCollider extends GameComponent {

    private final Transform transform;

    private final Subscriber<BoxCollider> bodyCollider; /* for colliding with players */
    private final Subscriber<DistanceTrigger> detectionTrigger; /* for detecting players */

    /* The target player is the closest player within the detection range. */
    private XYPair targetPlayerPos;
    private double targetPlayerSqrDistance;

    /* true iff there are any players within detection range */
    private boolean anyPlayersWithinRange;

    /**
     * @param collisionSystem the {@link CollisionSystem} to use
     * @param transform a {@link Transform} to specify the position and dimensions of the enemy
     * @param detectionRadius the maximum distance at which the enemy can detect players
     */
    public EnemyCollider(CollisionSystem collisionSystem, Transform transform,
                         double detectionRadius) {
        this.transform = transform;

        bodyCollider = collisionSystem.addBoxCollider(new BoxCollider(transform, ColliderTag.ENEMY));

        detectionTrigger = collisionSystem.addDistanceTrigger(
                new DistanceTrigger(transform, ColliderTag.ENEMY_VIEW, detectionRadius) {
                    @Override
                    public void onTrigger(XYPair position) {
                        onPlayerDetect(position);
                    }
                });

        targetPlayerPos = null;
        targetPlayerSqrDistance = -1;
        anyPlayersWithinRange = false;
    }

    /**
     * The update method for this game component.
     * @param now the timestamp of the current frame given in nanoseconds.
     */
    @Override
    public void update(long now) {
        if (!anyPlayersWithinRange) {
            targetPlayerPos = null;
            targetPlayerSqrDistance = -1;
        }
        anyPlayersWithinRange = false;
    }

    /**
     * Unsubscribe this component from any game systems it is using.
     */
    public void remove() {
        bodyCollider.unsubscribe();
        detectionTrigger.unsubscribe();
    }

    /**
     * @return an {@link XYPair} containing the position of the nearest player within detection range, or null if there
     * are no such players
     */
    public XYPair getTargetPlayerPos() {
        return targetPlayerPos;
    }

    /**
     * A hook to be called once per player within detection range per frame.
     * @param position an {@link XYPair} containing the position of the detected player
     */
    private void onPlayerDetect(XYPair position) {
        double dx = position.x - transform.position.x;
        double dy = position.y - transform.position.y;
        double sqrDistance = dx * dx + dy * dy;
        if (!anyPlayersWithinRange || sqrDistance < targetPlayerSqrDistance) {
            if (targetPlayerPos == null) {
                targetPlayerPos = new XYPair(position.x, position.y);
            } else {
                targetPlayerPos.x = position.x;
                targetPlayerPos.y = position.y;
            }
            targetPlayerSqrDistance = sqrDistance;
        }
        anyPlayersWithinRange = true;
    }
}