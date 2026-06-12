package ajavapractice.arrays.day2;

import java.util.Arrays;

public class PrefixSumInAnArray {
    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {
                1,
                2,
                3,
                4
        };
        System.out.println(" arr length is " + arr.length);
        System.out.println(Arrays.toString(prefixSum(arr)));
    }


    public static int[] prefixSum(int[] arr) {
        int prefixSum = 0;

        for (int i = 0; i < arr.length; i++) {
            prefixSum += arr[i];
            arr[i] = prefixSum;
        }

        return arr; /*[1, 3, 6, 10]*/
    }
}