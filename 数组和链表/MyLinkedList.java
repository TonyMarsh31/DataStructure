package 数组和链表;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Iterable<E> {
    // 虚拟头尾节点 (哨兵节点) ,使用虚拟头尾节点可以简化边界条件的判断
    final private Node<E> head, tail;
    private int size;

    // 双链表节点
    private static class Node<E> {
        E val;
        Node<E> next;
        Node<E> prev;

        Node(E val) {
            this.val = val;
        }
    }

    // 构造函数初始化头尾节点
    public MyLinkedList() {
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
        this.size = 0;
    }


    /***** 增 *****/

    public void addLast(E e) {
        Node<E> x = new Node<>(e);
        Node<E> temp = tail.prev;
        // temp <-> tail
        temp.next = x;
        x.prev = temp;

        x.next = tail;
        tail.prev = x;
        // temp <-> x <-> tail
        size++;
    }

    public void addFirst(E e) {
        Node<E> x = new Node<>(e);
        // head <-> temp
        Node<E> temp = head.next;

        // x <-> temp
        temp.prev = x;
        x.next = temp;

        // head <-> x <-> temp
        head.next = x;
        x.prev = head;
        size++;

        /*也可以写成
        x.next = temp;
        x.prev = head;
        head.next = x;
        temp.prev = x;*/
    }

    public void add(int index, E element) {
        checkPositionIndex(index);
        if (index == size) {
            addLast(element);
            return;
        }

        // 找到 index 对应的 Node
        Node<E> e = getNode(index);
        Node<E> pre = e.prev;
        // temp <-> p

        // 新要插入的 Node
        Node<E> eNew = new Node<>(element);

        e.prev = eNew;
        pre.next = eNew;

        eNew.prev = pre;
        eNew.next = e;

        // temp <-> x <-> p

        size++;
    }

    /***** 删 *****/

    public E removeFirst() {
        if (size < 1) {
            throw new NoSuchElementException();
        }
        // 虚拟节点的存在使得我们不用考虑空指针的问题
        Node<E> x = head.next;
        Node<E> temp = x.next;
        // head <-> x <-> temp
        head.next = temp;
        temp.prev = head;

        // 这一步是非必要的，因为java的GC会自动回收没有被引用的x
        x.prev = null;
        x.next = null;
        // head <-> temp

        size--;
        return x.val;
    }

    public E removeLast() {
        if (size < 1) {
            throw new NoSuchElementException();
        }
        Node<E> x = tail.prev;
        Node<E> temp = tail.prev.prev;
        // temp <-> x <-> tail

        tail.prev = temp;
        temp.next = tail;

        // 这一步是非必要的，因为java的GC会自动回收没有被引用的x
        x.prev = null;
        x.next = null;
        // temp <-> tail

        size--;
        return x.val;
    }

    public E remove(int index) {
        checkElementIndex(index);
        // 找到 index 对应的 Node
        Node<E> x = getNode(index);
        Node<E> prev = x.prev;
        Node<E> next = x.next;
        // prev <-> x <-> next
        prev.next = next;
        next.prev = prev;

        x.prev = x.next = null;
        // prev <-> next

        size--;

        return x.val;
    }

    /***** 查 *****/

    public E get(int index) {
        checkElementIndex(index);
        // 找到 index 对应的 Node
        Node<E> p = getNode(index);

        return p.val;
    }

    public E getFirst() {
        if (size < 1) {
            throw new NoSuchElementException();
        }

        return head.next.val;
    }

    public E getLast() {
        if (size < 1) {
            throw new NoSuchElementException();
        }

        return tail.prev.val;
    }

    /***** 改 *****/

    public E set(int index, E val) {
        checkElementIndex(index);
        // 找到 index 对应的 Node
        Node<E> p = getNode(index);

        E oldVal = p.val;
        p.val = val;

        return oldVal;
    }

    /***** 其他工具函数 *****/

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<E> getNode(int index) {
        checkElementIndex(index);
        Node<E> p;
        // TODO: 可以优化，通过 index 判断从 head 还是 tail 开始遍历
        if (index < (size >> 1)) {
            p = head.next;
            for (int i = 0; i < index; i++) {
                p = p.next;
            }
        } else {
            p = tail.prev;
            for (int i = size - 1; i > index; i--) {
                p = p.prev;
            }
        }
        return p;
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * 检查 index 索引位置是否可以存在元素
     */
    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /**
     * 检查 index 索引位置是否可以添加元素
     */
    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void display() {
        System.out.println("size = " + size);
        for (Node<E> p = head.next; p != tail; p = p.next) {
            System.out.print(p.val + " -> ");
        }
        System.out.println("null");
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Node<E> p = head.next;

            @Override
            public boolean hasNext() {
                return p != tail;
            }

            @Override
            public E next() {
                E val = p.val;
                p = p.next;
                return val;
            }
        };
    }

    // 生成一个使用到上述所有方法的测试案例
    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addLast(4);
        list.addLast(5);
        list.addLast(6);
        list.add(2, 7);
        list.add(5, 8);
        list.display();
        System.out.println("-----------------");
        Integer integer = list.removeFirst();
        Integer integer1 = list.removeLast();
        Integer remove = list.remove(2);
        System.out.println(integer);
        System.out.println(integer1);
        System.out.println(remove);
        list.display();
        System.out.println("-----------------");
        Integer originalVal = list.set(1, 9);
        System.out.println(originalVal);
        list.display();
        System.out.println("-----------------");
        System.out.println(list.get(1));
        System.out.println(list.getFirst());
        System.out.println(list.getLast());
        System.out.println(list.size());
        System.out.println(list.isEmpty());
    }


}
