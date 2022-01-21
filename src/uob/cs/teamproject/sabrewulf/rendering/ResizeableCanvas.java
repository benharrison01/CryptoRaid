package uob.cs.teamproject.sabrewulf.rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;

/**
 * A wrapper around JavaFX's {@link Canvas} class. Its width and height adjust to reflect that of the canvas, which can
 * be resized.
 */
public class ResizeableCanvas extends Region {
    private Canvas canvas;

    /**
     * Creates a new instance of {@link ResizeableCanvas} with the given size.
     * @param width the initial width to use for the canvas
     * @param height the initial height to use for the canvas
     */
    public ResizeableCanvas(double width, double height) {
        canvas = new Canvas(width, height);
        getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        setWidth(width);
        setHeight(height);
    }

    /**
     * Getter for the canvas' width property. Named to avoid conflict with {@link Region#getWidth()}.
     * @return the width of the canvas.
     */
    public double width() { return canvas.getWidth(); }

    /**
     * Getter for the canvas' height property. Named to avoid conflict with {@link Region#getHeight()}.
     * @return the height of the canvas.
     */
    public double height() { return canvas.getHeight(); }

    /**
     * Wrapper for {@link Canvas#getGraphicsContext2D()}
     * @return a {@link GraphicsContext} for drawing on the canvas.
     */
    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }
}