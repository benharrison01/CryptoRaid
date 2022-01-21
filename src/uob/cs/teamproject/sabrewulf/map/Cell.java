package uob.cs.teamproject.sabrewulf.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a cell on the cell grid.
 * It is used to generate the map for the game.
 */

public class Cell implements Serializable {

    /**
     * This is used to represent values which refer to the neighbouring cells.
     * The valid values are UP, DOWN, RIGHT and LEFT.
     */
    public enum Direction { UP,DOWN,RIGHT,LEFT }

    /**
     * This is used to reference the type of key in a cell.
     * The valid values are BLUEKEY, GREENKEY and YELLOWKEY
     */
    public enum KeyType { BLUEKEY, GREENKEY, YELLOWKEY }

    /** values used to reference type of power up in cell. */

    public enum PowerUpType {SPEEDUP, INVISIBILITY, ADDLIFE};

    /* stores directions in which cell is surrounded by dividers */
    private List<Direction> dividersActive = new ArrayList<Direction>();

    /* stores Divider objects */
    private Divider[] cellDividers;

    /* does the cell contain a key or a power up? If not then cell contains a coin*/
    private boolean hasKey;
    private boolean hasPowerUp;

    private KeyType keyType;
    private PowerUpType powerUpType;

    /* arrays to store neighbouring cells and cell borders*/
    private Divider.DividerType[] dividerArray = new Divider.DividerType[4];
    private Cell[] neighbourArray;

    /* stores whether cell is connected*/
    private boolean isConnected;

    private boolean isEmpty = true;

    /* stores whether cell is a dead end & if so the direction of the wall which terminates the dead end */

    private boolean isDeadEnd = false;
    private Direction terminatingWallDirection;

    /* stores whether cell contains a wall which is part of a row of length 3 or more and the direction of said wall
     * N.B. the cell could contain more than one wall which meets this criteria, the last cell which is found in the
     * setRowWalls() method is the one that is stored here*/

    private boolean hasRowWall = false;
    private Direction rowWallDirection;

    /* stores cell co-ordinates */
    private int x;
    private int y;

    /**
     * Cell constructor. This creates a new cell with four dividers.
     * @param x
     * This value is used to store the x co-ordinate of the cell.
     * @param y
     * This value is used to store the y co-ordinate of the cell.
     */
    public Cell(int x, int y){

        this.x = x;
        this.y = y;
        this.hasKey = false;

        /*Create borders of cell*/

        dividersActive.add(Direction.UP);
        dividersActive.add(Direction.DOWN);
        dividersActive.add(Direction.LEFT);
        dividersActive.add(Direction.RIGHT);

        /* initialises cellDivider array */

        cellDividers = new Divider[4];
        for(int i = 0;i<4;i++) {
            cellDividers[i] = new Divider(true);
        }

        /*Cell is not connected to any other cells when it is initialised*/

        isConnected = false;

        /*Array for use in GameMap class*/

        neighbourArray = new Cell[4];
    }

    /**
     * This is used to get a random neighbour cell which is unconnected.
     * @return
     * Returns a neighbouring cell which is unconnected.
     */
    public Cell getRandomUnconnectedNeighbour() {

        ArrayList<Direction> directionList = new ArrayList<>();
        /* for each direction, check whether an unconnected neighbour is present. If so, add it to the list */
        for (Direction direction : Direction.values()) {

            if ((hasNeighbour(direction)) && (!getNeighbour(direction).isConnected())) {
                directionList.add(direction);
            }

        }
        /* choose a random neighbour from the list */
        int directionIndex = (int)Math.floor( Math.random() * directionList.size());
        Direction direction = directionList.get(directionIndex);
        return getNeighbour(direction);
    }

    /**
     * This is used to set a certain cell to be a neighbour in a certain direction.
     * @param direction
     * The direction in which you want the cell to be the neighbour.
     * @param neighbour
     * The cell which you want to be the neighbour.
     */
    public void setNeighbour (Direction direction, Cell neighbour) {
        neighbourArray[direction.ordinal()] = neighbour;
        dividersActive.add(direction);
    }

