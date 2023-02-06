package 树.字典树;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTrieSet {

    //region test case
    public static void main(String[] args) {
        MyTrieSet set = new MyTrieSet();
        set.add("a");
        set.add("ab");
        set.add("abc");
        set.add("abcd");
        set.add("bbcd");
        set.add("cbcd");

        System.out.println(set.shortestPrefixOf("abcdef")); // a
        System.out.println(set.shortestPrefix("abcdef")); // a
        System.out.println(set.longestPrefixOf("abcdef")); // abcd
        System.out.println(set.longestPrefix("abcdef")); // abcd

        System.out.println(set.startsWith("ab")); // true
        System.out.println(set.startsWith("ba")); // false

        System.out.println(set.matches("a.c")); // true
        System.out.println(set.matches("axy")); // false
        System.out.println(set.matches("b.")); //false

        List<String> keyLists;
        keyLists = set.keysWithPrefixOf("ab");
        System.out.println(Arrays.toString(keyLists.toArray())); // [ab, abc, abcd]

        keyLists = set.keysThatMatch(".bcd");
        System.out.println(Arrays.toString(keyLists.toArray())); // [abcd, bbcd, cbcd]

        keyLists = set.keysWithPrefixOf("ab");
        System.out.println(Arrays.toString(keyLists.toArray()));

        keyLists = set.keysThatMatch(".bcd");
        System.out.println(Arrays.toString(keyLists.toArray()));
    }
    //endregion

    //region 数据结构
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

    //endregion

    //region set相关API
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

    public int size() {
        return size;
    }

    private static void check(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key can not be null");
        }
    }
    //endregion

    //region 字典树特性API

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

    /**
     * 寻找给定key在字典树中的最短(公共)前缀
     *
     * @param key key
     * @return 最短前缀
     */
    public String shortestPrefixOf(String key) {
        check(key);
        int index = shortestPrefixOf(root, key, 0, 0);
        return key.substring(0, index);
    }

    /**
     * 寻找给定key在字典树中的最长(公共)前缀
     *
     * @param key key
     * @return 最长前缀
     */
    public String longestPrefixOf(String key) {
        check(key);
        int index = longestPrefixOf(root, key, 0, 0);
        return key.substring(0, index);
    }

    /**
     * 使用非递归的方式实现
     */
    private String shortestPrefix(String key) {
        check(key);
        MyTrieNode p = root; // p stands for present
        int i = 0;
        while (i <= key.length() && p != null) {
            if (p.isEnd) {
                return key.substring(0, i);
                // substring的区间是 [0, i) 左闭右开 ,正好符合我们的要求,因为前缀是不包含单词本身的，
                // 在边界条件判断中，如果最短前缀是单词本身，那么此时index = key.length()，仍正常返回
            }
            char c = key.charAt(i);
            p = p.next[c];
            i++;
        }
        return ""; // 没有找到
    }

    private String longestPrefix(String key) {
        // 多维护了一个length变量，用来记录当前最长前缀的长度
        // 转换一下视角，其实也算是一种双指针的思路
        check(key);
        MyTrieNode p = root; // p stands for present
        int i = 0;
        int length = 0;
        while (i <= key.length() && p != null) {
            if (p.isEnd) {
                length = i;
            }
            char c = key.charAt(i);
            p = p.next[c];
            i++;
        }
        return key.substring(0, length);
    }

    /**
     * 递归寻找给定key的最短前缀
     *
     * @param node   当前节点
     * @param key    key
     * @param index  当前key的字符索引,从0开始
     * @param length 当前最短前缀的长度
     * @return 最短前缀的长度
     */
    private int shortestPrefixOf(MyTrieNode node, String key, int index, int length) {
        /*方法参数length其实可以省略，因为其恒等于给定时的0，
            但是为了和下面的longestPrefixOf保持一致，所以这里还是保留了length参数
            （两个方法结构一样，只有一行代码不同）*/
        if (node == null) {
            return length;
        }
        if (node.isEnd) {
            return index;
        }
        if (index == key.length()) {
            return length;
        }
        /*注意：虽然这个条件判断的方法体和上面的if (node.isEnd) {}一样，但是不能合并
         即条件判断的执行顺序需要严格保持 1.是否null  2. 是否end   3.是否结束迭代了key
         如果把 3 提前和 1 一起做 || 判断，则会在shortestPrefix就是自身的边界情况中 出现错误*/
        char c = key.charAt(index);
        return shortestPrefixOf(node.next[c], key, index + 1, length);

    }

    private int longestPrefixOf(MyTrieNode node, String key, int index, int length) {
        /*观察本方法与shortestPrefixOf的区别，其实所修改的代码只有一行
            就是当node.isEnd时，不返回index，而是更新length，
            直到遍历完key或者node为null时返回length */
        if (node == null) {
            return length;
        }
        if (node.isEnd) {
            length = index;
        }
        if (index == key.length()) {
            return length;
        }
        char c = key.charAt(index);
        return longestPrefixOf(node.next[c], key, index + 1, length);
    }

    /**
     * 返回字典树中所有以prefix为前缀的单词
     *
     * @param prefix 前缀
     * @return 所有以prefix为前缀的单词
     */
    public List<String> keysWithPrefixOf(String prefix) {
        check(prefix);
        List<String> list = new ArrayList<>();
        MyTrieNode node = getNode(prefix);
        if (node == null) { //不存在该前缀，返回一个空的list
            return list;
        }
        keysWithPrefixOf(node, prefix, list);
        return list;
    }

    private void keysWithPrefixOf(MyTrieNode node, String prefix, List<String> list) {
        if (node == null) {
            return;
        }
        if (node.isEnd) {
            list.add(prefix);
        }
        for (char i = 0; i < R; i++) {
            // i是char类型，在next[i]表达式中,i会自动转换为int类型
            // 但在prefix + i操作中，会自动转换为String类型 ,做的是字符串拼接
            keysWithPrefixOf(node.next[i], prefix + i, list);

            // 一般涉及到大量的字符串拼接时，使用StringBuilder会更高效
            // 这里为了代码清晰没有这么做，具体可以参考另一个方法中的实现
        }
    }

    /**
     * 使用StringBuilder优化keysWithPrefixOf方法
     */
    private void keysWithPrefixOf2(MyTrieNode node, String prefix, List<String> list) {
        if (node == null) {
            return;
        }
        if (node.isEnd) {
            list.add(prefix); //这个prefix也是一个单词，加入list
        }
        StringBuilder sb = new StringBuilder(prefix);
        for (char i = 0; i < R; i++) {
            sb.append(i);
            keysWithPrefixOf(node.next[i], sb.toString(), list);
            sb.deleteCharAt(sb.length() - 1);
            // 递归结束后，需要删除最后一个字符，这样下一次的循环才能拼接正确的字符
            // 正常递归为 …… aa ab ac ad …… az ……  如果没有删除操作则变成了 …… aa aab aabc aabcd ……
            /*
                值得一提的是，这三行代码其实是多叉树的一种通用遍历方式
                即 1.做出选择：指定递归方向
                2. 开始递归，遍历该方向的子树
                3. 撤销选择：从而让循环体可以遍历其他方向的子树
                */
        }
    }


    /**
     * 返回字典树中所有匹配pattern的单词
     *
     * @param pattern 通配符，仅支持.和字母，.表示任意字母
     * @return 所有匹配pattern的单词
     */
    public List<String> keysThatMatch(String pattern) {
        check(pattern);
        List<String> list = new ArrayList<>();
        keysThatMatch(root, "", pattern, list);
        return list;
    }

    /**
     * 递归搜索所有匹配pattern的单词
     *
     * @param node    本次搜索的起始节点
     * @param prefix  本次搜索匹配单词的前缀，根据通配符不断更新
     * @param pattern 搜索的通配符
     * @param list    结果集
     */
    private void keysThatMatch(MyTrieNode node, String prefix, String pattern, List<String> list) {
        /*
         主要的思路就是 不断更新prefix,直至满足pattern,然后判断该prefix是否为一个单词
         prefix初始值为空字符串，不断参照pattern以字符为单位更新，如果是通配符，那么就要迭代所有的字符
         直到最终prefix的长度等于pattern的长度，此时该prefix就是满足pattern的一种情况，
         然后判断prefix是否为一个单词
         */
        if (node == null) {
            // base case 1: 递归到底了
            return;
        }
        int pLength = prefix.length();
        if (pLength == pattern.length()) {
            // base case 2: prefix符合pattern，同时该prefix是一个单词
            // base case 3: prefix符合pattern，但该prefix不是一个单词
            // 2 和 3 的区别在于有没有通过下面的条件判断
            if (node.isEnd) {
                list.add(prefix);
            }
            return;
        }

        // 参照pattern 更新prefix
        char c = pattern.charAt(pLength);
        if (c == '.') {
            for (char i = 0; i < R; i++) { //如果是通配符，递归遍历所有的子树
                keysThatMatch(node.next[i], prefix + i, pattern, list); //这里的 prefix + i 是字符串拼接
            }
        } else { //否则递归pattern中指定的字符子树
            keysThatMatch(node.next[c], prefix + c, pattern, list);
        }

        /*同样的，可以使用StringBuilder来优化字符串拼接，但是懒得写了*/
    }

    /**
     * 给定一个pattern，返回字典树中是否存在匹配的单词
     *
     * @param pattern 通配符，仅支持.和字母，.表示任意字母
     * @return 是否存在匹配的单词
     */
    public boolean matches(String pattern) {
        check(pattern);
        return matches(root, pattern, 0);
    }

    private boolean matches(MyTrieNode node, String pattern, int index) {
        if (node == null) {
            return false;
        }
        if (index == pattern.length()) {
            return node.isEnd;
        }
        char c = pattern.charAt(index);
        if (c == '.') {
            for (char i = 0; i < R; i++) {
                if (matches(node.next[i], pattern, index + 1)) {
                    return true; // 只要存在任意一个匹配的就返回true，否则最终会执行到最后一行返回false
                }
            }
        } else {
            return matches(node.next[c], pattern, index + 1);
        }
        return false;
    }

    //endregion

}

