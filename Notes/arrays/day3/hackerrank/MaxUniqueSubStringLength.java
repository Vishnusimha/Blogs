package ajavapractice.arrays.day3.hackerrank;

import java.util.HashSet;

public class MaxUniqueSubStringLength {

    public static void main(String[] args) {

        System.out.println(maxDistinctSubstringLengthInSessions("abc*def*ghij"));
        System.out.println(maxDistinctSubstringLengthInSessions("abcabcbb"));
    }

    public static int maxDistinctSubstringLengthInSessions(String sessionString) {

        if (sessionString.length() == 0) {
            return 0;
        }

        HashSet<Character> set = new HashSet<>();

        int maxLength = 0;

        int left = 0;
        for (int i = 0; i < sessionString.length(); i++) {
            char current = sessionString.charAt(i);
            if (current == '*') {
                set.clear();
                left = i + 1;
                continue;
            }
            while (set.contains(current)) {
                set.remove(sessionString.charAt(left));
                left++;
            }
            set.add(current);

            maxLength = Math.max(maxLength, i - left + 1);
        }

        return maxLength;
    }
}
