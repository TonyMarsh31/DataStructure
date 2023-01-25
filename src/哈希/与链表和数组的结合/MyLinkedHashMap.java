package 哈希.与链表和数组的结合;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 新特性:可以顺序访问所有key
 * <p>在Hashmap中，由于hash函数，key在HashTable中的index是无具体意义的，同时每次resize也会改变,所以在顺序遍历HashTable的时候，key是无序的</p>
 * <p>所以为实现该特性，一种思路是额外封装一个LinkedList用于顺序保存key :见图示1</p>
 * <p>这样在CRUD操作中，读和修改操作没有影响，直接用原来的Map API. 而在新增和删除中 需要额外的对保存key的List做一些维护,
 * 在链表中，新增(到最后)的时间复杂度是常量级的,而根据index删除需要遍历链表造成额外耗时</p>
 * <p><u>在本实现(与接下来所有HashMap与其他数据结构的结合)中，我们要做一个思想上的转变:我们不再将Map作为一个直接用于存储的"容器"，
 * 而是将其直接作为抽象使用，也就是key到value的映射，此时value不是直接的数据，而可以是其他数据结构的引用</u></p>
 * <p>如图示2,将原先只保存key的LinkedList改为存储entry.而map的value存储entry(node)的引用</p
 * <p>即我们使用LinkedList存储entry,这样就保证了key的顺序，同时使用map来做key和entry的映射来实现随机访问</p>
 * <p>或者换句话说，我们将Map中的Value由直接的entry.value 包装为一个链表节点node，node中额外的前后指针为entry保存了顺序 </p>
 * <p>最终，我们CRUD操作的时间复杂度就仍然是常数级别:见图3</p>
 */
public class MyLinkedHashMap<K, V> {

    //region 数据结构

    /**
     * 链表的节点，保存key和value与前后节点的引用
     */
    private static class Node<K, V> {
        K key;
        V val;
        Node<K, V> prev, next;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private final Node<K, V> head, tail; //链表头尾节点
    private final HashMap<K, Node<K, V>> map = new HashMap<>(); //底部的HashMap

    /**
     * 构造函数中初始化链表
     */
    public MyLinkedHashMap() {
        head = new Node<>(null, null);
        tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }
    //endregion

    //region Map标准API
    public V get(K key) {
        Objects.requireNonNull(key);
        Node<K, V> entry = map.get(key);
        return entry == null ? null : entry.val;
    }


    public V put(K key, V val) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            // 更新
            Node<K, V> node = map.get(key);
            node.val = val;
            return node.val;
        } else {
            // 新增 -- 添加到链表尾部并保存到map中
            Node<K, V> node = new Node<>(key, val);
            // 添加到链表尾部
            addLastNode(node);
            // 保存到map中
            map.put(key, node);
            return null;
        }
    }

    public V remove(K key) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            Node<K, V> node = map.remove(key); // 从map中删除
            removeNode(node); // 从链表中删除
            return node.val;
        } else {
            return null;
        }
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    //endregion

    //region 顺序遍历所有key的实现
    // 注：新特性是指返回的keys是有序的(插入顺序),之前MyHashMap1中的keys返回的是无序的key集合
    public Iterable<K> keys() {
        // 只需要key，所以创建一个新的LinkedList来保存key
        // 如果要entryList，可以直接返回map的head.next
        LinkedList<K> keyList = new LinkedList<>();
        for (Node<K, V> node = head.next; node != tail; node = node.next) {
            keyList.add(node.key);
        }
        return keyList;
    }
    //endregion

    //region 辅助方法
    private void addLastNode(Node<K, V> node) {
        Node<K, V> lastNode = tail.prev;
        // lastNode <-> tail
        node.next = tail;
        node.prev = lastNode;
        lastNode.next = node;
        tail.prev = node;
        // lastNode <-> node <-> tail
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    //endregion
}
