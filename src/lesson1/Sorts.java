package lesson1;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Sorts {

    public static <T extends Comparable<T>> void insertionSort(T[] elements) {
        for (int i = 1; i < elements.length; i++) {
            T current = elements[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (elements[j].compareTo(current) > 0) elements[j+1] = elements[j];
                else break;
            }
            elements[j+1] = current;
        }
    }

    public static void insertionSort(int[] elements) {
        for (int i = 1; i < elements.length; i++) {
            int current = elements[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (elements[j] > current) elements[j+1] = elements[j];
                else break;
            }
            elements[j+1] = current;
        }
    }

    private static void merge(int[] elements, int begin, int middle, int end) {
        int[] left = Arrays.copyOfRange(elements, begin, middle);
        int[] right = Arrays.copyOfRange(elements, middle, end);
        int li = 0, ri = 0;
        for (int i = begin; i < end; i++) {
            if (li < left.length && (ri == right.length || left[li] <= right[ri])) {
                elements[i] = left[li++];
            }
            else {
                elements[i] = right[ri++];
            }
        }
    }

    private static void mergeSort(int[] elements, int begin, int end) {
        if (end - begin <= 1) return;
        int middle = (begin + end) / 2;
        mergeSort(elements, begin, middle);
        mergeSort(elements, middle, end);
        merge(elements, begin, middle, end);
    }

    public static void mergeSort(int[] elements) {
        mergeSort(elements, 0, elements.length);
    }

    private static void heapify(int[] elements, int start, int length) {
        int left = 2 * start + 1;
        int right = left + 1;
        int max = start;
        if (left < length && elements[left] > elements[max]) {
            max = left;
        }
        if (right < length && elements[right] > elements[max]) {
            max = right;
        }
        if (max != start) {
            int temp = elements[max];
            elements[max] = elements[start];
            elements[start] = temp;
            heapify(elements, max, length);
        }
    }

    private static void buildHeap(int[] elements) {
        for (int start = elements.length / 2 - 1; start >= 0; start--) {
            heapify(elements, start, elements.length);
        }
    }

    public static void heapSort(int[] elements) {
        buildHeap(elements);
        for (int j = elements.length - 1; j >= 1; j--) {
            int temp = elements[0];
            elements[0] = elements[j];
            elements[j] = temp;
            heapify(elements, 0, j);
        }
    }

    private static final Random random = new Random(Calendar.getInstance().getTimeInMillis());

    private static int partition(int[] elements, int min, int max) {
        int x = elements[min + random.nextInt(max - min + 1)];
        int left = min, right = max;
        while (left <= right) {
            while (elements[left] < x) {
                left++;
            }
            while (elements[right] > x) {
                right--;
            }
            if (left <= right) {
                int temp = elements[left];
                elements[left] = elements[right];
                elements[right] = temp;
                left++;
                right--;
            }
        }
        return right;
    }

    private static void quickSort(int[] elements, int min, int max) {
        if (min < max) {
            int border = partition(elements, min, max);
            quickSort(elements, min, border);
            quickSort(elements, border + 1, max);
        }
    }

    public static void quickSort(int[] elements) {
        quickSort(elements, 0, elements.length - 1);
    }

    private static <T extends Comparable<T>> int part(List<T> elements, int min, int max, boolean address) {
        T x = elements.get(min + random.nextInt(max - min + 1));
        int left = min, right = max;
        while (left <= right) {
            if (address) {
                while (compareAddress((String) elements.get(left), (String) x) < 0) {
                    left++;
                }
                while (compareAddress((String) elements.get(right), (String) x) > 0) {
                    right--;
                }
            } else {
                while (elements.get(left).compareTo(x) < 0) {
                    left++;
                }
                while (elements.get(right).compareTo(x) > 0) {
                    right--;
                }
            }
            if (left <= right) {
                T temp = elements.get(left);
                elements.set(left, elements.get(right));
                elements.set(right, temp);
                left++;
                right--;
            }
        }
        return right;
    }

    private static <T extends Comparable<T>> void qckSort(List<T> elements, int min, int max, boolean address) {
        if (min < max) {
            int border = part(elements, min, max, address);
            qckSort(elements, min, border, address);
            qckSort(elements, border + 1, max, address);
        }
    }

    public static <T extends Comparable<T>> void qckSort(List<T> elements, boolean address) {
        qckSort(elements, 0, elements.size() - 1, address);
    }

    public static int[] countingSort(int[] elements, int limit) {
        int[] count = new int[limit + 1];
        for (int element: elements) {
            count[element]++;
        }
        for (int j = 1; j <= limit; j++) {
            count[j] += count[j - 1];
        }
        int[] out = new int[elements.length];
        for (int j = elements.length - 1; j >= 0; j--) {
            out[count[elements[j]] - 1] = elements[j];
            count[elements[j]]--;
        }
        return out;
    }

    public static int compareAddress(String first, String second){
        if (first.split(" ")[0].compareTo(second.split(" ")[0]) > 0) return 1;
        else if (first.split(" ")[0].compareTo(second.split(" ")[0]) == 0) {
            if (Integer.parseInt(first.split(" ")[1]) > Integer.parseInt(second.split(" ")[1])) return 1;
            else if (Integer.parseInt(first.split(" ")[1]) == Integer.parseInt(second.split(" ")[1])) return 0;
            else return -1;
        } else return -1;
    }

    private static int partitionDouble(List<Double> elements, int min, int max) {
        double x = elements.get(min + random.nextInt(max - min + 1));
        int left = min, right = max;
        while (left <= right) {
            while (elements.get(left).compareTo(x) < 0) {
                left++;
            }
            while (elements.get(right).compareTo(x) > 0) {
                right--;
            }
            if (left <= right) {
                double temp = elements.get(left);
                elements.set(left, elements.get(right));
                elements.set(right, temp);
                left++;
                right--;
            }
        }
        return right;
    }

    private static void quickSortDouble(List<Double> elements, int min, int max) {
        if (min < max) {
            int border = partitionDouble(elements, min, max);
            quickSortDouble(elements, min, border);
            quickSortDouble(elements, border + 1, max);
        }
    }

    public static void quickSortDouble(List<Double> elements) {
        quickSortDouble(elements, 0, elements.size() - 1);
    }
}