package ajavapractice.arrays.day2;

//Medium level Problem
public class MinimumSizeSubarraySum {
    public static int minSubArrayLen(int target, int[] arr) {
        int runningSum = 0;
        int start = 0;
        int minSubArrayLen = Integer.MAX_VALUE;

        for (int i = 0; i < arr.length; i++) {
            // Step 1: Expand window
            runningSum += arr[i];

            // Step 2: Shrink window while it is valid
            while (runningSum >= target) {
                // Step 3: Update minimum length BEFORE shrinking further
                int currentLen = i - start + 1; // Calculate current window size. why + 1 is because we are calculating the length of the sub array. and the index starts from 0. so we need to add 1 to get the correct length.
                minSubArrayLen = Math.min(minSubArrayLen, currentLen);

                // Step 4: Shrink from left
                runningSum -= arr[start];
                start++;
            }
        }

        // If no valid subarray found, return 0 (LeetCode standard)
        return minSubArrayLen == Integer.MAX_VALUE ? 0 : minSubArrayLen;
    }
}

//Initial thiking process: this is wrong
// Traverse through array;
// runningSum variable
// as soon as we reach runningSum == target, we store the size of sub array.
// and restart the subArray position to current
// we're storing the size of sub array we use Math.min fun to store the min size. and then finally we retrun the min lenght of the arrat

//Correct is below.

