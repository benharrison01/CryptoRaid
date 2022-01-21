package test.cs.teamproject.sabrewulf.map;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.Divider;
import uob.cs.teamproject.sabrewulf.map.GameMap;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameMapTest {

    /* map generation parameters */
    private final int mapWidth = 1080;
    private final int mapHeight = 1080;
    private final int cellCountX = 14;
    private final int cellCountY = 14;

    @Test
    public void addColouredDoorsTest() {
        GameMap map = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        assertEquals(true, checkDoors(map), "Add coloured doors test failed");
    }

    @Test
    public void addKeysTest() {
        GameMap map = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        assertEquals(true,checkKeys(map),"Add keys test failed");
    }

    @Test
    public void addPowerUpsTest() {
        GameMap map = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        assertEquals(true,checkPowerUps(map),"Add powerups test failed");
    }

    @Test
    public void CopyConstructorTest() {
        GameMap map = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        Cell[][] cellGrid = map.getCellGrid();
        GameMap copy = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY,cellGrid);
        for(int y=0;y<cellCountY;y++) {
            for(int x = 0; x<cellCountX;x++) {
                assertEquals(cellGrid[x][y],copy.getCell(x,y),"Copy constructor test failed");
            }
        }
    }

    /*This function is used to check that doors are being added to the map correctly
    * All we need to check in these tests is the following:
    *   - more than 1 wall (1 wall consists of 2 dividers) of each colour is being added
    *   - an even number of dividers is being added (due to walls consisting of 2 dividers)
    *   - no more than 4 walls of each colour is being added*/
    private boolean checkDoors(GameMap map) {
        Cell[][] cellGrid = map.getCellGrid();
        Divider.DividerType[] colours = {Divider.DividerType.BLUEDOOR,Divider.DividerType.GREENDOOR,Divider.DividerType.YELLOWDOOR};
        int counter = 0;
        for(Divider.DividerType colour:colours) {
            counter = 0;
            for(Cell[] column:cellGrid) {
                for (Cell cell : column) {
                    for (Divider.DividerType div : cell.getDividerArray()) {
                        if (div == colour) {
                            counter++;
                        }
                    }
                }
            }
            if(counter==0 || counter%2!=0) {
                return false;
            }
            if(counter>8) {
                return false;
            }
        }
        return true;
    }


    /*This function is to test that the keys are being added to the map correctly
    * To do this we need to ensure that there is 1 and only 1 of each colour key in the map*/
    private boolean checkKeys(GameMap map) {
        Cell[][] cellGrid = map.getCellGrid();
        int counter = 0;
        Cell.KeyType[] keys = {Cell.KeyType.BLUEKEY, Cell.KeyType.GREENKEY, Cell.KeyType.YELLOWKEY};
        for(Cell.KeyType key:keys) {
            counter=0;
            for(Cell[] column:cellGrid) {
                for(Cell cell:column) {
                    if(cell.hasKey() && cell.getKeyType()==key) {
                        counter++;
                    }
                }
            }
            if(counter!=1) {
                return false;
            }
        }
        return true;
    }

    /* This function is to test that the powerups are being added to the map correctly
     * To do this we need to ensure that there are only 10 keys in the map, as this is the default number added when the
     * map is generated*/
    private boolean checkPowerUps(GameMap map) {
        Cell[][] cellGrid = map.getCellGrid();
        int counter = 0;
        for (Cell[] column : cellGrid) {
            for (Cell cell : column) {
                if (cell.hasPowerUp()) {
                    counter++;
                }
            }
        }
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER && counter == 9) {
            return true;
        } else if(GameSettings.getGameMode() == MODE.MULTIPLAYER && counter == 10) {
            return true;
        } else if((GameSettings.getGameMode() == null) && (counter == 10)) {
            return true;
        } else { return false; }
    }
}