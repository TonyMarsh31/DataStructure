package 哈希.与链表和数组的结合;

public class LRUCache<K, V> {
    private final MyLinkedHashMap<K, V> cache = new MyLinkedHashMap<>();
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        makeRecently(key);
        return cache.get(key);
    }

    public void put(K key, V val) {
        if (cache.containsKey(key)) {
            // update
            deleteKey(key);
            addRecently(key, val);
        } else { // insert
            // 其实这个判断逻辑可以直接封装进addRecent，但是为了强调逻辑，这里还是选择啰嗦一点
            if (cache.size() > capacity) removeLeastRecently();
            addRecently(key, val);
        }
    }


    // 底层封装，避免直接操作cache
    private void makeRecently(K key) {
        V val = cache.get(key);
        cache.remove(key);
        cache.put(key, val);
    }

    private void addRecently(K key, V val) {
        cache.put(key, val);
    }

    private void deleteKey(K key) {
        cache.remove(key);
    }

    private void removeLeastRecently() {
        cache.removeFirst();
    }

}
