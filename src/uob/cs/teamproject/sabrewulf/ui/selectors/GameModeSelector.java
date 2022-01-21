package uob.cs.teamproject.sabrewulf.ui.selectors;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

/** Game mode selector creates a new {@link HBox} which displays a new selector option of a {@link MODE} (single player
 *  or multiplayer). It creates a toggle switch to allow the mode to be selected or unselected and a label
 *  which states the mode associated with the selector
 */
public class GameModeSelector extends HBox {

    private final ImageView selectorCircle;
    private final String toggleSelected = "images/uielements/toggle_selected.png";
    private final String toggleUnselected = "images/uielements/toggle_unselected.png";
    private final MODE mode;
    private boolean isSelected;

    /** Constructor - Creates a new instance of mode selector
     * @param mode - {@link MODE} for the mode selector (single player or multiplayer)
     */
    public GameModeSelector(MODE mode) {
        WindowLabel modeLabel = new WindowLabel(mode.name(), 15);
        modeLabel.setMinWidth(130);
        selectorCircle = new ImageView(ResourceManager.getImage(toggleUnselected));
        isSelected = false;
        this.mode = mode;
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.getChildren().add(selectorCircle);
        this.getChildren().add(modeLabel);
    }

    /** @return Returns the {@link MODE} of the selector
     */
    public MODE getMode() {
        return mode;
    }

    /** Sets the toggle switch for the mode selector
     * @param isSelected - Boolean value indicating if the mode has been selected by the player or not
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