    /**
     * This is used to check if a cell has a neighbouring cell in a certain direction.
     * @param direction
     * The direction in which you're checking for a neighbour.
     * @return
     * Returns true if there is a neighbour in the given direction and false otherwise.
     */
    public boolean hasNeighbour(Direction direction) {
        return neighbourArray[direction.ordinal()] != null;
    }

    /**
     * This is used to get a cells neighbouring cell in a given direction.
     * @param direction
     * The direction of the neighbouring cell you want.
     * @return
     * Returns the neighbouring cell.
     */
    public Cell getNeighbour(Direction direction) {
        return neighbourArray[direction.ordinal()];
    }

    /**
     * This is used to check if a cell has an active divider in a given direction.
     * @param direction
     * The direction in which you want to check if there is an active divider.
     * @return
     * Returns true if there is an active divider in the given direction and false otherwise.
     */
    public boolean hasDivider(Direction direction) {
        return dividersActive.contains(direction);
    }

    /**
     * This is used to get a list of all the active dividers that belong to a cell.
     * @return
     * Returns a list of all the active dividers of the cell.
     */
    public List<Direction> getDividersActive() { return dividersActive; }

    /**
     * This is used to get a list of all the dividers that belong to a cell.
     * @return
     * Returns a list of all the dividers of the cell.
     */
    public Divider[] getCellDividers() { return cellDividers; }

    /**
     * This is used to get an array of the type of dividers which belong to the cell.
     * @return
     * Returns an array of the type DividerType.
     */
    public Divider.DividerType[] getDividerArray() {
        //try{
        for (int i = 0; i < 4; i++) {
            if(dividersActive.contains(Direction.values()[i])) {
                dividerArray[i] = cellDividers[i].getDividerType();
            }
        }
        for (int i = 0; i<4; i++){
            if (dividerArray[i] == null) {
                dividerArray[i] = Divider.DividerType.EMPTY;
            }
        }
        return dividerArray;
    }

    /**
     * This is used to set the value of the variable isConnected.
     * @param b
     * The value which you want to set isConnected to.
     */
    public void setIsConnected(boolean b) {
        isConnected = b;
    }

    /**
     * This is used to get the isConnected value of the cell.
     * @return
     * Returns true if isConnected is true and false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * This is used to set the value of the variable hasKey.
     * @param hasKey
     * The value which you want to set hasKey to.
     */
    public void setHasKey(boolean hasKey) { this.hasKey = hasKey; }

    /**
     * This is used to get the value of the hasKey variable.
     * @return
     * Returns true if the hasKey value is true and false otherwise.
     */
    public boolean hasKey() { return hasKey; }

    /**
     * This is used to get the value of the hasPowerUp value
     * @return
     * Returns true if the hasPowerUp value is true and false otherwise.
     */
    public boolean hasPowerUp() {
        return this.hasPowerUp;
    }

    /**
     * This is used to set the hasPowerUp value for the cell.
     * @param powerUp
     * The value which you want to set the hasPowerUp variable to.
     */
    public void setHasPowerUp(boolean powerUp) { this.hasPowerUp = powerUp; }

    /**
     * This is used to get the value of the KeyType variable of the cell.
     * @return
     * Returns the value of the KeyType of the key if the cell contains one or null otherwise.
     */
    public KeyType getKeyType() {
        return keyType;
    }

    /**
     * This is used to set the value of the KeyType variable.
     * @param keyType
     * The value which you want to set the KeyType variable to.
     */
    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    /**
     * This is used to set the value of the isDeadEnd variable of the cell.
     * @param isDeadEnd
     * The value which you want to set the isDeadEnd variable of the cell to.
     */
    public void setIsDeadEnd(boolean isDeadEnd) { this.isDeadEnd = isDeadEnd; }

    /**
     * This is used to get the value of the isDeadEnd variable of the cell.
     * @return
     * Returns true if the cell is a dead end and false otherwise.
     */
    public Boolean isDeadEnd() { return isDeadEnd; }

