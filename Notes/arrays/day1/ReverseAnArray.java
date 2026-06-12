package ajavapractice.arrays.day1;

import java.util.Arrays;

class ReverseAnArray {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5
        };

        int[] reversedArr = reverse(arr);
        System.out.println(Arrays.toString(reversedArr));
    }


    public static int[] reverse(int[] arr) {

        int[] reverseArr = arr;
        int start = 0;
        int end = reverseArr.length - 1;

//        Using for loop to reverse the array is not the best way, because we need to keep track of the start and end index, and we need to update them in each iteration, which can be error prone and less efficient than using a while loop
//        for (int i = 0; i < end; i++) { // end is the last index, so we can use it as the loop condition, if we put <arr.length, it will cause out of bounds error when we try to access reverseArr[end]
//            int temp = 0;
//            temp = reverseArr[start];
//            reverseArr[start] = reverseArr[end];
//            reverseArr[end] = temp;
//
//            start++;
//            end--;
//        }
//

//        Another way to reverse the array is to use a while loop, which is more concise and easier to read
        while (start < end) {
            int temp = 0;
            temp = reverseArr[start];
            reverseArr[start] = reverseArr[end];
            reverseArr[end] = temp;
            start++;
            end--;
        }

        return reverseArr;
    }
}