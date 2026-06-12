package ajavapractice.arrays.day3.hackerrank;

import java.util.Arrays;
import java.util.List;

public class PivotedBinarySearch {

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(4, 5, 6, 7, 0, 1, 2);
        int target = 0;

        int result = searchRotatedTimestamps(nums, target);

        if (result != -1) {
            System.out.println("Element " + target + " found at index " + result);
        } else {
            System.out.println("Element " + target + " not found in the array");
        }
    }

    public static int searchRotatedTimestamps(List<Integer> nums, int target) {

        int low = 0;
        int high = nums.size() - 1;


        while (low <= high) {
            int mid = (low + high) / 2;

            if (nums.get(mid) == target) {
                return mid;
            }

            if (nums.get(low) <= nums.get(mid)) {
                if (target >= nums.get(low) && target < nums.get(mid)) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }

            } else {
                if (target > nums.get(mid) && target <= nums.get(high)) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

        }

        return -1;
    }
}
