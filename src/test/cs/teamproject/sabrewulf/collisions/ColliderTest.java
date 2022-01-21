package test.cs.teamproject.sabrewulf.collisions;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.collisions.Collider;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.util.Transform;

import static org.junit.jupiter.api.Assertions.*;

/** Test class for {@link Collider} */
public class ColliderTest {

    /* Collider is an abstract class, so we need to subclass it to test it */
    private class TestCollider extends Collider {
        public TestCollider(Transform transform, ColliderTag tag, int tagData) {
            super(transform, tag, tagData);
        }

        public TestCollider(Transform transform, ColliderTag tag) {
            super(transform, tag);
        }
    }

    /** Test that {@link Collider#getTag()} works. */
    @Test
    public void getTagTest() {
        Transform tf = new Transform(0, 0, 10, 10);

        for (ColliderTag tag : ColliderTag.values()) {
            assertEquals(tag, new TestCollider(tf, tag, 0).getTag());
        }
    }

    /** Test that {@link Collider#getTagData()} works. */
    @Test
    public void getTagDataTest() {
        Transform tf = new Transform(0, 0, 10, 10);

        int[] testValues = {-100, -1, 0, 1, 100};

        for (int val : testValues) {
            assertEquals(val, new TestCollider(tf, ColliderTag.PLAYER, val).getTagData());
        }
    }

    /** Test that {@link Collider#setTagData(int)} works. */
    @Test
    public void setTagDataTest() {
        Transform tf = new Transform(0, 0, 10, 10);

        int[] testValues = {-100, -1, 0, 1, 100};
        TestCollider col = new TestCollider(tf, ColliderTag.PLAYER, 0);

        for (int val : testValues) {
            col.setTagData(val);
            assertEquals(val, col.getTagData());
        }
    }

    /** Test that {@link Collider#getPos()} works. */
    @Test
    public void getPosTest() {
        Transform tf = new Transform(0, 0, 10, 10);
        TestCollider col = new TestCollider(tf, ColliderTag.PLAYER, 0);

        double[][] testValues = {
                {-100, -100}, {-100, -1}, {-100, 0}, {-100, 1}, {-100, 100},
                {-1  , -100}, {-1  , -1}, {-1  , 0}, {-1  , 1}, {-1  , 100},
                {0   , -100}, {0   , -1}, {0   , 0}, {0   , 1}, {0   , 100},
                {1   , -100}, {1   , -1}, {1   , 0}, {1   , 1}, {1   , 100},
                {100 , -100}, {100 , -1}, {100 , 0}, {100 , 1}, {100 , 100},
        };

        for (double[] xy : testValues) {
            tf.position.x = xy[0];
            tf.position.y = xy[1];
            assertEquals(xy[0], col.getPos().x);
            assertEquals(xy[1], col.getPos().y);
        }
    }
}
