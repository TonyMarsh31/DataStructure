package 哈希.HashMap.线性探查法;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用线性探查法处理哈希冲突的 哈希.HashMap
 */
public class MyHashMap2<K, V> {
    private static final int INIT_CAP = 16;
    private static final float LOAD_FACTOR = 0.75f;
    // 拉链法中的HashTable是一个SLot数组，slot是一个存储node的单链表
    // 而在线性探查法中，HashTable是一个直接存储Node的数组
    private Node<K, V>[] table;
    private int size;
    private int cap;
    public MyHashMap2() {
        this(INIT_CAP);
    }

    public MyHashMap2(int initCap) {
        size = 0;
        cap = initCap;
        table = (Node<K, V>[]) new Node[initCap];
    }

    /**
     * 增加|修改 元素
     */
    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        ensureCapacity();
        int index = getNodeIndex(key);
        if (index != -1) { // key已经存在,更新val
            Node<K, V> entry = table[index];
            V oldVal = entry.val;
            entry.val = val;
            return oldVal;
        }
        // key不存在，插入新节点
        index = myHash(key);
        while (table[index] != null) { // 线性探查法,找一个空Node插入
            index = (index + 1) % cap; // 在方法体前段已经ensureCapacity，所以必定会找到空Node
        }
        table[index] = new Node<>(key, val);
        size++;
        return null;
    }

    //region CRUD

    /**
     * 删除元素
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (size < cap / 8) resize(cap / 2);
        int index = getNodeIndex(key);
        if (index == -1) { // key不存在
            return null;
        }
        Node<K, V> deletedEntry = table[index];
        table[index] = null;
        size--;

        // 在线性探查法的实现中，进行删除操作后，后面(直至下一个空节点)的元素需要向前移动(rehash),以保证哈希表的连续性
        // 这是因为在getNodeIndex中，遍历的终止条件是table[index] == null，如果不执行上述操作，那么可能会导致提前进入终止条件从而忽略之后的元素
        index = (index + 1) % cap;
        while (table[index] != null) {
            // rehash
            Node<K, V> entry = table[index];
            table[index] = null;
            size--;
            put(entry.key, entry.val); // 重新插入,size会自增回来
            index = (index + 1) % cap;
        }

        return deletedEntry.val;
    }

    /**
     * 获取元素
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        int index = getNodeIndex(key);
        if (index == -1) { // key不存在
            return null;
        }
        return table[index].val;
    }

    //region utils
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        return getNodeIndex(key) != -1;
    }

    //endregion

    public List<K> keys() {
        List<K> keys = new ArrayList<>();
        for (Node<K, V> entry : table) {
            if (entry != null) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    public List<Map.Entry<K, V>> entries() {
        List<Map.Entry<K, V>> entries = new ArrayList<>();
        for (Node<K, V> entry : table) {
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * key的hash操作
     */
    private int myHash(K key) {
        return (key.hashCode() & 0x7fffffff) % cap; //取正数后对cap取余，确保返回值在[0,cap)之间
    }

    /**
     * 获取key在HashTable中的index，使用线性探查法解决哈希冲突
     */
    private int getNodeIndex(K key) {
        int hash = myHash(key);
        // hashMap在put和remove的时候都会检查是否要resize 以确保HashMap不被填满，即至少会有1个null空位
        // 所以这个while循环是安全的，不会出现死循环
        while (table[hash] != null) {
            if (table[hash].key.equals(key)) {
                return hash;
            }
            hash = (hash + 1) % cap;
        }
        return -1;
    }

    /**
     * 确保HashTable有足够的空间
     */
    private void ensureCapacity() {
        if (size >= cap * LOAD_FACTOR) {
            resize(cap * 2);
        }
    }

    /**
     * 实际的扩容操作
     */
    private void resize(int newCap) {
        MyHashMap2<K, V> newMap = new MyHashMap2<>(newCap);
        for (Node<K, V> entry : table) {
            if (entry != null) {
                newMap.put(entry.key, entry.val);
            }
        }
        this.table = newMap.table;
        this.cap = newMap.cap;

    }

    /**
     * Node节点
     */
    private static class Node<K, V> implements Map.Entry<K, V> {
        K key;
        V val;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V val) {
            V oldVal = this.val;
            this.val = val;
            return oldVal;
        }

    }

    //endregion
}
