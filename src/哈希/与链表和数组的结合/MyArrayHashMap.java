package 哈希.与链表和数组的结合;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * 新特性：可以在O(1)时间复杂度内 等概率地 随机返回一个key  (pop)
 * <p>单纯的HashMap结构无法实现该特性: 拉链法存在多个链表，遍历必定无法保证O(1)；
 * 线性探查法的HashTable中存在一定的空位，所以随机获取到index的时间未知，运气不好的话会一直获取空位</p>
 * <p>在本实现中，与数组(ArrayList)结合，HashMap用于保存key到 数组Index的映射，数组存储entry ,这样，新特性的实现就是随机生成在范围内的数组index</p>
 * <p>接着在CRUD中，query和update操作直接用mapApi,时间复杂度没有影响</p>
 * <p>新增操作，添加到数组尾部，然后更新hashmap映射，时间复杂度仍然是常数级别</p>
 * <p>删除操作，我们可以将被删除的元素与数组最后一个元素做swap，然后reMap，这样每次删除就不需要在数组中做数据搬移，
 * 综上，在实现新特性的同时，没有增加时间复杂度</p>
 */
public class MyArrayHashMap<K, V> {

    //region 数据结构
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 用于保存key到 数组Index的映射
     */
    private final HashMap<K, Integer> map = new HashMap<>();
    /**
     * 保存entry的数组
     */
    private final ArrayList<Entry<K, V>> array = new ArrayList<>();
    private final Random random = new Random();

    //没有额外的初始化操作
    public MyArrayHashMap() {
    }

    //endregion

    //region Map标准API
    public V get(K key) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            Integer index = map.get(key);
            return array.get(index).value;
        } else {
            return null;
        }
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            // update
            Integer index = map.get(key);
            Entry<K, V> entry = array.get(index);
            V oldValue = entry.value;
            entry.value = value;
            return oldValue;
        } else {
            // insert
            Entry<K, V> entry = new Entry<>(key, value);
            array.add(entry);
            map.put(key, array.size() - 1);
            return null;
        }
    }

    public V remove(K key) {
        Objects.requireNonNull(key);
        // 思路:
        // 1. Swap   将要删除的元素与数组最后一个元素调换位置
        // 2. ReMap  更新这两个元素的映射
        // 3. Remove 删除数组最后一个元素,以及删除对应hashmap映射
        // 实际代码可以进行一些优化,
        // 比如虽然理解上是swap,但其实不需要维护要被删除的数据,所以实际上只要更新最后一个元素的映射即可
        if (map.containsKey(key)) {
            Integer index = map.get(key);
            Entry<K, V> entry = array.get(index);
            V oldValue = entry.value;
            // swap with last element
            Entry<K, V> lastEntry = array.get(array.size() - 1);
            array.set(index, lastEntry);
            array.remove(array.size() - 1);
            // reMap
            map.put(lastEntry.key, index);
            map.remove(key);
            return oldValue;
        } else {
            return null;
        }
    }

    //endregion

    //region 新特性实现

    /**
     * 等概率地返回一个key  (一般来说pop操作是删除最后一个元素，但觉得这个名字也符合这个操作)
     */
    public K pop() {
        if (array.size() == 0) {
            return null;
        }
        int index = random.nextInt(array.size());
        return array.get(index).key;
    }

    //endregion

    //region 辅助方法
    public int size() {
        return array.size(); // 也可以用map.size()
    }

    public boolean isEmpty() {
        return array.isEmpty(); // 也可以用map.isEmpty()
    }

    //endregion

}
