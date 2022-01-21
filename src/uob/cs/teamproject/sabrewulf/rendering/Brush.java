package uob.cs.teamproject.sabrewulf.rendering;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Provides methods for drawing onto a canvas.
 */
public interface Brush {

    /**
     * Draw a filled rectangle on the canvas. The coordinate (0, 0) is the centre of the canvas.
     * @param x the x-coordinate of the centre of the rectangle, in world-space coordinates
     * @param y the y-coordinate of the centre of the rectangle, in world-space coordinates
     * @param width the width of the rectangle, in world-space units
     * @param height the height of the rectangle, in world-space units
     * @param color the {@link Color} of the rectangle
     */
    void drawRect(double x, double y, double width, double height, Color color);

    /**
     * Draw a filled oval on the canvas. The coordinate (0, 0) is the centre of the canvas.
     * @param x the x-coordinate of the centre of the oval, in world-space coordinates
     * @param y the y-coordinate of the centre of the oval, in world-space coordinates
     * @param width the width of the oval, in world-space units
     * @param height the height of the oval, in world-space units
     * @param color the {@link Color} of the oval
     */
    void drawOval(double x, double y, double width, double height, Color color);

    /**
     * Draw an image on the canvas. The coordinate (0, 0) is the centre of the canvas.
     * @param x the x-coordinate of the centre of the image, in world-space coordinates
     * @param y the y-coordinate of the centre of the image, in world-space coordinates
     * @param width the width of the image, in world-space units
     * @param height the height of the image, in world-space units
     * @param img the {@link Image} to draw
     */
    void drawImage(double x, double y, double width, double height, Image img);
}