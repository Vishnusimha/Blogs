package ajavapractice.arrays.day2;

public class maxSumSubarrayKSlidingWindow {
    public static void main(String[] args) {
        int[] arr = {
                2,
                1,
                5,
                1,
                3,
                2
        };
        System.out.println("Arr length is " + arr.length);
        System.out.println(maxSumSubarrayK(arr, 3));
    }

    // 2,1,5,1,3,2
    public static int maxSumSubarrayK(int[] arr, int k) {
        int runningSum = 0;
        int maxSumSubarrayK = Integer.MIN_VALUE;
        int window = 0;
        for (int i = 0; i < arr.length; i++) {
            runningSum += arr[i];
            window++;

            if (window > k) {
                runningSum -= arr[i - k];
            }

            if (window >= k) {
                maxSumSubarrayK = Math.max(maxSumSubarrayK, runningSum);
            }

        }

        return maxSumSubarrayK;
    }
}