package 队列和栈.基本实现.Queue;

public class QueueTest {
    public static void main(String[] args) {
        MyArrayQueue<Object> test = new MyArrayQueue<>(10);
//        MyLinkedQueue<Object> test = new MyLinkedQueue<>();
        for (int i = 0; i < 5; i++) {
            test.offer(i);
            System.out.println(i + "入队");
        }
        System.out.println("-----------------");
        System.out.println(test.poll() + "出队");
        System.out.println(test.poll() + "出队");

    }

}
