package uob.cs.teamproject.sabrewulf.ui.selectors;

/** Window size enum provides a list of options of the game window size (windowed or full screen)
 */
public enum WINDOWSIZE {
    WINDOWED("Windowed"),
    FULLSCREEN("Fullscreen");

    private final String buttonText;

    /** Sets the text of the window selector
     * @param buttonText - Text to be added to the window size selector
     */
    WINDOWSIZE(String buttonText) {
        this.buttonText = buttonText;
    }

    /** @return Returns the text on the selector button
     */
    public String getButtonText() {
        return this.buttonText;
    }

}
