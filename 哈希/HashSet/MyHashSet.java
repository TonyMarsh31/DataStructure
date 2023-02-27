package 哈希.HashSet;

import 哈希.HashMap.拉链法.MyHashMap1;

/**
 * 就是一个只存key的HashMap
 */
public class MyHashSet<K> {
    private static final Object PRESENT = new Object(); // Value的占位符
    private final MyHashMap1<K, Object> map = new MyHashMap1<>();

    public boolean add(K key) {
        return map.put(key, PRESENT) == null;
    }

    public boolean remove(K key) {
        return map.remove(key) == PRESENT;
    }

    public boolean contains(K key) {
        return map.containKey(key);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
