package ajavapractice.arrays.day2;

import java.util.HashMap;

// VERY VERY VERY IMPORTANT PROBLEM. DO NOT MISS THIS. VERY CONFUSING.
public class SubArraySumEqualsK {

    public static void main(String[] args) {
        int[] arr = {1, 1, 1};
        System.out.println("Arr length is " + arr.length);
        System.out.println(subarraySum(arr, 2)); // Output: 2
    }
    public static int subarraySum(int[] arr, int k) {
        int prefixSum = 0;
        int count = 0;

        HashMap<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1); // Base case: prefix sum 0 appears once

        for (int i = 0; i < arr.length; i++) {
            prefixSum += arr[i]; // Update prefix sum
//subarray sum (j+1 → i) = prefixSum(i) - prefixSum(j)
//prefixSum(i) - prefixSum(j) = k
//prefixSum(j) = prefixSum(i) - k
//Have I ever seen a prefix sum equal to (current prefixSum - k)?
//If YES: Every such occurrence = one valid subarray ending at i
// So we check, freq.containsKey(prefixSum - k).

            if (freq.containsKey(prefixSum - k)) { // Check if there is a prefix sum that when subtracted from current prefix sum gives k
                count = count + freq.get(prefixSum - k); // If such a prefix sum exists, add its frequency to count
            }
            freq.put(prefixSum, freq.getOrDefault(prefixSum, 0) + 1); // Store current prefix sum frequency
        }

        return count;
    }
}

//    public static int subarraySum(int[] arr, int k) {
//        int prefixSum = 0;
//        int count = 0;
//
//        HashMap<Integer, Integer> freq = new HashMap<>();
//
//        // Base case: prefix sum 0 appears once
//        freq.put(0, 1);
//
//        for (int num : arr) {
//            prefixSum += num;

/// /            Is there a subarray ending here with sum = 2?
//            if (freq.containsKey(prefixSum - k)) {
//                count += freq.get(prefixSum - k);
//            }
//            // Store current prefix sum frequency
//            freq.put(prefixSum, freq.getOrDefault(prefixSum, 0) + 1);
//        }
//
//        return count;
//    }


/**
 * 🔹 Step 9: Final Problem-Solving Flow (From Your Standpoint)
 * <p>
 * Thinking process should be:
 * <p>
 * I need subarrays with sum = k
 * <p>
 * I traverse the array
 * <p>
 * At each index, I know total sum so far (prefixSum)
 * <p>
 * A subarray ending here = k if some earlier prefix sum = prefixSum - k
 * <p>
 * So I store past prefix sums in a HashMap with frequency
 * <p>
 * Add how many times (prefixSum - k) appeared to my answer
 */
