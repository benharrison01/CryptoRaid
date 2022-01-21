package test.cs.teamproject.sabrewulf.components;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.components.*;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.rendering.ResizeableCanvas;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * These tests test all the parts of Player and PlayerBehaviour which are testable via JUnits.
 * Since many Player and PlayerBehaviour methods rely on being in an actual game, the majority of testing
 * for these classes is done via manual tests in the report.
 */
public class PlayerTest {
    Renderer renderer = new Renderer(new ResizeableCanvas(1080,1080));
    CharacterMovement characterMovement = new CharacterMovement(new CharacterAnimator(renderer, null), null, null);
    StatisticsTracker statisticsTracker = new StatisticsTracker("json/achievements_challenges.json");
    Input input = new Input(null,null);
    Player player = new Player(null, characterMovement, input, null, statisticsTracker, null, null, null, null);


    @Test
    public void test_UP_RIGHT(){
        player.analyseInputs("W", new ArrayList<String>(Arrays.asList("D", "W")));
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getFacing());
    }

    @Test
    public void test_UP_LEFT(){
        player.analyseInputs("W", new ArrayList<String>(Arrays.asList("A","W")));
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getFacing());
    }

    @Test
    public void test_UP_UP(){
        player.analyseInputs("W", new ArrayList<String>(Arrays.asList("W")));
        assertEquals(CharacterMovement.Direction.NONE, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getFacing());
    }

    @Test
    public void test_RIGHT_UP(){
        player.analyseInputs("D", new ArrayList<String>(Arrays.asList("W", "D")));
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getFacing());
    }

    @Test
    public void test_RIGHT_DOWN(){
        player.analyseInputs("D", new ArrayList<String>(Arrays.asList("S","D")));
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getFacing());
    }

    @Test
    public void test_RIGHT_RIGHT(){
        player.analyseInputs("D", new ArrayList<String>(Arrays.asList("D")));
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.NONE, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getFacing());
    }

    @Test
    public void test_LEFT_UP(){
        player.analyseInputs("A", new ArrayList<String>(Arrays.asList("W","A")));
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.UP, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getFacing());
    }

    @Test
    public void test_LEFT_LEFT(){
        player.analyseInputs("A", new ArrayList<String>(Arrays.asList("A")));
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.NONE, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getFacing());
    }

    @Test
    public void test_LEFT_DOWN(){
        player.analyseInputs("A", new ArrayList<String>(Arrays.asList("S","A")));
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getFacing());
    }

    @Test
    public void test_DOWN_LEFT(){
        player.analyseInputs("S", new ArrayList<String>(Arrays.asList("A","S")));
        assertEquals(CharacterMovement.Direction.LEFT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getFacing());
    }

    @Test
    public void test_DOWN_RIGHT(){
        player.analyseInputs("S", new ArrayList<String>(Arrays.asList("D","S")));
        assertEquals(CharacterMovement.Direction.RIGHT, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getFacing());
    }

    @Test
    public void test_DOWN_DOWN(){
        player.analyseInputs("S", new ArrayList<String>(Arrays.asList("S")));
        assertEquals(CharacterMovement.Direction.NONE, characterMovement.getDirX());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getDirY());
        assertEquals(CharacterMovement.Direction.DOWN, characterMovement.getFacing());
    }

    @Test
    public void test_TriggerSpeedBoost() {
        //give the test player an inventory
        Inventory inventory = new Inventory();
        player.setInventory(inventory);

        //give the player a speed boost and check it adds to the inventory
        inventory.incrementNumSpeedBoostsWaiting();
        assertEquals(1, inventory.getNumSpeedBoostsWaiting());

        //let the test player use the speed boost and check the amount in the inventory decreases
        player.analyseInputs("PERIOD", new ArrayList<String>(Arrays.asList("PERIOD")));
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());

        //the test player tries to use a speed boost when none are available, check that the inventory does not
        //decrement
        player.analyseInputs("PERIOD", new ArrayList<String>(Arrays.asList("PERIOD")));
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());
    }

    @Test
    public void test_TriggerInvisible() {
        //give the test player an inventory
        Inventory inventory = new Inventory();
        player.setInventory(inventory);

        //give the player an invisibility power-up and check it adds to the inventory
        inventory.incrementNumInvisibilitiesWaiting();
        assertEquals(1, inventory.getNumInvisibilitiesWaiting());

        //let the test player use the invisibility power-up and check the amount in the inventory decreases
        player.analyseInputs("COMMA", new ArrayList<String>(Arrays.asList("COMMA")));
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());

        //the test player tries to use an invisibility power-up when none are available, check that the inventory does
        // not decrement
        player.analyseInputs("COMMA", new ArrayList<String>(Arrays.asList("COMMA")));
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());
    }

    @Test
    public void test_TriggerSpeedBoost2() {
        //give the test player an inventory
        Inventory inventory = new Inventory();
        player.setInventory(inventory);

        //give the player a speed boost and check it adds to the inventory
        inventory.incrementNumSpeedBoostsWaiting();
        assertEquals(1, inventory.getNumSpeedBoostsWaiting());

        //let the test player use the speed boost and check the amount in the inventory decreases
        player.analyseInputs("DIGIT2", new ArrayList<String>(Arrays.asList("DIGIT2")));
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());

        //the test player tries to use a speed boost when none are available, check that the inventory does not
        //decrement
        player.analyseInputs("PERIOD", new ArrayList<String>(Arrays.asList("PERIOD")));
        assertEquals(0, inventory.getNumSpeedBoostsWaiting());
    }

    @Test
    public void test_TriggerInvisible2() {
        //give the test player an inventory
        Inventory inventory = new Inventory();
        player.setInventory(inventory);

        //give the player an invisibility power-up and check it adds to the inventory
        inventory.incrementNumInvisibilitiesWaiting();
        assertEquals(1, inventory.getNumInvisibilitiesWaiting());

        //let the test player use the invisibility power-up and check the amount in the inventory decreases
        player.analyseInputs("DIGIT1", new ArrayList<String>(Arrays.asList("DIGIT1")));
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());

        //the test player tries to use an invisibility power-up when none are available, check that the inventory does
        //not decrement
        player.analyseInputs("DIGIT1", new ArrayList<String>(Arrays.asList("DIGIT1")));
        assertEquals(0, inventory.getNumInvisibilitiesWaiting());
    }
}