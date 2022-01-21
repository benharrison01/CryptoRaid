package test.cs.teamproject.sabrewulf.components;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.components.PlayerColliderData;
import uob.cs.teamproject.sabrewulf.map.Cell;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/** Tests for {@link PlayerColliderData}. */
public class PlayerColliderDataTest {

    /** Test that setting and getting the 'captured' flag works correctly */
    @Test
    public void capturedTest() {
        int value = PlayerColliderData.INITIAL_VALUE;

        assertFalse(PlayerColliderData.isCaptured(value));

        value = PlayerColliderData.setCaptured(value, true);

        assertTrue(PlayerColliderData.isCaptured(value));

        value = PlayerColliderData.setCaptured(value, false);

        assertFalse(PlayerColliderData.isCaptured(value));
    }

    /** Test that setting each of the 'key collected' flags works correctly */
    @Test
    public void keyTest() {
        int value = PlayerColliderData.INITIAL_VALUE;

        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.BLUEKEY));
        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.GREENKEY));
        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.YELLOWKEY));

        value = PlayerColliderData.addKey(value, Cell.KeyType.BLUEKEY);

        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.BLUEKEY));
        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.GREENKEY));
        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.YELLOWKEY));

        value = PlayerColliderData.addKey(value, Cell.KeyType.GREENKEY);

        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.BLUEKEY));
        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.GREENKEY));
        assertFalse(PlayerColliderData.hasKey(value, Cell.KeyType.YELLOWKEY));

        value = PlayerColliderData.addKey(value, Cell.KeyType.YELLOWKEY);

        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.BLUEKEY));
        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.GREENKEY));
        assertTrue(PlayerColliderData.hasKey(value, Cell.KeyType.YELLOWKEY));
    }

    /**
     * Test that setting and getting each of the 'powerup active' flags works correctly
     * Note that we ignore the 'add life' powerup because it applies an instant effect, rather than lasting for a
     * duration and so cannot be tested for as there is no 'window' when it is active.
     */
    @Test
    public void powerUpTest() {
        int value = PlayerColliderData.INITIAL_VALUE;

        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.SPEEDUP));
        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.INVISIBILITY));

        value = PlayerColliderData.addPowerUp(value, Cell.PowerUpType.SPEEDUP);

        assertTrue(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.SPEEDUP));
        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.INVISIBILITY));

        value = PlayerColliderData.addPowerUp(value, Cell.PowerUpType.INVISIBILITY);

        assertTrue(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.SPEEDUP));
        assertTrue(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.INVISIBILITY));

        value = PlayerColliderData.removePowerUp(value, Cell.PowerUpType.SPEEDUP);

        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.SPEEDUP));
        assertTrue(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.INVISIBILITY));

        value = PlayerColliderData.removePowerUp(value, Cell.PowerUpType.INVISIBILITY);

        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.SPEEDUP));
        assertFalse(PlayerColliderData.hasPowerUp(value, Cell.PowerUpType.INVISIBILITY));
    }
}
