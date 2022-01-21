package test.cs.teamproject.sabrewulf.map;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.Divider;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    public void singleInitialisationTest() {
        Cell cell = new Cell(0,0);
        for(int i=0;i<4;i++) {
            Divider.DividerType[] divType = cell.getDividerArray();
            Divider.DividerType[] expected = {Divider.DividerType.WALL,Divider.DividerType.WALL,Divider.DividerType.WALL,Divider.DividerType.WALL};
            assertArrayEquals(expected, divType, "default divider types are wrong");

        }
        assertEquals(false, cell.hasKey(), "hasKey default value is wrong");
        assertEquals(false,cell.hasPowerUp(), "hesPowerUp default value is wrong");
        assertEquals(false,cell.isConnected(), "isConnected default value is wrong");
        assertEquals(0,cell.getGridX(), "getGridX function failed");
        assertEquals(0,cell.getGridY(), "getGridY function failed");
    }

    @Test
    public void singleCellComplexTest() {
        Cell cell = new Cell(1,1);
        assertEquals(1,cell.getGridX(),"getGridX function failed");
        assertEquals(1,cell.getGridY(),"getGridY function failed");
        cell.setKeyType(Cell.KeyType.BLUEKEY);
        cell.setHasKey(true);
        assertEquals(true, cell.hasKey(), "setKey function failed");
        assertEquals(Cell.KeyType.BLUEKEY, cell.getKeyType(), "setKeyType function failed");
        cell.setKeyType(Cell.KeyType.GREENKEY);
        assertEquals(Cell.KeyType.GREENKEY, cell.getKeyType(), "getKeyType function failed after key colour changed to green");
        cell.setKeyType(Cell.KeyType.YELLOWKEY);
        assertEquals(Cell.KeyType.YELLOWKEY, cell.getKeyType(), "getKeyType function failed after key colour changed to yellow");
        cell.setHasPowerUp(true);
        assertEquals(true, cell.hasPowerUp(), "hasPowerUp function failed");
    }

    @Test
    public void multiCellInitialisationTest() {
        Cell[] set1 = new Cell[4];
        set1[0] = new Cell(0,0);
        set1[1] = new Cell(0,1);
        set1[2] = new Cell(1,0);
        set1[3] = new Cell(1,1);
        set1[0].setNeighbour(Cell.Direction.DOWN, set1[1]);
        set1[0].setNeighbour(Cell.Direction.RIGHT, set1[2]);
        set1[1].setNeighbour(Cell.Direction.UP, set1[0]);
        set1[1].setNeighbour(Cell.Direction.RIGHT,set1[3]);
        set1[2].setNeighbour(Cell.Direction.DOWN, set1[3]);
        set1[2].setNeighbour(Cell.Direction.LEFT, set1[0]);
        set1[3].setNeighbour(Cell.Direction.LEFT, set1[1]);
        set1[3].setNeighbour(Cell.Direction.UP, set1[2]);

        set1[0].removeDividersBetween(set1[1]);
        assertEquals(Divider.DividerType.EMPTY, set1[0].getDividerArray()[1], "removeDividersBetween function failed");
        assertEquals(Divider.DividerType.EMPTY,set1[1].getDividerArray()[0], "removeDividersBetween function failed");

        set1[1].removeDividersBetween(set1[3]);
        assertEquals(Divider.DividerType.EMPTY,set1[1].getDividerArray()[2], "removeDividersBetween function failed");
        assertEquals(Divider.DividerType.EMPTY, set1[3].getDividerArray()[3], "removeDividersBetween function failed");

        set1[2].removeDividersBetween(set1[0]);
        assertEquals(Divider.DividerType.EMPTY, set1[2].getDividerArray()[3], "removeDividersBetween function failed");
        assertEquals(Divider.DividerType.EMPTY, set1[0].getDividerArray()[2], "removeDividersBetween function failed");

        Cell[] cell1Neighbours = {null, set1[1], set1[2], null};
        Cell[] cell2Neighbours = {set1[0], null, set1[3], null};
        Cell[] cell3Neighbours = {null, set1[3], null, set1[0]};
        Cell[] cell4Neighbours = {set1[2], null, null, set1[1]};
        Cell[][] neighbours = {cell1Neighbours, cell2Neighbours, cell3Neighbours, cell4Neighbours};
        Cell.Direction[] directions = {Cell.Direction.UP, Cell.Direction.DOWN, Cell.Direction.RIGHT, Cell.Direction.LEFT};

        for(int i=0;i<4;i++) {
            for(int j=0; j<4; j++) {
                assertEquals(neighbours[i][j], set1[i].getNeighbour(directions[j]), "Incorrect neighbour array for cell " + i);
            }
        }
    }

    @Test
    public void multiCellComplexTest() {
        Cell[] cellSet = new Cell[6];

        cellSet[0] = new Cell(0,0);
        cellSet[1] = new Cell(0,1);
        cellSet[2] = new Cell(1,0);
        cellSet[3] = new Cell(1,1);
        cellSet[4] = new Cell(2,0);
        cellSet[5] = new Cell(2,1);

        cellSet[0].setNeighbour(Cell.Direction.RIGHT,cellSet[2]);
        cellSet[0].setNeighbour(Cell.Direction.DOWN, cellSet[1]);
        cellSet[1].setNeighbour(Cell.Direction.UP,cellSet[0]);
        cellSet[1].setNeighbour(Cell.Direction.RIGHT,cellSet[3]);
        cellSet[2].setNeighbour(Cell.Direction.LEFT,cellSet[0]);
        cellSet[2].setNeighbour(Cell.Direction.DOWN,cellSet[3]);
        cellSet[2].setNeighbour(Cell.Direction.RIGHT,cellSet[4]);
        cellSet[3].setNeighbour(Cell.Direction.UP,cellSet[2]);
        cellSet[3].setNeighbour(Cell.Direction.LEFT,cellSet[1]);
        cellSet[3].setNeighbour(Cell.Direction.RIGHT,cellSet[5]);
        cellSet[4].setNeighbour(Cell.Direction.LEFT,cellSet[2]);
        cellSet[4].setNeighbour(Cell.Direction.DOWN,cellSet[5]);
        cellSet[5].setNeighbour(Cell.Direction.LEFT,cellSet[3]);
        cellSet[5].setNeighbour(Cell.Direction.UP,cellSet[4]);

        cellSet[0].setHasKey(true);
        cellSet[0].setKeyType(Cell.KeyType.BLUEKEY);
        cellSet[3].setHasPowerUp(true);

        cellSet[0].removeDividersBetween(cellSet[1]);
        cellSet[1].removeDividersBetween(cellSet[3]);
        cellSet[2].removeDividersBetween(cellSet[3]);
        cellSet[2].removeDividersBetween(cellSet[4]);
        cellSet[3].removeDividersBetween(cellSet[5]);

        cellSet[4].makeBlueDoor(Cell.Direction.DOWN);

        //test cell (0,0) walls
        Divider.DividerType[] div0 = {Divider.DividerType.WALL, Divider.DividerType.EMPTY, Divider.DividerType.WALL, Divider.DividerType.WALL};
        assertArrayEquals(div0, cellSet[0].getDividerArray(), "getDividerArray function failed for cell (0,0)");

        //test cell (0,0) content
        assertEquals(true,cellSet[0].hasKey(),"hasKey function failed for cell (0,0)");
        assertEquals(false,cellSet[0].hasPowerUp(),"hasPowerUp function failed for cell (0,0)");
        assertEquals(Cell.KeyType.BLUEKEY, cellSet[0].getKeyType());

        //test cell (0,0) drawOrder function
        int[] correctOrder0 = {1,0,2,3};
        assertArrayEquals(correctOrder0,cellSet[0].drawOrder(),"drawOrder function failed for cell (0,0)");

        //test cell (0,1) walls
        Divider.DividerType[] div1 = {Divider.DividerType.EMPTY, Divider.DividerType.WALL, Divider.DividerType.EMPTY, Divider.DividerType.WALL};
        assertArrayEquals(div1,cellSet[1].getDividerArray(),"getDividerArray function failed for cell (0,1)");

        //test cell (0,1) content
        assertEquals(false,cellSet[1].hasKey(),"hasKey function failed on cell (0,1)");
        assertEquals(false,cellSet[1].hasPowerUp(),"hasPowerUp function failed on cell (0,1)");
        assertNull(cellSet[1].getKeyType(),"getKeyType function failed on Cell (0,1)");

        //test cell (0,1) drawOrder function
        int[] correctOrder1 = {0,2,1,3};
        assertArrayEquals(correctOrder1,cellSet[1].drawOrder(),"drawOrder function failed on cell (0,1)");

        //test cell (1,0) walls
        Divider.DividerType[] div2 = {Divider.DividerType.WALL, Divider.DividerType.EMPTY, Divider.DividerType.EMPTY, Divider.DividerType.WALL};
        assertArrayEquals(div2,cellSet[2].getDividerArray(),"getDividerArray function failed on cell (1,0)");

        //test cell (1,0) content
        assertEquals(false,cellSet[2].hasKey(),"hasKey function failed on cell (1,0)");
        assertEquals(false,cellSet[2].hasPowerUp(),"hasPowerUp function failed on cell(1,0)");
        assertNull(cellSet[2].getKeyType(),"getKeyType function failed on cell (1,0)");

        //test cell (1,0) drawOrder
        int[] correctOrder2 = {1,2,0,3};
        assertArrayEquals(correctOrder2,cellSet[2].drawOrder(),"drawOrder function failed on cell (1,0)");

        //test cell (1,1) walls
        Divider.DividerType[] div3 = {Divider.DividerType.EMPTY, Divider.DividerType.WALL, Divider.DividerType.EMPTY, Divider.DividerType.EMPTY};
        assertArrayEquals(div3,cellSet[3].getDividerArray(),"getDividerArray function failed on cell (1,1)");

        //test cell (1,1) content
        assertEquals(false,cellSet[3].hasKey(),"hasKey function failed on cell (1,1)");
        assertEquals(true, cellSet[3].hasPowerUp(),"hasPowerUp function failed on cell (1,1)");
        assertNull(cellSet[3].getKeyType());

        //test cell (1,1) drawOrder
        int[] correctOrder3 = {0,2,3,1};
        assertArrayEquals(correctOrder3, cellSet[3].drawOrder(),"drawOrderFunction failed on cell (1,1)");

        //test cell(2,0) walls
        Divider.DividerType[] div4 = {Divider.DividerType.WALL, Divider.DividerType.BLUEDOOR, Divider.DividerType.WALL, Divider.DividerType.EMPTY};
        assertArrayEquals(div4,cellSet[4].getDividerArray(),"getDividerArray function failed for cell (2,0)");

        //test cell (2,0) content
        assertEquals(false,cellSet[4].hasKey(),"hasKey function failed on cell (2,0)");
        assertEquals(false,cellSet[4].hasPowerUp(),"hasPowerUp function failed on cell (2,0)");
        assertNull(cellSet[4].getKeyType(),"getKeyType function failed on cell (2,0)");

        //test cell (2,0) drawOrder
        int[] correctOrder = {3,1,0,2};
        assertArrayEquals(correctOrder,cellSet[4].drawOrder(),"drawOrder function failed on cell (2,0)");

        //test cell (2,1) walls
        Divider.DividerType[] div5 = {Divider.DividerType.BLUEDOOR, Divider.DividerType.WALL, Divider.DividerType.WALL, Divider.DividerType.EMPTY};
        assertArrayEquals(div5,cellSet[5].getDividerArray(),"getDividerArray function failed on cell (2,1)");

        //test cell (2,1) content
        assertEquals(false,cellSet[5].hasKey(),"hasKey function failed on cell (2,1)");
        assertEquals(false,cellSet[5].hasPowerUp(),"hasPowerUp function failed on cell (2,1)");
        assertNull(cellSet[5].getKeyType(), "getKeyType function failed on cell (2,1)");

        //test cell (2,1) drawOrder
        int[] correctOrder5 = {3,0,1,2};
        assertArrayEquals(correctOrder5,cellSet[5].drawOrder(),"drawOrder function failed on cell (2,1)");
    }


}
