package 队列和栈;

public class QueueTest {
    public static void main(String[] args) {
        MyArrayDeque<Object> test = new MyArrayDeque<>();
//        MyLinkedQueue<Object> test = new MyLinkedQueue<>();
        for (int i = 0; i < 5; i++) {
            test.add(i);
            System.out.println(i + "入队");
        }
        System.out.println("-----------------");
        System.out.println(test.remove() + "出队");
        System.out.println(test.remove() + "出队");

    }

}
