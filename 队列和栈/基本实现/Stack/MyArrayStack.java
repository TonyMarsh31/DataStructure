package 队列和栈.基本实现.Stack;

import 数组和链表.MyArrayList;

public class MyArrayStack<E> {
    private final MyArrayList<E> data;

    public MyArrayStack() {
        data = new MyArrayList<>();
    }

    public void push(E e) {
        data.addLast(e);
    }

    public E pop() {
        return data.removeLast();
    }

    public E peek() {
        if (data.isEmpty()) throw new IllegalArgumentException("栈为空");
        return data.get(data.size() - 1);
    }

}
