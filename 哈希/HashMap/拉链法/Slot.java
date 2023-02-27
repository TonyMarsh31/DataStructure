package 哈希.HashMap.拉链法;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HashMap拉链法实现中的槽位，(哈希桶)，本质是一个单链表
 *
 * @param <K>
 * @param <V>
 */
public class Slot<K, V> {
    // 头尾节点占位符
    private final Node<K, V> head, tail;
    private int size;
    /**
     * 构造函数，初始化头尾节点
     */
    public Slot() {
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        this.size = 0;
    }

    //region 增
    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        Node<K, V> p = getNode(key);
        // 如果key已经存在，更新val
        if (p != null) {
            V oldValue = p.val;
            p.val = val;
            return oldValue;
        }
        // 如果key不存在，插入新节点
        Node<K, V> newNode = new Node<>(key, val);
        newNode.next = head.next; // 单链表，头插
        head.next = newNode;
        size++;
        return null;
    }

    //region 删
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        Node<K, V> pre = head;
        while (pre.next != tail) {
            if (pre.next.key.equals(key)) {
                V oldValue = pre.next.val;
                pre.next = pre.next.next;
                size--;
                return oldValue;
            }
            pre = pre.next;
        }
        return null;
    }

    //endregion

    //region 查
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        Node<K, V> p = getNode(key);
        return p == null ? null : p.val;
    }

    //endregion

    public boolean containKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        return getNode(key) != null;
    }

    /**
     * 遍历slot,返回key对应的node
     */
    private Node<K, V> getNode(K key) {
        for (Node<K, V> p = head.next; p != tail; p = p.next) {
            if (p.key.equals(key)) {
                return p;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }


    //endregion

    //region 改

    //endregion

    public boolean isEmpty() {
        return size == 0;
    }

    public List<K> keys() {
        ArrayList<K> keyList = new ArrayList<>();
        for (Node<K, V> p = head.next; p != tail; p = p.next) {
            keyList.add(p.key);
        }
        return keyList;
    }

    public List<Map.Entry<K, V>> entries() {
        ArrayList<Map.Entry<K, V>> entryList = new ArrayList<>();
        for (Node<K, V> p = head.next; p != tail; p = p.next) {
            entryList.add(p);
        }
        return entryList;
    }

    /**
     * 链表节点
     *
     * @param <K>
     * @param <V>
     */
    private static class Node<K, V> implements Map.Entry<K, V> {
        K key;
        V val;
        Node<K, V> next;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public K getKey() {
            return key;
        }

        public V getValue() {
            return val;
        }

        public V setValue(V val) {
            V oldValue = this.val;
            this.val = val;
            return oldValue;
        }

    }

}
