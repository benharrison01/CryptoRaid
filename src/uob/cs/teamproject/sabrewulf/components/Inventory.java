package uob.cs.teamproject.sabrewulf.components;

import javafx.beans.property.*;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.map.Cell;

/** Multipurpose class to be used as both an inventory and a power up logic manager which communicates with
 *  the player behaviour class
 */
public class Inventory extends GameComponent {

    private final Cell.KeyType[] keys = new Cell.KeyType[3];
    private int numberOfCoins = 0;
    private int invisibilityTimer = 0;
    private int speedBoostTimer = 0;
    private int inactiveTimer = 0;
    private int hitCount = 0;
    private final SimpleIntegerProperty score = new SimpleIntegerProperty();
    private final SimpleIntegerProperty numberOfLives = new SimpleIntegerProperty();
    private final SimpleBooleanProperty hasInvisibility = new SimpleBooleanProperty();
    private final SimpleBooleanProperty hasSpeedBoost = new SimpleBooleanProperty();
    private final SimpleBooleanProperty isInactive = new SimpleBooleanProperty();
    private final SimpleBooleanProperty blueKey = new SimpleBooleanProperty();
    private final SimpleBooleanProperty greenKey = new SimpleBooleanProperty();
    private final SimpleBooleanProperty yellowKey = new SimpleBooleanProperty();
    private final SimpleIntegerProperty numInvisibilitiesWaiting = new SimpleIntegerProperty();
    private final SimpleIntegerProperty numSpeedBoostsWaiting = new SimpleIntegerProperty();
    private final SimpleIntegerProperty enemiesChasingPlayer = new SimpleIntegerProperty();

    /** Constructor creates a new instance of the inventory and sets the initial values of each score metric
     */
    public Inventory() {
        score.set(0);
        numberOfLives.set(3);
        numInvisibilitiesWaiting.set(0);
        numSpeedBoostsWaiting.set(0);
        hasInvisibility.set(false);
        hasSpeedBoost.set(false);
        isInactive.set(false);
        blueKey.set(false);
        greenKey.set(false);
        yellowKey.set(false);
    }

    /** @return Returns a boolean to say whether the player has collected the blue key
     */
    public boolean getBlueKey() {
        return blueKey.get();
    }

    /** @return Returns a boolean to say whether the player has collected the green key
     */
    public boolean getGreenKey() {
        return greenKey.get();
    }

    /** @return Returns a boolean to say whether the player has collected the yellow key
     */
    public boolean getYellowKey() {
        return yellowKey.get();
    }

    /** @return Returns the blue key property
     */
    public BooleanProperty blueProperty() { return blueKey;}

    /** @return Returns the green key property
     */
    public BooleanProperty greenProperty() { return greenKey;}

    /** @return Returns the yellow key property
     */
    public BooleanProperty yellowProperty() { return yellowKey;}

    /** Increase the number of lives the player has by (1)
     */
    public void addLife() {
        numberOfLives.set(numberOfLives.get() + 1);
    }

    /** Decrease the number of lives the player has by (1)
     */
    public void removeLife() {
        numberOfLives.set(numberOfLives.get() - 1);
    }

    /** Increase the hit count for the player by (1) and update their score
     */
    public void incrementHitCount() {
        hitCount++;
        updateScore();
    }

    /** Method used for testing
     * @return Return the hit count
     */
    public int getHitCount() {
        return hitCount;
    }

    /** @return Return the number of lives which the player has
     */
    public int getNumberOfLives() {
        return numberOfLives.get();
    }

    /** @return Return a boolean to say whether the player is currently inactive
     */
    public boolean isInactive() {
        return isInactive.get();
    }

    /** Set the 'is inactive' value in the inventory
     * @param isInactive - Boolean whether player is inactive
     */
    public void setIsInactive(boolean isInactive) {
        this.isInactive.set(isInactive);
    }

    /** @return Returns the number of lives property
     */
    public IntegerProperty livesProperty() { return numberOfLives;}

    /** @return Returns the integer value of the number of lives property
     */
    public int getNumLives() {
        return livesProperty().get();
    }

    /** @return Returns whether the player is currently invisible
     */
    public boolean hasInvisibility() {
        return hasInvisibility.get();
    }

    /** Set the 'invisibility' value in the inventory
     * @param b - Boolean whether player should be invisible
     */
    public void setInvisibility(boolean b) {
        hasInvisibility.set(b);
    }

    /** @return Returns the invisibility property
     */
    public BooleanProperty invisibilityProperty() { return hasInvisibility;}

    /** @return Returns the boolean value of the invisibility property
     */
    public boolean getInvisible() {
        return invisibilityProperty().get();
    }

    /** @return Returns whether the player currently has a speed boost
     */
    public boolean hasSpeedBoost() {
        return hasSpeedBoost.get();
    }

    /** Set the 'speed boost' value in the inventory
     * @param b - Boolean whether player should have speed boost
     */
    public void setBoost(boolean b) {
        hasSpeedBoost.set(b);
    }

    /** @return Returns the speed boost property
     */
    public BooleanProperty boostProperty() { return hasSpeedBoost;}

    /** @return Returns the boolean value of the boost property
     */
    public boolean getBoostProperty() {
        return boostProperty().get();
    }

