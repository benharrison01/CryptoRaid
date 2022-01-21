package uob.cs.teamproject.sabrewulf.achievements;

import uob.cs.teamproject.sabrewulf.ui.scene.AchievementsScene;
import javax.swing.text.TableView;

/**
 * AchievementElement is used by {@link AchievementsScene} to store achievements in a data structure such that they
 * are in a format compatible with a {@link TableView} in order to be displayed to the user.
 */
public class AchievementElement {

    private final String achievement;
    private Boolean achieved;

    /**
     * Constructor for a LeaderboardElement.
     * @param achievement: name of achievement.
     * @param achieved: whether player has been awarded this achievement.
     *
     */
    public AchievementElement(String achievement, Boolean achieved) {
        this.achievement = achievement;
        this.achieved = achieved;
    }

    public void setAchieved(Boolean achieved) { this.achieved = achieved; }

    public String getAchievement() { return achievement; }

    public Boolean getAchieved() { return achieved; }

}
