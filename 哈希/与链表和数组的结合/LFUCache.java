package 哈希.与链表和数组的结合;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUCache<K, V> {
    HashMap<K, V> keyToVal;
    HashMap<K, Integer> keyToFreq;
    /**
     * <p>一个freq对应多个key，操作要O1复杂度，同时保证插入顺序(时序)以删除相同freq中最旧的那个; 综上Value的泛型选择使用LinkedHashSet</p>
     */
    HashMap<Integer, LinkedHashSet<K>> freqToKeys;

    int minFreq;
    int cap;

    public LFUCache(int capacity) {
        keyToVal = new HashMap<>();
        keyToFreq = new HashMap<>();
        freqToKeys = new HashMap<>();
        this.cap = capacity;
        this.minFreq = 0;
    }

    public V get(K key) {
        if (!keyToVal.containsKey(key)) return null;
        increaseFreq(key);
        return keyToVal.get(key);
    }

    public void put(K key, V value) {
        if (keyToVal.containsKey(key)) {
            //update
            keyToVal.put(key, value);
            increaseFreq(key);
        } else {
            if (keyToVal.size() == cap) {
                removeMinFreqKey();
            }
            //insert
            insertNewKey(key, value);
        }

    }

    private void insertNewKey(K key, V value) {
        keyToVal.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.putIfAbsent(1, new LinkedHashSet<>());
        freqToKeys.get(1).add(key);
        minFreq = 1;
    }


    //region LFU的核心算法
    private void removeMinFreqKey() {
        LinkedHashSet<K> keyList = freqToKeys.get(minFreq);
        // 相同freq的情况下，删除最先被插入的(最旧的),即链表的头部的元素
        K deleteKey = keyList.iterator().next();
        keyList.remove(deleteKey);
        if (keyList.isEmpty()) {
            freqToKeys.remove(minFreq);
            /*补充说明:
             * 如果进入该条件判断删除了minFreq,那么按逻辑应该更新minFreq
             * 而更新肯定要遍历一遍，时间复杂度就不是常数级了
             * 但其实我们不需要这么做，因为本method只有一个usage，就是在insertNewKey且cap不够的时候执行本方法
             * 而在insertNewKey的时候，新的minFreq肯定是1，所以这里就不需要更新minFreq了
             */
        }
        // 同步数据
        keyToVal.remove(deleteKey);
        keyToFreq.remove(deleteKey);
    }

    private void increaseFreq(K key) {
        int freq = keyToFreq.get(key);
        // update keyToFreq
        keyToFreq.put(key, freq + 1);
        // update freqToKeys
        // 添加新的
        freqToKeys.putIfAbsent(freq + 1, new LinkedHashSet<>());
        freqToKeys.get(freq + 1).add(key);
        // 删除旧的
        freqToKeys.get(freq).remove(key);
        // 如果旧的freq唯一，则删除整个LinkedHashSet
        if (freqToKeys.get(freq).isEmpty()) {
            freqToKeys.remove(freq);
            if (freq == minFreq) {
                // 如果删除的freq还是minFreq，那么minFreq++
                minFreq++;
            }
        }
    }
    //endregion

}
