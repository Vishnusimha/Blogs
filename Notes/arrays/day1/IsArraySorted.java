package ajavapractice.arrays.day1;

public class IsArraySorted {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5
        };
        // int[] arr = {
        //     1,
        //     2,
        //     3,
        //     7,
        //     5
        // };

        System.out.println(isSorted(arr));
    }


    public static boolean isSorted(int[] arr) {

        for (int i = 0; i < arr.length - 1; i++) { // -1 to avoid ArrayIndexOutOfBoundsException

            if (arr[i] > arr[i + 1]) { // compare current element with the next one
                return false; // if current element is greater than next, array is not sorted
            }
        }
        return true; // if no such pair is found, array is sorted
    }
}