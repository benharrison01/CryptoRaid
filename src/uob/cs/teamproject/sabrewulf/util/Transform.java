package uob.cs.teamproject.sabrewulf.util;

/** A data structure which stores the position and size of an entity in the game. */
public class Transform {

    /** the position of the entity */
    public final XYPair position;

    /** the width of the entity */
    public double width;

    /** the height of the entity */
    public double height;

    /**
     * @param posX the position along the x-axis
     * @param posY the position along the y-axis
     * @param width the width of the entity
     * @param height the height of the entity
     */
    public Transform(double posX, double posY, double width, double height) {
        this.position = new XYPair(posX, posY);
        this.width = width;
        this.height = height;
    }

    /**
     * @param xy the position represented by a pair of x and y coordinates
     * @param width the width of the entity
     * @param height the height of the entity
     */
    public Transform(XYPair xy, double width, double height) {
        this.position = xy;
        this.width = width;
        this.height = height;
    }
}