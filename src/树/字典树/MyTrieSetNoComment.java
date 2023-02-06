package 树.字典树;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTrieSetNoComment {
    public static void main(String[] args) {
        MyTrieSetNoComment set = new MyTrieSetNoComment();
        set.add("a");
        set.add("ab");
        set.add("abc");
        set.add("abcd");
        set.add("bbcd");
        set.add("cbcd");

        System.out.println(set.shortestPrefixOf("abcdef"));
        System.out.println(set.shortestPrefix("abcdef"));
        System.out.println(set.longestPrefixOf("abcdef"));
        System.out.println(set.longestPrefix("abcdef"));

        System.out.println(set.startsWith("ab"));
        System.out.println(set.startsWith("ba"));

        System.out.println(set.matches("a.c"));
        System.out.println(set.matches("axy"));
        System.out.println(set.matches("b."));

        List<String> keyLists;
        keyLists = set.keysWithPrefixOf("ab");
        System.out.println(Arrays.toString(keyLists.toArray()));

        keyLists = set.keysThatMatch(".bcd");
        System.out.println(Arrays.toString(keyLists.toArray()));

        keyLists = set.keysWithPrefixOf("ab");
        System.out.println(Arrays.toString(keyLists.toArray()));

        keyLists = set.keysThatMatch(".bcd");
        System.out.println(Arrays.toString(keyLists.toArray()));
    }

    private static class MyTrieNode {
        MyTrieNode[] next = new MyTrieNode[R];
        boolean isEnd = false;
    }

    private MyTrieNode root;
    private static final int R = 256;
    private int size;

    public MyTrieSetNoComment() {
        root = null;
        size = 0;
    }

    public boolean add(String key) {
        check(key);
        root = add(root, key, 0);
        return true;
    }

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

        if (node.isEnd) {
            return node;
        }
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

    public boolean startsWith(String prefix) {
        check(prefix);
        return getNode(prefix) != null;
    }

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

    public String shortestPrefixOf(String key) {
        check(key);
        int index = shortestPrefixOf(root, key, 0, 0);
        return key.substring(0, index);
    }

    public String longestPrefixOf(String key) {
        check(key);
        int index = longestPrefixOf(root, key, 0, 0);
        return key.substring(0, index);
    }

    private String shortestPrefix(String key) {
        check(key);
        MyTrieNode p = root;
        int i = 0;
        while (i <= key.length() && p != null) {
            if (p.isEnd) {
                return key.substring(0, i);


            }
            char c = key.charAt(i);
            p = p.next[c];
            i++;
        }
        return "";
    }

    private String longestPrefix(String key) {
        check(key);
        MyTrieNode p = root;
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

    private int shortestPrefixOf(MyTrieNode node, String key, int index, int length) {
        if (node == null) {
            return length;
        }
        if (node.isEnd) {
            return index;
        }
        if (index == key.length()) {
            return length;
        }
        char c = key.charAt(index);
        return shortestPrefixOf(node.next[c], key, index + 1, length);
    }

    private int longestPrefixOf(MyTrieNode node, String key, int index, int length) {
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

    public List<String> keysWithPrefixOf(String prefix) {
        check(prefix);
        List<String> list = new ArrayList<>();
        MyTrieNode node = getNode(prefix);
        if (node == null) {
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
            keysWithPrefixOf(node.next[i], prefix + i, list);
        }
    }

    private void keysWithPrefixOf2(MyTrieNode node, String prefix, List<String> list) {
        if (node == null) {
            return;
        }
        if (node.isEnd) {
            list.add(prefix);
        }
        StringBuilder sb = new StringBuilder(prefix);
        for (char i = 0; i < R; i++) {
            sb.append(i);
            keysWithPrefixOf(node.next[i], sb.toString(), list);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public List<String> keysThatMatch(String pattern) {
        check(pattern);
        List<String> list = new ArrayList<>();
        keysThatMatch(root, "", pattern, list);
        return list;
    }

    private void keysThatMatch(MyTrieNode node, String prefix, String pattern, List<String> list) {
        if (node == null) {
            return;
        }
        int pLength = prefix.length();
        if (pLength == pattern.length()) {
            if (node.isEnd) {
                list.add(prefix);
            }
            return;
        }
        char c = pattern.charAt(pLength);
        if (c == '.') {
            for (char i = 0; i < R; i++) {
                keysThatMatch(node.next[i], prefix + i, pattern, list);
            }
        } else {
            keysThatMatch(node.next[c], prefix + c, pattern, list);
        }
    }

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
                    return true;
                }
            }
        } else {
            return matches(node.next[c], pattern, index + 1);
        }
        return false;
    }


}

