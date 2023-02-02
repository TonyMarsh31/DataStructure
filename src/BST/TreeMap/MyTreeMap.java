package BST.TreeMap;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Objects;

public class MyTreeMap<K extends Comparable<K>, V> {
    private class TreeNode {
        K key;
        V val;
        TreeNode left, right;

        TreeNode(K key, V val) {
            this.key = key;
            this.val = val;
            left = right = null;
        }
    }

    private TreeNode root = null;
    private int size = 0;

    public MyTreeMap() {
    }


    //region 增 改
    public V put(K key, V val) {
        Objects.requireNonNull(key);
        V oldVal = get(key);
        root = put(root, key, val);
        return oldVal; // 更新操作返回旧值，新增操作返回null
    }

    private @NotNull TreeNode put(TreeNode node, K key, V val) {
        if (node == null) { // 递归到底，没有找到key，执行新增操作
            size++;
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
        size--;
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
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    public void removeMax() {
        root = removeMax(root);
        size--;
    }

    private TreeNode removeMax(@NotNull TreeNode node) {
        if (node.right == null) { // 找到最大节点
            TreeNode leftNode = node.left; // 注意不要直接返回null，因为可能存在左子树
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
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
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            node.left = remove(node.left, key);
            return node;
        } else if (cmp > 0) { // key > node.key
            node.right = remove(node.right, key);
            return node;
        } else { // key == node.key
            // 被删除节点仅一侧存在子树，则删除后直接返回子树做嫁接即可
            if (node.left == null) {
                // 叶子结点（left right都为null）也可以合并到这一处理方式中，不需要单独处理
                TreeNode rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            } else if (node.right == null) {
                TreeNode leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }
            /*
                 被删除节点左右子树均不为空的情况
                 为维护二分搜索树的性质，删除节点后需要找到一个合适的节点来替代被删除的节点
                 这个节点可以是被删除节点右子树的最小节点，或者是被删除节点左子树的最大节点
               */
            TreeNode successor = minNode(node.right); // 找到右子树的最小节点作为后继节点
            successor.right = removeMin(node.right); // 删除右子树的最小节点，即清理掉后继节点，然后将该子树嫁接到后继节点的右子树上
            successor.left = node.left; // 将左子树嫁接到后继节点的左子树上
            node.left = node.right = null; // 删除node
            return successor; // 返回后继节点

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
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // key < node.key
            return get(node.left, key);
        } else if (cmp > 0) { // key > node.key
            return get(node.right, key);
        } else { // key == node.key
            return node;
        }
    }

    /**
     * 获得二分搜索树中最小的键值
     *
     * @return 二分搜索树中最小的键值
     */
    public V getMin() {
        if (isEmpty()) throw new NoSuchElementException("BST is empty");
        return minNode(root).val;
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
        return size;
    }

    public boolean isEmpty() {

        return size == 0;
    }
    //endregion
}
