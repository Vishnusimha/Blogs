package ajavapractice.arrays.day1;

class Sum {

    public static void main(String[] args) throws java.lang.Exception {

        int[] arr = {
                1,
                2,
                3,
                4,
                5
        };

        System.out.println(sum(arr));
    }


    public static int sum(int[] arr) {
        int res = 0;

        for (int num : arr) {
            res += num;
        }
        return res;
    }


}