package 队列和栈.单调队列;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class MonotonicQueue<T extends Comparable<T>> {
    private final Deque<T> max; // 一个单调队列，第一个为max，接下来是次max ……
    private final Deque<T> min; // 一个单调队列，第一个为min，接下来是次min ……
    private final Deque<T> data; // 存放数据的队列

    public MonotonicQueue() {
        // min和max在有序度不高的情况下，size不会很大，所以用ArrayDeque
        max = new ArrayDeque<>(10);
        min = new ArrayDeque<>(10);
        data = new LinkedList<>();
    }

    public void push(T n) {
        while (!max.isEmpty() && max.getLast().compareTo(n) < 0) {
            max.removeLast();
        }
        max.addLast(n);

        while (!min.isEmpty() && min.getLast().compareTo(n) > 0) {
            min.removeLast();
        }
        min.addLast(n);

        data.addLast(n);
    }

    public T pop(T n) {
        T t = data.removeFirst();
        if (!max.isEmpty() && max.getFirst().compareTo(t) == 0) {
            max.removeFirst();
        }
        if (!min.isEmpty() && min.getFirst().compareTo(t) == 0) {
            min.removeFirst();
        }
        return t;
    }

    public T max() {
        return max.getFirst();
    }

    public T min() {
        return min.getFirst();
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

}
