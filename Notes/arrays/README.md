# Arrays Package README

This package contains comprehensive Java array practice problems, organized by difficulty and concepts.

## Package Structure

```
arrays/
├── README.md (this file)
├── A_Java_Practice_EasyTOHardArrays_DAY1.md
├── A_Java_Practice_EasyTOHardArrays_Day2.md
├── A_Java_Practice_EasyTOHardArrays_Day3.md
├── LeetcodeArrays.md
├── array_patterns_20_questions_set1.html
├── array_patterns_20_questions_set2.html
├── day1/
│   ├── array_pattern_code_quiz DAY 1.md
│   ├── array_two_pointer_quiz DAY1.md
│   ├── CountNumbersGreaterThanAvgInArray.java
│   ├── FindThirdLargestElement.java
│   ├── isAPalindrome.java
│   ├── IsArraySorted.java
│   ├── IsStrictlyIncreasingArray.java
│   ├── MaxMinDifferenceInAnArray.java
│   ├── MoveNegativesToTheFrontAndMaintainOrder.java
│   ├── MoveZeroToLast.java
│   ├── RemoveDuplicatesInASortedArray.java
│   ├── ReverseAnArray.java
│   ├── ReverseFirstHalfOfArray.java
│   ├── SecondLargest.java
│   └── Sum.java
├── day2/
│   ├── ContainerWithMostWater.java
│   ├── findMajorityElementInAnArray.java
│   ├── FindMissingNumber.java
│   ├── MaximumSubarraySumInaArray.java
│   ├── maxSumSubarrayKSlidingWindow.java
│   ├── matrix/
│   │   ├── RotateMatrix.java
│   │   ├── SetMatrixZeroes.java
│   │   └── SpiralMatrix.java
│   ├── MergeOverlappingIntervals.java
│   ├── MinimumSizeSubarraySum.java
│   ├── PivotIndexLeftSumEqualToRightSum.java
│   ├── PrefixSumInAnArray.java
│   ├── ProductExceptSelfInAnArray.java
│   ├── SearchInRotatedSortedArray.java
│   ├── SortColors.java
│   ├── SubArraySumEqualsK.java
│   └── TwoSumMatchesTarget.java
└── day3/
    ├── RotateArrayByKElements.java
    └── hackerrank/
        ├── FindSmallestMissingPositive.java
        ├── MaxUniqueSubStringLength.java
        ├── NonIdenticalStringRotation.java
        ├── PivotedBinarySearch.java
        └── TwoSum.java
```

## Day 1: Basic Array Operations

### Concepts Practiced in Day 1

Based on your Day 1 practice, you mastered these foundational array concepts:

#### 🔹 1. Basic Array Traversal
- **Skills**: Enhanced for-loops, running values (max, min, sum, count), handling negatives
- **Problems Solved**: Find max/min, sum elements, count evens
- **Files**:
  - `Sum.java` - Sum of array elements
  - `MaxMinDifferenceInAnArray.java` - Find max/min difference
  - `CountNumbersGreaterThanAvgInArray.java` - Count numbers greater than average

#### 🔹 2. Two-Pointer Technique (Opposite Direction)
- **Skills**: Swapping elements, midpoint loops, visualizing movement
- **Problems Solved**: Reverse array in-place
- **Files**:
  - `ReverseAnArray.java` - Reverse array using two pointers
  - `ReverseFirstHalfOfArray.java` - Reverse first half

#### 🔹 3. Two-Pointer Technique (Same Direction / Compaction)
- **Skills**: Traversal vs placement pointers, compacting forward, filling positions
- **Problems Solved**: Move zeros to end while maintaining order
- **Files**:
  - `MoveZeroToLast.java` - Move zeros to end
  - `MoveNegativesToTheFrontAndMaintainOrder.java` - Move negatives to front
  - `RemoveDuplicatesInASortedArray.java` - Remove duplicates in sorted array

#### 🔹 4. Neighbor Comparison Pattern
- **Skills**: Adjacent element comparison, loop boundary control (i < length - 1)
- **Problems Solved**: Check if array is sorted
- **Files**:
  - `IsArraySorted.java` - Check if array is sorted
  - `IsStrictlyIncreasingArray.java` - Check strictly increasing
  - `isAPalindrome.java` - Check palindrome

