package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;

/**
 * A collection of left, right, up, down sprites and getter/ setter methods which will be used by a player and their
 * characterAnimator component.
 */
public class SpriteSets {

    private Image[] leftStrips;
    private Image[] rightStrips;
    private Image[] upStrips;
    private Image[] downStrips;

    /**
     * Constructor for this class.
     */
    public SpriteSets() {
    }

    /**
     * Getter method.
     * @return an array of left facing image to be animated through.
     */
    public Image[] getLeftStrips() {
        return leftStrips;
    }

    /**
     * Setter method.
     * @param leftStrips: an array of left facing image to be animated through.
     */
    public void setLeftStrips(Image[] leftStrips) {
        this.leftStrips = leftStrips;
    }

    /**
     * Getter method.
     * @return an array of right facing image to be animated through.
     */
    public Image[] getRightStrips() {
        return rightStrips;
    }

    /**
     * Setter method.
     * @param rightStrips: an array of right facing image to be animated through.
     */
    public void setRightStrips(Image[] rightStrips) {
        this.rightStrips = rightStrips;
    }

    /**
     * Getter method.
     * @return an array of up facing image to be animated through.
     */
    public Image[] getUpStrips() {
        return upStrips;
    }

    /**
     * Setter method.
     * @param upStrips: an array of up facing image to be animated through.
     */
    public void setUpStrips(Image[] upStrips) {
        this.upStrips = upStrips;
    }

    /**
     * Getter method.
     * @return an array of down facing image to be animated through.
     */
    public Image[] getDownStrips() {
        return downStrips;
    }

    /**
     * Setter method.
     * @param downStrips: an array of down facing image to be animated through.
     */
    public void setDownStrips(Image[] downStrips) {
        this.downStrips = downStrips;
    }
}
