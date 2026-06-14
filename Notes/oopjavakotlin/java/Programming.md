# Java Interview Prep Programs

1. Write a Java program that finds the k-th largest element in an integer array using a max heap (priority queue). OR

2. Implement a Java program that takes an integer array and an integer 'k' as input, and returns the k-th largest
   element in the array. Use a max heap (priority queue) to efficiently solve this problem.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.*;

class Main {
    public static void main(String[] args) {
        int[] nums = {3, 2, 3, 6, 4, 20, 30};
        int k = 4;

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

        for (int num : nums) {
            maxHeap.offer(num);

            if (maxHeap.size() > k) {
                System.out.println("The  polled  element is:  " + maxHeap.poll());
            }
        }
        System.out.println("The " + k + "th largest element is: " + maxHeap.peek());
    }
}
```
