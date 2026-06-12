package ajavapractice.arrays.day1;

import java.util.Arrays;

public class MoveNegativesToTheFrontAndMaintainOrder {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {0, -2, 3, -4, 5};
        System.out.println(" arr length is " + arr.length);
        System.out.println(Arrays.toString(moveNegativesToFront(arr)));

    }


    public static int[] moveNegativesToFront(int[] arr) {
        int start = 0;

        for (int i = 1; i < arr.length; i++) {
            int temp = 0;
            if (arr[i] < 0) { // if current element is negative, we need to move it to the front and maintain the order of the rest of the elements.
                temp = arr[i]; // store the current negative element in temp variable, we will use it to move it to the front later.
                int j = i; // we will use j to move the elements to the right, starting from the current index i, we will move all the elements to the right until we reach the start index, which is the index of the last negative element that we moved to the front.
                while (j > start) { // we will move the elements to the right until we reach the start index, which is the index of the last negative element that we moved to the front.
                    arr[j] = arr[j - 1]; // move the element at index j-1 to index j, this will create a gap at index start where we can move the current negative element.
                    j--; // move j to the left, we will continue moving the elements to the right until we reach the start index.
                }

                arr[start] = temp; // move the current negative element to the front, which is the index of the last negative element that we moved to the front, this will maintain the order of the rest of the elements.
                start++; // move the start index to the right, this will be the index of the last negative element that we moved to the front, we will use this index to move the next negative element to the front.
            }
        }
        System.out.println("Arrays " + Arrays.toString(arr));
        return arr;
    }
}

