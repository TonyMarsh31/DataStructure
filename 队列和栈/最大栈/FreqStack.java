package 队列和栈.最大栈;

import java.util.HashMap;
import java.util.Stack;

public class FreqStack<V> {
    private int maxFreq;
    private final HashMap<V, Integer> valToFreq = new HashMap<>();
    private final HashMap<Integer, Stack<V>> freqToVars = new HashMap<>();

    public void push(V val) {
        // 更新频率，以新频率存入valToFreq
        int freq = valToFreq.getOrDefault(val, 0) + 1;
        valToFreq.put(val, freq);
        // 更新最大频率
        maxFreq = Math.max(maxFreq, freq);
        // 更新freqToVars
        freqToVars.putIfAbsent(freq, new Stack<>());
        freqToVars.get(freq).push(val);
    }

    public V pop() {
        // 取值
        Stack<V> vars = freqToVars.get(maxFreq);
        V val = vars.pop();
        // 更新数据
        valToFreq.put(val, maxFreq - 1);
        if (vars.isEmpty()) maxFreq--; //vars自己会被GC
        return val;
    }
}
