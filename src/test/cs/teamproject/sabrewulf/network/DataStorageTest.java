package test.cs.teamproject.sabrewulf.network;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.components.CharacterMovement;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class DataStorageTest {

    private class DataStorageTestSubclass extends DataStorage{
        public DataStorageTestSubclass(int numOfPlayers, int numOfEnemies){
            super(numOfPlayers,numOfEnemies);
        }

        public void setReceivedEnemyDirections(int num, String dirX, String dirY, String isMoving, String facing){
            super.setReceivedEnemyDirections(num,dirX,dirY,isMoving,facing);
        }


        public void setReceivedPlayerInfo(int num, String dirX, String dirY, String isMoving, String facing,
                                          String invisibility, String speed, String transformX, String transformY){
            super.setReceivedPlayerInfo(num,dirX,dirY,isMoving,facing,invisibility,speed,transformX,transformY);
        }
    }

    @Test
    public void setAndGetReceivedEnemyDirectionsTest(){
        DataStorageTestSubclass dataStorage = new DataStorageTestSubclass(2,3);

        dataStorage.setReceivedEnemyDirections(0,"LEFT", "UP", "true", "UP");
        dataStorage.setReceivedEnemyDirections(1,"RIGHT", "NONE", "false", "RIGHT");
        dataStorage.setReceivedEnemyDirections(2,"a", "2", "r", "");

        CharacterMovement.Direction[] directions = dataStorage.getEnemyDirections();
        CharacterMovement.Direction[] facing = dataStorage.getEnemyIsFacing();
        boolean[] isMoving = dataStorage.getEnemyIsMoving();

        assertEquals(CharacterMovement.Direction.LEFT,directions[0]);
        assertEquals(CharacterMovement.Direction.UP,directions[1]);
        assertEquals(CharacterMovement.Direction.UP,facing[0]);
        assertTrue(isMoving[0]);

        assertEquals(CharacterMovement.Direction.RIGHT,directions[2]);
        assertEquals(CharacterMovement.Direction.NONE,directions[3]);
        assertEquals(CharacterMovement.Direction.RIGHT,facing[1]);
        assertFalse(isMoving[1]);

        assertEquals(CharacterMovement.Direction.NONE,directions[4]);
        assertEquals(CharacterMovement.Direction.DOWN,directions[5]);
        assertEquals(CharacterMovement.Direction.DOWN,facing[2]);
        assertFalse(isMoving[2]);
    }

    @Test
    public void setAndGetReceivedPlayerInfoTest(){
        DataStorageTestSubclass dataStorage = new DataStorageTestSubclass(3,0);

        dataStorage.setReceivedPlayerInfo(0,"LEFT", "UP", "true", "UP","false",
                "false","130.0","150.0");
        dataStorage.setReceivedPlayerInfo(1,"RIGHT", "NONE", "false", "RIGHT",
                "true", "true","-135.0","0.0");
        dataStorage.setReceivedPlayerInfo(2,"a", "2", "r", "", "a","no?",
                "f","n");

        CharacterMovement.Direction[] directions = dataStorage.getPlayerDirections();

        assertEquals(CharacterMovement.Direction.LEFT,directions[0]);
        assertEquals(CharacterMovement.Direction.UP,directions[1]);
        assertEquals(CharacterMovement.Direction.UP,dataStorage.getPlayerIsFacing(0));
        assertTrue(dataStorage.getPlayerIsMoving(0));
        assertFalse(dataStorage.getInvisibilityBoost(0));
        assertFalse(dataStorage.getSpeedBoost(0));
        assertEquals(130.0,dataStorage.getTransformCheck(0).x,0.01);
        assertEquals(150.0,dataStorage.getTransformCheck(0).y,0.01);

        assertEquals(CharacterMovement.Direction.RIGHT,directions[2]);
        assertEquals(CharacterMovement.Direction.NONE,directions[3]);
        assertEquals(CharacterMovement.Direction.RIGHT,dataStorage.getPlayerIsFacing(1));
        assertFalse(dataStorage.getPlayerIsMoving(1));
        assertTrue(dataStorage.getInvisibilityBoost(1));
        assertTrue(dataStorage.getSpeedBoost(1));
        assertEquals(-135.0,dataStorage.getTransformCheck(1).x,0.01);
        assertEquals(0.0,dataStorage.getTransformCheck(1).y,0.01);

        assertEquals(CharacterMovement.Direction.NONE,directions[4]);
        assertEquals(CharacterMovement.Direction.DOWN,directions[5]);
        assertEquals(CharacterMovement.Direction.DOWN,dataStorage.getPlayerIsFacing(2));
        assertFalse(dataStorage.getPlayerIsMoving(2));
        assertFalse(dataStorage.getInvisibilityBoost(2));
        assertFalse(dataStorage.getSpeedBoost(2));
        assertNull(dataStorage.getTransformCheck(2));
    }

    @Test
    public void setAndGetUsernameTest(){
        DataStorage dataStorage = new DataStorage(1,0);
        dataStorage.setUsername("username");
        assertEquals("username",dataStorage.getUsername());
    }

    @Test
    public void setAndGetCellGridTest(){
        DataStorage dataStorage = new DataStorage(1,0);
        Cell[][] cellGrid = new Cell[1][1];
        Cell cell = new Cell(10,10);
        cellGrid[0][0] = cell;

        dataStorage.setCellGrid(cellGrid);
        assertEquals(cell,dataStorage.getCellGrid()[0][0]);
    }

    @Test
    public void setAndGetSpawnAtTest(){
        DataStorage dataStorage = new DataStorage(1,0);
        XYPair pair = new XYPair(10.0,10.0);
        dataStorage.setSpawnAt(0,pair);
        assertEquals(pair,dataStorage.getSpawnAt()[0]);
    }

    @Test
    public void setAndGetEnemyInfoTest(){
        DataStorage dataStorage = new DataStorage(1,1);
        dataStorage.setEnemyDirections(0,CharacterMovement.Direction.LEFT,CharacterMovement.Direction.DOWN,
                CharacterMovement.Direction.DOWN);
        dataStorage.setEnemyIsMoving(0,true);

        CharacterMovement.Direction[] directions = dataStorage.enemyDirections;
        CharacterMovement.Direction[] facing = dataStorage.enemyIsFacing;
        boolean[] isMoving = dataStorage.enemyIsMoving;

        assertEquals(CharacterMovement.Direction.LEFT,directions[0]);
        assertEquals(CharacterMovement.Direction.DOWN,directions[1]);
        assertEquals(CharacterMovement.Direction.DOWN,facing[0]);
        assertTrue(isMoving[0]);
    }

    @Test
    public void updateAndGetUsernamesTest(){
        DataStorage dataStorage = new DataStorage(2,0);

        dataStorage.addUsername("test1");
        dataStorage.addUsername("test2");
        List<String> usernames = dataStorage.getUsernames();

        assertEquals(2,usernames.size());
        assertEquals("test1",usernames.get(0));
        assertEquals("test2",usernames.get(1));
    }

    @Test
    public void updateAndGetScoresTest(){
        DataStorage dataStorage = new DataStorage(2,0);
        HashMap<String,Integer> scores = new HashMap<>(){{
            put("test1",160);
            put("test2",300);
        }};
        dataStorage.setScores(scores);

        assertEquals(2,dataStorage.getScores().size());
        assertTrue(dataStorage.getScores().containsKey("test1"));
        assertTrue(dataStorage.getScores().containsKey("test2"));
        assertTrue(dataStorage.getScores().containsValue(160));
        assertTrue(dataStorage.getScores().containsValue(300));
    }
}
