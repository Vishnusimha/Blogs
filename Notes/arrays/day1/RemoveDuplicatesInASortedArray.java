package ajavapractice.arrays.day1;

import java.util.Arrays;

public class RemoveDuplicatesInASortedArray {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println("Array length is " + arr.length);
        System.out.println(removeDuplicatesInSortedArray(arr));
    }

    public static int removeDuplicatesInSortedArray(int[] arr) {
        if (arr == null || arr.length == 0) return 0;

        int writePointer = 0;

        for (int i = 1; i < arr.length; i++) { // we start from index 1, as we have already considered the first element at index 0 as unique, and we will compare the current element with the last unique element at writePointer index, if they are not equal, then we have found a new unique element, and we will move the writePointer to the next position and copy the current element to the writePointer index, if they are equal, then we have found a duplicate element, and we will skip it and move to the next element in the array.
            if (arr[i] != arr[writePointer]) {
                // Important: move the writePointer to the next position, as we have found a new unique element, and we will copy the current element to the writePointer index, this will ensure that all unique elements are moved to the front of the array, and the rest of the elements are duplicates, which we can ignore.
                writePointer++;
                arr[writePointer] = arr[i];
            }

        }

        System.out.println("last unique element is; " + arr[writePointer]);
        System.out.println("pos of last unique element is; " + writePointer);
        System.out.println("---------------------- VERY VERY VERY IMPORTANT------------------------");
        System.out.println("NOW The full array traversal is completed \n and all remaining elements after the start index could have duplicates, just until the index we have non duplicate values.");
        System.out.println("So we must only consider the elements from index 0 to start index, as they are the unique elements, \n and we can ignore the rest of the elements as they are duplicates.");
        System.out.println("So we copy the array from index 0 to start index + 1, as we need to include the last unique element at start index, and we can ignore the rest of the elements as they are duplicates.");

//        VERY VERY VERY IMPORTANT
        System.out.println(Arrays.toString(arr)); // this will print the entire array with duplicates, as we have not removed the duplicates from the array, we have only moved the unique elements to the front of the array, and the rest of the elements are still there, but we can ignore them as they are duplicates.
        System.out.println(Arrays.toString(Arrays.copyOf(arr, writePointer + 1))); // this will print only the unique elements from index 0 to start index, as we have copied the array from index 0 to start index + 1, as we need to include the last unique element at start index, and we can ignore the rest of the elements as they are duplicates.

        return writePointer + 1; // since start is index of last unique element, we need to add 1 to get the count of unique elements
    }

}