    /** @return Returns the invisibility timer (amount of time player is invisible for)
     */
    public int getInvisibilityTimer() {
        return invisibilityTimer;
    }

    /** Set the invisibility timer
     * @param invisibilityTimer - Time to be added to the invisibility timer
     */
    public void setInvisibilityTimer(int invisibilityTimer) {
        this.invisibilityTimer = invisibilityTimer;
    }

    /** @return Returns the speed boost timer (amount of time player has speed boost for)
     */
    public int getSpeedBoostTimer() {
        return speedBoostTimer;
    }

    /** Set the speed boost timer
     * @param speedBoostTimer - Time to be added to the speed boost timer
     */
    public void setSpeedBoostTimer(int speedBoostTimer) {
        this.speedBoostTimer = speedBoostTimer;
    }

    /** @return Returns the inactive timer (amount of time the player is inactive for)
     */
    public int getInactiveTimer() {
        return inactiveTimer;
    }

    /** Set the inactive timer
     * @param inactiveTimer - Time to be added to the inactive timer
     */
    public void setInactiveTimer(int inactiveTimer) {
        this.inactiveTimer = inactiveTimer;
    }

    /** @return Retyrns the player's current score
     */
    public int getScore() {
        return score.get();
    }

    /** @return Returns the score property
     */
    public IntegerProperty scoreProperty() { return score;}

    /**
     *
     * @return the number of enemies chasing the player.
     */
    public int getEnemiesChasingPlayer() {
        return enemiesChasingPlayer.get();
    }

    /**
     *
     * @return the number of enemies chasing the player as a SimpleIntegerProperty for the UI.
     */
    public SimpleIntegerProperty enemiesChasingPlayerProperty() {
        return enemiesChasingPlayer;
    }

    /**
     * Updates the number of enemies chasing the player.
     * @param enemiesChasingPlayer: number of enemies chasing player.
     */
    public void setEnemiesChasingPlayer(int enemiesChasingPlayer) {
        this.enemiesChasingPlayer.set(enemiesChasingPlayer);
    }

    /** @return Returns the number of invisibilities available property
     */
    public IntegerProperty numInvisibilitiesWaitingProperty() { return numInvisibilitiesWaiting;}

    /** @return Returns the number of speed boosts available property
     */
    public IntegerProperty numSpeedBoostsWaitingProperty() { return numSpeedBoostsWaiting;}

    /** @return Returns the number of invisibilities available in the inventory
     */
    public int getNumInvisibilitiesWaiting() {
        return numInvisibilitiesWaiting.get();
    }

    /** Increases the number of invisibilities available in the inventory by (1)
     */
    public void incrementNumInvisibilitiesWaiting() {
        numInvisibilitiesWaiting.set(numInvisibilitiesWaiting.get() + 1);
    }

    /** Decreases the number of invisibilities available in the inventory by (1)
     */
    public void decrementNumInvisibilitiesWaiting() {
        numInvisibilitiesWaiting.set(numInvisibilitiesWaiting.get() - 1);
   }

    /** @return Returns the number of speed boosts available in the inventory
     */
    public int getNumSpeedBoostsWaiting() {
        return numSpeedBoostsWaiting.get();
    }

    /** Increases the number of speed boosts available in the inventory by (1)
     */
    public void incrementNumSpeedBoostsWaiting() {
        numSpeedBoostsWaiting.set(numSpeedBoostsWaiting.get() + 1);
    }

    /** Decreases the number of speed boosts available in the inventory by (1)
     */
    public void decrementNumSpeedBoostsWaiting() {
        numSpeedBoostsWaiting.set(numSpeedBoostsWaiting.get() - 1);
    }

    /** @return Returns the number of coins
     */
    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    /** Add a coin to the player's inventory
     */
    public void addCoin() {
        this.numberOfCoins ++;
        updateScore();
    }

    /** Remove coins from the player's inventory
     * @param n - Number of coins to be removed
     */
    public void removeNCoins(int n) {
        if (numberOfCoins - n < 0) {
            numberOfCoins = 0;
        }
        else {
            numberOfCoins = numberOfCoins - n;
        }
        updateScore();
    }

    /** Add a key to the player's inventory
     * @param key - Key to be added to the inventory
     */
    public void addKey(Cell.KeyType key) {
        int i =0;
        while(i<3) {
            if(keys[i]==null) {
                keys[i] = key;
                if (key == Cell.KeyType.BLUEKEY) {
                    blueKey.set(true);
                }
                if (key == Cell.KeyType.GREENKEY) {
                    greenKey.set(true);
                }if (key == Cell.KeyType.YELLOWKEY) {
                    yellowKey.set(true);
                }
                break;
            }
            i++;
        }
    }

    /** @param key - Key to be tested
     * @return Returns whether the inventory contains the specified key
     */
    public boolean hasKey(Cell.KeyType key) {
        int i = 0;
        while(i<3) {
            if(keys[i]==key) {
                return true;
            }
            i++;
        }
        return false;
    }

    /** Update the player's score based on the number of coins collected and their hit count
     */
    public void updateScore() {
        score.set((numberOfCoins * 10) - (hitCount * 50));
        if (score.get() < 0) {
            score.set(0);
            numberOfCoins = 0;
            hitCount = 0;
        }
    }

}
