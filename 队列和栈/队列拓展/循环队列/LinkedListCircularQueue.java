package 队列和栈.队列拓展.循环队列;


import java.util.LinkedList;

/**
 * 没有特别大的意义，本质就是受限的LinkedList
 * <p> 需要明白的是,LinkedList本身就是Deque了（实现了Deque接口）</p>
 * 循环队列这个词应该就是用于用数组实现队列，然后复用数组空间的
 * 这里用LinkedList实现，就是用LinkedList的addLast和removeFirst方法
 */
public class LinkedListCircularQueue {
    LinkedList<Integer> list;
    int size;

    public LinkedListCircularQueue(int initCap) {
        list = new LinkedList<>();
        size = initCap;
    }

    public boolean enQueue(int value) {
        if (list.size() == size) return false;
        list.addLast(value);
        return true;
    }

    public boolean deQueue() {
        if (list.isEmpty()) return false;
        list.removeFirst();
        return true;
    }

    public int Front() {
        if (list.isEmpty()) return -1;
        return list.peekFirst();
    }

    public int Rear() {
        if (list.isEmpty()) return -1;
        return list.peekLast();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean ifFull() {
        return list.size() == size;
    }

}