#### 🔹 5. Multi-Variable Tracking in Single Pass
- **Skills**: Updating multiple variables, edge cases (duplicates, small arrays)
- **Problems Solved**: Find second/third largest element
- **Files**:
  - `SecondLargest.java` - Find second largest
  - `FindThirdLargestElement.java` - Find third largest

### Key Mistakes Avoided
- Resetting pointers inside loops
- Looping full length when half traversal needed
- Mixing pointer roles
- Forgetting loop variable updates
- Accessing i+1 without boundary adjustment
- Overcomplicating conditions

### Interview Skills Strengthened
- Clean loop writing
- Pattern recognition (single vs two pointers)
- Edge case thinking
- Logical debugging

### Outcome
Completed foundation cycle for arrays - building blocks for sliding window, strings, partitioning, and search/sort optimizations.

## Day 2: Advanced Array Algorithms & Interview Patterns

### 🧠 Interview Patterns Coverage

Based on your Day 2 learning plan, here's how the current implementations map to key interview patterns:

#### ✅ 1️⃣ Sliding Window Problems
- **Maximum sum subarray of size K**: `maxSumSubarrayKSlidingWindow.java`
- **Minimum size subarray sum**: `MinimumSizeSubarraySum.java`
- *Skills: Two pointers + running sum + window shrink logic*

#### ✅ 2️⃣ Prefix Sum Problems
- **Range sum query**: `PrefixSumInAnArray.java`
- **Subarray sum equals K**: `SubArraySumEqualsK.java`
- **Find equilibrium index**: `PivotIndexLeftSumEqualToRightSum.java`
- *Skills: Cumulative sum + HashMap for frequency*

#### ✅ 3️⃣ Kadane's Algorithm (Max Subarray)
- **Maximum subarray sum**: `MaximumSubarraySumInaArray.java`
- *Skill: Dynamic programming in one pass*

#### ✅ 4️⃣ Sorting + Greedy
- **Merge overlapping intervals**: `MergeOverlappingIntervals.java`
- *Skill: Sort + smart traversal*

#### ✅ 5️⃣ Binary Search on Arrays
- **Search in rotated sorted array**: `SearchInRotatedSortedArray.java`
- *Skill: Divide search space correctly*

#### ✅ 6️⃣ Two-Pointer Same Direction
- **Container with most water**: `ContainerWithMostWater.java`
- *Skill: Pointer movement based on condition*

#### ✅ 7️⃣ Partitioning Problems
- **Sort colors (Dutch National Flag)**: `SortColors.java`
- *Skill: Multiple pointers with regions*

#### ✅ 8️⃣ Cyclic Sort / Index Mapping
- **Find missing number**: `FindMissingNumber.java`
- *Skill: Place elements at correct index*

#### ✅ 9️⃣ Hashing + Arrays
- **Two sum**: `TwoSumMatchesTarget.java`
- **Find majority element**: `findMajorityElementInAnArray.java`
- *Skill: HashMap/HashSet + traversal*

#### ✅ 1️⃣ Matrix (2D Array) Problems

- **Spiral matrix**: `matrix/SpiralMatrix.java`
- **Rotate matrix**: `matrix/RotateMatrix.java`
- **Set matrix zeroes**: `matrix/SetMatrixZeroes.java`
- *Skill: 2D pointer control*

### Recommended Learning Order (Step-by-Step Growth)
Based on your current foundation, follow this progression for optimal skill-building:

1. **Kadane (Max Subarray)** → `MaximumSubarraySumInaArray.java`
2. **Prefix Sum** → `PrefixSumInAnArray.java`, `PivotIndexLeftSumEqualToRightSum.java`, `SubArraySumEqualsK.java`
3. **Sliding Window** → `maxSumSubarrayKSlidingWindow.java`, `MinimumSizeSubarraySum.java`
4. **Two Pointers Advanced** → `ContainerWithMostWater.java`
5. **Binary Search** → `SearchInRotatedSortedArray.java`
6. **Sorting + Greedy** → `MergeOverlappingIntervals.java`
7. **Partitioning** → `SortColors.java`
8. **Hashing + Arrays** → `TwoSumMatchesTarget.java`, `findMajorityElementInAnArray.java`
9. **Cyclic Sort** → `FindMissingNumber.java`
10. **Matrix** → `matrix/SpiralMatrix.java`, `matrix/RotateMatrix.java`, `matrix/SetMatrixZeroes.java`

