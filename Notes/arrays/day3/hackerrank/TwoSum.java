package ajavapractice.arrays.day3.hackerrank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TwoSum {
    public static void main(String[] args) {
        List<Integer> taskDurations = Arrays.asList(1, 2, 3, 4, 5);
        int slotLength = 5;

        List<Integer> result = findTaskPairForSlot(taskDurations, slotLength);
        System.out.println(result); // Output: [0, 3] (tasks with durations 1 and 4)
    }

    public static List<Integer> findTaskPairForSlot(List<Integer> taskDurations, int slotLength) {
        int n = taskDurations.size();
        HashMap<Integer, Integer> map = new HashMap<>();

        if (n < 2) {
            return Arrays.asList(-1, -1);
        }

        for (int i = 0; i < n; i++) {
            int current = taskDurations.get(i);
            int required = slotLength - current;

            if (map.containsKey(required)) {
                return Arrays.asList(map.get(required), i);
            }
            map.put(current, i);
        }

        return Arrays.asList(-1, -1);
    }
}
