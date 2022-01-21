package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;

/**
 * Manages interactions between a wall segment and the collision system.
 */
public class WallCollider extends GameComponent {

    private final Subscriber<BoxCollider> colliderSub;

    /**
     * @param collisionSystem the {@link CollisionSystem} to use
     * @param transform the {@link Transform} which describes the wall's position and size
     * @param wallType an integer field describing whether this is a wall or a certain type of door.
     */
    public WallCollider(CollisionSystem collisionSystem, Transform transform, int wallType) {
        colliderSub = collisionSystem.addBoxCollider(new BoxCollider(transform, ColliderTag.WALL, wallType));
    }

    /**
     * See {@link GameComponent#remove()}
     */
    @Override
    public void remove() {
        colliderSub.unsubscribe();
    }
}