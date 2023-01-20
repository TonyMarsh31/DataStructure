package HashMap.线性探查法;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用线性探查法处理哈希冲突的 HashMap
 * <p>与MyHashMap2的不同:用占位符标记完成删除操作，这样就不用每一次删除时都要rehash了</p>
 * <p>rehash的频率从每remove操作发生时进行一次，降低为每resize操作发生时进行一次</p>
 * <P> 修改的地方：getNodeIndex | put | remove | resize</P>
 */
public class MyHashMap3<K, V> {
    private static final int INIT_CAP = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private final Node<K, V> DUMMY = new Node<>(null, null); // 占位符，用于标记删除
    // 拉链法中的HashTable是一个SLot数组，slot是一个存储node的单链表
    // 而在线性探查法中，HashTable是一个直接存储Node的数组
    private Node<K, V>[] table;
    private int size;
    private int cap;

    public MyHashMap3() {
        this(INIT_CAP);
    }

    public MyHashMap3(int initCap) {
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
        while (table[index] != null && table[index] != DUMMY) { // 线性探查法,找一个空Node插入
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
        table[index] = DUMMY;
        size--;
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
        int i = myHash(key);
        /*
         hashMap在put和remove的时候都会结合负载因子检查是否要resize 以确保HashMap不被填满
         但是在本类的实现中，由于dummy的存在，可能还是会出现HashTable被填满的情况 
         例如在数据量size适中的时候，此时put和remove不触发resize,
         之后又不断交替触发put和remove, size变化很小，仍然不触发resize，但不断有dummy节点被插入，最终导致HashTable被填满
         遍历一个不存在nullNode的HashTable，会出现死循环
         所以补充一个step变量记录遍历次数，超过size后退出循环，以此避免死循环
        */
        int step = 0;
        while (table[i] != null) {
            if (table[i] == DUMMY) continue; //跳过占位符 //? 这一步是必要的吗 
            if (table[i].key.equals(key)) return i;
            i = (i + 1) % cap;
            step++;
            if (step == size) return -1;
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
        MyHashMap3<K, V> newMap = new MyHashMap3<>(newCap);
        for (Node<K, V> entry : table) {
            if (entry != null && entry != DUMMY) {
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