    /**
     * This is used to set the value of the variable hasRowWall.
     * @param hasRowWall
     * The value which you want to set the variable hasRowWall to.
     */
    public void setHasRowWall(boolean hasRowWall) { this.hasRowWall = hasRowWall; }

    /**
     * This is used to get the value of the variable hasRowWall.
     * @return
     * Returns a boolean value which is the value of the variable hasRowWall.
     */
    public Boolean hasRowWall() { return hasRowWall; }

    /**
     * This is used to get the type of power up which the cell contains if the cell contains a power up.
     * @return
     * Returns the type of power up if the cell contains a power up or null otherwise.
     */
    public PowerUpType getPowerUpType() { return powerUpType; }

    /**
     * This is used to set the value of the variable powerUpType for the cell.
     * @param powerUpType
     * The value which you want to set the variable powerUpType to.
     */
    public void setPowerUpType(PowerUpType powerUpType) { this.powerUpType = powerUpType; }

    /**
     * This is used to set the value of the variable terminatingWallDirection.
     * @param direction
     * The value which you want to set the variable terminatingWallDirection to.
     */
    public void setTerminatingWallDirection(Direction direction) { this.terminatingWallDirection = direction; }

    /**
     * This is used to get the value of the variable terinatingWallDirection.
     * @return
     * Returns the value of the variable terminatingWallDirection.
     */
    public Direction getTerminatingWallDirection() { return terminatingWallDirection; }

    /**
     * This is used to set the value of the variable rowWallDirection.
     * @param direction
     * The value which you want to set the variable rowWallDirection to.
     */
    public void setRowWallDirection(Direction direction) { this.rowWallDirection = direction; }

    /**
     * This is used to get the value of the variable rowWallDirection.
     * @return
     * Returns the value of the variable rowWallDirection.
     */
    public Direction getRowWallDirection() { return rowWallDirection; }

    /**
     * This is used to get the game world x coordinate of the centre of the cell.
     * @return
     * Returns an integer representing the game world x coordinate of the centre of the cell.
     */
    public int getGridX() { return this.x; }

    /**
     * This is used to get the game world y coordinate of the centre of the cell.
     * @return
     * Returns an integer representing the game world y coordinate of the centre of the cell.
     */
    public int getGridY() { return this.y; }

    /**
     * This is used to get the x coordinate of the cell in the cell grid.
     * @return
     * Returns an integer representing the x coordinate of the cell in the cell grid.
     */
    public int getCoordX() { return this.x/77; }

    /**
     * This is used to get the y coordinate of the cell in the cell grid.
     * @return
     * Returns an integer representing the y coordinate of the cell in the cell grid.
     */
    public int getCoordY() { return this.y/77; }

    /**
     * This is used to get the game world x coordinate of the centre of the cell.
     * @return
     * Returns an integer representing the game world x coordinate of the centre of the cell.
     */
    public int getX() { return x - 540 + 77/2; }

    /**
     * This is used to get the game world y coordinate of the centre of the cell.
     * @return
     * Returns an integer representing the game world y coordinate of the centre of the cell.
     */
    public int getY() { return y - 540 + 77/2; }

    /**
     * This is used to check whether the cell is empty.
     * @return
     * Returns true if the cell is empty and false otherwise.
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * This is used to set the value of the variable isEmpty.
     * @param empty
     * The value you want to set the variable isEmpty to.
     */
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    /*Functions to be used by GameMap class in generating maze*/

