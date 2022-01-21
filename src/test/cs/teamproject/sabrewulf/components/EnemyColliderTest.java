package test.cs.teamproject.sabrewulf.components;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.components.EnemyCollider;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for the {@link EnemyCollider} class. */
public class EnemyColliderTest {

    /**
     * Test that an {@link EnemyCollider} is able to detect players correctly and determine which to target.
     * The targeted player should be the one which is closest to the enemy.
     */
    @Test
    public void playerDetectionTest() {
        CollisionSystem collisionSystem = new CollisionSystem();

        /* create an enemy collider */
        Transform enemyTf = new Transform(0, 0, 2, 2);
        EnemyCollider enemy = new EnemyCollider(collisionSystem, enemyTf, 100);

        /* create 4 'players' */
        Transform player1 = new Transform(0, 0, 2, 2);
        Transform player2 = new Transform(0, 0, 2, 2);
        Transform player3 = new Transform(0, 0, 2, 2);
        Transform player4 = new Transform(0, 0, 2, 2);
        collisionSystem.addBoxCollider(new BoxCollider(player1, ColliderTag.PLAYER));
        collisionSystem.addBoxCollider(new BoxCollider(player2, ColliderTag.PLAYER));
        collisionSystem.addBoxCollider(new BoxCollider(player3, ColliderTag.PLAYER));
        collisionSystem.addBoxCollider(new BoxCollider(player4, ColliderTag.PLAYER));

        /*
        each test entry is 12 values:
         - player 1's position in X
         - player 1's position in Y
         - whether player 1 is a correct player for the EnemyCollider to target (1 for true, 0 for false)
         - the above 3 values repeated for players 2, 3 and 4.
         */
        double[][] testValues = {
                /* p1X  p1Y  p1V  p2X  p2Y  p2V  p3X  p3Y  p3V  p4X  p4Y p4V */
                {  999, 999,   0, 999, 999,   0, 999, 999,   0, 999, 999,  0 },
                {    0,  20,   1, 999, 999,   0, 999, 999,   0, 999, 999,  0 },
                {    0,  20,   1,   0,  40,   0, 999, 999,   0, 999, 999,  0 },
                {    0,  20,   1,   0, -20,   1,  20,   0,   1, -20,   0,  1 },
                {    0,  50,   0,   0,  40,   1, 999, 999,   0, 999, 999,  0 },
                {   30,  40,   1,  40,  30,   1, 999, 999,   0, 999, 999,  0 },
                {   30,  40,   1,  40,  30,   1, -30,  40,   1, -40,  30,  1 },
                {   30,  40,   1,  40,  30,   1,  30, -40,   1,  40, -30,  1 },
        };

        /* run the test */
        for (double[] testData : testValues) {
            player1.position.x = testData[0];
            player1.position.y = testData[1];
            player2.position.x = testData[3];
            player2.position.y = testData[4];
            player3.position.x = testData[6];
            player3.position.y = testData[7];
            player4.position.x = testData[9];
            player4.position.y = testData[10];

            enemy.update(0); // parameter can be ignored, it is inherited from GameComponent but unused
            collisionSystem.update();

            /* check whether the enemy has detected the players */
            XYPair result = enemy.getTargetPlayerPos();

            /* determine whether a player should have been targeted */
            boolean validOptionExists = false;
            for (int i = 2; i < testData.length; i+=3) { // i = 2, 5, 8, 11
                if (testData[i] == 1) {
                    validOptionExists = true;
                    break;
                }
            }

            if (validOptionExists) {
                /* check that the player targeted by the enemy was a valid choice */
                boolean anyPlayerTargeted = false;
                for (int i = 0; i < testData.length; i+=3) {
                    double x = testData[i];
                    double y = testData[i+1];
                    boolean valid = testData[i+2] == 1;
                    if (result.x == x && result.y == y) {
                        anyPlayerTargeted = true;
                        if (!valid) {
                            fail();
                        }
                    }
                }
                if (!anyPlayerTargeted) {
                    fail();
                }
            }
            else {
                /* no player should have been targeted */
                assertNull(result);
            }
        }
    }
}