---

## Day 3: Advanced Interview Problems & HackerRank Solutions

### Concepts Practiced in Day 3

Day 3 focuses on **advanced array rotation, binary search variations, and real-world HackerRank problems**.

#### 🔹 **Array Rotation Techniques**
- **Rotate Array by K Elements**: `RotateArrayByKElements.java`
- *Skill: In-place rotation, reversal algorithm, circular shifts*

#### 🔹 **HackerRank Problem Set** (Advanced Patterns)
- **Pivoted Binary Search**: `hackerrank/PivotedBinarySearch.java`
  - *Skill: Modified binary search on rotated/pivoted arrays*

- **Find Smallest Missing Positive**: `hackerrank/FindSmallestMissingPositive.java`
  - *Skill: Array as hash map, cyclic sort, finding absent elements*

- **Maximum Unique Substring Length**: `hackerrank/MaxUniqueSubStringLength.java`
  - *Skill: Sliding window for substring problems*

- **Non-Identical String Rotation**: `hackerrank/NonIdenticalStringRotation.java`
  - *Skill: String rotation patterns, comparison techniques*

- **Two Sum (HackerRank Variant)**: `hackerrank/TwoSum.java`
  - *Skill: Alternative implementation of two-pointer/hashing approach*

---

## 📊 Arrays Progress Summary

### 🟢 **COMPLETED** (34 files implemented)

- **Day 1**: 16 files covering 5 foundational concepts
- **Day 2**: 12 files covering all 10 interview patterns
- **Day 3**: 6 files covering advanced patterns & HackerRank problems
  - Array rotation techniques
  - Advanced binary search variants
  - Cyclic sort applications
  - Substring/sliding window problems

### 🟡 **PARTIAL PATTERNS** (Strengthened with additional variants)

- **Two Pointers Advanced** (have Container With Most Water, still need Trapping Rain Water **Hard**, 3Sum **Medium**)
- **Binary Search** (have Search in Rotated Array + PivotedBinarySearch, still need Find Peak Element **Medium**)
- **Array Rotation** (have RotateArrayByKElements - **NEW PATTERN ADDED**)
- **Cyclic Sort** (have Find Missing + FindSmallestMissingPositive, still need Find All Duplicates **Medium**)
- **Sorting + Greedy** (have Merge Intervals, missing Meeting Rooms **Medium**)
- **Partitioning** (have Sort Colors, missing Partition by Pivot **Medium**)
- **Hashing + Arrays** (have Two Sum & Majority & HackerRank Two Sum variant, still need Longest Consecutive **Medium**)

### ⬜ **PENDING** (7 remaining high-value problems)

1. Trapping Rain Water **Hard**
2. 3Sum **Medium**
3. Maximum Product Subarray **Medium**
4. Find First and Last Position **Medium**
5. Find Peak Element **Medium**
6. Meeting Rooms **Medium**
7. Longest Consecutive Sequence **Medium**
8. Find All Duplicates **Medium** (8 more to complete the 15 core problems)

---

## 📊 Coverage Analysis

**Problems You Already Have**: 8/15 (53%)
**Problems You Need**: 7/15 (47%)

Your current implementation covers the **foundational patterns**. The missing ones are **advanced variations** of
patterns you already know.

## 🎯 Priority Implementation Order

1. **Trapping Rain Water** (builds on Container With Most Water)
2. **3Sum** (builds on Two Sum + sorting)
3. **Longest Consecutive Sequence** (builds on hashing)
4. **Search in 2D Matrix** (builds on binary search)
5. **Sliding Window Maximum** (builds on your sliding window basics)
6. **Find All Duplicates** (builds on cyclic sort)
7. **Find Peak Element** (builds on binary search)

## 💡 Interview Strategy

- **Master the 8 you have** → 50% of interviews covered
- **Add the 7 missing ones** → 90%+ coverage
- **Focus on patterns, not memorization**

This list eliminates the "nice-to-have" problems and focuses on the "must-know" ones that actually appear in interviews.

# Top 15 Array Problems That Cover 90% of Interviews

Here's a **curated list of the most important array problems** that appear in coding interviews. These 15 problems
cover ~90% of array-related questions you'll encounter. Focus on these rather than trying to solve hundreds.

## 🏆 The Essential 15

### 1. **Two Sum** (Hashing)

