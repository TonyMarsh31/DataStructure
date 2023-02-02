package BST.TreeMap;

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


    public V put(K key, V val) {
        Objects.requireNonNull(key);
        V oldVal = get(key);
        root = put(root, key, val);
        return oldVal; // 更新操作返回旧值，新增操作返回null
    }

    private TreeNode put(TreeNode node, K key, V val) {
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


    /* MinNode 在最左，MaxNode在最右
     * 注意两者是从root开始一直向左和右遍历，而这两个节点本身并不一定是最深的节点（与深度无关） */
    public void removeMin() {
        root = removeMin(root);
        size--;
    }

    private TreeNode removeMin(TreeNode node) {
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

    private TreeNode removeMax(TreeNode node) {
        if (node.right == null) { // 找到最大节点
            TreeNode leftNode = node.left; // 注意不要直接返回null，因为可能存在左子树
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
        return node;
    }

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

    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {

        return size == 0;
    }
}
