package uob.cs.teamproject.sabrewulf.map;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;

import java.util.ArrayList;

/** GameMap is used to generate the structure of the map and call the renderer to draw all of the map elements.
 *
 * GameMap's main function is populating a grid of cells called 'cellGrid'. This is referred to by other functions to
 * determine where dividers, walls and items are located within the map. It is also used internally within GameMap to
 * communicate to the renderer which map elements need to be drawn and where they should go.
 *
 */

public class GameMap extends GameComponent {

    /* used for rendering */

    /* 2d array, used as a grid to store Cell objects which comprise the map */
    private Cell[][] cellGrid;
    /* list of cells where it's acceptable to place coloured doors in the map */
    Boolean[][] usableCells;
    /* list of cells which contain dead ends in map */
    private ArrayList<Cell> deadEnds;
    /* list of cells which contain walls which are part of a row of length 3 of more in map */
    private ArrayList<Cell> rowWalls;
    /* wrapper which needs to be referenced */
    private GameMapWrapper map;

    /* maze size attributes */

    private int cellCountX;
    private int cellCountY;
    private int cellWidth;
    private int cellHeight;

    /**
     * The constructor for the GameMap class. This calls functions which generate the game map randomly.
     * @param cellCountX
     * The number of cells across the x-axis of the cell grid.
     * @param cellCountY
     * The number of cells across the y-axis of the cell grid.
     * @param cellWidth
     * The real-world width of the cells.
     * @param cellHeight
     * The real-world height of the cells.
     */
    public GameMap(int cellCountX, int cellCountY, int cellWidth, int cellHeight) {
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.deadEnds = new ArrayList<>();
        this.rowWalls = new ArrayList<>();

        /* Initialises cellGrid & usableCells */

        cellGrid = new Cell[cellCountX][cellCountY];
        usableCells = new Boolean[cellCountX][cellCountY];

        /* Map generated through the following 5 steps:
        * (1) create a cell grid, where each cell has borders on all four of its sides
        * (2) remove dividers until the condition is satisfied that for all cells in the grid, you are able to get from
        * any cell to any other cell you pick via some path - all cells are connected to one another by at least 1 path
        * (3) remove extra dividers to make the map easier to traverse across
        * (4) turn some dividers into coloured doors which increases the intensity of the game as players will be able
        * to take shortcuts if they can go through doors
        * (5) add coins & keys to the game map
        *
        * The map is then rendered.
        *
        *  */

        /* step (1) */
        createCells();
        setCellNeighbours();
        /* step (2) */
        connectAllCells();
        /* step (3) */
        removeDividers(40);
        /* step (4) */
        //setRowWalls();
        setDeadEnds();
        setRowWalls();
        addColouredDoors("blue", 4);
        addColouredDoors("green", 4);
        addColouredDoors("yellow", 4);
        /* step (5) */
        /* wrapper is initialised and passed attributes of map */
        map = new GameMapWrapper(cellCountX,cellCountY,cellWidth,cellHeight,cellGrid);
        addKeys();
        //need to add the powerups
        addPowerUps();
        //setRowWalls();
        /* render the map */
        //renderMap();

    }

    /**
     * This is an alternative constructor for the GameMap class which is used for replicating an already existing map.
     * @param cellCountX
     * The number of cells across the x-axis of the cell grid.
     * @param cellCountY
     * The number of cells across the y-axis of the cell grid.
     * @param cellWidth
     * The real-world width of the cells.
     * @param cellHeight
     * The real-world height of the cells.
     * @param cellGrid
     * The existing grid of cells from the map you are replicating.
     */
    public GameMap(int cellCountX, int cellCountY, int cellWidth, int cellHeight, Cell[][] cellGrid) {
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellGrid = cellGrid;
        this.map = new GameMapWrapper(cellCountX, cellCountY, cellWidth, cellHeight, cellGrid);
    }

