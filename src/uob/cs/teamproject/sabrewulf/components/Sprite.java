package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.rendering.Brush;
import uob.cs.teamproject.sabrewulf.rendering.Graphic;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;

/**
 * A {@link GameComponent} which draws an image.
 */
public class Sprite extends GameComponent {

    private final Subscriber<Graphic> graphicElemSub;

    /**
     * @param renderer the {@link Renderer} to use
     * @param transform the {@link Transform} which specifies the position and size of the image
     * @param image the {@link Image} to draw
     */
    public Sprite(Renderer renderer, Transform transform, Image image) {
        Graphic graphicElem = (Brush brush) -> brush.drawImage(
                transform.position.x, transform.position.y,
                transform.width, transform.height,
                image
        );
        graphicElemSub = renderer.addBackgroundElem(graphicElem);
    }

    /**
     * See {@link GameComponent#remove()}
     */
    @Override
    public void remove() {
        graphicElemSub.unsubscribe();
    }
}