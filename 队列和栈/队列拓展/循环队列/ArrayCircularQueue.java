package 队列和栈.队列拓展.循环队列;

public class ArrayCircularQueue<E> {
    private int size;
    private E[] data;
    private final static int INIT_CAP = 2;

    /**
     * 注意fist是elementIndex， last是positionIndex,指向下一个可以插入的位置
     * <p>也就是说第一个元素是data[first]，最后一个元素时data[last-1]</p>
     */
    private int first, last;

    public ArrayCircularQueue(int initCap) {
        size = 0;
        data = (E[]) new Object[initCap];
        first = last = 0;
    }

    public ArrayCircularQueue() {
        this(INIT_CAP);
    }

    private void resize(int newCap) {
        E[] temp = (E[]) new Object[newCap];
        // copyData
        for (int i = 0; i < size; i++) {
            temp[i] = data[(first + i) % data.length];
        }
        first = 0;
        last = size;
        data = temp;
    }

    public void enqueue(E e) {
        if (size == data.length) resize(size * 2);
        data[last] = e;
        last++;
        if (last == data.length) last = 0;
        size++;
    }

    public E dequeue() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");
        E oldVal = data[first];
        data[first] = null;
        first++;
        if (first == data.length) first = 0;
        size--;
        if (size == data.length / 4 && data.length / 2 != 0) resize(data.length / 2);
        return oldVal;
    }

    public E peekFirst() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");
        return data[first];
    }

    public E peekLast() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");
        if (last == 0) return data[data.length - 1];
        return data[last - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

}


