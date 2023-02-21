package 队列和栈.单调队列;

import java.util.LinkedList;

public class MonotonicQueue<T extends Comparable<T>> {
    private final LinkedList<T> data = new LinkedList<>();

    public void push(T n) {
        while (!data.isEmpty() && data.getLast().compareTo(n) < 0) {
            data.removeLast();
        }
        data.addLast(n);
    }

    public void pop(T n) {
        if (!data.isEmpty() && data.getFirst().compareTo(n) == 0) {
            data.removeFirst();
        }
    }

    public T max() {
        return data.getFirst();
    }

    public T min() {
        return data.getLast();
    }

    public int size() {
        return data.size();
    }
}
