package ajavapractice.arrays.day2;

import java.util.Arrays;

//Return index where left sum = right sum. If none, return -1.
public class PivotIndexLeftSumEqualToRightSum {
    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {
                1,
                7,
                3,
                6,
                5,
                6
        };
        System.out.println(" arr length is " + arr.length);
        System.out.println(pivotIndex(arr));
    }

    public static int pivotIndex(int[] arr) {
        int leftSum = 0;
//        int rightSum = 0;
        int total = Arrays.stream(arr).sum();

        System.out.println("total:" + total);

        for (int i = 0; i < arr.length; i++) {
            int rightSum = total - leftSum - arr[i]; // right sum is total - left sum - current element
            if (leftSum == rightSum) {
                return i;
            }

            leftSum += arr[i];
        }

        return -1;
    }
}