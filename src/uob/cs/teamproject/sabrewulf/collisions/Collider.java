package uob.cs.teamproject.sabrewulf.collisions;

import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * The base class for different types/shapes of collider.
 */
public abstract class Collider {

    protected final Transform transform;
    protected final ColliderTag tag;
    protected int tagData;

    /**
     * @param transform the {@link Transform} which determines the position and dimensions of the collider
     * @param tag data field to convey which category of entity the collider is attached to
     * @param tagData field for storing additional information about the attached entity
     */
    public Collider(Transform transform, ColliderTag tag, int tagData) {
        this.transform = transform;
        this.tag = tag;
        this.tagData = tagData;
    }

    /**
     * This constructor sets the 'tag data' field to 0, and is intended for Colliders which don't use the tag data.
     * @param transform the {@link Transform} which determines the position and dimensions of the collider
     * @param tag data field to convey which category of entity the collider is attached to
     */
    public Collider(Transform transform, ColliderTag tag) {
        this.transform = transform;
        this.tag = tag;
        this.tagData = 0;
    }

    /**
     * @return the {@link ColliderTag} specifying what category of entity this collider is attached to
     */
    public ColliderTag getTag() {
        return tag;
    }

    /**
     * @return an integer value which can contain additional information about the attached entity
     */
    public int getTagData() {
        return tagData;
    }

    /**
     * @param data an integer value to set the 'tag data' field to
     */
    public void setTagData(int data) {
        tagData = data;
    }

    /**
     * @return an {@link XYPair} describing the position of the collider
     */
    public XYPair getPos() {
        return new XYPair(transform.position.x, transform.position.y);
    }

}
