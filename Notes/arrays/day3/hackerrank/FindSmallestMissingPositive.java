package ajavapractice.arrays.day3.hackerrank;

import java.util.ArrayList;
import java.util.List;

public class FindSmallestMissingPositive {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(List.of(3, 4, -1, 1));

        System.out.println(findSmallestMissingPositive(nums));
    }

    public static int findSmallestMissingPositive(List<Integer> orderNumbers) {

        int n = orderNumbers.size();

        for (int i = 0; i < n; i++) {

            while (true) {
                int value = orderNumbers.get(i);
                int correctIndex = value - 1;
                if (value < 1 || value > n) break;

                if (orderNumbers.get(correctIndex) == value) break;

                int temp = orderNumbers.get(i);
                orderNumbers.set(i, orderNumbers.get(correctIndex));
                orderNumbers.set(correctIndex, temp);
            }

        }
        // Step 2: Find first missing positive
        for (int i = 0; i < n; i++) {
            if (orderNumbers.get(i) != i + 1) {
                return i + 1;
            }
        }

        // Step 3: If all positions are correct
        return n + 1;
    }
}

