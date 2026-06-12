package ajavapractice.arrays.day3;

public class RotateArrayByKElements {

    public static void main(String[] args) {
        RotateArrayByKElements solution = new RotateArrayByKElements();
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        int k = 3;
        solution.rotate(nums, k);
        // Output: [5, 6, 7, 1, 2, 3, 4]
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }

    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n; //Because rotating n times brings the array back to the same position.

        // reverse whole array
        // reverse first k
        // reverse remaining n-k

        reverse(nums, 0, n - 1); //Step 1 — Reverse Entire Array -> [7 6 5 4 3 2 1]
        reverse(nums, 0, k - 1); //Step 2 — Fix First Segment -> [5 6 7 | 4 3 2 1]
        reverse(nums, k, n - 1); //Step 3 — Fix Remaining Segment -> [5 6 7 | 1 2 3 4]
    }

    private void reverse(int[] arr, int left, int right) {
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}