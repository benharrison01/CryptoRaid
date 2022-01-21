package test.cs.teamproject.sabrewulf.collisions;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.collisions.DistanceTrigger;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Test class for {@link DistanceTrigger} */
public class DistanceTriggerTest {

    /**
     * Test that {@link DistanceTrigger#isWithinRange(XYPair)} is able to correctly determine whether a point is within
     * the detection range.
     */
    @Test
    public void isWithinRangeTest() {
        Transform tf = new Transform(0, 0, 10, 10);

        /*
        Each test value is an array of 6 values:
        {
            trigger's X-pos, trigger's Y-pos, trigger's radius
            other X-pos, other Y-pos,
            [1 or 0, where 1 means the 'other' point is within the detection radius]
            A's X-pos, A's Y-pos, A's width, A's height,
            B's X-pos, B's Y-pos, B's width, B's height,
        }

        */
        double[][] testValues = {
                /*   Tx   Ty   Tr     Ox   Oy    res  */
                {     0,   0,  10,     0,   0,     1  },
                {     0,   0,  10,     9,   0,     1  },
                {     0,   0,  10,  9.9d,   0,     1  },
                {     0,   0,  10,    10,   0,     1  },
                {     0,   0,  10, 10.1d,   0,     0  },
                {     0,   0,  10,    11,   0,     0  },
                {     0,   0,  10,     0,   0,     1  },
                {     0,   0,  10,    -9,   0,     1  },
                {     0,   0,  10, -9.9d,   0,     1  },
                {     0,   0,  10,   -10,   0,     1  },
                {     0,   0,  10,-10.1d,   0,     0  },
                {     0,   0,  10,   -11,   0,     0  },
                {     0,   0,   5,     3,   4,     1  },
                {     0,   0,   5,     3,3.9d,     1  },
                {     0,   0,   5,     3,4.1d,     0  },
                {     0,   0,   5,  2.9d,   4,     1  },
                {     0,   0,   5,  3.1d,   4,     0  },
                {     0,   0,   5,     3,  -4,     1  },
                {     0,   0,   5,    -3,3.9d,     1  },
                {     0,   0,   5,    -3,4.1d,     0  },
                {     0,   0,   5, -2.9d,   4,     1  },
                {     0,   0,   5, -3.1d,  -4,     0  },
        };

        for (double[] val : testValues) {
            tf.position.x = val[0];
            tf.position.y = val[1];

            DistanceTrigger trig = new DistanceTrigger(tf, ColliderTag.PLAYER, val[2]);
            XYPair other = new XYPair(val[3], val[4]);

            assertEquals(val[5], trig.isWithinRange(other) ? 1 : 0);
        }
    }
}