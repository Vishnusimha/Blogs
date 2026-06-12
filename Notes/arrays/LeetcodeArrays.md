
````md
---
title: "LeetCode Arrays Practice Notes"
date: 2026-06-15
slug: "leetcode-arrays-practice-notes"
tags: [ "Notes", "Java", "Arrays", "LeetCode", "HashSet", "Intersection" ]
summary: "Detailed notes on solving the Intersection of Two Arrays problem on LeetCode using HashSet for optimal performance."
categories: Notes
readTime: 5
---

## Intersection of Two Arrays — Description

### Question

LeetCode 349 — Intersection of Two Arrays

Given two integer arrays `nums1` and `nums2`, return an array of their **intersection**.

Each element in the result must be **unique**, and you may return the result in **any order**.

Example 1

Input:
nums1 = [1,2,2,1]
nums2 = [2,2]

Output:
[2]

Example 2

Input:
nums1 = [4,9,5]
nums2 = [9,4,9,8,4]

Output:
[9,4]

Explanation:
[4,9] is also accepted.

Constraints:

1 <= nums1.length, nums2.length <= 1000  
0 <= nums1[i], nums2[i] <= 1000

Edge cases:

- arrays may contain duplicates
- intersection must contain **unique elements**
- order of output **does not matter**

---

### Thinking

* Traverse elements of `nums1`
* For each element, check whether it exists in `nums2`
* If a match is found, add it to the result list
* Stop checking further for that element once found
* Ensure duplicates are not added
* Convert the result list to `int[]`

#### Example

Input:

nums1 = [4,9,5]  
nums2 = [9,4,9,8,4]

Processing:

4 → found in nums2 → add  
9 → found in nums2 → add  
5 → not found

Output:

[4,9]

---

### ❌ Mistakes

#### Mistake 1 — Duplicate Values Added

What you wrote

```java
list.add(n);
````

Problem

If `nums1` contains duplicates:

nums1 = [1,2,2,1]
nums2 = [2,2]

Your result becomes:

[2,2]

But the problem requires:

[2]

Correct

Prevent duplicate insertion.

```java
if(!list.contains(n)){
    list.add(n);
}
```

Better solution uses **HashSet**.

---

#### Mistake 2 — Inefficient Nested Loop

What you wrote

```java
for nums1
    for nums2
```

Problem

Time complexity becomes:

O(n × m)

This works for small inputs but is not optimal.

Correct

Use a **HashSet lookup approach**.

```java
store nums1 elements in set
check nums2 elements in set
```

Lookup complexity becomes:

O(1)

Total complexity becomes:

O(n + m)

---

### ⚠ Minor Cleanup

Cleaner conversion from List to array.

Before

```java
return list.stream().mapToInt(Integer::intValue).toArray();
```

After (if using Set)

```java
return result.stream().mapToInt(Integer::intValue).toArray();
```

Removes duplicates automatically.

---

## ✅ Correct Mental Model

Use a **set lookup strategy**.

Pseudo code:

```
set1 = store nums1 elements
resultSet = empty set

for element in nums2
    if element exists in set1
        add element to resultSet

convert resultSet to int[]
return result
```

Time Complexity

O(n + m)

Space Complexity

O(n)

---

## 🧠 Pattern

HashSet Lookup Pattern

Pattern idea

```
Store elements in a set
Check existence using O(1) lookup
```

Used in:

* Two Sum
* Duplicate detection
* Longest consecutive sequence
* Array intersection problems

---

## 🏆 What You Did Right

✔ Correctly identified the need to compare both arrays
✔ Used `break` to avoid unnecessary comparisons
✔ Converted `List<Integer>` to `int[]` correctly using streams
✔ Implemented readable nested-loop logic first before optimizing

---

### Code

```java
class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {

        Set<Integer> set1 = new HashSet<>();
        Set<Integer> result = new HashSet<>();

        for(int n : nums1){
            set1.add(n);
        }

        for(int n : nums2){
            if(set1.contains(n)){
                result.add(n);
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
```

---
