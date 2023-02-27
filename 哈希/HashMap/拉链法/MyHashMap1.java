package 哈希.HashMap.拉链法;

import java.util.ArrayList;
import java.util.Map;

/**
 * 使用拉链思路处理哈希冲突的 哈希.HashMap
 * 主要的部分就是用myHash(key) 计算对应的Slot，剩下的就是用Slot的API来进行CRUD了
 * 难点在于hash与rehash的设计，这方面有很多优化的空间，但这里仅仅是一个简单的实现用于阐释思路，所以不多展开了
 */
public class MyHashMap1<K, V> {
    private static final int INIT_CAP = 16; // 默认初始容量
    private static final float LOAD_FACTOR = 0.75f; // 负载因子
    private int size;
    private Slot<K, V>[] table; // 本哈希表本质是一个单链表数组

    public MyHashMap1() {
        this(INIT_CAP);
    }

    public MyHashMap1(int initCapacity) {
        size = 0;
        table = (Slot<K, V>[]) new Slot[initCapacity];
        // 初始化每个槽位（虚拟头尾指针
        for (int i = 0; i < initCapacity; i++) {
            table[i] = new Slot<>();
        }
    }

    //region CRUD

    /*
     */

    /**
     * 添加元素，如果key已经存在，则覆盖旧值
     */
    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (size >= table.length * LOAD_FACTOR) {
            resize(table.length * 2);
        }
        Slot<K, V> slot = table[myHash(key)];
        if (!slot.containKey(key)) {
            size++;
        }
        return slot.put(key, val);
    }

    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        Slot<K, V> slot = table[myHash(key)];
        if (slot.containKey(key)) {
            size--;
            return slot.remove(key);
        }
        return null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        Slot<K, V> slot = table[myHash(key)];
        return slot.get(key);
    }


    //endregion

    //region Utils
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        Slot<K, V> slot = table[myHash(key)];
        return slot.containKey(key);
    }

    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<>();
        for (Slot<K, V> slot : table) {
            keyList.addAll(slot.keys());
        }
        return keyList;
    }

    /**
     * 将任意key映射到HashTable的Index,也有很多种实现方式和优化方案，这里不展开了
     */
    private int myHash(K key) {
//        取绝对值后与哈希表长度取模，保证哈希值在 [0, table.length) 范围内
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /**
     * 重点在于rehash的部分，这方面有很多优化的空间可以挖掘 这里就不深入了
     */
    private void resize(int newCap) {
        MyHashMap1<K, V> newMap = new MyHashMap1<>(newCap);
        for (Slot<K, V> slot : table) {
            for (Map.Entry<K, V> entry : slot.entries()) {
                K key = entry.getKey();
                V val = entry.getValue();
                newMap.put(key, val);
            }
        }
        this.table = newMap.table;
    }

    //endregion
}
