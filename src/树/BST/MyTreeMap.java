package 树.BST;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;

public class MyTreeMap<K extends Comparable<K>, V> {
    private class TreeNode {
        K key;
        V val;
        TreeNode left, right;
        int size; //记录以该节点为根的子树节点的个数(包含自己)

        TreeNode(K key, V val) {
            this.key = key;
            this.val = val;
            left = right = null;
            this.size = 1;
        }
    }

    private TreeNode root = null;

    public MyTreeMap() {
    }

    //region 扩展

/*
    这一部分的方法不在BST的标准API范围内，但是在实际应用中很有用，一些LeetCode的题目也会用到这些方法
    为了高效地实现这些方法，我们需要对BST进行一些改造，使得BST的节点中存储一些额外的信息，例如：
    1. 节点的高度
    2. 节点的子树节点的个数
*/


    /**
     * 返回小于key的键的个数
     *
     * @param key key
     * @return 小于key的键的个数
     */
    public int rank(K key) {
        Objects.requireNonNull(key);
        return rank(root, key);
    }

    /**
     * 返回以node为根的二分搜索树中小于key的键的个数
     *
     * @param node 二分搜索树的根
     * @param key  key
     * @return 小于key的键的个数
     */
    private int rank(TreeNode node, K key) {
        // 其实就是计算key节点左子树节点的个数
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            return rank(node.left, key); //左子树继续迭代
        } else if (cmp > 0) { // key > node.key
            return 1 + size(node.left) + rank(node.right, key); //该节点+左子树节点个数+右子树继续迭代
        } else { // key == node.key
            return size(node.left); //最优情况key存在，直接返回key节点左子树节点个数
        }
    }

    /**
     * 返回索引为i的键，注意索引从0开始计算。
     * <p>由于BST的有序性，可以将其中序遍历的结果看作是一个有序数组，这样就可以通过索引来访问BST中的元素</p>
     * <p>可以将select函数理解为rank函数的镜像，rank将key映射为index，select将index映射为key</p>
     *
     * @param i 索引 | 排名
     * @return 索引为i的键
     */
    public K select(int i) {
        if (i < 0 || i >= size()) {
            throw new IllegalArgumentException("索引越界");
        }
        return select(root, i).key;
    }

    /**
     * 返回以node为根的二分搜索树中索引为i的节点
     *
     * @param node 二分搜索树的根
     * @param i    索引
     * @return 索引为i的节点
     */
    private TreeNode select(TreeNode node, int i) {
        int currentIndex = size(node.left);
        if (i < currentIndex) {
            return select(node.left, i); //左子树继续迭代
        } else if (i > currentIndex) {
            return select(node.right, i - currentIndex - 1);
            //右子树继续迭代，根节点发生了变化, 为保持逻辑一致，新的index应当是i减去左子树节点个数和当前节点
        } else {
            return node; //找到了
        }
    }


    //endregion

    //region 增 改
    public V put(K key, V val) {
        Objects.requireNonNull(key);
        V oldVal = get(key);
        root = put(root, key, val);
        return oldVal; // 更新操作返回旧值，新增操作返回null
    }

    private @NotNull TreeNode put(TreeNode node, K key, V val) {
        if (node == null) { // 递归到底，没有找到key，执行新增操作
            return new TreeNode(key, val);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            node.left = put(node.left, key, val);
        } else if (cmp > 0) { // key > node.key
            node.right = put(node.right, key, val);
        } else { // key == node.key
            node.val = val; // 更新值
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    //endregion

    //region 删
    /* MinNode 在最左，MaxNode在最右
     * 注意两者是从root开始一直向左和右遍历，而这两个节点本身并不一定是最深的节点（与深度无关） */

    /**
     * 删除二分搜索树中的最小节点，并返回删除后的新的二分搜索树的根
     */
    public void removeMin() {
        root = removeMin(root);
    }

    /**
     * 删除以node为根的二分搜索树中的最小节点，并返回删除后的新的二分搜索树的根，注意本方法内部已经修改了size
     *
     * @param node 二分搜索树的根
     * @return 删除后的新的二分搜索树的根
     */
    private TreeNode removeMin(@NotNull TreeNode node) {
        if (node.left == null) { // 找到最小节点
            TreeNode rightNode = node.right; // 注意不要直接返回null，因为可能存在右子树
            node.right = null;
            return rightNode;
        }
        node.left = removeMin(node.left);
        // 维护每个Node的size
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void removeMax() {
        root = removeMax(root);
    }

    private TreeNode removeMax(@NotNull TreeNode node) {
        if (node.right == null) { // 找到最大节点
            TreeNode leftNode = node.left; // 注意不要直接返回null，因为可能存在左子树
            node.left = null;
            return leftNode;
        }
        node.right = removeMax(node.right);
        // 维护每个Node的size
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public V remove(K key) {
        Objects.requireNonNull(key);
        if (!containsKey(key)) return null;

        V oldVal = get(key);
        root = remove(root, key);
        return oldVal;
    }

    private TreeNode remove(TreeNode node, K key) {
        Objects.requireNonNull(node);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            node.left = remove(node.left, key);
        } else if (cmp > 0) { // key > node.key
            node.right = remove(node.right, key);
        } else { // key == node.key
            // 被删除节点仅一侧存在子树，则删除后直接返回子树做嫁接即可
            if (node.left == null) { // 叶子结点（left right都为null）也可以合并到这一处理方式中，不需要单独处理
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            /*
                 被删除节点左右子树均不为空的情况
                 为维护二分搜索树的性质，删除节点后需要找到一个合适的节点来替代被删除的节点
                 这个节点可以是被删除节点右子树的最小节点，或者是被删除节点左子树的最大节点
               */
            TreeNode successor = minNode(node.right); // 找到右子树的最小节点作为后继节点
            successor.right = removeMin(node.right); // 删除右子树的最小节点，即清理掉后继节点，然后将该子树嫁接到后继节点的右子树上
            successor.left = node.left; // 将左子树嫁接到后继节点的左子树上
            node = successor; // 将后继节点替换被删除节点

/*
            比起操作Node的引用做子树嫁接操作，我们也可以直接操作Node的值，例如：

            TreeNode successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            removeMin(node.right); //remove successor

            原先的方式是处理子树嫁接到新的后继节点，然后将后继节点的引用替换被删除节点，
            而第二种方式是将后继节点的值(的引用) 直接赋值给被删除节点，然后删除后继节点。
            第二种方式更容易让人理解，

            (在java语言中)两种方式在逻辑上是等价的，且性能上也没有差别。
            但在其他的一些语言中，进行数据搬移的代价可能会比处理引用/指针的更高
            在value结构很复杂的情况下，使用第二种方法可能效率会降低
*/
        }
        // 维护每个Node的size
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    //endregion

    //region 查
    public V get(K key) {
        Objects.requireNonNull(key);
        TreeNode x = get(root, key);
        return x == null ? null : x.val;
    }

    private TreeNode get(TreeNode node, K key) {
        if (node == null) {
            return null; // base case 1: 没找到返回null
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            return get(node.left, key);
        } else if (cmp > 0) { // key > node.key
            return get(node.right, key);
        } else { // key == node.key
            return node; //base case 2: 找到了返回node
        }
    }


/*
    floor 和 celling 的实现思路就类似于get方法的思路
    最优情况下是key存在于树中，此时floor和celling就是key本身,代码逻辑就是get的逻辑
    而关键就在于当找不到key的时候，此时不返回null，而是返回当前正在递归访问的node，该node就是floor或者celling
*/

    /**
     * 返回最接近且小于等于key的key
     *
     * @param key key
     * @return 最接近且小于等于key的节点的key
     */
    public K floorKey(K key) {
        Objects.requireNonNull(key);
        TreeNode x = floorNode(root, key);
        return x == null ? null : x.key;
    }

    /**
     * 返回最接近且小于等于key的节点
     *
     * @param node 当前节点
     * @param key  key
     * @return 最接近且小于等于key的节点
     */
    private TreeNode floorNode(TreeNode node, K key) {
        if (node == null) {
            return null; //
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            // 当前节点仍然大于key,继续递归
            return floorNode(node.left, key); //若最终递归完毕返回null，则说明最小的节点仍大于key，不存在floor节点
        }
        if (cmp > 0) { // key > node.key
            TreeNode t = floorNode(node.right, key);
            // 当前节点已经小于key,可能是floor节点
            // 如果右子树中仍存在节点，则继续递归
            // 否则便说明，该节点就是floor节点
            return t == null ? node : t;
        } else {
            return node; //最优情况，key存在于树中，此时的floor就是他自己
        }
    }

    /**
     * 返回最接近且大于等于key的key
     *
     * @param key key
     * @return 最接近且大于等于key的节点的key
     */
    public K cellingKey(K key) {
        Objects.requireNonNull(key);
        TreeNode x = cellingNode(root, key);
        return x == null ? null : x.key;
    }

    /**
     * 返回最接近且大于等于key的节点
     *
     * @param node 当前节点
     * @param key  key
     * @return 最接近且大于等于key的节点
     */
    private TreeNode cellingNode(TreeNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0) { // key > node.key
            // 当前节点仍然小于key,继续递归
            return cellingNode(node.right, key); //若最终递归完毕返回null，则说明最大的节点仍小于key，不存在celling节点
        }
        if (cmp < 0) { // key < node.key
            TreeNode t = cellingNode(node.left, key);
            // 当前节点已经大于key,可能是celling节点
            // 如果左子树中仍存在节点，则继续递归
            // 否则便说明，该节点就是celling节点
            return t == null ? node : t;
        } else {
            return node; //最优情况，key存在于树中，此时的celling就是他自己
        }
    }

    /**
     * 从小到大返回二分搜索树中所有的key
     *
     * @return 二分搜索树中所有的key
     */
    public Iterable<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        traverse(root, keys);
        return keys;
    }

    /**
     * 从小到大返回二分搜索树中指定范围内的key
     *
     * @param min 最小阈值
     * @param max 最大阈值
     * @return 指定范围内的key
     */
    public Iterable<K> keys(K min, K max) {
        LinkedList<K> keys = new LinkedList<>();
        traverse(root, keys, min, max);
        return keys;
    }


    /**
     * 返回二分搜索树中最小的key
     *
     * @return 二分搜索树中最小的key
     */
    public K minKey() {
        return minNode(root).key;
    }

    /**
     * 返回二分搜索树中最大的key
     *
     * @return 二分搜索树中最大的key
     */
    public K maxKey() {
        return maxNode(root).key;
    }

    /**
     * 返回以node为根的二分搜索树的最小值所在的节点
     *
     * @param node 二分搜索树的根
     * @return 以node为根的二分搜索树的最小值所在的节点
     */
    private TreeNode minNode(TreeNode node) {
        if (node.left == null) {
            return node;
        }
        return minNode(node.left);
    }

    /**
     * 返回以node为根的二分搜索树的最大值所在的节点
     *
     * @param node 二分搜索树的根
     * @return 以node为根的二分搜索树的最大值所在的节点
     */
    private TreeNode maxNode(TreeNode node) {
        if (node.right == null) {
            return node;
        }
        return maxNode(node.right);
    }
    //endregion

    //region 工具函数
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    public int size() {
        return size(root);
    }

    /**
     * 返回以node为根的二分搜索树的节点个数
     *
     * @param node 二分搜索树的根
     * @return 以node为根的二分搜索树的节点个数
     */
    private int size(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 二分搜索树keys的中序遍历
     *
     * @param node 根节点
     * @param list 保存遍历结果 ,在BST中，中序遍历的结果就是从小到大的顺序
     */
    private void traverse(TreeNode node, LinkedList<K> list) {
        if (node == null) {
            return;
        }
        traverse(node.left, list);
        list.add(node.key);
        traverse(node.right, list);
    }

    /**
     * 二分搜索树指定部分的中序遍历
     *
     * @param node 根节点
     * @param list 保存遍历结果
     * @param min  最小阈值
     * @param max  最大阈值
     */
    private void traverse(TreeNode node, LinkedList<K> list, K min, K max) {
        if (node == null) {
            return;
        }
        int cmpMin = min.compareTo(node.key);
        int cmpMax = max.compareTo(node.key);

/*
        在下面这种实现中，还是进行中序遍历，只是只有当节点的key在[min,max]范围内时，才将其加入到keys中

        traverse(node.left, keys);
        if (cmpMin <= 0 && cmpMax >= 0) {
            keys.add(node.key);
        }
        traverse(node.right, keys);

        这样做虽然可以，但毕竟还是遍历了整棵树，效率并不高
        其实只要当min > node时，就不需要再遍历node的左子树了，因为左子树中的所有节点只会比node更小
        max也是同理,所以代码可以优化为下面这种形式
*/
        // 其实就是在中序遍历的基础上进一步加一些条件判断，减少非必要的递归，避免遍历整棵树
        if (cmpMin < 0) { // min < node.key , 如果这个条件都不满足，那么就不需要再遍历node的左子树了，因为左子树中的所有节点只会比node更小
            traverse(node.left, list, min, max);
        }
        if (cmpMin <= 0 && cmpMax >= 0) {
            list.add(node.key);
        }
        if (cmpMax > 0) { // max > node.key
            traverse(node.right, list, min, max);
        }
    }

    /**
     * 校验二分搜索树
     *
     * @param root 二分搜索树的根节点
     * @return 是否是二分搜索树
     */
    public boolean isValidBST(TreeNode root) {
        /*
         一种直接的方法是中序遍历，然后判断遍历结果是否是升序的,但是这种方法需要遍历完整个树后才能有结果
         我们可以在遍历的过程中，直接对每一个节点进行判断，如果发现不满足条件，就可以直接返回false了
         这样如果不是二分搜索树，就可以提前结束遍历，节省时间
        */
        return isValidBST(root, null, null);
    }

    private boolean isValidBST(TreeNode node, K low, K high) {
        if (node == null) return true;

        //如果有任意一个节点不满足条件，那么就不是二分搜索树
        if (low != null && low.compareTo(node.key) > 0) return false;
        if (high != null && high.compareTo(node.key) < 0) return false;

        //递归迭代整个树 , 递归的过程中，不断更新low和high的值，左子树更新high，右子树更新low
        return isValidBST(node.left, low, node.key) && isValidBST(node.right, node.key, high);
/*
        上面这行代码是简写的形式，用if条件判断表示是下面的形式
        if (!isValidBST(root.left, low, root.key)) return false;
        if (!isValidBST(root.right, root.key, high)) return false;
        return true;
*/
    }

//endregion
}
