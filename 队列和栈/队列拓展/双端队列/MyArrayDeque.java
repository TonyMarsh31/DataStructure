package 队列和栈.队列拓展.双端队列;

/**
 * 底层用数组，运用环形数组技巧实现双端队列
 * <p>Dequeue stands for Double Ended Queue</p>
 *
 * @param <E>
 */
public class MyArrayDeque<E> {
    private final static int INIT_CAP = 10;
    private int size;
    private E[] data;
    private int first, last;


    public MyArrayDeque(int initCap) {
        size = 0;
        data = (E[]) new Object[initCap];
        first = last = 0;
        // last 是下一次应该添加元素的索引
        // fist----last , [first, last)
        // 比如first = 1，last = 3 ,size = 2
    }

    public MyArrayDeque() {
        this(INIT_CAP);
    }

    /**
     * 从队尾入队
     *
     * @param e
     */
    public void addLast(E e) {
        ensureCapacity();
        data[last] = e;
        last++;
        if (last == data.length) last = 0; // 循环队列
        size++;
    }

    /**
     * 从队首出队
     *
     * @return
     */
    public E removeFirst() {
        if (isEmpty()) throw new IllegalArgumentException("队列为空");
        E toDel = data[first];
        data[first] = null;
        first++;
        if (first == data.length) first = 0;
        size--;

        // 缩容
        shrinkIfNecessary();
        return toDel;
    }

/*
     我们还可以实现从队尾入队，从队首出队的操作，让这个队列变成一个双端队列
     虽然思路上没有变化，但是这两个操作的代码会比上述两个方法会有所不同，
     因为我们维护的指针范围是 [first, last)，即first是元素的索引，last是下一次应该添加元素的索引
     这种指针范围的设计，是的我们在队尾入队，队首出队时，可以先操作，后更新指针
     而在队首入队，队尾出队的时候，需要先更新指针，再进行操作

     还要注意的是，更新指针时，是自增还是自减
     我们按照阅读从左往右的习惯，默认左边是队首，右边是队尾
     上述两个方法会使得数据一直向右移动，所以索引的更新都是++
     下面两个方法则相反
*/

    /**
     * 从队首入队
     *
     * @param e
     */
    public void addFirst(E e) {
        ensureCapacity();
        // first 是elementIndex，即这个位置上已经有元素了，所以要添加的话先更新first，再赋值
        if (first == 0) first = data.length - 1; // 往左没位置了，将头指向最后一个位置
        else first--;
        data[first] = e;
        size++;
    }

    /**
     * 从队尾出队
     *
     * @return
     */
    public E removeLast() {
        if (isEmpty()) throw new IllegalArgumentException("队列为空");
        // 类似的，last是positionIndex而不是ElementIndex，所以执行取值操作需要先更新last后取值
        if (last == 0) last = data.length - 1; // 往左没位置了，将尾指向最后一个位置
        else last--;
        E toDel = data[last];
        data[last] = null;
        size--;

        shrinkIfNecessary();
        return toDel;
    }

    public E peekFirst() {
        if (isEmpty()) throw new IllegalArgumentException("队列为空");
        return data[first];
    }

    public E peekLast() {
        if (isEmpty()) throw new IllegalArgumentException("队列为空");
        if (last == 0) return data[data.length - 1];
        return data[last - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == data.length) resize(size * 2); // 扩容
    }

    private void shrinkIfNecessary() {
        // 缩容
        if (size == data.length / 4 && data.length / 2 != 0) {
            resize(data.length / 2);
        }
    }

    private void resize(int newCap) {
        E[] newData = (E[]) new Object[newCap];
        //  first------last
        //  ---last first--  （绝大部分情况）
        for (int i = 0; i < size; i++) {
            newData[i] = data[(first + i) % data.length]; //使用取模运算保证索引不越界
            // 求模运算的效率其实不高，进一步优化可以查看RingBuffer中的位运算实现
        }
        data = newData;
        first = 0; // 注意新数组时从0开始copy的，所以需要重置first
        last = size;
        // first------last___________
    }


    // 用于给测试案例统一接口
    public void add(E e) {
        addLast(e);
//        addFirst(e);
    }

    public E remove() {
        return removeFirst();
//        return removeLast();
    }

}