    /**
     * This is used to get the order which the dividers of the cell should be rendered in. This is necessary to prevent
     * rendering issues. The divider to be rendered first will be in the direction that the direction in the first place in
     * the array points to.
     * @return
     * Returns an array of directions.
     */
    public int[] drawOrder() {
        int[] order = new int[4];
        int i = 0;
        //checks through all the boarders and adds any inactive boarders to the draw order
        for(int j = 0; j<4; j++) {
            if(!this.cellDividers[j].isActive()) {
                order[i] = j;
                i++;
            }
        }
        //checks through all the boarders and adds any coloured doors to the draw order
        for(int j = 0; j<4; j++) {
            if(this.cellDividers[j].isActive() && this.cellDividers[j].isColoured()) {
                order[i] = j;
                i++;
            }
        }
        //checks through all the boarders and adds any active boarders which arent coloured doors
        for(int j = 0; j<4; j++) {
            if(this.cellDividers[j].isActive() && !this.cellDividers[j].isColoured()) {
                order[i] = j;
                i++;
            }
        }
        //return the draw order where order[0] is drawn first and order[3] is drawn last
        return order;
    }

    /**
     * This is used to get a divider in a given direction.
     * @param direction
     * The direction of the divider that you want to get.
     * @return
     * Returns the divider in the given direction.
     */
    public Divider getDivider(Direction direction) {
        return cellDividers[direction.ordinal()];
    }

    /**
     * This is used to determine whether a cell divider is a boundary in a given direction.
     * @param direction
     * The direction in which you want to check whether the divider is a boundary or not.
     * @return
     * Returns true if the divider is a boundary and false otherwise.
     */
    public Boolean isWall(Cell.Direction direction) {

        Divider divider = getDivider(direction);

        if(divider.getDividerType() == Divider.DividerType.WALL) { return true;
        } else { return false; }
    }

    /**
     * This is used to remove a the dividers between the cell and a neighbouring cell in a given direction.
     * This should only be used if the cell has a neighbouring cell in the desired direction.
     * @param direction
     * The direction in which you want to remove the dividers.
     */
    public void removeDividers(Direction direction) {
        removeDividersBetween(neighbourArray[direction.ordinal()]);
    }

    /**
     * This is used to remove dividers between a cell and the target cell. This should only be used if the cell
     * and the target cell are neighbouring cells.
     * @param targetcell
     * The neighbouring cell which we want to remove the dividers between.
     */
    public void removeDividersBetween(Cell targetcell) {
        /* if the divider to be removed is above the current cell, remove its bottom divider */
        if (targetcell == neighbourArray[Direction.UP.ordinal()]) {
            this.dividersActive.remove(Direction.UP);
            this.cellDividers[Direction.UP.ordinal()].inactivate();
            targetcell.dividersActive.remove(Direction.DOWN);
            targetcell.cellDividers[Direction.DOWN.ordinal()].inactivate();
        }
        /* if the divider to be removed is below the current cell, remove its top divider */
        else if (targetcell == neighbourArray[Direction.DOWN.ordinal()]) {
            this.dividersActive.remove(Direction.DOWN);
            this.cellDividers[Direction.DOWN.ordinal()].inactivate();
            targetcell.dividersActive.remove(Direction.UP);
            targetcell.cellDividers[Direction.UP.ordinal()].inactivate();
        }
        /* if the divider to be removed is to the right of the current cell, remove its left divider */
        else if (targetcell == neighbourArray[Direction.RIGHT.ordinal()]) {
            this.dividersActive.remove(Direction.RIGHT);
            this.cellDividers[Direction.RIGHT.ordinal()].inactivate();
            targetcell.dividersActive.remove(Direction.LEFT);
            targetcell.cellDividers[Direction.LEFT.ordinal()].inactivate();
        }
        /* if the divider to be removed is to the left of the current cell, remove its right divider */
        else {
            this.dividersActive.remove(Direction.LEFT);
            this.cellDividers[Direction.LEFT.ordinal()].inactivate();
            targetcell.dividersActive.remove(Direction.RIGHT);
            targetcell.cellDividers[Direction.RIGHT.ordinal()].inactivate();
        }
    }

    /**
     * This is used to create a blue door between two cells in a certain direction.
     * @param direction
     * The direction in which you want to create the blue door.
     */
    public void makeBlueDoor(Direction direction) {
        makeBlueDoorBetween(neighbourArray[direction.ordinal()]);
    }

