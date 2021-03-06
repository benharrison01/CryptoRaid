package uob.cs.teamproject.sabrewulf.map;

/**
 * This class is used for any operations which reference the map generated by the game.
 */

import javafx.scene.paint.Color;

public class GameMapWrapper {

    /* window & maze size attributes */

    private int cellCountX;
    private int cellCountY;
    private int cellWidth;
    private int cellHeight;
    private Cell[][] cellGrid;

    /* set maze size attributes */
    /**
     * @param cellCountX
     * The number of cells across the x-axis of the {@link Cell} grid.
     * @param cellCountY
     * The number of cells across the y-axis of the {@link Cell} grid.
     * @param cellWidth
     * The real-world width of the {@link Cell}s.
     * @param cellHeight
     * The real-world height of the {@link Cell}s.
     * @param cellGrid
     * A representation of the map stored in a 2d array of {@link Cell} objects, generated by {@link GameMap}
     */
    public GameMapWrapper(int cellCountX, int cellCountY, int cellWidth, int cellHeight, Cell[][] cellGrid) {
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellGrid = cellGrid;
    }

    public Cell[][] getCellGrid() {
        return cellGrid;
    }

    /**
     * Picks a random {@link Cell} from the map.
     * @return random {@link Cell} from the map */

    public Cell getRandomCell() {
        /* generate random X and Y grid references, bound by dimensions of map */
        int min = 0;
        int max = cellCountX - 1;
        int randomX = (int) (Math.random() * (max - min - 1) + min);
        int randomY = (int) (Math.random() * (max - min - 1) + min);
        /* return random cell from these grid references */
        return cellGrid[randomX][randomY];
    }

    /**
     * Gets the midpoint of a {@link Cell}.
     * @return the co-ordinates of the midpoint of a {@link Cell} as an array */

    public int[] getCellCoordinates(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();

        int[] coords = {x, y};

        return coords;
    }

    /**
     * Get the dimensions of a {@link Cell}.
     * @return the dimensions of a {@link Cell} as an array of format [x,y] */

    public int[] getCellDimensions() {
        int[] dim = new int[2];
        dim[0] = cellWidth;
        dim[1] = cellHeight;
        return dim;
    }

    /**
     * Get the colour of each of the {@link Cell}'s dividers.
     * @return the colour of each the {@link Cell}'s dividers in the order [UP, DOWN, LEFT, RIGHT] */
    public Color[] getCellDividerColours(Cell cell) {
        Color[] colours = new Color[4];
        for (int i = 0; i < 4; i++) {
            switch (cell.getCellDividers()[i].dividerType) {
                case WALL:
                    colours[i] = Color.RED;
                    break;
                case BLUEDOOR:
                    colours[i] = Color.BLUE;
                    break;
                case GREENDOOR:
                    colours[i] = Color.GREEN;
                    break;
                case YELLOWDOOR:
                    colours[i] = Color.YELLOW;
                    break;
                default:
                    colours[i] = Color.BLACK;
                    break;
            }
        }
        return colours;
    }

    /**
     * Get a random {@link Cell} between a range of specified grid references.
     * @return the randomly picked {@link Cell} from within the specified grid references. */
    public Cell getRandomCellOnRows(int lowerbound, int upperbound) {
        /* generate random X and Y grid references, bound by dimensions of map */
        int min = 0;
        int max = cellCountX - 1;
        int randomX = (int) (Math.random() * (max - min - 1) + min);
        int randomY = (int) (Math.random() * (upperbound - lowerbound - 1) + lowerbound);
        /* return random cell from these grid references */
        return cellGrid[randomX][randomY];
    }

    /**
     * Get a random empty {@link Cell} between a range of specified grid references.
     * @return the randomly picked empty {@link Cell} from within the specified grid references. */
    public Cell getRandomEmptyCellOnRows(int lowerbound, int upperbound) {
        Cell randomCell = getRandomCellOnRows(lowerbound, upperbound);
        while (!randomCell.isEmpty()) {
            randomCell = getRandomCellOnRows(lowerbound, upperbound);
        }
        return randomCell;
    }

    /** Function which takes map co-ordinates as parameters and returns the {@link Cell} which the point given by the
     * co-ordinates lies within.
     * -See report for explanation of wsdu.
     * @param x the x co-ordinate of the {@link Cell} in wsdu
     * @param y the y co-ordinate of the {@link Cell} in wsdu*/

    public Cell coordsToCell(int x, int y) {

        /* if the co-ordinates are out of bounds of the map, return null */

        if (x > (cellCountX * cellWidth)) {
            return null;
        }
        if (y > (cellCountY * cellWidth)) {
            return null;
        }

        /* find x and y values of cellGrid which correspond to the x and y map co-ordinates given */

        int x1 = (int) Math.floor((float) x / cellWidth);
        int y1 = (int) Math.floor((float) y / cellHeight);
        try {
        } catch (Exception e) {
            System.out.println("error in coordsToCell\n");
        }

        /* adjusted for scaling of the map */
        return cellGrid[x1 + (cellCountX / 2)][y1 + (cellCountY / 2)];
    }
}
