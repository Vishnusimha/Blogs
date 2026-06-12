---
title: "Array & Two-Pointer Brain Teaser Quiz"
date: 2026-06-17
slug: "array-two-pointer-brain-teaser-quiz"
tags: [ "Notes", "Java", "Arrays", "Quiz", "Two-Pointer", "DSA" ]
summary: "A 30-question brain teaser quiz designed to sharpen your instincts for array and two-pointer problems, covering loop conditions, stability, and common patterns."
categories: Notes
readTime: 8
---

# 🧠 Array & Two-Pointer Brain Teaser Quiz (30 Questions)

Sharpen your problem-solving instincts. Try to answer before expanding the solution.

---

## 1. Fill in the blank  
To reverse an array using two pointers, the loop should run while:  
`while ( _____ )`

<details>
<summary>Answer</summary>

`start < end`  
We stop when pointers meet or cross.
</details>

---

## 2. Multiple Choice  
Which pattern is used in "move zeros to end"?  
A) Swapping  
B) Recursion  
C) Compaction  
D) Binary Search  

<details>
<summary>Answer</summary>

C) Compaction — we overwrite non-zero elements forward.
</details>

---

## 3. Fill in the blank  
In compaction problems, one pointer reads, the other pointer ______.

<details>
<summary>Answer</summary>

writes (or places elements).
</details>

---

## 4. True / False  
A palindrome array must have even length.

<details>
<summary>Answer</summary>

False — odd-length palindromes are valid.
</details>

---

## 5. Fill in the blank  
To avoid index out of bounds when checking `arr[i+1]`, loop until:  
`i < ______`

<details>
<summary>Answer</summary>

`arr.length - 1`
</details>

---

## 6. Multiple Choice  
Which technique preserves order while partitioning?  
A) Swapping  
B) Stable compaction  
C) Sorting  
D) Hashing  

<details>
<summary>Answer</summary>

B) Stable compaction
</details>

---

## 7. Fill in the blank  
When tracking the maximum value, initialize with ______.

<details>
<summary>Answer</summary>

`Integer.MIN_VALUE`
</details>

---

## 8. Fill in the blank  
When counting items, initialize the counter with ______.

<details>
<summary>Answer</summary>

0
</details>

---

## 9. True / False  
Swapping always preserves relative order.

<details>
<summary>Answer</summary>

False — swapping can change order.
</details>

---

## 10. Fill in the blank  
In palindrome check, pointers move ______ direction.

<details>
<summary>Answer</summary>

towards each other (opposite directions)
</details>

---

## 11. Multiple Choice  
Remove duplicates from sorted array uses:  
A) HashMap  
B) Sorting  
C) Two-pointer compaction  
D) Recursion  

<details>
<summary>Answer</summary>

C) Two-pointer compaction
</details>

---

## 12. Fill in the blank  
Strictly increasing means `arr[i] ____ arr[i+1]`

<details>
<summary>Answer</summary>

`<`
</details>

---

## 13. Fill in the blank  
Non-decreasing means `arr[i] ____ arr[i+1]`

<details>
<summary>Answer</summary>

`<=`
</details>

---

## 14. True / False  
Reverse array requires full traversal.

<details>
<summary>Answer</summary>

False — only half traversal needed.
</details>

---

## 15. Fill in the blank  
In two-pointer compaction, write pointer moves only when ______.

<details>
<summary>Answer</summary>

a valid element is placed
</details>

---

## 16. Multiple Choice  
Which is NOT a two-pointer opposite-direction problem?  
A) Reverse array  
B) Palindrome  
C) Move zeros  
D) Check symmetry  

<details>
<summary>Answer</summary>

C) Move zeros
</details>

---

## 17. Fill in the blank  
The stop condition in reverse logic is `start ____ end`.

<details>
<summary>Answer</summary>

>=
</details>

---

## 18. Fill in the blank  
Stable partitioning may require ______ instead of swapping.

<details>
<summary>Answer</summary>

shifting elements
</details>

---

## 19. True / False  
A sorted array can contain duplicates.

<details>
<summary>Answer</summary>

True
</details>

---

## 20. Fill in the blank  
To find second largest, we must update ______ before largest.

<details>
<summary>Answer</summary>

secondLargest
</details>

---

## 21. Multiple Choice  
Time complexity of single pass max search:  
A) O(n²)  
B) O(n)  
C) O(log n)  
D) O(1)  

<details>
<summary>Answer</summary>

B) O(n)
</details>

---

## 22. Fill in the blank  
Two-pointer compaction is also called ______.

<details>
<summary>Answer</summary>

stable overwrite pattern
</details>

---

## 23. Fill in the blank  
When moving negatives stably, we insert at index ______.

<details>
<summary>Answer</summary>

start
</details>

---

## 24. True / False  
`while(start > end)` is correct for palindrome.

<details>
<summary>Answer</summary>

False
</details>

---

## 25. Fill in the blank  
If loop logic depends on pointer positions, prefer ______ over for-loop.

<details>
<summary>Answer</summary>

while loop
</details>

---

## 26. Multiple Choice  
Which is multi-variable tracking?  
A) Count evens  
B) Find max  
C) Third largest  
D) Reverse array  

<details>
<summary>Answer</summary>

C) Third largest
</details>

---

## 27. Fill in the blank  
When comparing neighbors, this is called ______ pattern.

<details>
<summary>Answer</summary>

neighbor comparison
</details>

---

## 28. Fill in the blank  
Swap-based partition is usually ______ (stable / unstable).

<details>
<summary>Answer</summary>

unstable
</details>

---

## 29. Fill in the blank  
Palindrome check complexity is O( ____ ).

<details>
<summary>Answer</summary>

n
</details>

---

## 30. Fill in the blank  
Before coding, first identify the ______ of the problem.

<details>
<summary>Answer</summary>

pattern
</details>

---

🎯 Practice these daily for faster recognition and fewer mistakes!
