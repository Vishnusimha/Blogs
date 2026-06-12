package ajavapractice.arrays.day1;

import java.util.Arrays;

class MoveZeroToLast {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {0, 2, 0, 4, 0, 5};
        int[] moveZeros = moveZeros(arr);

        System.out.println("Moved Zero To Last");
        System.out.println(Arrays.toString(moveZeros));
    }

    public static int[] moveZeros(int[] arr) {

        int[] moveZeros = arr;
        int start = 0;

        for (int i = 0; i < moveZeros.length; i++) {

            if (arr[i] != 0) {
                arr[start] = arr[i];
                start++;
            }
        }

        while (start < moveZeros.length) {
            arr[start] = 0;
            start++;
        }

        return moveZeros;
    }

}