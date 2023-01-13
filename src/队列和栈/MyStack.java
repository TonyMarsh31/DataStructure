package 队列和栈;

import 数组和链表.MyLinkedList;

public class MyStack<E> {
    private final MyLinkedList<E> data;

    public MyStack() {
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

    public static void main(String[] args) {
        MyStack<Integer> stack = new MyStack<>();
        for (int i = 0; i < 10; i++) {
            stack.push(i);
            System.out.println(i + "入栈");
        }
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}
