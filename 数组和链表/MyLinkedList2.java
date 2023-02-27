package 数组和链表;

import java.util.NoSuchElementException;

/**
 * 单链表实现
 *
 * @param <E>
 */
public class MyLinkedList2<E> {
    // 节点声明
    private static class Node<E> {
        E val;
        Node<E> next;

        Node(E val) {
            this.val = val;
        }
    }

    // 虚拟头尾节点
    private final Node<E> head, tail;
    private int size;

    // 构造函数初始化头尾节点
    public MyLinkedList2() {
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.next = tail;

        this.size = 0;
    }

    /* ---增--- */
    public void addFirst(E e) {
        Node<E> x = new Node<>(e);
        Node<E> temp = head.next;
        // x <-> temp
        x.next = temp;

        // head <-> x <-> temp
        head.next = x;
        size++;

        // 优化：可以不需要暂存temp
        // 简写为 x.next = head.next; head.next = x; size++;
        // 但注意这么写之后,上面的代码语句顺序不能改变,详见add方法中的注释
    }

    public void addLast(E e) {
        Node<E> toAdd = new Node<>(e);
        Node<E> lastElement = size == 0 ? head : getNode(size - 1);
        toAdd.next = tail;
        lastElement.next = toAdd;
        size++;
    }

    public void add(int index, E element) {
        checkPositionIndex(index);
        if (index == size) {
            addLast(element);
            return;
        }
        Node<E> toAdd = new Node<>(element);
        Node<E> original = getNode(index);
        Node<E> originalPre = index >= 1 ? getNode(index - 1) : head;
        // originalPre <-> original      ->    originalPre <-> toAdd <-> original
        toAdd.next = original;
        originalPre.next = toAdd;
        size++;

        // 优化：
        // 上述代码调用了两次getNode操作,本质上执行了两次的遍历操作,可以优化为只遍历一次
        // 因为我们其实只需要originalPre即可,original不需要通过遍历获取，即可以用originalPre.next表示

        // 优化后的代码如下:
        /* new node toAdd , get originalPre Node  ……
            toAdd.next = originalPre.next;
            originalPre.next = toAdd; */
        // 注意：上述两行代码的执行顺序不能颠倒！
        // 如果先执行originalPre.next = toAdd; 那么我们便无法表示original节点了,整个链表就断了

        // 同时，使用优化版本后，我们原来的 if (index == size)的单独处理也可以省略
        // 因为来原来的版本中，如果index == size,那么original就是tail节点
        // 当我们通过getNode方法来获取original节点时，会抛出异常
        // 因为我们自己的私有边间条件判定checkElementIndex中定义elementIndex < size，
        // 而在优化版本中，我们不需要original，只需要originalPre，而后者必定非空


    }

    /* ---删--- */
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<E> first = head.next;
        // second变量可以内联，这里写出来是为了方便理解
        Node<E> second = first.next;
        head.next = second;
        size--;
        return first.val;
    }

    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<E> last = getNode(size - 1);
        Node<E> lastPre;
        if (size > 1) {
            lastPre = getNode(size - 2); // minimal: getNode(0), which is the first element
        } else {
            lastPre = head;
        }
        // lastPre -> last -> tail
        lastPre.next = tail;
        last.next = null; // 非必须，GC会自动回收
        // lastPre -> tail
        size--;
        return last.val;

        //优化:同样的，不需要两次getNode操作，我们只需要lastPre即可
        // 暂存lastPre.next用于返回
        // 删除操作: lastPre.next = LastPre.next.next;
    }

    public E remove(int index) {
        checkElementIndex(index);
        Node<E> toRemove = getNode(index);
        Node<E> toRemovePre = index >= 1 ? getNode(index - 1) : head;
        Node<E> toRemoveNext = toRemove.next;
        // toRemovePre -> toRemove -> toRemoveNext
        toRemovePre.next = toRemoveNext;
        toRemove.next = null; // 非必须，GC会自动回收
        // toRemovePre -> toRemoveNext
        size--;
        return toRemove.val;

        //优化版本,只需要获取toRemovePre
        // 暂存toRemovePre.next用于返回
        // 删除操作: toRemovePre.next = toRemovePre.next.next;
    }

    /* ---查--- */
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.next.val;
    }

    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return getNode(size - 1).val;
    }

    public E get(int index) {
        checkElementIndex(index);
        return getNode(index).val;
    }
    /* ---改--- */
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> toSet = getNode(index);
        E oldVal = toSet.val;
        toSet.val = element;
        return oldVal;
    }
    /* ---辅助--- */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private Node<E> getNode(int index) {
        checkElementIndex(index);
        Node<E> p = head.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p;
    }

}
