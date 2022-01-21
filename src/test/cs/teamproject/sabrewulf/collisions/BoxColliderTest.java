package test.cs.teamproject.sabrewulf.collisions;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.collisions.BoxCollider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Test class for {@link BoxCollider} */
public class BoxColliderTest {

    /**
     * Test that {@link BoxCollider#getOverlapSize(BoxCollider)} is able to properly detect overlap between
     * two {@link BoxCollider}s.
     */
    @Test
    public void getOverlapSizeTest() {
        Transform tfA = new Transform(0, 0, 10, 10);
        BoxCollider colA = new BoxCollider(tfA, ColliderTag.PLAYER, 1);

        Transform tfB = new Transform(0, 0, 10, 10);
        BoxCollider colB = new BoxCollider(tfB, ColliderTag.ENEMY, 2);

        /*
        Each test value is an array of 10 values:
        {
            A's X-pos, A's Y-pos, A's width, A's height,
            B's X-pos, B's Y-pos, B's width, B's height,
            overlap width, overlap height
        }

        -1 is used for overlap width and height where there is no overlap
        */
        double[][] testValues = {
                /*   Ax   Ay   Aw   Ah     Bx   By   Bw   Bh     Ow   Oh   */
                {   -25, -25,  10,  10,    25,  25,  10,  10,    -1,  -1   },
                {    -6,   0,  10,  10,     6,   0,  10,  10,    -1,  -1   },
                {    -5,   0,  10,  10,     5,   0,  10,  10,    -1,  -1   },
                {    -4,   0,  10,  10,     5,   0,  10,  10,     1,  10   },
                {    -4,   0,  10,  10,     4,   0,  10,  10,     2,  10   },
                {     0,   0,  10,  10,     0,   0,  10,  10,    10,  10   },
                {     0,   1,  10,  10,     0,   0,  10,  10,    10,   9   },
                {     0,   9,  10,  10,     0,   0,  10,  10,    10,   1   },
                {     0,  10,  10,  10,     0,   0,  10,  10,    -1,  -1   },
                {     0,   0,   0,   0,     0,   0,   0,   0,    -1,  -1   },
                {     0,   0,   1,   0,     0,   0,   1,   0,    -1,  -1   },
                {     0,   0,   0,   1,     0,   0,   0,   1,    -1,  -1   },
                {     0,   0,   1,   1,     0,   0,   1,   1,     1,   1   },
                {     0,   0,  -1,  -1,     0,   0,  -1,  -1,    -1,  -1   },
        };

        for (double[] val : testValues) {
            tfA.position.x = val[0];
            tfA.position.y = val[1];
            tfA.width      = val[2];
            tfA.height     = val[3];
            tfB.position.x = val[4];
            tfB.position.y = val[5];
            tfB.width      = val[6];
            tfB.height     = val[7];

            XYPair resultA = colA.getOverlapSize(colB);
            XYPair resultB = colB.getOverlapSize(colA);

            if (val[8] == -1 || val[9] == -1) {
                assertNull(resultA);
                assertNull(resultB);
            }
            else {
                assertEquals(val[8], resultA.x);
                assertEquals(val[9], resultA.y);
                assertEquals(val[8], resultB.x);
                assertEquals(val[9], resultB.y);
            }
        }
    }
}