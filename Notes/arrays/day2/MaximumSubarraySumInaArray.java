package ajavapractice.arrays.day2;

public class MaximumSubarraySumInaArray {

    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
//        int[] arr = {-3,-2,-5,-1};
        System.out.println(" arr length is " + arr.length);
        System.out.println(maxSubArray(arr));
    }


    public static int maxSubArray(int[] arr) {
        int maxSum = Integer.MIN_VALUE; // Important to initialize to Integer.MIN_VALUE to handle cases where all elements are negative
        int runningSum = 0;
        for (int num : arr) {
            runningSum += num;
            maxSum = Math.max(maxSum, runningSum);

            if (runningSum < 0) {
                runningSum = 0;
            }
        }

        return maxSum;
    }
}

//