- **Why**: Most common array problem
- **Variations**: Two Sum II (sorted), 3Sum, 4Sum
- **File**: `TwoSumMatchesTarget.java`

### 2. **Maximum Subarray Sum (Kadane)** (DP)

- **Why**: Classic DP on arrays
- **Variations**: Maximum Product Subarray, Circular Maximum Subarray
- **File**: `MaximumSubarraySumInaArray.java`

### 3. **Container With Most Water** (Two Pointers)

- **Why**: Advanced two pointers
- **Variations**: Trapping Rain Water, 3Sum
- **File**: `ContainerWithMostWater.java`

### 4. **Merge Intervals** (Sorting + Greedy)

- **Why**: Real-world scheduling problems
- **Variations**: Meeting Rooms, Insert Interval
- **File**: `MergeOverlappingIntervals.java`

### 5. **Subarray Sum Equals K** (Prefix Sum + Hashing)

- **Why**: Combines prefix sum with hash maps
- **Variations**: Continuous Subarray Sum, Subarray Sum Divisible by K
- **File**: `SubArraySumEqualsK.java`

### 6. **Sliding Window Maximum** (Sliding Window + Deque)

- **Why**: Advanced sliding window
- **Variations**: Minimum Window Substring, Longest Substring Without Repeating
- **Note**: You have basic sliding window, this is the advanced version

### 7. **Sort Colors (Dutch National Flag)** (Partitioning)

- **Why**: Multiple pointers partitioning
- **Variations**: Partition Array by Pivot, Move Zeros
- **File**: `SortColors.java`

### 8. **Find Peak Element** (Binary Search)

- **Why**: Binary search on unsorted arrays
- **Variations**: Search in Rotated Sorted Array, Find Minimum in Rotated Array
- **File**: `SearchInRotatedSortedArray.java` (related)

### 9. **Longest Consecutive Sequence** (Hashing)

- **Why**: Hash set for sequence detection
- **Variations**: Longest Increasing Subsequence (DP), Longest Common Subsequence
- **Note**: Missing from your current set

### 10. **Spiral Matrix** (2D Arrays)

- **Why**: 2D array traversal patterns
- **Variations**: Rotate Matrix, Set Matrix Zeroes, Diagonal Traverse
- **File**: `matrix/SpiralMatrix.java`

### 11. **Product of Array Except Self** (Prefix/Suffix Arrays)

- **Why**: Multiple pass techniques
- **Variations**: Trapping Rain Water (similar approach)
- **File**: `ProductExceptSelfInAnArray.java`

### 12. **Find All Duplicates in Array** (Cyclic Sort)

- **Why**: Index mapping techniques
- **Variations**: Find Missing Number, First Missing Positive
- **File**: `FindMissingNumber.java` (related)

### 13. **Trapping Rain Water** (Two Pointers/Stack)

- **Why**: Complex two pointers with height calculations
- **Variations**: Container With Most Water, Largest Rectangle in Histogram
- **Note**: Missing from your current set

### 14. **3Sum** (Two Pointers + Sorting)

- **Why**: Combination of sorting and two pointers
- **Variations**: 4Sum, Two Sum II
- **Note**: Missing from your current set

### 15. **Search in 2D Matrix** (Binary Search on 2D)

- **Why**: 2D binary search patterns
- **Variations**: Search a 2D Matrix II, Kth Smallest in Matrix
- **Note**: Missing from your current set

## 📊 Coverage Analysis

**Problems You Already Have**: 8/15 (53%)
**Problems You Need**: 7/15 (47%)

Your current implementation covers the **foundational patterns**. The missing ones are **advanced variations** of
patterns you already know.

## 🎯 Priority Implementation Order

1. **Trapping Rain Water** (builds on Container With Most Water)
2. **3Sum** (builds on Two Sum + sorting)
3. **Longest Consecutive Sequence** (builds on hashing)
4. **Search in 2D Matrix** (builds on binary search)
5. **Sliding Window Maximum** (builds on your sliding window basics)
6. **Find All Duplicates** (builds on cyclic sort)
7. **Find Peak Element** (builds on binary search)

## 💡 Interview Strategy

- **Master the 8 you have** → 50% of interviews covered
- **Add the 7 missing ones** → 90%+ coverage
- **Focus on patterns, not memorization**

This list eliminates the "nice-to-have" problems and focuses on the "must-know" ones that actually appear in interviews.
