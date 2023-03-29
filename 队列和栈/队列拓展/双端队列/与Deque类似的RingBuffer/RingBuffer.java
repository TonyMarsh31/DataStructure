package 队列和栈.队列拓展.双端队列.与Deque类似的RingBuffer;

public class RingBuffer {
    private static final int INIT_CAP = 1024;
    /**
     * 存储数据的数组
     */
    private byte[] buffer;
    /**
     * 用于防止索引越界
     */
    private int mask;
    /**
     * 读写指针
     */
    private int r, w;
    /**
     * 记录可以读取的字节个数
     */
    private int size;

    public RingBuffer() {
        this(INIT_CAP);
    }

    public RingBuffer(int cap) {
        // 将输入的 cap 变成 2 的指数
        cap = ceilToPowerOfTwo(cap);
        mask = cap - 1;
        // mask的作用：用于简化之后的取模运算。 (之后指针越界后需要进行mod运算重新确定索引)
        // 如果保证 capacity 是 2 的指数，
        // 那么(i + n) % capacity 这一mod运算 就等价于 (i + n) & mask (2进制的与运算)

        buffer = new byte[cap];
        // 读/写指针初始化在索引 0
        r = w = 0;
        // 还没有写入任何数据，可读取字节数为 0
        size = 0;

    }

    public static void main(String[] args) {
        RingBuffer rb = new RingBuffer(3);

        String s = "123456789abc";
        int nwrite = rb.writeBuffer(s.getBytes());
        System.out.println("write " + nwrite + " bytes " + s);

        byte[] out = new byte[9];
        int nread = rb.readBuffer(out);
        System.out.println("read " + nread + " bytes " + new String(out));

        nread = rb.readBuffer(out);
        System.out.println("read " + nread + " bytes " + new String(out));

        //write 12 bytes 123456789abc
        //read 9 bytes 123456789
        //read 3 bytes abc456789  因为用的是同一个nread存储所以后面有重复数据
    }

    /**
     * 将n转换为 大于且最接近于它的 一个2的指数，比如输入12，返回16 (2^4)
     *
     * @param n
     */
    private static int ceilToPowerOfTwo(int n) {
        // 本方法做的事情可以用下述代码块表示
//        int res = 1;
//        while (res < n) {
//            res = res * 2;
//        }
//        return res;
        if (n < 0) {
            // 肯定不能小于 0
            n = 2;
        }

        if (n > (1 << 30)) {
            // int 型最大值为 2^31 - 1
            // 所以无法向上取整到 2^31
            n = 1 << 30;
        }

        // 位运算技巧，参考如下链接：
        // http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;

        return n;
    }

    /**
     * 将in中的数据写入RingBuffer中，返回写入的字节数
     *
     * @param in
     * @return
     */
    public int writeBuffer(byte[] in) {
        if (in == null || in.length == 0) return 0;
        final int n = in.length;
        int free = buffer.length - size;
        if (free < n) ensureCapacity(size + n);

/*
        //  for循环实现的数组写入
        for (int i = 0; i < size; i++) {
            buffer[(w + i) % buffer.length] = in[i];
            buffer[(w + i) & mask] = in[i]; //用位运算优化mod操作
        }
*/
/*
        //使用system库中的arraycopy方法优化写入效率
        if (w >= r) { // 1. r-------w  r指针在w指针左边
            if (buffer.length - w >= n) { // 情况1.1：r---w 写入后变成 r---**w  即直至数组末尾，空间还够
                // copy in[0..] to data [w..w+n]
                System.arraycopy(in, 0, buffer, w, n);
            } else { // 情况1.2：r---w 写入后变成 **w  r---* 即添加到数组末尾后还不够，剩下的添加到数组头开始的一段空闲区域内
                int n1 = buffer.length - w;  //w到数组末尾的长度
                int n2 = n - n1; //还需要写入的长度
                // copy in[0..n1] to data[w..]
                System.arraycopy(in, 0, buffer, w, n1);
                // copy in[n1..] to data[0..n2]
                System.arraycopy(in, n1, buffer, 0, n2);
            }
        } else { // 2. ---w r--- w指针在r指针左边
            // 已经确保了buffer的空间足够，直接写入
            System.arraycopy(in, 0, buffer, w, n);
        }
*/
        //可以简化代码，只分2种情况，w指针越界 | w指针不越界 (就像一开始for循环代码中的思路)
        if (buffer.length - w < n) {// 直至数组最右边的空间还不够写入，w指针会越界，需要将w重新映射到数组开头
            // r---w  写入后变成 **w  r---*
            int n1 = buffer.length - w;  //w到数组末尾的长度
            int n2 = n - n1; //还需要写入的长度
            // copy in[0..n1] to data[w..]
            System.arraycopy(in, 0, buffer, w, n1);
            // copy in[n1..] to data[0..n2]
            System.arraycopy(in, n1, buffer, 0, n2);
        } else { //直至r指针，或者数组最右边的空间足够写入
            // 即r---w  写入后变成 r---**w
            // 或者---w   r--- 写入后变成 ---w***   r---
            // 因为已经做了ensureCapacity了,buffer空间足够，所以上述两种情况可以统一处理
            System.arraycopy(in, 0, buffer, w, n);
        }
        w = (w + n) & mask;
        size += n;
        return n;
    }

    /**
     * 从RingBuffer中读取数据到out中，返回读取的字节数
     *
     * @param out
     * @return
     */
    public int readBuffer(byte[] out) {
        if (out == null || out.length == 0 || isEmpty())
            return 0;

        // 实际读取的字节数 （返回值）
        int n = Math.min(size, out.length);

        // 情况1： r----w
        if (w > r) {
            // r----w 读取后变成 **r--w
            // copy data[r..r+n] to out[0..]
            System.arraycopy(buffer, r, out, 0, n);
            // 向前移动读指针
            r += n;
            // 可读取的字节数减少了 n
            size -= n;
            return n;
        }

        // 情况2：--w  r---
        if (r + n <= buffer.length) {
            // 情况2.1：--w  r--- 读取后变成 --w  **r-
            // copy data[r..r+n] to out[0..]
            System.arraycopy(buffer, r, out, 0, n);
        } else {
            // 情况2.2：----w  r-- 读取后变成  *r--w  ***
            int n1 = buffer.length - r;
            int n2 = n - n1;
            // copy data[r..] to out[0..n1]
            System.arraycopy(buffer, r, out, 0, n1);
            // copy data[0..n2] to out[n1..]
            System.arraycopy(buffer, 0, out, n1, n2);
        }

        //todo 与write方法中类似的, 可以简化为 r指针越位 | 不越位 两种情况

        // 向前移动读指针
        r = (r + n) & mask;

        // 可读取的字节数减少了 n
        size -= n;
        return n;
    }

    /**
     * 返回可读的字节数
     *
     * @return
     */
    public int length() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;

    }

    /**
     * do resize stuff
     *
     * @param newCap
     */
    private void ensureCapacity(int newCap) {
        newCap = ceilToPowerOfTwo(newCap);
        byte[] newBytes = new byte[newCap];
        int size = readBuffer(newBytes);
        this.buffer = newBytes;
        this.r = 0;
        this.w = size;
        this.mask = newCap - 1;
    }
}
