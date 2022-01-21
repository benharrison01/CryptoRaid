package uob.cs.teamproject.sabrewulf.rendering;

/**
 * An interface for describing how to draw a particular graphic using a {@link Brush}.
 */
public interface Graphic {

    /**
     * Draw the graphic represented by this object.
     * @param brush the {@link Brush} to use
     */
    void draw(Brush brush);
}