package test.cs.teamproject.sabrewulf.map;

import javafx.scene.paint.Color;
import org.junit.Test;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMap;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapWrapperTest {

    /* map generation parameters */
    private final int mapWidth = 1080;
    private final int mapHeight = 1080;
    private final int cellCountX = 14;
    private final int cellCountY = 14;

    @Test
    public void InitialisationTest() {
        Cell[][] cellGrid = new Cell[cellCountX][cellCountY];
        for(int y=0;y<cellCountX;y++) {
            for(int x=0;x<cellCountY;x++) {
                cellGrid[x][y] = new Cell(x,y);
            }
        }

        GameMapWrapper wrapper = new GameMapWrapper(cellCountX,cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY,cellGrid);
        int[] dim = {mapWidth/cellCountX, mapHeight/cellCountY};
        assertArrayEquals(dim,wrapper.getCellDimensions(),"getCellDimensions function failed");
        assertArrayEquals(cellGrid,wrapper.getCellGrid(),"getCellGrid function failed");

        wrapper = new GameMapWrapper(cellCountX,cellCountY,mapWidth/cellCountX,
                15,cellGrid);
        dim[1] = 15;
        assertArrayEquals(dim,wrapper.getCellDimensions(),"getCellDimensions function failed");
    }

    @Test
    public void GameMapTest() {
        GameMap map = new GameMap(cellCountX,cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        assertArrayEquals(map.getCellGrid(),map.getGameMapWrapper().getCellGrid(),"getCellGrid function failed");
        int[] dim = {mapWidth/cellCountX,mapHeight/cellCountY};
        assertArrayEquals(dim, map.getGameMapWrapper().getCellDimensions(),"getCellDimensions function failed");
    }

    @Test
    public void getDividerColoursTest() {
        GameMap map = new GameMap(cellCountX,cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY);
        Cell[][] cellGrid = map.getGameMapWrapper().getCellGrid();
        for(int y=0;y<10;y++) {
            for(int x=0;x<10;x++) {
                assertArrayEquals(getColours(cellGrid[x][y]),map.getGameMapWrapper().getCellDividerColours(cellGrid[x][y]),"getCellDividerColours function failed");
            }
        }
    }

    @Test
    public void testGetCellCoordinates() {

        GameMapWrapper gameMapWrapper = new GameMapWrapper(14,14,20,20,null);

        Cell cell = new Cell(10,20);

        int[] cellCoordinates = {-492,-482};

        assertArrayEquals(cellCoordinates, gameMapWrapper.getCellCoordinates(cell));

    }

    private Color[] getColours(Cell cell) {
        Color[] colours = new Color[4];
        Cell.Direction[] directions = {Cell.Direction.UP, Cell.Direction.DOWN, Cell.Direction.RIGHT, Cell.Direction.LEFT};
        for(Cell.Direction direction:directions) {
            switch(cell.getCellDividers()[direction.ordinal()].getDividerType()) {
                case EMPTY:
                    colours[direction.ordinal()] = Color.BLACK;
                    break;
                case WALL:
                    colours[direction.ordinal()] = Color.RED;
                    break;
                case BLUEDOOR:
                    colours[direction.ordinal()] = Color.BLUE;
                    break;
                case GREENDOOR:
                    colours[direction.ordinal()] = Color.GREEN;
                    break;
                default:
                    colours[direction.ordinal()] = Color.YELLOW;
                    break;
            }
        }
        return colours;
    }

}
