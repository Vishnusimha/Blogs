package ajavapractice.arrays.day1;

import java.util.Arrays;

public class CountNumbersGreaterThanAvgInArray {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5,
                6
        };
        System.out.println("Array is : " + Arrays.toString(arr));

        System.out.println(countAboveAverage(arr));

    }


    public static int countAboveAverage(int[] arr) {
        double avg = 0;
        int count = 0;
        double sum = 0;

        for (int num : arr) {
            sum += num;
        }

        avg = sum / arr.length;

        System.out.println("Sum is " + sum);
        System.out.println("Average is " + avg);

        for (int num : arr) {
            if (num > avg) {
                count++;
            }
        }
        System.out.println("Numbers Greater Than Avg In Array is " + count);

        return count;
    }

}