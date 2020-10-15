package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    private int size;
    private HashMap<T, Integer> hashMap;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>(10);
        items.add(0, null);
        size = 0;
        hashMap = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        hashMap.put(items.get(a).getItem(), b);
        hashMap.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        }
        size++;
        items.add(size, new PriorityNode<>(item, priority));
        hashMap.put(item, size);
        percolateUp(size);
    }

    private int percolateUp(int index) {
        while (index > 1 && itemPriority(index / 2) > itemPriority(index)) {
            swap(index, index / 2);
            index = index / 2;
        }
        return index;
    }

    @Override
    public boolean contains(T item) {
        return hashMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(1).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        size--;
        T temp = items.get(1).getItem();
        if (size == 0) {
            hashMap.remove(temp);
            return items.remove(1).getItem();
        }
        hashMap.remove(temp);
        items.set(1, items.remove(size + 1));
        percolateDown(1);
        return temp;
    }

    private int percolateDown(int index) {
        while (index * 2 + 1 <= size && (itemPriority(index) > itemPriority(index * 2)
            || itemPriority(index) > itemPriority(index * 2 + 1))) {
            if (itemPriority(index * 2) > itemPriority(index * 2 + 1)) {
                swap(index, index * 2 + 1);
                index = index * 2 + 1;
            } else {
                swap(index, index * 2);
                index = index * 2;
            }
        }
        if (index * 2 <= size && itemPriority(index) > itemPriority(index * 2)) {
            swap(index, index * 2);
            index = index * 2;
        }
        return index;
    }

    private double itemPriority(int i) {
        return items.get(i).getPriority();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = hashMap.get(item);
        items.get(index).setPriority(priority);
        index = percolateUp(index);
        index = percolateDown(index);
        hashMap.put(item, index);
    }

    @Override
    public int size() {
        return this.size;
    }
}
