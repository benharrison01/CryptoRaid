package uob.cs.teamproject.sabrewulf.map;

import java.io.Serializable;

/**
 * This class represents a border for a cell.
 * It can be either empty (i.e. the divider is not visible), a wall or a door.
 */

public class Divider implements Serializable {
    /**
     * Values used to store the type of the divider.
     * Valid values are EMPTY, WALL, BLUEDOOR, GREENDOOR and YELLOWDOOR
     */
    public enum DividerType { EMPTY, WALL, BLUEDOOR, GREENDOOR, YELLOWDOOR  };
    public DividerType dividerType;
    private boolean isActive;

    /* if the divider is initialised as active, it is set to be a wall by default, otherwise an inactive divider is
    * initialised as empty */

    /**
     * The Divider class is used to create walls for cells. Dividers are either active (i.e. they are a wall) or inactive.
     * @param active
     * This describes whether the divider is a wall which you cannot not
     */
    public Divider(boolean active) {
        this.isActive = active;
        if (this.isActive) {
            dividerType = DividerType.WALL;
        } else {
            dividerType = DividerType.EMPTY;
        }
    }

    /**
     * This is used to find out whether a given divider is active or not
     * @return
     * Returns 'true' if the divider is active and 'false' if the divider is inactive
     */
    public boolean isActive() { return this.isActive; }

    /**
     * This is used to set a divider to be a blue door. This should only be used on a divider which is active.
     * The divider type is set to BLUEDOOR when a blue door is created.
     * @param blue
     * When set to true the divider becomes a blue door. When set to false the divider becomes a normal active divider.
     */
    public void setBlue(boolean blue) {
        if(blue) {
            dividerType = dividerType.BLUEDOOR;
        } else {
            dividerType = DividerType.WALL;
        }
    }

    /**
     * This is used to set a divider to be a green door. This should only be used on a divider which is active.
     * The divider type is set to a GREENDOOR when a green door is created.
     * @param green
     * When set to true the divider becomes a green door. When set to false the divider becomes a normal active divider.
     */
    public void setGreen(boolean green) {
        if(green) {
            dividerType = dividerType.GREENDOOR;
        }else {
            dividerType = DividerType.WALL;
        }
    }

    /**
     * This is used to set a divider to be a yellow door. This should only be used on a divider which is acitve.
     * The divider type is set to YELLOWDOOR when a yellow door is created.
     * @param yellow
     * When set to true the divider becomes a yellow door. When set to false the divider becomes a normal active divider.
     */
    public void setYellow(boolean yellow) {
        if(yellow) {
            dividerType = dividerType.YELLOWDOOR;
        }else {
            dividerType = DividerType.WALL;
        }
    }

    /**
     * This is used to find out whether a divider is a blue door or not.
     * @return
     * Returns true if the divider type is BLUEDOOR and false otherwise.
     */
    public boolean isBlue() { return (dividerType == DividerType.BLUEDOOR); }

    /**
     * This is used to find out whether a divider is a green door or not.
     * @return
     * Returns true if the divider type is GREENDOOR and false otherwise.
     */
    public boolean isGreen() { return (dividerType == DividerType.GREENDOOR); }

    /**
     * This is used to find out whether a divider is a yellow door or not.
     * @return
     * Returns true if the divider type is YELLOWDOOR and false otherwise.
     */
    public boolean isYellow() { return (dividerType == DividerType.YELLOWDOOR); }

    /**
     * This is used to find out whether a divider is a coloured door or not (i.e. whether it is a blue/green/yellow door).
     * @return
     * Returns true if the divider type is BLUEDOOR, GREENDOOR or YELLOWDOOR and false otherwise.
     */
    public boolean isColoured() { return (!((dividerType == DividerType.WALL) || (dividerType == DividerType.EMPTY))); }

    /**
     * This is used to get the divider type of a divider
     * @return
     * Returns the {@link DividerType} of the divider
     */
    public DividerType getDividerType() { return this.dividerType; }

    /**
     * This function causes an inactive divider to become an active boarder.
     * Doing this causes a divider to be set to type WALL by default.
     */
    public void activate() {
        if(!this.isActive) {
            this.isActive = true;
            dividerType = DividerType.WALL;
        }
    }

    /**
     * This function causes an active divider to become an inactive boarder.
     * Doing this causes a divider to be set to type EMPTY by default.
     */
    public void inactivate() {
        if(this.isActive) {
            this.isActive = false;
            dividerType = DividerType.EMPTY;
        }
    }
}
