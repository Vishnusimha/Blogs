package ajavapractice.arrays.day2;

import java.util.HashMap;

public class findMajorityElementInAnArray {
    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {
                1, 1, 1, 2, 3, 4, 5, 1, 1
        };

        System.out.println("Result: " + findMajorityElement(arr));
        System.out.println("Result: " + findMajorityElementUsingBoyerMooreVotingAlgorithm(arr));
    }

    public static int findMajorityElement(int[] arr) {

        if (arr == null || arr.length < 2) {
            return -1;
        }

        // int majorityElement = Integer.MIN_VALUE;
        int threshold = arr.length / 2;

        HashMap<Integer, Integer> map = new HashMap<>();


        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
            if (map.get(arr[i]) > threshold) {
                return arr[i];
            }
        }

        return -1;
    }

    public static int findMajorityElementUsingBoyerMooreVotingAlgorithm(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }

        int candidate = 0;
        int count = 0;

        // Phase 1: Find potential candidate
        for (int num : arr) {
            if (count == 0) {
                candidate = num; // Set candidate to current number when count drops to 0
            }

            if (num == candidate) {
                count++; // Increment count for candidate
            } else {
                count--; // Decrement count for non-candidate
            }
        }

        return candidate;
    }
}

//For Hashmap version
//Time Complexity: O(n)
//Single pass over array
//HashMap operations are O(1) average
//Space Complexity: O(n)
//In worst case, all elements are unique → map stores n entries