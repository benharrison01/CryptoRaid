package uob.cs.teamproject.sabrewulf.ui.selectors;

/** Difficulty enum provides a list of options of game difficulty (easy, medium or hard)
 */
public enum DIFFICULTY {
    EASY("images/uielements/one_star.png"),
    MEDIUM("images/uielements/two_star.png"),
    HARD("images/uielements/three_star.png");

    private final String difficultyPath;

    /** @param difficultyPath - Path for the difficulty image (number of stars)
     */
    DIFFICULTY(String difficultyPath) {
        this.difficultyPath = difficultyPath;
    }

    /** @return Returns the path of the difficulty image
     */
    public String getDifficultyPath() {
        return this.difficultyPath;
    }

}
