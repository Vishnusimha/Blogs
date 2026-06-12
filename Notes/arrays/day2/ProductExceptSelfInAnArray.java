package ajavapractice.arrays.day2;

import java.util.Arrays;

public class ProductExceptSelfInAnArray {

    public static void main(String[] args) throws java.lang.Exception {
        int[] arr = {
                1,
                1,
                1,
                2,
                5
        };

        System.out.println("Result: " + Arrays.toString(productExceptSelf(arr)));
    }

    public static int[] productExceptSelf(int[] arr) {
        int[] resultArr = new int[arr.length];

        int prefixProd = 1;
        int suffixProd = 1;

        resultArr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            prefixProd *= arr[i - 1];
            resultArr[i] = prefixProd;
        }

        for (int i = arr.length - 1; i >= 0; i--) { //We are traversing from the end of the array because we need to calculate the suffix product and multiply it with the prefix product which is already stored in resultArr
            resultArr[i] *= suffixProd; //This step should be first then below one because we need to multiply the suffix product with the prefix product which is already stored in resultArr
            suffixProd *= arr[i];
        }
        return resultArr;
    }
}