    /**
     * This is used to get the 2D grid containing all the cells in the map.
     * @return
     * Returns the 2D array of {@link Cell}s for the map.
     */
    public Cell[][] getCellGrid() { return cellGrid; }

    /**
     * This is used to get a specific cell from within the 2D array of cells.
     * @param gridX
     * The x-coordinate of the cell in the cell grid.
     * @param gridY
     * The y-coordinate of the cell in the cell grid.
     * @return
     * Returns the {@link Cell} which corresponds to the x and y coorindate provided.
     */
    public Cell getCell(int gridX, int gridY) {
        return cellGrid[gridX][gridY];
    }

    /**
     * This is used to get the wrapper class for the game map. The wrapper class is used to read the game map.
     * @return
     * Returns the {@link GameMapWrapper} object for the game map.
     */
    public GameMapWrapper getGameMapWrapper() {
        return map;
    }

    /** Create cell objects and store them in cellGrid 2d array. */

    private void createCells() {
        for(int gridY = 0; gridY < cellCountY; gridY++) {

            for (int gridX = 0; gridX < cellCountX; gridX++) {

                int x = gridX * cellWidth;
                int y = gridY * cellHeight;
                Cell cell = new Cell(x,y);
                cellGrid[gridX][gridY] = cell;
                usableCells[gridX][gridY] = true;

            }

        }
    }

    /** Store the neighbours of each cell within each cell object. */

    private void setCellNeighbours() {
        for (int gridY = 0; gridY < cellCountY; gridY++) {

            for (int gridX = 0; gridX < cellCountX; gridX++) {
                Cell cell = cellGrid[gridX][gridY];

                /* set the neighbour above each cell apart from the top row of cells */
                if (gridY > 0) {
                    cell.setNeighbour(Cell.Direction.UP, cellGrid[gridX][gridY-1]);
                }

                /* set the neighbour below each cell apart from the bottom row of cells */

                if (gridY < cellCountY - 1) {
                    cell.setNeighbour(Cell.Direction.DOWN, cellGrid[gridX][gridY+1]);
                }

                /* set the neighbour to the right of each cell apart from the far right row of cells */

                if (gridX > 0) {
                    cell.setNeighbour(Cell.Direction.LEFT, cellGrid[gridX-1][gridY]);
                }

                /* set the neighbour to the left of each cell apart from the far left of cells */

                if (gridX < cellCountX - 1) {
                    cell.setNeighbour(Cell.Direction.RIGHT, cellGrid[gridX+1][gridY]);
                }

            }

        }
    }

    /** Remove dividers until the condition is satisfied that for all cells in the grid, you are able to get from
        any cell to any other cell you pick via some path - all cells are connected to one another by at least 1 path. */

    private void connectAllCells() {

        /* List to store connected cells which have neighbouring cells which aren't connected */
        ArrayList<Cell> activeCellList = new ArrayList<Cell>();

        /* start at top left of grid (arbitrary starting point)
        * mark this room as connected and add it to activeCellList
        * */

        Cell currentCell = cellGrid[0][0];
        currentCell.setIsConnected(true);
        activeCellList.add(0, currentCell);

        float branchProbability = 0.5f;

        /* while there's still unconnected cells... */

        while (activeCellList.size() > 0) {

            /* pick a room from activeCellList */

            /* 50% chance of choosing a random cell in activeCellList */

            if (Math.random() < branchProbability) {
                int cellIndex = (int) (Math.random() * activeCellList.size());
                currentCell = activeCellList.get(cellIndex);
            }

            /* 50% chance of picking the cell which is at the end of activeCellList */

            else {
                currentCell = activeCellList.get(activeCellList.size() - 1);
            }

            /* pick a random unconnected neighbour of currentCell and remove the divider between the two cells */

            if (currentCell.hasUnconnectedNeighbour()) {
                Cell nextCell = currentCell.getRandomUnconnectedNeighbour();
                currentCell.removeDividersBetween(nextCell);
                nextCell.setIsConnected(true);
                activeCellList.add(0, nextCell);
            }

            /* if you find currentCell has no unconnected neighbours, remove it from activeCellList */

            else {
                activeCellList.remove(currentCell);
            }

        }
    }

