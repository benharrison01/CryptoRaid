package uob.cs.teamproject.sabrewulf.collisions;

import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * The BoxCollider is a bounding-box which can be queried to test whether it overlaps another BoxCollider. Its position
 * and dimensions are set by the {@link Transform} it is passed. It has a {@link ColliderTag} attached to convey what
 * category of entity it is attached to, plus an integer 'tag data' field which can store additional information about
 * the entity.
 */
public class BoxCollider extends Collider {

    /**
     * @param transform the {@link Transform} which determines the position and dimensions of the collider
     * @param tag data field to convey which category of entity the collider is attached to
     * @param tagData field for storing additional information about the attached entity
     */
    public BoxCollider(Transform transform, ColliderTag tag, int tagData) {
        super(transform, tag, tagData);
    }

    /**
     * @param transform the {@link Transform} which determines the position and dimensions of the collider
     * @param tag data field to convey which category of entity the collider is attached to
     */
    public BoxCollider(Transform transform, ColliderTag tag) {
        super(transform, tag);
    }

    /**
     * Test whether this bounding-box collider overlaps another.
     * @param other the {@link BoxCollider} to test against
     * @return an {@link} XYPair containing the width (x) and height (y) of the overlap, or null if there is no overlap
     */
    public XYPair getOverlapSize(BoxCollider other) {

        double aX1 = transform.position.x - transform.width/2;
        double aX2 = transform.position.x + transform.width/2;
        double aY1 = transform.position.y - transform.height/2;
        double aY2 = transform.position.y + transform.height/2;

        double bX1 = other.transform.position.x - other.transform.width/2;
        double bX2 = other.transform.position.x + other.transform.width/2;
        double bY1 = other.transform.position.y - other.transform.height/2;
        double bY2 = other.transform.position.y + other.transform.height/2;

        if (aX1 < bX2 && aX2 > bX1 && aY1 < bY2 && aY2 > bY1) {
            /* the rectangles overlap */
            return new XYPair(
                    Math.min(aX2, bX2) - Math.max(aX1, bX1),
                    Math.min(aY2, bY2) - Math.max(aY1, bY1)
            );
        } else {
            /* the rectangles don't overlap */
            return null;
        }
    }

    /**
     * A place for subclasses to add behaviour for when the collider hits another collider.
     * @param tag the {@link ColliderTag} of the other collider
     * @param tagData the 'tag data' field of the other collider
     * @param posOfOther an {@link XYPair} containing the position of the other collider
     * @param overlapSize an {@link XYPair} containing the width (x) and height (y) of the overlap between the two
     *                    box colliders
     */
    public void onCollision(ColliderTag tag, int tagData, XYPair posOfOther, XYPair overlapSize) {}
}