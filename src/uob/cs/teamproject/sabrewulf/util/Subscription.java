package uob.cs.teamproject.sabrewulf.util;

import java.util.Iterator;

/**
 * An unordered collection of elements, each of which is associated with a {@link Subscriber} instance.
 * @param <T> the type of object to store in the collection
 */
public class Subscription<T> implements Iterable<T> {

    /* the head of the linked list used to implement the collection */
    private final Subscriber<T> head;

    /** Creates a new instance of {@link Subscription}. */
    public Subscription() {
        head = new Subscriber<>(null);
    }

    /**
     * Adds a {@link Subscriber} to the collection.
     * @param subscriber the {@link Subscriber} instance to add
     */
    public void addSubscriber(Subscriber<T> subscriber) {
        head.insertAfter(subscriber);
    }

    /**
     * @return an {@link Iterator} for this collection.
     */
    @Override
    public Iterator<T> iterator() {
        return new SubscriptionIterator<>(head);
    }

    /* the iterator allows the use of for-each loops to traverse the collection */
    private static class SubscriptionIterator<T> implements Iterator<T> {

        private Subscriber<T> current;

        public SubscriptionIterator(Subscriber<T> head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current.hasNext();
        }

        @Override
        public T next() {
            current = current.getNext();
            return current.getValue();
        }
    }
}