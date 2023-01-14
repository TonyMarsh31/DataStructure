package 队列和栈;

public class StackTest {
    public static void main(String[] args) {
        MyArrayStack<Object> test = new MyArrayStack<>();
//        MyLinkedStack<Object> test = new MyLinkedStack<>();

        for (int i = 0; i < 5; i++) {
            test.push(i);
            System.out.println(i + "入栈");
        }
        System.out.println("-----------------");
        System.out.println(test.pop() + "出栈");
        System.out.println(test.pop() + "出栈");
    }
}
