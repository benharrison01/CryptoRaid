package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.rendering.Brush;
import uob.cs.teamproject.sabrewulf.rendering.Graphic;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;

/**
 * A {@link GameComponent} which draws a wall.
 */
public class WallGraphic extends GameComponent {

    private final Subscriber<Graphic> graphicElemSub;

    /**
     * @param renderer the {@link Renderer} to use
     * @param transform the {@link Transform} which specifies the position and dimensions of the wall
     * @param color the {@link Color} of the wall
     */
    public WallGraphic(Renderer renderer, Transform transform, Color color) {
        Graphic graphicElem = (Brush brush) -> brush.drawRect(
                transform.position.x, transform.position.y,
                transform.width, transform.height,
                color
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