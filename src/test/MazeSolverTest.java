package test;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMap;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.enemyai.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MazeSolverTest {
    private final int mapWidth = 1080;
    private final int mapHeight = 1080;
    private final int cellCountX = 14;
    private final int cellCountY = 14;
    private final int dividerThickness = 5;
    GameMap map = new GameMap(cellCountX,cellCountY,mapWidth/cellCountX,mapHeight/cellCountY);
    GameMapWrapper gameMapWrapper = map.getGameMapWrapper();

    /** Testing the search algorithm on the map is difficult since
       the map is randomly generated. To overcome this, we wrote
       specific tests for the graphs and general graph search
       (see GraphTest.java), but for testing the application of
       these classes and methods on the gameMap, we randomly
       generate multiple maps and check for a valid path, while
       analysing whether the information the test returns is valid.
       This is placed inside a loop such that many test cases are
       performed on many random maps.
     */
    @Test
    public void test1() {

        int testsPassed = 0;
        int totalTests = 1000;
        String output = "";
        for (int testNo = 1; testNo<=totalTests; testNo++) {
            boolean testPassed = true;

            output = output + "\n[Test " + testNo + "] Testing for a path on randomly generated map.";
            MazeSolver m = new MazeSolver(gameMapWrapper);
            Search s = new Search();
            Cell start = gameMapWrapper.getRandomCell();
            Cell end = gameMapWrapper.getRandomCell();
            int[][] path = m.solvePathAsCells(start, end);
            ArrayList<Node> pathN = m.solvePath(start, end);
            output = output + "\nstart " + start + " to end " + end + " =";

            for (int i = 0; i < path.length; i++) {
                output = output + pathN.get(i);
                if (i != path.length - 1) {
                    output = output + ", ";
                }
            }

            //if not null then there is a path found
            assertNotNull(pathN);
            if (pathN != null) {
                output = output + "\nSuccessfully found a path in this maze.\n\n";
            }
            else {
                testPassed = false;
            }

            //if equal then the path starts in the correct node
            assertEquals(start, nodeToCell(pathN.get(0)));
            if (start != nodeToCell(pathN.get(0))) {
                testPassed = false;
            }

            //if equal then the path reaches the correct node
            assertEquals(end, nodeToCell(pathN.get(pathN.size()-1)));
            if (end != nodeToCell(pathN.get(pathN.size()-1))) {
                testPassed = false;
            }


            //if true then all intermediate nodes are connected
            boolean connectionsValid = true;
            for (int i = 0; i<pathN.size() -1; i++) {
                boolean foundFlag = false;
                for (Connection connection : pathN.get(i).getConnections()) {
                    if (connection.getNode() == pathN.get(i+1)) {
                        foundFlag = true;
                        break;
                    }
                }
                if (foundFlag == false) {
                    connectionsValid = false;
                    testPassed = false;
                    output = output + "\nTest failed: " + pathN.get(i) + " is not connected to " + pathN.get(i + 1);

                }
                break;
            }
            assertTrue(connectionsValid);

            if (testPassed = true) {
                testsPassed++;
            }

        }
    }

    @Test
    public void test2() {
        for (int x=0;x<1000;x++) {
            MazeSolver m = new MazeSolver(gameMapWrapper);
            Search s = new Search();
            Cell start = gameMapWrapper.getRandomCell();
            Cell end = start;
            int[][] path = m.solvePathAsCells(start, end);
            ArrayList<Node> pathN = m.solvePath(start, end);


            //if not null then there is a path found
            assertNotNull(pathN);

            //if equal then the path starts in the correct node
            assertEquals(start, nodeToCell(pathN.get(0)));

            //if equal then the path reaches the correct node
            assertEquals(end, nodeToCell(pathN.get(pathN.size() - 1)));

            //if true then all intermediate nodes are connected
            boolean connectionsValid = true;
            for (int i = 0; i < pathN.size() - 1; i++) {
                boolean foundFlag = false;
                for (Connection connection : pathN.get(i).getConnections()) {
                    if (connection.getNode() == pathN.get(i + 1)) {
                        foundFlag = true;
                        break;
                    }
                }
                if (foundFlag == false) {
                    connectionsValid = false;
                }
                break;
            }
            assertTrue(connectionsValid);
        }
    }


    private Cell nodeToCell(Node node) {
        Cell cell = gameMapWrapper.getCellGrid()[node.getX()][node.getY()];
        return cell;
    }

}