    /**
     * This is used to create a green door between two cells in a certain direction.
     * @param direction
     * The direction in which you want to create the green door.
     */
    public void makeGreenDoor(Direction direction) {
        makeGreenDoorBetween(neighbourArray[direction.ordinal()]);
    }

    /**
     * This is used to create a yellow door between two cells in a certain direction.
     * @param direction
     * The direction in which you want to create the yellow door.
     */
    public void makeYellowDoor(Direction direction) {
        makeYellowDoorBetween(neighbourArray[direction.ordinal()]);
    }

    /**
     * This is used to determine whether a cell has an unconnected neighbour or not.
     * @return
     * Returns true if the cell has an unconnected neighbour and false otherwise.
     **/
    public Boolean hasUnconnectedNeighbour() {
        /* for each direction, check whether an unconnected neighbour is present */
        for (Direction direction : Direction.values()) {

            if ((hasNeighbour(direction)) && (!getNeighbour(direction).isConnected())) { return true; }

        }

        return false;

    }

    /**
     * This is used to get a string representation of the cell.
     * @return
     * Returns a string which represents the cell.
     */
    @Override
    public String toString() {
        return "cell(" + x/77 + ", " + y/77 + ")";
    }

    /**
     * This is used to make a blue door between two cells. This should only be used between two neighbouring cells.
     * @param targetCell
     * The neigbouring cell which you want to connect to the current cell via a blue door.
     */
    private void makeBlueDoorBetween(Cell targetCell) {
        /* if the divider to be turned into a blue door is above the current cell, turn the bottom divider of the cell
         * above it into a blue door*/
        if(targetCell==neighbourArray[Direction.UP.ordinal()]) {
            this.cellDividers[Direction.UP.ordinal()].activate();
            this.cellDividers[Direction.UP.ordinal()].setBlue(true);
            targetCell.cellDividers[Direction.DOWN.ordinal()].activate();
            targetCell.cellDividers[Direction.DOWN.ordinal()].setBlue(true);
            /* if the divider to be turned into a blue door is below the current cell, turn the top divider of the cell
             * below it into a blue door*/
        } else if(targetCell==neighbourArray[Direction.DOWN.ordinal()]) {
            this.cellDividers[Direction.DOWN.ordinal()].activate();
            this.cellDividers[Direction.DOWN.ordinal()].setBlue(true);
            targetCell.cellDividers[Direction.UP.ordinal()].activate();
            targetCell.cellDividers[Direction.UP.ordinal()].setBlue(true);
            /* if the divider to be turned into a blue door is to the right of the current cell, turn the left divider
             * of the cell to the right of it into a blue door*/
        } else if(targetCell==neighbourArray[Direction.RIGHT.ordinal()]) {
            this.cellDividers[Direction.RIGHT.ordinal()].activate();
            this.cellDividers[Direction.RIGHT.ordinal()].setBlue(true);
            targetCell.cellDividers[Direction.LEFT.ordinal()].activate();
            targetCell.cellDividers[Direction.LEFT.ordinal()].setBlue(true);
            /* if the divider to be turned into a blue door is to the left of the current cell, turn the right divider
             * of the cell to the left of it into a blue door*/
        } else {
            this.cellDividers[Direction.LEFT.ordinal()].activate();
            this.cellDividers[Direction.LEFT.ordinal()].setBlue(true);
            targetCell.cellDividers[Direction.RIGHT.ordinal()].activate();
            targetCell.cellDividers[Direction.RIGHT.ordinal()].setBlue(true);
        }
    }

