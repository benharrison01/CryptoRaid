package test.cs.teamproject.sabrewulf.input;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.input.Input;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Please note that all main functionality for this class, including the
 * constructor cannot be tested using JUnit tests due to the fact they use
 * EventHandlers. Due to this, their uses are tested implicitly in
 * PlayerBehaviourTest and manual testing for the player movement. Since all
 * manual player movement tests have passed, this implies that the Input class
 * works as intended, since a subset of player movement tests require the user input.
 */

public class InputTest {

    @Test
    public void testOpposite() {
        Input input = new Input();

        //Test valid inputs for wasd system
        assertArrayEquals(new String[]{"D", "RIGHT"}, input.oppositeOf("A"));
        assertArrayEquals(new String[]{"A", "LEFT"}, input.oppositeOf("D"));
        assertArrayEquals(new String[]{"W", "UP"}, input.oppositeOf("S"));
        assertArrayEquals(new String[]{"S", "DOWN"}, input.oppositeOf("W"));

        //Test valid inputs for left, right, up, down system
        assertArrayEquals(new String[]{"D", "RIGHT"}, input.oppositeOf("LEFT"));
        assertArrayEquals(new String[]{"A", "LEFT"}, input.oppositeOf("RIGHT"));
        assertArrayEquals(new String[]{"W", "UP"}, input.oppositeOf("DOWN"));
        assertArrayEquals(new String[]{"S", "DOWN"}, input.oppositeOf("UP"));

        //Test some invalid inputs
        assertNull(input.oppositeOf("not a character"));
        assertNull(input.oppositeOf("Q"));
        assertNull(input.oppositeOf(" "));
        assertNull(input.oppositeOf("J"));
    }

}
