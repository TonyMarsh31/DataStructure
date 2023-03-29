package 队列和栈.基本实现.Stack;

import 数组和链表.MyLinkedList;

public class MyLinkedStack<E> {
    private final MyLinkedList<E> data;

    public MyLinkedStack() {
        data = new MyLinkedList<>();
    }

    public void push(E e) {
        data.addLast(e);
    }

    public E pop() {
        return data.removeLast();
    }

    public E peek() {
        return data.getLast();
    }


}