    /**
     * This is used to make a green door between two cells. This should only be used between two neighbouring cells.
     * @param targetCell
     * The neigbouring cell which you want to connect to the current cell via a green door.
     */
    private void makeGreenDoorBetween(Cell targetCell) {
        /* if the divider to be turned into a green door is above the current cell, turn the bottom divider of the cell
         * above it into a green door*/
        if(targetCell==neighbourArray[Direction.UP.ordinal()]) {
            this.cellDividers[Direction.UP.ordinal()].activate();
            this.cellDividers[Direction.UP.ordinal()].setGreen(true);
            targetCell.cellDividers[Direction.DOWN.ordinal()].activate();
            targetCell.cellDividers[Direction.DOWN.ordinal()].setGreen(true);
            /* if the divider to be turned into a green door is below the current cell, turn the top divider of the cell
             * below it into a green door*/
        } else if(targetCell==neighbourArray[Direction.DOWN.ordinal()]) {
            this.cellDividers[Direction.DOWN.ordinal()].activate();
            this.cellDividers[Direction.DOWN.ordinal()].setGreen(true);
            targetCell.cellDividers[Direction.UP.ordinal()].activate();
            targetCell.cellDividers[Direction.UP.ordinal()].setGreen(true);
            /* if the divider to be turned into a green door is to the right of the current cell, turn the left divider
             * of the cell to the right of it into a green door*/
        } else if(targetCell==neighbourArray[Direction.RIGHT.ordinal()]) {
            this.cellDividers[Direction.RIGHT.ordinal()].activate();
            this.cellDividers[Direction.RIGHT.ordinal()].setGreen(true);
            targetCell.cellDividers[Direction.LEFT.ordinal()].activate();
            targetCell.cellDividers[Direction.LEFT.ordinal()].setGreen(true);
            /* if the divider to be turned into a green door is to the left of the current cell, turn the right divider
             * of the cell to the left of it into a green door*/
        } else {
            this.cellDividers[Direction.LEFT.ordinal()].activate();
            this.cellDividers[Direction.LEFT.ordinal()].setGreen(true);
            targetCell.cellDividers[Direction.RIGHT.ordinal()].activate();
            targetCell.cellDividers[Direction.RIGHT.ordinal()].setGreen(true);
        }
    }

    /**
     * This is used to make a yellow door between two cells. This should only be used between two neighbouring cells.
     * @param targetCell
     * The neigbouring cell which you want to connect to the current cell via a yellow door.
     **/
    private void makeYellowDoorBetween(Cell targetCell) {
        /* if the divider to be turned into a yellow door is above the current cell, turn the bottom divider of the cell
         * above it into a green door*/
        if(targetCell==neighbourArray[Direction.UP.ordinal()]) {
            this.cellDividers[Direction.UP.ordinal()].activate();
            this.cellDividers[Direction.UP.ordinal()].setYellow(true);
            targetCell.cellDividers[Direction.DOWN.ordinal()].activate();
            targetCell.cellDividers[Direction.DOWN.ordinal()].setYellow(true);
            /* if the divider to be turned into a yellow door is below the current cell, turn the top divider of the cell
             * below it into a yellow door*/
        } else if(targetCell==neighbourArray[Direction.DOWN.ordinal()]) {
            this.cellDividers[Direction.DOWN.ordinal()].activate();
            this.cellDividers[Direction.DOWN.ordinal()].setYellow(true);
            targetCell.cellDividers[Direction.UP.ordinal()].activate();
            targetCell.cellDividers[Direction.UP.ordinal()].setYellow(true);
            /* if the divider to be turned into a yellow door is to the right of the current cell, turn the left divider
             * of the cell to the right of it into a yellow door*/
        } else if(targetCell==neighbourArray[Direction.RIGHT.ordinal()]) {
            this.cellDividers[Direction.RIGHT.ordinal()].activate();
            this.cellDividers[Direction.RIGHT.ordinal()].setYellow(true);
            targetCell.cellDividers[Direction.LEFT.ordinal()].activate();
            targetCell.cellDividers[Direction.LEFT.ordinal()].setYellow(true);
            /* if the divider to be turned into a yellow door is to the left of the current cell, turn the right divider
             * of the cell to the left of it into a yellow door*/
        } else {
            this.cellDividers[Direction.LEFT.ordinal()].activate();
            this.cellDividers[Direction.LEFT.ordinal()].setYellow(true);
            targetCell.cellDividers[Direction.RIGHT.ordinal()].activate();
            targetCell.cellDividers[Direction.RIGHT.ordinal()].setYellow(true);
        }
    }
}
