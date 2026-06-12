package ajavapractice.arrays.day1;

import java.util.Arrays;

public class ReverseFirstHalfOfArray {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10
        };

        System.out.println(Arrays.toString(reverseFirstHalf(arr)));

    }


    public static int[] reverseFirstHalf(int[] arr) {
        // int[] arr = arr;
        double avg = 0;

        int start = 0;
        int end = (arr.length / 2) - 1;
        System.out.println("End index is " + end);

//        for (int i = 0; i < end; i++) {
//            int temp = 0;
//
//            temp = arr[start];
//            arr[start] = arr[end];
//            arr[end] = temp;
//
//            start++;
//            end--;
//        }

        while (start < end) {
            int temp = 0;

            temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;

            start++;
            end--;
        }

        return arr;
    }

}