    /** Remove a specified number of dividers randomly in order to make the map easier to traverse across. */

    private void removeDividers(int dividersToRemove) {
        while (dividersToRemove > 0) {
            /* pick a random cell */
            int gridX = (int)Math.floor(Math.random() * cellCountX);
            int gridY = (int)Math.floor(Math.random() * cellCountY);
            /* pick a random direction to delete a divider */
            Cell.Direction direction = (Cell.Direction.values()[(int)Math.floor(Math.random() * 4)]);
            Cell cell = cellGrid[gridX][gridY];
            /* delete the divider if a divider exists in that direction */
            if ((cell.hasNeighbour(direction)) && cell.hasDivider(direction)) {
                    cell.removeDividers(direction);
                    dividersToRemove--;
            }
        }
    }

    /** Returns a list of all cells in the map which contain dead ends. */

    private void setDeadEnds() {

        for (int gridY = 0; gridY < cellCountY; gridY++) {

            for (int gridX = 0; gridX < cellCountX; gridX++) {
                Cell cell = cellGrid[gridX][gridY];

                /* if the current cell has three walls, find which one terminates the dead end */

                if(cell.getDividersActive().size() > 3) {

                    /* assess whether dead end is terminated by a wall at the bottom of the cell */

                    if((cell.isWall(Cell.Direction.DOWN)) && (cell.isWall(Cell.Direction.LEFT)) && (cell.isWall(Cell.Direction.RIGHT))) {

                        cell.setIsDeadEnd(true);
                        cell.setTerminatingWallDirection(Cell.Direction.DOWN);
                    }

                    /* assess whether dead end is terminated by the left wall of the cell */

                    else if ((cell.isWall(Cell.Direction.LEFT)) && (cell.isWall(Cell.Direction.UP)) && (cell.isWall(Cell.Direction.DOWN))) {

                        cell.setIsDeadEnd(true);
                        cell.setTerminatingWallDirection(Cell.Direction.LEFT);
                    }

                    /* assess whether dead end is terminated by a wall at the top of the cell */

                    else if ((cell.isWall(Cell.Direction.UP) && (cell.isWall(Cell.Direction.LEFT)) && (cell.isWall(Cell.Direction.RIGHT)))) {

                        cell.setIsDeadEnd(true);
                        cell.setTerminatingWallDirection(Cell.Direction.UP);
                    }

                    /* assess whether dead end is terminated by the right wall of the cell */

                    else if ((cell.isWall(Cell.Direction.RIGHT) && (cell.isWall(Cell.Direction.UP)) && (cell.isWall(Cell.Direction.DOWN)))) {

                        cell.setIsDeadEnd(true);
                        cell.setTerminatingWallDirection(Cell.Direction.RIGHT);
                    }

                    else { cell.setIsDeadEnd(false); }

                }

                else { cell.setIsDeadEnd(false); }

            }

        }

        generateDeadEndList();

    }

    /** Returns a list of all cells in the map which contain walls which are part of a row. */

