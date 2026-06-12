package ajavapractice.arrays.day2;

import java.util.HashMap;

public class TwoSumMatchesTarget {
    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {
                1,
                2,
                3,
                4,
                5
        };

        System.out.println("Result: " + twoSum(arr, 5));

    }

    public static String twoSum(int[] arr, int sum) {

        HashMap<Integer, Integer> freq = new HashMap<>();

        for (int num : arr) {

            int complement = sum - num;

            if (freq.containsKey(complement)) {
                return "complement" + complement + "num: " + num;
            }
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        return "Not found";
    }
}