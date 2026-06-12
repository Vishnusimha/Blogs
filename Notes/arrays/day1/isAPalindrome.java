package ajavapractice.arrays.day1;

import java.util.Arrays;

public class isAPalindrome {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {1, 2, 1};
//         int[] arr = { 1,2,2,1 };
//         int[] arr = { 1,2,2,4 };
        System.out.println(" arr length is " + arr.length);
        System.out.println(isArrayPalindrome(arr) ? " is a palindrome" : " is not a palindrome");
    }


    public static boolean isArrayPalindrome(int[] arr) {

        int start = 0;
        int end = arr.length - 1;

        while (start < end) { // we can also use start <= end, but in that case we need to add an extra condition to check if start and end are pointing to the same element, if they are then we can break the loop since it's a palindrome
            if (arr[start] != arr[end]) { // if the elements at start and end are not equal, then it's not a palindrome, we can return false immediately without checking the rest of the elements
                return false;
            }
            start++;
            end--;
        }

        System.out.println("Arrays " + Arrays.toString(arr));
        return true;
    }
}