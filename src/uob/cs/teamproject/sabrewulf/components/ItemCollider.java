package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.GameEntity;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * Manages interactions between an item (coin, key or power-up) and the collision system.
 * When the item collides with a player, this class calls {@link GameEntity#remove()} on the provided {@link GameEntity}
 * to remove all of the {@link GameComponent}s which make up the item.
 */
public class ItemCollider extends GameComponent {

    private final Subscriber<BoxCollider> colliderSub;

    /**
     * @param collisionSystem the {@link CollisionSystem} to use
     * @param transform the {@link Transform} which describes the item's position and size
     * @param colliderTag the {@link ColliderTag} describing what type of item this is
     * @param colliderData an integer field to pass additional data describing the item
     * @param entity the {@link GameEntity} instance being used to group together all of the {@link GameComponent}s
     *               which make up the item
     */
    public ItemCollider(CollisionSystem collisionSystem, Transform transform,
                        ColliderTag colliderTag, int colliderData,
                        GameEntity entity) {
        colliderSub = collisionSystem.addBoxCollider(new BoxCollider(transform, colliderTag, colliderData) {
            @Override
            public void onCollision(ColliderTag tag, int tagData, XYPair posOfOther, XYPair overlapSize) {
                entity.remove();
            }
        });
    }

    /**
     * See {@link GameComponent#remove()}
     */
    @Override
    public void remove() {
        colliderSub.unsubscribe();
    }
}