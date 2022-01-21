package test.cs.teamproject.sabrewulf.components;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.components.Inventory;
import uob.cs.teamproject.sabrewulf.map.Cell;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    @Test
    public void testInventory() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getScore());
        assertEquals(3, inventory.getNumberOfLives());
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());
        assertFalse(inventory.hasInvisibility());
        assertFalse(inventory.hasSpeedBoost());
        assertFalse(inventory.isInactive());
        assertFalse(inventory.getBlueKey());
        assertFalse(inventory.getGreenKey());
        assertFalse(inventory.getYellowKey());
    }

    @Test
    public void testEnemiesChasingPlayer() {
        Inventory inventory = new Inventory();
        inventory.setEnemiesChasingPlayer(0);
        assertEquals(0, inventory.getEnemiesChasingPlayer());
        inventory.setEnemiesChasingPlayer(3);
        assertEquals(3, inventory.getEnemiesChasingPlayer());
        inventory.setEnemiesChasingPlayer(10);
        assertEquals(10, inventory.getEnemiesChasingPlayer());
    }

    @Test
    public void testNumInvisibilitiesWaiting() {
        Inventory inventory = new Inventory();
        inventory.incrementNumInvisibilitiesWaiting();
        assertEquals(1, inventory.getNumInvisibilitiesWaiting());
        assertEquals(1, inventory.numInvisibilitiesWaitingProperty().get());
        inventory.incrementNumInvisibilitiesWaiting();
        inventory.incrementNumInvisibilitiesWaiting();
        assertEquals(3, inventory.getNumInvisibilitiesWaiting());
        assertEquals(3, inventory.numInvisibilitiesWaitingProperty().get());
        inventory.decrementNumInvisibilitiesWaiting();
        assertEquals(2, inventory.getNumInvisibilitiesWaiting());
        assertEquals(2, inventory.numInvisibilitiesWaitingProperty().get());
        inventory.decrementNumInvisibilitiesWaiting();
        inventory.decrementNumInvisibilitiesWaiting();
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());
        assertEquals(0, inventory.numInvisibilitiesWaitingProperty().get());
    }

    @Test
    public void testNumSpeedBoostsWaiting() {
        Inventory inventory = new Inventory();
        inventory.incrementNumSpeedBoostsWaiting();
        assertEquals(1, inventory.getNumSpeedBoostsWaiting());
        assertEquals(1, inventory.numSpeedBoostsWaitingProperty().get());
        inventory.incrementNumSpeedBoostsWaiting();
        inventory.incrementNumSpeedBoostsWaiting();
        assertEquals(3, inventory.getNumSpeedBoostsWaiting());
        assertEquals(3, inventory.numSpeedBoostsWaitingProperty().get());
        inventory.decrementNumSpeedBoostsWaiting();
        assertEquals(2, inventory.getNumSpeedBoostsWaiting());
        assertEquals(2, inventory.numSpeedBoostsWaitingProperty().get());
        inventory.decrementNumSpeedBoostsWaiting();
        inventory.decrementNumSpeedBoostsWaiting();
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());
        assertEquals(0, inventory.numSpeedBoostsWaitingProperty().get());
    }

    @Test
    public void testCoinScoring() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getScore());
        for (int i = 0; i < 10; i++) {
            inventory.addCoin();
        }
        inventory.updateScore();
        assertEquals(100, inventory.getScore());
        assertEquals(100, inventory.scoreProperty().get());
        inventory.removeNCoins(5);
        assertEquals(50, inventory.getScore());
        assertEquals(50, inventory.scoreProperty().get());
        for (int i = 0; i < 100; i++) {
            inventory.addCoin();
        }
        assertEquals(1050, inventory.getScore());
        assertEquals(1050, inventory.scoreProperty().get());
        inventory.removeNCoins(105);
        assertEquals(0, inventory.getScore());
        assertEquals(0, inventory.scoreProperty().get());
        inventory.removeNCoins(5);
        assertEquals(0, inventory.getScore());
        assertEquals(0, inventory.scoreProperty().get());
    }

    @Test
    public void testHitCountScoring() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getScore());
        assertEquals(0, inventory.getHitCount());
        inventory.incrementHitCount();
        assertEquals(0, inventory.getHitCount());
        assertEquals(0, inventory.getScore());
        for (int i = 0; i < 10; i++) {
            inventory.addCoin();
        }
        inventory.incrementHitCount();
        assertEquals(50, inventory.getScore());
    }

    @Test
    public void testInactiveTimer() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getInactiveTimer());
        inventory.setInactiveTimer(10);
        assertEquals(10, inventory.getInactiveTimer());
        inventory.setInactiveTimer(0);
        assertEquals(0, inventory.getInactiveTimer());
    }

    @Test
    public void testSpeedBoostTimer() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getSpeedBoostTimer());
        inventory.setSpeedBoostTimer(10);
        assertEquals(10, inventory.getSpeedBoostTimer());
        inventory.setSpeedBoostTimer(0);
        assertEquals(0, inventory.getSpeedBoostTimer());
    }

    @Test
    public void testInvisibilityTimer() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getInvisibilityTimer());
        inventory.setInvisibilityTimer(10);
        assertEquals(10, inventory.getInvisibilityTimer());
        inventory.setInvisibilityTimer(0);
        assertEquals(0, inventory.getInvisibilityTimer());
    }

    @Test
    public void testSpeedBoostProperty() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.getBoostProperty());
        inventory.setBoost(true);
        assertTrue(inventory.getBoostProperty());
        inventory.setBoost(false);
        assertFalse(inventory.getBoostProperty());
    }

    @Test
    public void testInvisibilityProperty() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.getInvisible());
        inventory.setInvisibility(true);
        assertTrue(inventory.getInvisible());
        inventory.setInvisibility(false);
        assertFalse(inventory.getInvisible());
    }

    @Test
    public void testLivesProperty() {
        Inventory inventory = new Inventory();
        assertEquals(3, inventory.getNumberOfLives());
        assertEquals(3, inventory.getNumLives());
        inventory.addLife();
        assertEquals(4, inventory.getNumberOfLives());
        assertEquals(4, inventory.getNumLives());
        for (int i = 0; i < 4; i++) {
            inventory.removeLife();
        }
        assertEquals(0, inventory.getNumberOfLives());
        assertEquals(0, inventory.getNumLives());
    }

    @Test
    public void testIsInactive() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.isInactive());
        inventory.setIsInactive(true);
        assertTrue(inventory.isInactive());
        inventory.setIsInactive(false);
        assertFalse(inventory.isInactive());
    }

    @Test
    public void testKeys() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.blueProperty().get());
        assertFalse(inventory.greenProperty().get());
        assertFalse(inventory.yellowProperty().get());
        inventory.addKey(Cell.KeyType.BLUEKEY);
        assertTrue(inventory.getBlueKey());
        assertTrue(inventory.blueProperty().get());
        inventory.addKey(Cell.KeyType.GREENKEY);
        assertTrue(inventory.getGreenKey());
        assertTrue(inventory.greenProperty().get());
        inventory.addKey(Cell.KeyType.YELLOWKEY);
        assertTrue(inventory.getYellowKey());
        assertTrue(inventory.yellowProperty().get());
    }

    @Test
    public void testEnemiesChasingProperty() {
        Inventory inventory = new Inventory();
        assertEquals(0, inventory.getEnemiesChasingPlayer());
        assertEquals(0, inventory.enemiesChasingPlayerProperty().get());
        inventory.setEnemiesChasingPlayer(5);
        assertEquals(5, inventory.getEnemiesChasingPlayer());
        assertEquals(5, inventory.enemiesChasingPlayerProperty().get());
        inventory.setEnemiesChasingPlayer(0);
        assertEquals(0, inventory.getEnemiesChasingPlayer());
        assertEquals(0, inventory.enemiesChasingPlayerProperty().get());
    }


}
