package 队列和栈.基本实现.Queue;

public class MyArrayQueue<T> {
    private T[] array;
    private int head;
    private int tail;
    private int size;

    public MyArrayQueue(int capacity) {
        array = (T[]) new Object[capacity];
        head = tail = 0;
        size = 0;
    }

    public void offer(T element) {
        if (size == array.length) {
            throw new IllegalStateException("Queue is full");
        }
        array[tail] = element;
        tail = (tail + 1) % array.length; //计算新的tail，当已经到数组的尾部时，从头开始
        size++;
    }

    public T poll() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        T element = array[head];
        array[head] = null;
        head = (head + 1) % array.length; //更新新的头部，当已经到数组的尾部时，从头开始
        size--;
        return element;
    }

    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        return array[head];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