    private void setRowWalls() {

        for (int gridY = 0; gridY < cellCountY; gridY++) {

            for (int gridX = 0; gridX < cellCountX; gridX++) {
                Cell cell = cellGrid[gridX][gridY];

                Cell.Direction[] directionLeftRight = {Cell.Direction.LEFT, Cell.Direction.RIGHT};
                Cell.Direction[] directionUpDown = {Cell.Direction.DOWN, Cell.Direction.UP};

                for (Cell.Direction direction : directionLeftRight) {

                    if (cell.isWall(direction)) {

                        if ((gridY < cellCountY - 1) && (gridY > 0)) {

                            if (cellGrid[gridX][gridY - 1].isWall(direction) && cellGrid[gridX][gridY + 1].isWall(direction)
                                    && (gridX > 0) && (gridX < cellCountX - 1)) {

                                cell.setHasRowWall(true);
                                cell.setRowWallDirection(direction);
                            }
                        }
                    }
                }

                for (Cell.Direction direction : directionUpDown) {

                    if (cell.isWall(direction)) {
                        if ((gridX < cellCountY - 1) && (gridX > 0)) {

                            if (cellGrid[gridX - 1][gridY].isWall(direction) && cellGrid[gridX + 1][gridY].isWall(direction)
                                    && (gridY > 0) && (gridY < cellCountY - 1)) {

                                cell.setHasRowWall(true);
                                cell.setRowWallDirection(direction);
                            }
                        }
                    }
                }
            }
        }
        generateRowWallList();

    }

    /** Turns some dividers into coloured doors which increases the intensity of the game as players will be able
     * to take shortcuts if they can go through doors. */

    private void addColouredDoors(String doorColour, int maxColouredDoors) {
        /* pick a random number of doors to add */
        int minColouredDoors = maxColouredDoors/2;
        int numColouredDoors = (int) Math.floor(Math.random() * (maxColouredDoors-(minColouredDoors)) + minColouredDoors);
        numColouredDoors++;
        int i = 0;
        /* cells which have dead ends or walls that are part of a row of length 3 or more chosen to add coloured doors to */
        ArrayList<Cell> cells = generateColouredDoorCellList(numColouredDoors);
        while(i<cells.size()) {
            /* retrieves a cell to add a coloured door to */
            Cell cell = cells.get(i);
            /* add appropriate coloured door */
            switch(doorColour) {
                /* door is added to replace wall which terminates dead end */
                case("blue"):
                    if(i < cells.size()/2) { cell.makeBlueDoor(cell.getTerminatingWallDirection()); }
                    else { cell.makeBlueDoor(cell.getRowWallDirection()); }
                    break;
                case("green"):
                    if(i < cells.size()/2) { cell.makeGreenDoor(cell.getTerminatingWallDirection()); }
                    else { cell.makeGreenDoor(cell.getRowWallDirection()); }
                    break;
                default:
                    if(i < cells.size()/2) { cell.makeYellowDoor(cell.getTerminatingWallDirection()); }
                    else { cell.makeYellowDoor(cell.getRowWallDirection()); }
                    break;
            }
            i++;
        }
    }

    /**
     * Checks the specifics of the map layout to determine which locations coloured doors can be added to.
     * @return a list of size n of {@link Cell}s which are appropriate to add coloured doors to */
    private ArrayList<Cell> generateColouredDoorCellList(int n) {

        ArrayList<Cell> colouredDoorCellList = new ArrayList<>();

        /* add n cells to list */

        for (int i = 0; i < n; i++) {
            Cell element;
            int nextElement;
            Boolean validElement = false;

            /* repeats until a valid cell has been found and added to colouredDoorCellList */
            /* cell is valid if its Boolean value stored in usableCells[[] is True */
            while(!validElement) {

                /* fill first half of the colouredDoorCellList with cells which contain dead ends */
                if (i < n / 2) {
                    nextElement = (int) Math.floor(Math.random() * deadEnds.size());
                    element = deadEnds.get(nextElement);
                }
                /* fill second half of colouredDoorCellList with cells which contains walls which are part of
                * walls of length 3 or more */
                else {
                    nextElement = (int) Math.floor(Math.random() * rowWalls.size());
                    element = rowWalls.get(nextElement); }
                /* confirm selected cell is valid and add to colouredDoorCellList, if not loop */
                if(usableCells[element.getCoordX()][element.getCoordY()]) {
                    validElement = true;
                    colouredDoorCellList.add(element);
                    usableCells = makeNeighboursUnusable(usableCells, element);
                }
            }
        }
        return colouredDoorCellList;
    }

