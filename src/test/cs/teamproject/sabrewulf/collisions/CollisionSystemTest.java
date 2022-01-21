package test.cs.teamproject.sabrewulf.collisions;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.DistanceTrigger;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.components.PlayerColliderData;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/** Test class for the {@link CollisionSystem}. */
public class CollisionSystemTest {

    /* a DistanceTrigger that counts the number of players it detects */
    private class CountingTrigger extends DistanceTrigger {
        public int triggerCount = 0;

        public CountingTrigger(Transform transform) {
            super(transform, ColliderTag.ENEMY_VIEW, 100);
        }

        @Override
        public void onTrigger(XYPair position) {
            triggerCount++;
        }
    }

    /**
     * Test that the {@link CollisionSystem} detects the correct collisions between the different types of collider.
     */
    @Test
    public void collisionMatrixTest() {
        CollisionSystem collisionSystem = new CollisionSystem();

        /* all the colliders in this test use the same transform so that they are all able to collide */
        Transform tf = new Transform(0, 0, 10, 10);

        /* tag values for the box colliders */
        ColliderTag[] boxTags = {
                ColliderTag.PLAYER, /* player 1 isn't captured and isn't invisible */
                ColliderTag.PLAYER, /* player 2 isn't captured and is    invisible */
                ColliderTag.PLAYER, /* player 3 is    captured and isn't invisible */
                ColliderTag.PLAYER, /* player 4 is    captured and is    invisible */
                ColliderTag.ENEMY,
                ColliderTag.WALL,
                ColliderTag.COIN,
                ColliderTag.KEY,
                ColliderTag.POWERUP
        };
        int numBoxes = boxTags.length;

        int[] boxTagData = new int[numBoxes];
        for (int i = 0; i < numBoxes; i++) boxTagData[i] = 0;
        /* player 1 isn't captured and isn't invisible */
        boxTagData[0] = PlayerColliderData.INITIAL_VALUE;
        /* player 2 isn't captured and is invisible */
        boxTagData[1] = PlayerColliderData.INITIAL_VALUE;
        boxTagData[1] = PlayerColliderData.addPowerUp(boxTagData[1], Cell.PowerUpType.INVISIBILITY);
        /* player 3 is captured and isn't invisible */
        boxTagData[2] = PlayerColliderData.INITIAL_VALUE;
        boxTagData[2] = PlayerColliderData.setCaptured(boxTagData[2], true);
        /* player 4 is    captured and is    invisible */
        boxTagData[3] = PlayerColliderData.INITIAL_VALUE;
        boxTagData[3] = PlayerColliderData.setCaptured(boxTagData[3], true);
        boxTagData[3] = PlayerColliderData.addPowerUp(boxTagData[3], Cell.PowerUpType.INVISIBILITY);

        int numColliders = numBoxes + 1; // all the box colliders plus the DistanceTrigger

        boolean[][] expectedCollisions = {
                /*                PLAYER1  PLAYER2  PLAYER3  PLAYER4  ENEMY   WALL   COIN    KEY POWERUP ENEMY_VIEW */
                /*    PLAYER1 */{   false,   false,  false,   false,  true,  true,   true,  true,   true,     true, },
                /*    PLAYER2 */{   false,   false,  false,   false,  true,  true,   true,  true,   true,    false, },
                /*    PLAYER3 */{   false,   false,  false,   false, false,  true,  false, false,  false,    false, },
                /*    PLAYER4 */{   false,   false,  false,   false, false,  true,  false, false,  false,    false, },
                /*      ENEMY */{   false,   false,  false,   false, false, false,  false, false,  false,    false, },
                /*       WALL */{   false,   false,  false,   false, false, false,  false, false,  false,    false, },
                /*       COIN */{   true,     true,  false,   false, false, false,  false, false,  false,    false, },
                /*        KEY */{   true,     true,  false,   false, false, false,  false, false,  false,    false, },
                /*    POWERUP */{   true,     true,  false,   false, false, false,  false, false,  false,    false, },
                /* ENEMY_VIEW isn't able to tell what type of collider it hit, so doesn't need a row here */
        };

        /*
        Only one type of collider (player 1, who is neither captured or invisible) should be detected.
        We are testing two of each type of collider, so there should be two player 1 instances to be detected.
         */
        int expectedDetectedPlayers = 2;

        boolean[][] actualCollisions = new boolean[numBoxes][numColliders];
        for (boolean[] row : actualCollisions)
            Arrays.fill(row, false);

        BoxCollider[] boxCollidersA = new BoxCollider[numBoxes]; // vertical axis
        BoxCollider[] boxCollidersB = new BoxCollider[numBoxes]; // horizontal axis

        for (int i = 0; i < numBoxes; i++) {
            final int row = i;

            /* create a collider of each type */
            boxCollidersA[i] = new BoxCollider(tf, boxTags[row], boxTagData[row]) {
                @Override
                public void onCollision(ColliderTag tag, int tagData, XYPair posOfOther, XYPair overlapSize) {

                    if (tag == ColliderTag.ENEMY_VIEW) { // use the final column of the matrix
                        actualCollisions[row][numColliders-1] = true;
                    }

                    else { // other collider is a box collider
                        // find the correct column to use
                        int column = -1;
                        for (int j = 0; j < numBoxes; j++) {
                            if (boxTags[j] == tag && boxTagData[j] == tagData) {
                                column = j;
                                break;
                            }
                        }
                        assertTrue(column != -1); // check that search was successful

                        actualCollisions[row][column] = true;
                    }
                }
            };

            /* create a second collider of each type so that each type is tested colliding against itself */
            boxCollidersB[i] = new BoxCollider(tf, boxTags[row], boxTagData[row]);

            /* add the box colliders to the collision system */
            collisionSystem.addBoxCollider(boxCollidersA[i]);
            collisionSystem.addBoxCollider(boxCollidersB[i]);
        }

        /* add a distance trigger to check detection of players */
        CountingTrigger trigger = new CountingTrigger(tf);
        collisionSystem.addDistanceTrigger(trigger);

        /* run the collision detection */
        collisionSystem.update();

        /* check the collision matrices match */
        for (int i = 0; i < numBoxes; i++) {
            for (int j = 0; j < numColliders; j++) {
                if (expectedCollisions[i][j] != actualCollisions[i][j]) {

                    /* for viewing in debugger */
                    ColliderTag row = boxTags[i];
                    ColliderTag col = j == numColliders-1 ? ColliderTag.ENEMY_VIEW : boxTags[j];

                    fail();
                }
            }
        }

        /* check that the ENEMY_VIEW detected the correct number of players */
        assertEquals(expectedDetectedPlayers, trigger.triggerCount);
    }
}