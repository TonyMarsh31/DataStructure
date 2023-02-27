package 哈希.与链表和数组的结合;

import java.util.LinkedHashMap;

public class LRUCacheBaseOnJavaAPI<K, V> {
    LinkedHashMap<K, V> cache = new LinkedHashMap<>();
    int cap;

    public LRUCacheBaseOnJavaAPI(int cap) {
        this.cap = cap;
    }

    public V get(K key) {
        if (!cache.containsKey(key)) return null;
        makeRecently(key);
        return cache.get(key);
    }

    public void put(K key, V val) {
        if (cache.containsKey(key)) {
            cache.put(key, val); //update
            makeRecently(key);
        } else {
            if (cache.size() >= cap) {
                removeLeastRecent();
            }
            cache.put(key, val); //insert
        }
    }

    private void removeLeastRecent() {
        K first = cache.keySet().iterator().next();
        // which is the least recent one
        cache.remove(first);
    }


    private void makeRecently(K key) {
        V val = cache.get(key);
        cache.remove(key);
        cache.put(key, val);
    }
}
