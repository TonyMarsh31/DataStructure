如果要使用List,(如果明确不需要多次扩容)缺省使用ArrayList,
如果要使用Queue,就用ArrayDeque

因为LinkedList 不管是作为List来用还是作为Queue来用性能(往往,可能)都比不上ArrayList和ArrayDeque

## List

### List：插入

1.头插 linkedlist 优于 arrayList
2.中间插以及 尾部插入 LinkedList 劣于arraylist
3.但只要发送了扩容操作，那么 这一次的效率 一定是linkedlist 优于arraylist

2的解释： linkedlist做插入要获得前一个节点，所以要遍历，而arraylist是直接通过下标获得，所以效率高
即使是在尾差，java对linkedlist做了优化自动从尾部向前遍历，但是linkedlist的效率还是不如arraylist
3的解释，一旦涉及扩容，那么就要搬移整个数据，但这只是一次性操作，平均下来的花arraylist的效率还是很高

### List：删除

1.根据索引删除
2.根据object删除，需要遍历查找该Object
对于arraylist来说，两者都是O(n)的复杂度 ，因为都要进行数组重组，删除元素的位置越靠前，代价越大

对于linkedlist来说，有4种常用的删除
1.根据index删除
2.直接删除object
3/4 removeFirst / removeLast
1,2是差不多的，都是O(n)的复杂度，因为都要遍历查找
3和4 是常数级复杂度
LinkedList 在删除比较靠前和比较靠后的元素时，非常高效，但如果删除的是中间位置的元素，效率就比较低了。

做比较：
从集合头部删除元素时，ArrayList 花费的时间比 LinkedList 多很多
从集合尾部删除元素时，ArrayList 花费的时间比 LinkedList 少一点。
从集合中间位置删除元素时，ArrayList 花费的时间比 LinkedList 少很多；

### List:遍历

实际场景中，集合遍历通常有两种做法，一种是使用 for 循环，一种是使用迭代器（Iterator）。
如果使用的是 fori 循环，那么linkedlist的效率非常差，因为每次都要从头开始遍历
而arraylist效率不会收到影响
所以要记住，遍历linkedlist的时候，千万不要使用 for 循环，而是要使用有进行优化的迭代器。
在使用迭代器的场景下，两个List的效率差不多，但是如果使用for循环，那么linkedlist的效率就会很低

### 小总结

只有对头部区域的元素进行操作时，Arraylist的性能劣于LinkedList。
如果明确不需要多次扩容，多数场景下，可以缺省使用ArrayList

反之如果明确有需求，要在一次迭代中对List中的多个项目执行处理，  
那么 LinkedList 的开销比 ArrayList 使用时多次复制数组元素的开销要小。

## Queue:(Deque)

关于ArrayDeque, 插入和删除操作直接写成O(1)是不合适的，
大部分情况下都是O(1)的复杂度， 只有在扩容和缩容的时候才是O（n）的时间复杂度, 所以均摊复杂度是O (1)，

如果用Linkedlist作为Queue,那么只会有从头或者尾对元素进行操作，
但因为Queue的使用场景中只需要对头尾元素进行操作，所以可以认为其所有操作是O(1)的复杂度

Linkedlist虽然不需要考虑扩容，但Linkedlist在enqueue时，创建node节点(类加载与对象创建)，与一些指针操作也是需要额外消耗的
从速度的角度：ArrayDeque 基于数组实现双端队列，而 LinkedList 基于双向链表实现双端队列，数组采用连续的内存地址空间，通过下标索引访问，
链表是非连续的内存地址空间，通过指针访问，所以在寻址方面数组的效率高于链表。

综上，还是说当没有多次扩容需要时，ArrayDeque是优于Linkedlist做队列的
何况实际场景中，队列的长度往往是定长的

补充：接口 Deque 的子类 ArrayDeque ，作为栈使用时比 Stack 快，因为原来的 Java 的 Stack 继承自 Vector，而 Vector 在每个方法中都加了锁，而
Deque 的子类 ArrayDeque 并没有锁的开销。
