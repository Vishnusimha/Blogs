package ajavapractice.arrays.day1;

class SecondLargest {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5
        };

        System.out.println(secondLargest(arr));
    }


    public static int secondLargest(int[] arr) {
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;

        if (arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least two elements");
        }

        for (int num : arr) {

            if (num > largest) {
                secondLargest = largest;
                largest = num;
            } else if (num > secondLargest) {
//                dont forget this case checking second largest when num is not greater than largest
                secondLargest = num;
            }

        }

        return secondLargest;
    }


}