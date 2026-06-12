package ajavapractice.arrays.day1;

public class IsStrictlyIncreasingArray {// Means no duplicates too

    public static void main(String[] args) throws java.lang.Exception {

//        int[] arr1 = {
//                1,
//                2,
//                3,
//                4,
//                5,
//                6,
//                7,
//                8,
//                9,
//                10
//        };

        int[] arr = {0, 0, 1, 1, 2, 2, 3, 3,};

        System.out.println(" arr length is " + arr.length);
        System.out.println(isStrictlyIncreasing(arr));

    }


    public static boolean isStrictlyIncreasing(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] >= arr[i + 1]) { // we need to check till arr.length - 1 because we are comparing current element with next element, if we go till arr.length, then we will get ArrayIndexOutOfBoundsException
                return false; // if current element is greater than or equal to next element, then it's not strictly increasing
            }
        }
        return true;
    }

}