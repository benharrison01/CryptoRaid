package uob.cs.teamproject.sabrewulf.collisions;

import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * A form of collider which 'triggers' when entities are within a certain distance.
 */
public class DistanceTrigger extends Collider {

    private final double sqrDistance;

    /**
     * @param transform a {@link Transform} describing the position of the trigger
     * @param tag data field to convey which category of entity the collider is attached to
     * @param distance the maximum distance at which the trigger can detect entities
     */
    public DistanceTrigger(Transform transform, ColliderTag tag, double distance) {
        super(transform, tag);
        this.sqrDistance = distance * distance;
    }

    /**
     * @param position the position to check
     * @return true iff the given position is within the trigger distance of the trigger
     */
    public boolean isWithinRange(XYPair position)  {
        double dx = position.x - transform.position.x;
        double dy = position.y - transform.position.y;
        return dx * dx + dy * dy <= sqrDistance;
    }

    /**
     * A place for subclasses to provide behaviour for when the trigger is triggered.
     * @param position an {@link XYPair} containing the position of the entity which triggered the trigger
     */
    public void onTrigger(XYPair position) {}
}