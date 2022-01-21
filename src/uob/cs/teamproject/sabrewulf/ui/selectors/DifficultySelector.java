package uob.cs.teamproject.sabrewulf.ui.selectors;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

/** Difficulty Selector creates a new {@link HBox} which displays a new difficulty selector. The selector consists of
 *  a toggle switch to select the setting, a label stating the difficulty and an image showing the number
 *  of stars associated with the difficulty
 */
public class DifficultySelector extends HBox {

    private final ImageView selectorCircle;
    private final String toggleSelected = "images/uielements/toggle_selected.png";
    private final String toggleUnselected = "images/uielements/toggle_unselected.png";
    private final DIFFICULTY difficulty;
    private boolean isSelected;

    /** Constructor - Creates a new instance of the Difficulty Selector
     * @param difficulty - Game {@link DIFFICULTY} (easy, medium, hard)
     */
    public DifficultySelector(DIFFICULTY difficulty) {
        ImageView difficultyImage = new ImageView(ResourceManager.getImage(difficulty.getDifficultyPath()));
        WindowLabel difficultyLabel = new WindowLabel(difficulty.name(), 15);
        selectorCircle = new ImageView(ResourceManager.getImage(toggleUnselected));
        isSelected = false;
        difficultyLabel.setMinWidth(80);
        difficultyImage.prefWidth(40);
        this.difficulty = difficulty;
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.getChildren().add(selectorCircle);
        this.getChildren().add(difficultyLabel);
        this.getChildren().add(difficultyImage);
    }

    /** @return Returns the {@link DIFFICULTY} of the selector
     */
    public DIFFICULTY getDifficulty() {
        return difficulty;
    }

    /** Sets the toggle switch for the difficulty selector
     * @param isSelected - Boolean value indicating if {@link DIFFICULTY} has been selected or not
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


