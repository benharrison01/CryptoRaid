package uob.cs.teamproject.sabrewulf.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Subscription;

/**
 * The renderer manages how the game world is drawn onto a canvas in the application window.
 * It allows {@link Graphic}s to be added to the background or foreground of the world, and draws the given
 * elements on the canvas each frame.
 * The renderer provides a {@link Brush} instance for use in {@link Graphic#draw(Brush)}.
 */
public class Renderer {

    /*
     * This constant scales the game world's coordinate system in relation to to the height of the canvas.
     * The value of 1080 was chosen to give a 1-to-1 conversion for a 1920x1080 window, as this is a common screen
     * resolution.
     */
    private final double WORLD_HEIGHT = 1080;

    private ResizeableCanvas canvas;
    private GraphicsContext context;
    private Brush brush;
    private Subscription<Graphic> backgroundLayer;
    private Subscription<Graphic> foregroundLayer;
    private Color canvasColor;

    /**
     * @param canvas the {@link ResizeableCanvas} to draw on
     */
    public Renderer(ResizeableCanvas canvas){
        this.canvas = canvas;
        this.context = canvas.getGraphicsContext2D();
        this.backgroundLayer = new Subscription<>();
        this.foregroundLayer = new Subscription<>();
        this.canvasColor = Color.WHITE; /* default background color */

        this.brush = new Brush() {

            /** See {@link Brush#drawRect(double, double, double, double, Color)}. */
            @Override
            public void drawRect(double x, double y, double width, double height, Color color) {
                context.setFill(color);
                context.fillRect(
                        worldToScreenX(x - width/2), worldToScreenY(y - height/2),
                        worldUnitsToPx(width), worldUnitsToPx(height)
                );
            }

            /** See {@link Brush#drawOval(double, double, double, double, Color)}. */
            @Override
            public void drawOval(double x, double y, double width, double height, Color color) {
                context.setFill(color);
                context.fillOval(
                        worldToScreenX(x - width/2), worldToScreenY(y - height/2),
                        worldUnitsToPx(width), worldUnitsToPx(height)
                );
            }

            /** See {@link Brush#drawImage(double, double, double, double, Image)}. */
            @Override
            public void drawImage(double x, double y, double width, double height, Image img){
                context.drawImage(img,
                        worldToScreenX(x - width/2), worldToScreenY(y - height/2),
                        worldUnitsToPx(width), worldUnitsToPx(height)
                );
            }
        };
    }

    /**
     * Clear the canvas and draw the provided graphics.
     */
    public void update() {
        /* clear the canvas */
        context.setFill(canvasColor);
        context.fillRect(0, 0, canvas.width(), canvas.height());

        /* draw the contents of the frame, with the layers in the correct order */
        for (Graphic elem : backgroundLayer) {
            elem.draw(this.brush);
        }
        for (Graphic elem : foregroundLayer) {
            elem.draw(this.brush);
        }
    }

    /**
     * Add a {@link Graphic} to the background layer.
     * @param elem the {@link Graphic} to add
     * @return the {@link Subscriber} instance attached to the {@link Graphic}
     */
    public Subscriber<Graphic> addBackgroundElem(Graphic elem) {
        Subscriber<Graphic> sub = new Subscriber<>(elem);
        backgroundLayer.addSubscriber(sub);
        return sub;
    }

    /**
     * Add a {@link Graphic} to the foreground layer.
     * @param elem the {@link Graphic} to add
     * @return the {@link Subscriber} instance attached to the {@link Graphic}
     */
    public Subscriber<Graphic> addForegroundElem(Graphic elem) {
        Subscriber<Graphic> sub = new Subscriber<>(elem);
        foregroundLayer.addSubscriber(sub);
        return sub;
    }

    /**
     * Set the background color of the canvas.
     * @param c the {@link Color} to use
     */
    public void setCanvasColor(Color c) {
        canvasColor = c;
    }

    /* convert from world-space distance units to pixels. */
    private double worldUnitsToPx(double units) {
        return canvas.height() * (units / WORLD_HEIGHT);
    }

    /* convert a world-space x-coordinate to a screen-space x-coordinate. */
    private double worldToScreenX(double worldX) {
        return (canvas.width() / 2) + worldUnitsToPx(worldX);
    }

    /* convert a world-space y-coordinate to a screen-space y-coordinate. */
    private double worldToScreenY(double worldY) {
        return (canvas.height() / 2) + worldUnitsToPx(worldY);
    }

}