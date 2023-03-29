package 队列和栈.基本实现.Queue;

import 数组和链表.MyLinkedList;

public class MyLinkedQueue<E> {
    private final MyLinkedList<E> data;

    public MyLinkedQueue() {
        data = new MyLinkedList<>();
    }

    public void add(E e) {
        data.addLast(e);
    }

    public E remove() {
        return data.removeFirst();
    }

    public E peek() {
        return data.getFirst();
    }

}
