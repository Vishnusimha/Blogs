package ajavapractice.arrays.day1;

public class MaxMinDifferenceInAnArray {
    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5,
                6
        };

        System.out.println(maxMinDiff(arr));
    }

    public static int maxMinDiff(int[] arr) {

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] < min) {
                min = arr[i];
            }

            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return (max - min);
    }
}