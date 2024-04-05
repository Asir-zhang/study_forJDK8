import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork/Join框架实现归并排序
 */
public class MergeSort {
    static class SortTask extends RecursiveTask<int[]> {
        private int[] arr;

        public SortTask(int[] arr) {
            this.arr = Arrays.copyOf(arr, arr.length);
        }

        @Override
        protected int[] compute() {
            if (arr.length <= 1) {
                return arr;
            }
            int mid = arr.length >> 1;
            SortTask left = new SortTask(Arrays.copyOfRange(arr, 0, mid));
            SortTask right = new SortTask(Arrays.copyOfRange(arr, mid, arr.length));
            left.fork();
            right.fork();
            int[] leftResult = left.join();
            int[] rightResult = right.join();
            return merge(leftResult, rightResult);
        }
    }

    public static int[] mergeSort(int[] arr) {
        return new SortTask(arr).fork().join();
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {3, 5, 1, 4, 2, 6};

        int[] result = mergeSort(arr);

        System.out.println(Arrays.toString(result));
    }
}
