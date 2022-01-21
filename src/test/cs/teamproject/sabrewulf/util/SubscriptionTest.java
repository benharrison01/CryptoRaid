package test.cs.teamproject.sabrewulf.util;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.util.Subscription;
import uob.cs.teamproject.sabrewulf.util.Subscriber;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    /** Check that iterating through the subscription presents each object exactly once.
     * @param subscription the subscription to test
     * @param objects an array storing the same objects as the subscription
     * @param size the size of the array (indices greater than or equal to this are ignored)
     * */
    private void checkIteration(Subscription<Object> subscription, Object[] objects, int size) {

        /* mark each value as unread */
        boolean[] objRead = new boolean[size]; // each element is 'false' by default

        /* check that iterating through the set presents each object exactly once */
        for (Object obj : subscription) {
            int i = 0;

            /* find the index of the element in the array */
            while (i < size && obj != objects[i]) {
                i++;
            }

            /* check that the element hasn't been read yet */
            assertFalse(objRead[i]);

            /* mark the element as read */
            objRead[i] = true;
        }

        /* check that each value was read */
        for (boolean b : objRead) assertTrue(b);
    }

    /** Test that the subscription stores objects, and that they can be iterated through properly. */
    @Test
    public void iterationTest() {
        final int NUM_OBJS = 5;

        Subscription<Object> subscription = new Subscription<>();

        /* create some objects to test with */
        Object[] objs = new Object[NUM_OBJS];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = new Object();
        }

        /* populate the subscription */
        for (Object obj : objs) {
            subscription.addSubscriber(new Subscriber<>(obj));
        }

        checkIteration(subscription, objs, objs.length);
    }

    /** Test that objects can unsubscribe, and that this doesn't stop iteration from working correctly */
    @Test
    public void unsubscribeTest() {
        final int NUM_OBJS = 5;

        for (int indexToRemove = 0; indexToRemove < NUM_OBJS; indexToRemove++) {

            /* create a subscription to test */
            Subscription<Object> subscription = new Subscription<>();

            /* create some objects to test with */
            Object[] objs = new Object[NUM_OBJS];
            for (int i = 0; i < objs.length; i++) {
                objs[i] = new Object();
            }

            /* populate the subscription, and record the subscriber which we'll remove */
            Subscriber<Object> subToRemove = null;
            for (int i = 0; i < objs.length; i++) {
                Subscriber<Object> sub = new Subscriber<>(objs[i]);
                if (i == indexToRemove) {
                    subToRemove = sub;
                }
                subscription.addSubscriber(sub);
            }
            assertNotNull(subToRemove);

            /* move the object to remove to the end of the array so that it can be ignored by checkIteration() */
            Object objToRemove = objs[indexToRemove];
            objs[indexToRemove] = objs[objs.length - 1];
            objs[objs.length - 1] = objToRemove;

            /* check iteration works */
            checkIteration(subscription, objs, objs.length);

            /* unsubscribe the object */
            subToRemove.unsubscribe();

            /* check iteration still works */
            checkIteration(subscription, objs, objs.length - 1);
        }

    }
}