    /**
    * Determines whether each cell is suitable to add a coloured door to.
    * Given a cell, prevents a coloured door from being added to all of its surrounding neighbours.
    * This prevents two coloured doors being added in neighbouring cells.
     * @return a 2D Boolean array, with each element corresponding to an element in cellGrid[][]*/
    private Boolean[][] makeNeighboursUnusable(Boolean[][] usableCells, Cell element) {
      /* get x and y grid reference of current cell */
        int x = element.getCoordX();
        int y = element.getCoordY();

        /* edge cases of the map are taken into consideration before usableCells is modified
        *  each case is demonstrated with a commented illustration of the scenario
        *  NA = cell doesn't exist, FALSE = cell exists and will be set to have value FALSE in usableCells
        *  X = current cell, will be set to have value FALSE in usableCells */

        if (x == 0) {
            if (y == 0) {

                /**
                 *
                 * edge case
                 *
                 NA |   NA  |  NA
                 —————————————————————
                 NA |   X   | FALSE
                 —————————————————————
                 NA | FALSE | FALSE
                 **/

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            } else if (y == cellCountY - 1) {

                /**
                 *
                 * edge case
                 *
                 NA | FALSE | FALSE
                 —————————————————————
                 NA |   X   | FALSE
                 —————————————————————
                 NA |  NA   |  NA
                 **/

                for (int i = 0; i < 2; i++) {
                    for (int j = -1; j < 1; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            } else {

                /**
                 *
                 * general case
                 *
                 NA | FALSE | FALSE
                 —————————————————————
                 NA |   X   | FALSE
                 —————————————————————
                 NA | FALSE | FALSE
                 **/

                for (int i = 0; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            }
        }
        else if (x == cellCountX - 1) {
            if (y == 0) {

                /**
                 *
                 * edge case
                 *
                 NA    |   NA  |  NA
                 —————————————————————
                 FALSE |   X   |  NA
                 —————————————————————
                 FALSE | FALSE |  NA
                 **/

                for (int i = -1; i < 1; i++) {
                    for (int j = 0; j < 2; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            } else if (y == cellCountY - 1) {

                /**
                 *
                 * edge case
                 *
                 FALSE | FALSE |  NA
                 —————————————————————
                 FALSE |   X   |  NA
                 —————————————————————
                 NA    |  NA   |  NA
                 **/

                for (int i = -1; i < 1; i++) {
                    for (int j = -1; j < 1; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            } else {

                /**
                 *
                 * general case
                 *
                 FALSE | FALSE |  NA
                 —————————————————————
                    X  | FALSE |  NA
                 —————————————————————
                 FALSE | FALSE |  NA
                 **/

                for (int i = -1; i < 1; i++) {
                    for (int j = -1; j < 2; j++) {
                        usableCells[x + i][y + j] = false;
                    }
                }
            }
        } else if (y == 0) {

            /**
             *
             * general case
             *
               NA  |   NA   |  NA
             —————————————————————
             FALSE |    X   | FALSE
             —————————————————————
             FALSE | FALSE | FALSE
             **/

            for (int i = -1; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    usableCells[x + i][y + j] = false;
                }
            }
        } else if (y == cellCountY - 1) {

            /**
             *
             * general case
             *
             FALSE |  FALSE | FALSE
             —————————————————————
             FALSE |   X   | FALSE
             —————————————————————
             NA  |  NA   |  NA
             **/

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 1; j++) {
                    usableCells[x + i][y + j] = false;
                }
            }
        } else {

            /**
             *
             * general case
             *
             FALSE | FALSE | FALSE
             —————————————————————
             FALSE |   X   | FALSE
             —————————————————————
             FALSE | FALSE | FALSE
             **/

            for(int i = -1; i < 2; i++) {
                for(int j = -1; j < 2; j++) {
                    usableCells[x+i][y+j] = false;
                }
            }
        }

        return usableCells;
    }

    /** Generate the list of cells which contain walls which are part of a row of length 3 of more in map */

    private void generateRowWallList() {
        for (int gridY = 0; gridY < cellCountY; gridY++) {
            for (int gridX = 0; gridX < cellCountX; gridX++) {
                Cell cell = cellGrid[gridX][gridY];
                /* if a cell contains a wall which is part of a row of length 3 or more, add it to the list of dead ends */
                if (cell.hasRowWall() == true) {
                    rowWalls.add(cell);
                }
            }
        }
    }

    /** Generate the list of cells which contain dead ends. */

    private void generateDeadEndList() {
        /* excludes dead ends which exist as a result of walls which exist as borders of the map */
        for (int gridY = 1; gridY < cellCountY - 1; gridY++) {
            for (int gridX = 1; gridX < cellCountX - 1; gridX++) {
                Cell cell = cellGrid[gridX][gridY];
                /* if a cell contains a dead end, add it to the list of dead ends */
                if (cell.isDeadEnd() == true) {
                    deadEnds.add(cell);
                }
            }
        }
    }

    /** Randomly places keys around map. */

    private void addKeys() {
        /* repeats for each colour of key */
        for(int i = 0; i<3; i++) {
            int x;
            int y;
            Cell cell;
            while(true) {
                /* find a random cell to add a key to */
                x = (int) Math.floor(Math.random()*cellCountX);
                y = (int) Math.floor(Math.random()*cellCountY);
                cell = cellGrid[x][y];
                /* make sure the cell doesn't already contain a key */
                if(!cell.hasKey()) {
                    break;
                }
            }
            /* cell set to contain a key so that GameWorld can instantiate a key with the correct cell coordinates */
            switch (i) {
                case 0:
                    cell.setKeyType(Cell.KeyType.BLUEKEY);
                    break;
                case 1:
                    cell.setKeyType(Cell.KeyType.GREENKEY);
                    break;
                default:
                    cell.setKeyType(Cell.KeyType.YELLOWKEY);
                    break;
            }
            cell.setHasKey(true);
        }
    }

    /** Randomly places power ups around map. */
    private void addPowerUps() {
        /* repeats until the specified number of powerups have been added */
        int numPowerUps;
        boolean singlePlayerGame = (GameSettings.getGameMode() == MODE.SINGLEPLAYER);
        if(singlePlayerGame) { numPowerUps = 9; } else { numPowerUps = 10; }
            for(int i = 0; i<numPowerUps; i++) {
                int x;
                int y;
                Cell cell;
                while(true) {
                    /* find a random cell to add a powerup to */
                    x = (int) Math.floor(Math.random()*cellCountX);
                    y = (int) Math.floor(Math.random()*cellCountY);
                    cell = cellGrid[x][y];
                    /* make sure the cell doesn't already contain a powerup */
                    if(!cell.hasPowerUp()) {
                        break;
                    }
                }
                /* determine which powerups are going to be added through which gamemode is set */
                if(singlePlayerGame) {
                    /* add types and numbers of powerup for a singleplayer session*/
                    if (i < 3) {
                        cell.setPowerUpType(Cell.PowerUpType.INVISIBILITY);
                    } else if (i < 6) {
                        cell.setPowerUpType(Cell.PowerUpType.SPEEDUP);
                    } else {
                        cell.setPowerUpType(Cell.PowerUpType.ADDLIFE);
                    }
                } else {
                    /* add the appropriate types and numbers of powerup - there is no addlife powerup in a
                    * multiplayer session */
                    if (i < 5) {
                        cell.setPowerUpType(Cell.PowerUpType.INVISIBILITY);
                    } else {
                        cell.setPowerUpType(Cell.PowerUpType.SPEEDUP);
                    }
                }
                /* cell set to contain a powerup so that GameWorld can instantiate a powerup with the correct cell coordinates */
                cell.setHasPowerUp(true);
            }
        }

}
