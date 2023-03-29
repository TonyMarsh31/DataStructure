package 队列和栈.队列拓展.优先级队列;

public class MyPriorityQueue<T extends Comparable<T>> {

    // 二叉堆的代码在  -> 树/二叉堆和优先级队列/MyBinaryHeap.java
/*
    优先级队列的底层就是二叉堆,整个类中的代码完全可以简化为以下所示:

    private MyBinaryHeap<T> heap; //存储数据的二叉堆

    public void enqueue(T element) {
        heap.add(element);
    }

    public T dequeue() {
        return heap.remove();
    }

    public T front() {
        return heap.get();
    }

    这是因为优先级队列特性是：每次出队的元素都是优先级最高的元素
    而在我们实现的二叉堆中已经实现了该特性
*/
}
