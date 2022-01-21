package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.map.Cell;

/**
 * This class provides helper methods for setting and interpreting the 'tag data' value attached to a player's collider.
 * The {@link PlayerCollider} sets this value to reflect the state of the player (e.g. which powerups the player has
 * active), and colliders which collide with the player can read the value to determine the state of that player.
 * The data value is a bit field stored as an integer, with bits to flag whether the player:
 *  - is in the 'stunned' state after being captured by an enemy
 *  - is using a powerup (one bit for each type of powerup)
 *  - has collected any keys (one bit for each type of key)
 */
public class PlayerColliderData {

    /* value which reflects the state of the player at the start of the game, ie. not captured & no powerups or keys */
    public static final int INITIAL_VALUE = 0;

    private static final int CAPTURED_OFFSET; /* the position of the 'captured' bit */
    private static final int KEY_OFFSET;      /* the position of the first 'key' bit */
    private static final int POWER_UP_OFFSET; /* the position of the first 'powerup' bit */

    static {
        /* the least significant bit stores whether the player is captured */
        CAPTURED_OFFSET = 0;

        /* the next N bits store which keys are collected, where N is the number of types of key */
        KEY_OFFSET = CAPTURED_OFFSET + 1;

        /* the next N bits store which power-ups are active, where N is the number of types of power-up */
        POWER_UP_OFFSET = KEY_OFFSET + Cell.KeyType.values().length;
    }

    /**
     * Sets the 'captured' flag of a player's collider data.
     * @param tagData the current value of the player's collider data
     * @param captured whether the player is now in the 'captured' state
     * @return the new value of the player's collider data
     */
    public static int setCaptured(int tagData, boolean captured) {
        if (captured) {
            return tagData | (1 << CAPTURED_OFFSET); // set bit to 1
        } else {
            return tagData & ~(1 << CAPTURED_OFFSET); // set bit to 0
        }
    }

    /**
     * Gets the 'captured' flag of a player's collider data.
     * @param tagData the player's collider data
     * @return true iff that player is in the 'captured' state, according to the value provided
     */
    public static boolean isCaptured(int tagData) {
        return (tagData & (1 << CAPTURED_OFFSET)) != 0; // return (bit == 1)
    }

    /**
     * Sets the 'key collected' flag of a player's collider data to indicate that the player has collected that key.
     * @param tagData the current value of the player's collider data
     * @param keyType which type of key the player has just collected
     * @return the new value of the player's collider data
     */
    public static int addKey(int tagData, Cell.KeyType keyType) {
        return tagData | (1 << (KEY_OFFSET + keyType.ordinal())); // set bit to 1
    }

    /**
     * Gets the 'key collected' flag of a player's collider data to determine whether the player has collected that key.
     * @param tagData the player's collider data
     * @param keyType the type of key to test for
     * @return true iff the player has collected that type of key, according to the player's collider data.
     */
    public static boolean hasKey(int tagData, Cell.KeyType keyType) {
        return (tagData & (1 << (KEY_OFFSET + keyType.ordinal()))) != 0; // return (bit == 1)
    }

    /**
     * Sets the 'powerup active' flag of a player's collider data to indicate that the player is using a particular
     * powerup.
     * @param tagData the current value of the player's collider data
     * @param powerUpType which type of powerup the player has just activated
     * @return the new value of the player's collider data
     */
    public static int addPowerUp(int tagData, Cell.PowerUpType powerUpType) {
        return tagData | (1 << (POWER_UP_OFFSET + powerUpType.ordinal())); // set bit to 1
    }

    /**
     * Sets the 'powerup active' flag of a player's collider data to indicate that the player is no longer using a
     * particular powerup.
     * @param tagData the current value of the player's collider data
     * @param powerUpType which type of powerup the player is no longer using
     * @return the new value of the player's collider data
     */
    public static int removePowerUp(int tagData, Cell.PowerUpType powerUpType) {
        return tagData & ~(1 << (POWER_UP_OFFSET + powerUpType.ordinal())); // set bit to 0
    }

    /**
     * Gets the 'powerup active' flag of a player's collider data to determine whether the player is using a particular
     * powerups.
     * @param tagData the value of the player's collider data
     * @param powerUpType which type of powerup to test for
     * @return true iff the player is currently using that powerup, according to the player's collider data
     */
    public static boolean hasPowerUp(int tagData, Cell.PowerUpType powerUpType) {
        return (tagData & (1 << (POWER_UP_OFFSET + powerUpType.ordinal()))) != 0; // return (bit == 1)
    }
}