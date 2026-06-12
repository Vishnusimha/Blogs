package ajavapractice.arrays.day1;

//medium level problem
public class FindThirdLargestElement {

    public static void main(String[] args) throws java.lang.Exception {

//        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        int[] arr = {-1, -2, -3, 0, 4, 5};
        int[] arr = {5, 5, 5, 5, 5};
        System.out.println("Array length is " + arr.length);
        System.out.println(thirdLargest(arr));

    }

    public static int thirdLargest(int[] arr) {

        if (arr.length < 3) {
            return -1;
        }

        int firstlargest = Integer.MIN_VALUE;
        int secondlargest = Integer.MIN_VALUE;
        int thirdlargest = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > firstlargest) {
                thirdlargest = secondlargest;
                secondlargest = firstlargest;
                firstlargest = arr[i];
            } else if (arr[i] > secondlargest && arr[i] < firstlargest) { // also check if arr[i] is less than firstlargest to avoid duplicates
                thirdlargest = secondlargest;
                secondlargest = arr[i];
            } else if (arr[i] > thirdlargest && arr[i] < secondlargest) {
                thirdlargest = arr[i];
            }
        }

        System.out.println("first largest element is; " + firstlargest);
        System.out.println("second largest element is; " + secondlargest);
        System.out.println("third largest element is; " + thirdlargest);

//        if (thirdlargest == Integer.MIN_VALUE) {
//            return -1; // if thirdlargest is still Integer.MIN_VALUE, it means there are not enough distinct elements in the array
//        }

        if (secondlargest == Integer.MIN_VALUE || thirdlargest == Integer.MIN_VALUE) { // if secondlargest or thirdlargest is still Integer.MIN_VALUE, it means there are not enough distinct elements in the array
            return -1; // if secondlargest, thirdlargest OR is still Integer.MIN_VALUE, it means there are not enough distinct elements in the array
        }

        return thirdlargest;
    }
}