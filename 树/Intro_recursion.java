package 树;

public class Intro_recursion {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        // 遍历这个数组
        for (int j : arr) {
            System.out.print(j);
        }

        System.out.println();

        // 递归遍历这个数组
        printArr(arr, 0);

        int targetValue = 5;
        int index = searchItem(arr, 0, targetValue);
    }

    private static int searchItem(int[] arr, int i, int targetValue) {
        if (i == arr.length) return -1; // base case (没找到)
        if (arr[i] == targetValue) return i; // base case (找到了)
        return searchItem(arr, i + 1, targetValue); // recursion
    }

    private static void printArr(int[] arr, int i) {
        if (i == arr.length) return; // base case
        System.out.print(arr[i]);  // action
        printArr(arr, i + 1); // recursion
        // trick: 交换上面两行代码的位置，则会倒序打印数组，因为此时的action是从最深处开始执行的
    }
}