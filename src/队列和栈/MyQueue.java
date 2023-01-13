package 队列和栈;

import 数组和链表.MyLinkedList;

public class MyQueue<E> {
    private final MyLinkedList<E> data;

    public MyQueue() {
        data = new MyLinkedList<>();
    }

    public void enqueue(E e) {
        data.addLast(e);
    }

    public E dequeue() {
        return data.removeFirst();
    }

    public E peek() {
        return data.getFirst();
    }

    public static void main(String[] args) {
        MyQueue<Integer> queue = new MyQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
            System.out.println(i + "入队");
        }
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
    }
}
