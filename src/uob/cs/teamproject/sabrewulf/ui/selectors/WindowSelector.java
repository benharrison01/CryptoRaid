package uob.cs.teamproject.sabrewulf.ui.selectors;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

/** Window Selector scene creates a new {@link HBox} which displays a new window size selector. The selector
 *  consists of a toggle switch to the mark the option as selected and a label stating the {@link WINDOWSIZE}
 */
public class WindowSelector extends HBox {

    private final ImageView selectorCircle;
    private final String toggleSelected = "images/uielements/toggle_selected.png";
    private final String toggleUnselected = "images/uielements/toggle_unselected.png";
    private final WINDOWSIZE windowSize;
    private boolean isSelected;

    /** Constructor - Creates a new window selector for the menu
     * @param windowSize - {@link WINDOWSIZE} option (windowed or full screen)
     */
    public WindowSelector(WINDOWSIZE windowSize) {
        WindowLabel button = new WindowLabel(windowSize.getButtonText(), 15);
        button.setMinWidth(120);
        selectorCircle = new ImageView(ResourceManager.getImage(toggleUnselected));
        isSelected = false;
        this.windowSize = windowSize;
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.getChildren().add(selectorCircle);
        this.getChildren().add(button);
    }

    /** @return Returns the {@link WINDOWSIZE} of this window selector
     */
    public WINDOWSIZE getWindowSize() {
        return windowSize;
    }

    /** Sets the toggle switch for the window selector
     * @param isSelected - Boolean value indicating whether the player has selected this {@link WINDOWSIZE}
     */
    public void setSelectorCircle(boolean isSelected) {
        this.isSelected = isSelected;
        String imageSelect;
        if (this.isSelected) {
            imageSelect = toggleSelected;
        } else {
            imageSelect = toggleUnselected;
        }
        selectorCircle.setImage(ResourceManager.getImage(imageSelect));
    }
}
