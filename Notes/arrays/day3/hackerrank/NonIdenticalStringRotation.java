package ajavapractice.arrays.day3.hackerrank;

public class NonIdenticalStringRotation {

    public static void main(String[] args) {
        String s1 = "abcde";
        String s2 = "deabc";

        System.out.println(isNonTrivialRotation(s1, s2)); // Output: true
    }
    public static boolean isNonTrivialRotation(String s1, String s2) {
        if(s1.length() <1 || s2.length()<1 ){
            return false;
        }

        if(s1.equals(s2) || s1.length() != s2.length()){ // lengths must be same, and both must not be same
            return false;
        }

        String s = s1+s1;
        return s.contains(s2); // why


        // HashMap<Character, Integer> map = new HashMap<>();

        // for (char c : s1.toCharArray()) {
        //     map.put(c, map.getOrDefault(c, 0)+1);
        // }
        // for (char c : s1.toCharArray()) {
        //     if(map.containsKey(c) && map.getOrDefault(c,0) != 0){
        //         map.put(c, map.getOrDefault(c, 0)-1);
        //     }

        // }

        // for (int iterable_element : map.values()) {
        //     if(iterable_element > 0){
        //         return false;
        //     }
        // }
    }
}
