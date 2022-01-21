package test.cs.teamproject.sabrewulf.leaderboards;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.leaderboards.LeaderboardElement;
import uob.cs.teamproject.sabrewulf.leaderboards.StoredLeaderboard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoredLeaderboardTest {

    private String path = "json/leaderboardTEST.json";
    
    @Test
    public void testAllSameScores() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 5, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 5, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 5, now);

        leaderboard.insertScore(e1);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e4);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e4,e3,e2,e1));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testAddNewHigh() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 5, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 5, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 5, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 6, now);

        leaderboard.insertScore(e1);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e5);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e5,e4,e3,e2,e1));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testAddNewLow() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 5, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 5, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 5, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 4, now);

        leaderboard.insertScore(e1);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e5);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e4,e3,e2,e1,e5));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testGeneral() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 1, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 4, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 6, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 3, now);

        leaderboard.insertScore(e1);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e5);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e4,e2,e3,e5,e1));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testGeneral2() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 1, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 4, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 6, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 3, now);
        LeaderboardElement e6 = new LeaderboardElement("Ben", 3, now);

        leaderboard.insertScore(e1);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e5);
        leaderboard.insertScore(e6);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e4,e2,e3,e6,e5,e1));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testGeneralInReverse() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 1, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 4, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 6, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 3, now);

        leaderboard.insertScore(e5);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e1);

        ArrayList<LeaderboardElement> expectedOrder = new ArrayList<>(Arrays.asList(e4,e2,e3,e5,e1));
        assertEquals(expectedOrder, leaderboard.getElements());
    }

    @Test
    public void testStoresOnly1000() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 2000; i++) {
            leaderboard.insertScore(new LeaderboardElement("Ben", 3, now));
        }
        assertEquals(1000, leaderboard.getElements().size());
    }

    @Test
    public void test2StoresOnly1000() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 2000; i++) {
            leaderboard.insertScore(new LeaderboardElement("Ben", 3, now));
        }
        leaderboard.insertScore(new LeaderboardElement("Bernadeta", 100, now));
        leaderboard.insertScore(new LeaderboardElement("Will", 1, now));

        assertEquals(1000, leaderboard.getElements().size());

        assertEquals("Bernadeta", leaderboard.getElements().get(0).getName());
        assertEquals(100, leaderboard.getElements().get(0).getScore());

        assertEquals("Ben", leaderboard.getElements().get(999).getName());
        assertEquals(3, leaderboard.getElements().get(999).getScore());
    }

    @Test
    public void testJson() {
        StoredLeaderboard leaderboard = new StoredLeaderboard(path);
        leaderboard.clearLeaderboard();
        LocalDateTime now = LocalDateTime.now();
        LeaderboardElement e1 = new LeaderboardElement("Ben", 1, now);
        LeaderboardElement e2 = new LeaderboardElement("Thomas", 5, now);
        LeaderboardElement e3 = new LeaderboardElement("Matt", 4, now);
        LeaderboardElement e4 = new LeaderboardElement("Miles", 6, now);
        LeaderboardElement e5 = new LeaderboardElement("Kushal", 3, now);

        leaderboard.insertScore(e5);
        leaderboard.insertScore(e4);
        leaderboard.insertScore(e3);
        leaderboard.insertScore(e2);
        leaderboard.insertScore(e1);

        leaderboard.leaderboardToJSON();

        StoredLeaderboard leaderboard2 = new StoredLeaderboard(path);
        leaderboard2.JSONtoleaderboard();

        assertEquals(leaderboard.toString(), leaderboard2.toString());
    }
}
