package test.cs.teamproject.sabrewulf.achievements;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.achievements.AchievementElement;

import static junit.framework.TestCase.assertEquals;

public class AchievementElementTest {

    @Test
    public void testGetAchievement() {

        AchievementElement achievementElement = new AchievementElement(
                "COMPLETED GAME IN HARD DIFFICULTY 1 TIME",true);

        assertEquals("COMPLETED GAME IN HARD DIFFICULTY 1 TIME", achievementElement.getAchievement());

    }

    @Test
    public void testGetAchieved() {

        AchievementElement achievementElement = new AchievementElement(
                "COMPLETED GAME IN HARD DIFFICULTY 1 TIME", true);

        achievementElement.setAchieved(false);

        assertEquals(java.util.Optional.of(false), java.util.Optional.of(achievementElement.getAchieved()));

    }

}
