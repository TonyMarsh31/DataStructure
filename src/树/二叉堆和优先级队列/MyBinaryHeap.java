package 树.二叉堆和优先级队列;

import java.util.ArrayList;

/**
 * 二叉堆实现：最大堆(每个节点都大于其两个字节点)，index0不存储数据，从index1开始存储数据
 */
public class MyBinaryHeap<T extends Comparable<T>> {
    /*
        在BST中，用链表来存储数据，使用指针操作每一个树节点
        而二叉堆在逻辑上可以视为一个完全二叉树，只是在底层使用数组来存储数据，而不是使用链表
        我们直接使用数组索引来操作每一个树节点，索引的计算原理可参考附件图示

        图示中不在index0处存储数据，而是从index1开始存储数据,这样计算索引更加方便,java标准库也是这么做的
        但在本实现中，为了方便我们使用ArrayList来存储数据，所以不得不在index0处存储数据

        若从index1开始存储数据，那么计算索引的公式为：
        parentIndex = index / 2;
        leftChildIndex = index * 2;
        rightChildIndex = index * 2 + 1;

        从index0开始存储数据，计算索引的公式为：
        parentIndex = (index - 1) / 2;
        leftChildIndex = index * 2 + 1;
        rightChildIndex = index * 2 + 2;
    */

    private final ArrayList<T> data;

    public MyBinaryHeap() {
        data = new ArrayList<>();
    }

    /**
     * 计算节点在数据中的索引
     *
     * @param index
     * @return
     */
    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private int getRightChildIndex(int index) {
        return index * 2 + 2;
    }

    /**
     * 获取节点数据
     *
     * @param index
     * @return
     */
    private T parent(int index) {
        return data.get(getParentIndex(index));
    }

    private T leftChild(int index) {
        return data.get(getLeftChildIndex(index));
    }

    private T rightChild(int index) {
        return data.get(getRightChildIndex(index));
    }

    /**
     * 检查是否存在父节点或左右子节点
     *
     * @param index
     * @return
     */
    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < data.size();
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < data.size();
    }

    /**
     * 交换两个节点的数据
     *
     * @param index1
     * @param index2
     */
    private void swap(int index1, int index2) {
        T temp = data.get(index1);
        data.set(index1, data.get(index2));
        data.set(index2, temp);
    }

    public T peek() {
        if (data.size() == 0) {
            throw new IllegalStateException("堆中没有数据");
        }
        return data.get(0);
    }


    public void add(T element) {
        data.add(element);
        int index = data.size() - 1;
        swim(index);
    }

    public T poll() {
        // 删除栈顶的操作，底层是将栈顶元素和最后一个元素交换，然后删除最后一个元素
        // (这是因为底层是一个数组，这样做可以不用数据搬移）
        // 接着，同样为了维护二叉堆的性质，交换后的新栈顶需要向下调整到合适的位置
        if (data.size() == 0) {
            throw new IllegalStateException("堆中没有数据");
        }

        T deletedValue = data.get(0);
        //删除操作，先调换，再删除
        data.set(0, data.get(data.size() - 1));
        data.remove(data.size() - 1);

        sink(); // 将新的栈顶元素向下调整到合适的位置
        return deletedValue;
    }


    /**
     * 维护二叉堆的性质，加入元素后,需要向上调整到合适的位置
     *
     * @param index 被调整元素的Index
     */
    private void swim(int index) {
        // 如果存在父节点，并且父节点小于当前节点，那么交换父节点和当前节点的数据
        while (hasParent(index) && parent(index).compareTo(data.get(index)) < 0) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }
    /*
        两个维护操作中都有while语句，好像为了维护二叉堆的性质，需要很大的开销
        但我们的向上调整和向下调整的幅度最多也就整棵树的高度，所以时间复杂度还是log级别的
    */

    /**
     * 维护二叉堆的性质,删除元素后，新的栈顶元素需要向下调整到合适的位置
     * <p>固定是在删除操作后将栈顶元素下沉，所以没有设定方法参数</p>
     */
    private void sink() {
        // 如果存在子节点，并且子节点大于当前节点，那么交换子节点和当前节点的数据
        int index = 0; // 将栈顶元素下沉
        while (hasLeftChild(index)) {
            int biggerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) > 0) {
                biggerChildIndex = getRightChildIndex(index);
            }
            if (data.get(index).compareTo(data.get(biggerChildIndex)) > 0) {
                break;
            } else {
                swap(index, biggerChildIndex);
            }
            index = biggerChildIndex;
        }
    }

}
