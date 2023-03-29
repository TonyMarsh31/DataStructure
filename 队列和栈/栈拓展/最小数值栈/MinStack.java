package 队列和栈.栈拓展.最小数值栈;

import java.util.Stack;

/**
 * <P>空间换时间，另开一个stack来记录对应值到栈底范围内的最小值</P>
 * <p>优化：另开的用于保存最小值的stack可能会存储重复元素，使用一些方法可以优化这一点</p>
 *
 * @param <T>
 */
public class MinStack<T extends Comparable<T>> {
    Stack<T> data = new Stack<>();
    Stack<T> minStk = new Stack<>();

    public T min() {
        return minStk.peek();
    }

    public void push(T val) {
        data.push(val);
        if (minStk.isEmpty() || val.compareTo(minStk.peek()) <= 0) {
            minStk.push(val);
        } else minStk.push(minStk.peek()); // 在这里minStk会存储重复元素
    }

    public T pop() {
        minStk.pop(); // 存储重复元素后，这里逻辑是直接弹出即可
        return data.pop();
    }

    public void pushImproved(T val) {
        data.push(val);
        // 同步minStk中的数据
        if (minStk.isEmpty() || val.compareTo(minStk.peek()) <= 0) {
            minStk.push(val);
        } //优化版本中 ，不存储重复元素
    }

    public T popImproved() {
        T val = data.pop();
        // 同步minStk中的数据
        if (val == minStk.peek()) { // 但是弹出时要检查，如果相等才弹出
            minStk.pop();
        }
        return val;
    }

    public T top() {
        return data.peek();
    }
}
