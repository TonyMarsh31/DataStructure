package 树.字典树;

public class MyTrieSet {

    private static class MyTrieNode {
        MyTrieNode[] next = new MyTrieNode[R];
        boolean isEnd = false;
    }

    private MyTrieNode root;
    /**
     * ASCII码表中的字符数
     */
    private static final int R = 256;
    private int size;

    public MyTrieSet() {
        root = null;
        size = 0;
    }

    public boolean add(String key) {
        check(key);
        // add方法内部已经防止重复添加了，所以不需要在这里做contains key检查进行判断
        // if (contains(key)) { return false; }
        root = add(root, key, 0);
        return true;
    }

    /**
     * 向以node为根节点的字典树中添加key，递归算法
     *
     * @param node  开始节点，在递归中表示当前节点
     * @param key   要添加的字符串
     * @param index 在递归中表示当前字符串中当前字符的索引,调用方法时传入0
     * @return 返回添加后的字典树的根节点
     */
    private MyTrieNode add(MyTrieNode node, String key, int index) {
        if (node == null) {
            node = new MyTrieNode();
        }
        if (index == key.length()) {
            if (!node.isEnd) {
                node.isEnd = true;
                size++;
            }
            return node;
        }
        char c = key.charAt(index);
        node.next[c] = add(node.next[c], key, index + 1);
        return node;
    }

    public boolean remove(String key) {
        check(key);
        if (contains(key)) {
            root = remove(root, key, 0);
            return true;
        }
        return false;
    }

    private MyTrieNode remove(MyTrieNode node, String key, int index) {
        if (node == null) {
            return null;
        }
        if (index == key.length()) {
            if (node.isEnd) {
                node.isEnd = false;
                size--;
            }
            return node;
        }
        char c = key.charAt(index);
        node.next[c] = remove(node.next[c], key, index + 1);

        // 当执行到这里时，标识符已经修改，单词已经删除了
        // 但还需要判断prefix是否还有其他单词，如果没有，就还要把prefix节点也删除
        if (node.isEnd) { //节点是其他单词，不执行额外操作
            return node;
        }
        //节点是prefix，判断这个prefix是否还有其他单词 ,没有则删除该prefix
        for (int i = 0; i < R; i++) {
            if (node.next[i] != null) {
                return node;
            }
        }
        return null;
    }

    public boolean contains(String key) {
        check(key);
        MyTrieNode node = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (node.next[c] == null) {
                return false;
            }
            node = node.next[c];
        }
        return node.isEnd;
    }

    /**
     * 字典树中是否有以prefix为前缀的单词
     *
     * @param prefix 前缀
     * @return 是否有以prefix为前缀的单词
     */
    public boolean startsWith(String prefix) {
        check(prefix);
        return getNode(prefix) != null;
    }

    /**
     * 返回字典树种key的终点Node
     *
     * @param key 要查找的key
     * @return 返回key的终点Node
     */
    private MyTrieNode getNode(String key) {
        check(key);
        MyTrieNode node = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (node.next[c] == null) {
                return null;
            }
            node = node.next[c];
        }
        return node;
    }

    public int size() {
        return size;
    }

    private static void check(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key can not be null");
        }
    }
}
