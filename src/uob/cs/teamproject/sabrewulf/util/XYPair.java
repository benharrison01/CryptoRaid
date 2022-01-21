package uob.cs.teamproject.sabrewulf.util;

/** A data structure for storing a pair of floating-point values named x and y. */
public class XYPair {

    /** the 'x' value */
    public double x;

    /** the 'y' value */
    public double y;

    /**
     * Creates a new instance of {@link XYPair} with the given X and Y values.
     * @param x the x value to store
     * @param y the y value to store
     */
    public XYPair(double x, double y) {
        this.x = x;
        this.y = y;
    }
}