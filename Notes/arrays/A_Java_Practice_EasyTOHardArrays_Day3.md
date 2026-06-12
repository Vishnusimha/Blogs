# A Java Practice: Easy to Hard Arrays - Day 3

## Table of Contents

## Content

[RotateArrayByKElements.java](day3/RotateArrayByKElements.java)

You are referring to the **array rotation using the reverse algorithm** (the O(1) space solution). Let’s break it down
in a structured way so the pointer logic becomes intuitive.

### Problem

Rotate array to the **right by `k` steps**.

Example:
`[1,2,3,4,5,6,7]`, `k = 3`

Result should be:
`[5,6,7,1,2,3,4]`

---

# 1️⃣ The Core Idea (Strategic Decomposition)

Instead of shifting elements repeatedly, we **restructure the array using reversals**.

We split the array conceptually into **two segments**:

```
Original:
[1 2 3 4 | 5 6 7]
  first      last k elements
```

The **last k elements must move to the front**.

---

# 2️⃣ Three Reverse Operations

Algorithm:

```
reverse(0, n-1)
reverse(0, k-1)
reverse(k, n-1)
```

---

# 3️⃣ Step-by-Step Transformation

### Step 1 — Reverse Entire Array

`reverse(0, n-1)`

```
[1 2 3 4 5 6 7]
      ↓
[7 6 5 4 3 2 1]
```

Now the elements that must move to front (`5,6,7`) are already at the beginning but **in reversed order**.

---

### Step 2 — Fix First Segment

`reverse(0, k-1)`

Why `k-1`?

Because the **first k elements belong together**.

```
[7 6 5 | 4 3 2 1]
 ↓
[5 6 7 | 4 3 2 1]
```

So the **right pointer = k-1** because indexing starts from **0**.

Segment length = `k`

Indices:

```
0 ..... k-1
```

---

### Step 3 — Fix Remaining Segment

`reverse(k, n-1)`

Now fix the rest.

```
[5 6 7 | 4 3 2 1]
        ↓
[5 6 7 | 1 2 3 4]
```

---

# 4️⃣ Why `reverse(k, n-1)` ?

Because the **remaining segment starts at index `k`**.

Example:

```
n = 7
k = 3
```

Index positions:

```
0 1 2 3 4 5 6
| first k | rest |
```

First segment: `0 → k-1`
Second segment: `k → n-1`

So:

```
reverse(3,6)
```

---

# 5️⃣ Visual Index Understanding

```
Index:  0 1 2 3 4 5 6
Array: [1 2 3 4 5 6 7]

k = 3

Segments:

0..2   → first segment
3..6   → second segment
```

Thus:

```
reverse(0,2)
reverse(3,6)
```

---

# 6️⃣ Mental Model (Most Important)

Whenever you see reverse ranges:

```
left pointer = start index
right pointer = end index
```

So for **k elements starting from 0**:

```
start = 0
end = k-1
```

Because:

```
count = end - start + 1
k = (k-1 - 0 + 1)
```

---

# 7️⃣ Code Reference

```java
public void rotate(int[] nums, int k) {
    int n = nums.length;
    k = k % n;

    reverse(nums, 0, n - 1);
    reverse(nums, 0, k - 1);
    reverse(nums, k, n - 1);
}

private void reverse(int[] nums, int l, int r) {
    while (l < r) {
        int temp = nums[l];
        nums[l] = nums[r];
        nums[r] = temp;
        l++;
        r--;
    }
}
```

---

# 8️⃣ Golden Rule for Interview Thinking

Whenever you rotate:

```
Right rotation by k

split index = n-k
```

Segments:

```
[0 → n-k-1]
[n-k → n-1]
```

Reverse strategy rearranges these efficiently.

---

✅ **Key Insight:**
`k-1` and `n-1` come from **0-based indexing and segment boundaries**.

---

If you want, I can also show you a **super powerful visualization trick interviewers expect**, which makes you *
*instantly derive the reverse ranges without memorizing them.** It makes this problem trivial in interviews.


---
[FindSmallestMissingPositive.java](../leetcode/FindSmallestMissingPositive.java)

# 🧠 Core Mental Model (Cyclic Sort Thinking)

### 🎯 Goal

```text
Place each number x at index (x - 1)
```

---

## 🔁 Step 1 — Think in Terms of “Correct Position”

```text
Value → Correct Index
1     → 0
2     → 1
3     → 2
...
n     → n-1
```

👉 So for any value `v`:

```java
correctIndex =v -1;
```

---

## 🔄 Step 2 — Rearrange In-Place

For each index `i`:

```text
If current value is valid (1 to n)
AND not already in correct position
→ swap it to correct position
```

---

### 🔥 Core Loop Idea

```text
While current number is NOT in correct place:
    swap it to its correct place
```

👉 Why **while**?
Because after one swap, the new number at index `i` might also be wrong.

---

## ⚠️ Step 3 — Ignore Invalid Values

Skip if:

```text
value <= 0  ❌
value > n   ❌
```

👉 These can never occupy valid indices → ignore

---

## 🔍 Step 4 — After Rearrangement

Now array looks like:

```text
[1, 2, ?, 4, ...]
```

👉 First place where:

```text
index i != value (i + 1)
```

That’s your answer.

---

## 🧠 Final Logic

```text
1. Place numbers in correct index positions
2. Scan array:
   first mismatch → answer
3. If no mismatch → answer = n + 1
```

---

## 🔥 Example Walkthrough

```text
Input: [3, 4, -1, 1]
```

### Step 1: Rearrange

```text
[3,4,-1,1]

i=0 → 3 → index 2 → swap
[-1,4,3,1]

i=0 → -1 ❌ skip

i=1 → 4 → index 3 → swap
[-1,1,3,4]

i=1 → 1 → index 0 → swap
[1,-1,3,4]
```

---

### Step 2: Scan

```text
index 0 → 1 ✅
index 1 → -1 ❌ → expected 2
```

👉 Answer = **2**

---

## ⚠️ Key Insights (VERY IMPORTANT)

```text
✔ You are NOT sorting
✔ You are PLACING
✔ Each number tries to go to its "home index"
```

---

## ❗ Why O(1) Space?

```text
You are using the array itself as a hash structure
index = position
value = identity
```

---

## 🧠 Pattern Name

```text
Cyclic Sort / Index Mapping
```

---

## 🧠 When to Use This Pattern

```text
✔ Array contains numbers in range [1...n]
✔ Need smallest missing / duplicate / misplaced
✔ Constraint: O(n) + O(1) space
```

---

## 🏁 Final One-Line Thinking

```text
“Put every number in its correct index, then find the first missing spot.”
```

---

Perfect — this is where you move from **problem-solving → pattern recognition (interview mastery)** 🚀

---

## [TwoSum.java](day3/TwoSum.java)

# 🧠 The “Two Sum Pattern” (High ROI Pattern)

## 🔑 Core Signal (When to detect it)

Whenever you see:

* ✔ “Find **two elements**”
* ✔ “Sum = target”
* ✔ “Return indices / pair”
* ✔ Array is **unsorted**

👉 Trigger this mental model instantly:

```text
current + x = target
→ x = target - current
```

---

# ⚡ Pattern Blueprint (Reusable Framework)

## Step-by-step thinking:

```text
1. Loop through array
2. For each element:
      needed = target - current
3. Check if needed already exists
4. If yes → DONE
5. Else → store current
```

---

# 🧩 Generic Template (Memorize this)

```java
Map<Integer, Integer> map = new HashMap<>();

for (int i = 0; i < n; i++) {
    int current = arr[i];
    int needed = target - current;

    if (map.containsKey(needed)) {
        return answer;
    }

    map.put(current, i);
}
```

👉 This template alone solves **multiple problems**

---

# 🔥 Where this pattern appears (VERY IMPORTANT)

## 1. Classic Two Sum

👉 Exactly your problem

---

## 2. Two Sum II (Sorted Array)

👉 Use **two pointers instead of HashMap**

```text
left + right → adjust pointers
```

---

## 3. Three Sum

👉 Fix one element + apply Two Sum

```text
for i:
   solve two sum on remaining array
```

---

## 4. Subarray Sum Equals K

👉 Same idea but with **prefix sum**

```text
prefixSum - k = needed
```

---

## 5. Pair with Given Difference

```text
current - target OR current + target
```

---

## 6. Count Pairs Instead of Returning

```text
count += map.get(needed)
```

---

# 🧠 Recognition Shortcut (Interview Hack)

If you see:

```text
pair + target + array
```

👉 Ask yourself immediately:

> “Can I convert this into **target - current lookup**?”

If YES → HashMap solution

---

# ⚖️ Decision Matrix (VERY IMPORTANT)

| Scenario       | Approach            |
|----------------|---------------------|
| Unsorted array | ✅ HashMap           |
| Sorted array   | ✅ Two pointers      |
| Need all pairs | ⚠️ Modified HashMap |
| Need count     | ⚠️ Frequency map    |
| Subarray       | ✅ Prefix sum + map  |

---

# 🚀 Your Mental Upgrade (This is key)

Before:

```text
“Check all pairs”
```

After:

```text
“Can I avoid re-checking using memory?”
```

👉 That’s the shift from **O(n²) → O(n)** thinking

---

# 🏁 Interview One-Liner (Use this)

When explaining:

> “I optimize the brute-force approach by storing visited elements in a HashMap and checking if the complement (target - current) exists, reducing time complexity to O(n).”

---

# 🔥 Next Step (High Value)

You should practice these **in order**:

1. ✅ Two Sum (done)
2. 🔜 Two Sum II (sorted)
3. 🔜 Three Sum
4. 🔜 Subarray Sum = K

---

## [NonIdenticalStringRotation.java](day3/hackerrank/NonIdenticalStringRotation.java)

---

## ✅ What you got right

### ✔ Core rotation logic

```java
String s = s1 + s1;
s.

contains(s2)
```

👉 This is the **optimal and standard solution**

---

### ✔ Non-trivial condition

```java
if(s1.equals(s2))
```

👉 Correctly eliminates identical strings

---

### ✔ Length validation

```java
s1.length() !=s2.

length()
```

👉 Prevents invalid comparisons

---

### ✔ Final return logic

```java
return false;
```

👉 Fixed the earlier bug — now behaves correctly

---

## 🧠 Final Mental Model (Lock this in)

```text
Rotation = substring of (s1 + s1)
```

---

## ⚡ Complexity

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

👉 This is **optimal** for this problem

---

## 🚀 Micro-Optimization (Cleaner Enterprise Style)

You can compress everything into one return:

```java
return s1.length() >0&&
        s1.

length() ==s2.

length() &&
        !s1.

equals(s2) &&
        (s1 +s1).

contains(s2);
```

---

## 🧠 Pattern Positioning (Important)

You just used:

```text
String Pattern → Rotation via concatenation
```

This is different from:

| Pattern             | Use case           |
|---------------------|--------------------|
| HashMap             | frequency / lookup |
| Two pointers        | sorted arrays      |
| Sliding window      | substrings         |
| **String doubling** | rotations          |

---

## 🏁 Final Verdict

| Area                | Status    |
|---------------------|-----------|
| Logic               | ✅ Perfect |
| Edge cases          | ✅ Covered |
| Performance         | ✅ Optimal |
| Interview readiness | 🔥 Strong |

---

Perfect — this is exactly the mindset shift you need. Let’s **lock this permanently in your brain** in a simple, repeatable way.

---

# 🧠 WHAT YOU SHOULD MEMORIZE (CORE IDEA)

### 🔑 Golden Rule:

```text
In a rotated sorted array → at least one half is always sorted
```

👉 This is **NOT obvious**, so you must **remember it as a fact**, not derive it every time.

---

# ⚡ HOW DO YOU DETECT WHICH HALF IS SORTED?

### 🔍 One simple condition:

```java
if (nums[low] <= nums[mid])
```

👉 Then:

```text
LEFT HALF is sorted
```

Else:

```text
RIGHT HALF is sorted
```

---

# 🧠 WHY THIS WORKS (INTUITION)

Normal sorted:

```text
[1,2,3,4,5,6,7]
```

Rotated:

```text
[4,5,6,7,1,2,3]
```

👉 Break happens at pivot
👉 But **one side still remains cleanly sorted**

---

# 🧭 YOUR STEP-BY-STEP THINKING (MEMORIZE THIS FLOW)

## 🔁 Every iteration:

### 1. Find mid

```text
mid = (low + high) / 2
```

---

### 2. Found target?

```text
nums[mid] == target → DONE
```

---

### 3. Find sorted half

```text
nums[low] <= nums[mid] → left sorted
else → right sorted
```

---

### 4. Check if target is inside sorted half

#### If LEFT sorted:

```text
target >= nums[low] && target < nums[mid]
→ go LEFT
else → go RIGHT
```

---

#### If RIGHT sorted:

```text
target > nums[mid] && target <= nums[high]
→ go RIGHT
else → go LEFT
```

---

# 🧠 SIMPLE MEMORY TRICK (VERY IMPORTANT)

```text
1. Find sorted half
2. Check if target is inside it
3. If yes → go there
4. Else → go other side
```

---

# ❗ WHAT YOU SHOULD STOP DOING

❌ Don’t try:

* checking if full array is sorted
* scanning elements
* doing linear thinking

👉 That’s slow thinking (O(n))

---

# 🚀 WHAT YOU SHOULD START THINKING

Instead of:

```text
“Where is the target?”
```

Think:

```text
“Which side is sorted, and is target inside it?”
```

---

# 🔥 WHY ONLY ONE CONDITION WORKS

You said:

> “Usually we do long process to check sorted”

👉 Here’s the trick:

```text
nums[low] <= nums[mid]
```

This works because:

* In a rotated array, **only one place is broken**
* So comparing just these two tells the story

👉 That’s why it’s powerful

---

# 🧠 PATTERN TAG (IMPORTANT FOR INTERVIEWS)

Whenever you see:

```text
sorted array + rotated + search
```

👉 Immediately think:

```text
Modified Binary Search
```

---

# 🏁 FINAL CHECKLIST (MEMORIZE THIS)

Before coding, ask:

```text
1. Is array rotated sorted? ✅
2. Can I use binary search? ✅
3. Can I detect sorted half? ✅
4. Can I check target range? ✅
```

👉 Then solve

---

# 🎯 ONE-LINE INTERVIEW ANSWER

> “Since the array is rotated, I use modified binary search by identifying the sorted half and checking whether the target lies within it to decide the search direction.”

---

# 🔥 FINAL TAKEAWAY

```text
You are not searching for the target directly.

You are:
→ finding sorted half
→ checking range
→ reducing search space
```

---

## [MaxUniqueSubStringLength.java](day3/hackerrank/MaxUniqueSubStringLength.java)

---

## Thinking process:

1. This problem is a variation of **Longest Substring Without Repeating Characters**, but with an additional constraint
   of session separation using `'*'`.
2. Each session (substring without `'*'`) must be treated **independently**.
3. Within each session, we need to find the **longest substring with all unique characters**.
4. This naturally leads to the **Sliding Window (Two Pointer)** approach.
5. Use a **HashSet** to track characters currently in the window.
6. Expand the window using the right pointer (`i`), and when a duplicate is found, shrink the window from the left
   pointer until the duplicate is removed.
7. When encountering `'*'`, reset the window because sessions are independent.
8. Track the maximum length of valid substrings across all sessions.

---

#### ⏱ Time Complexity: O(n)

* Each character is processed at most twice:

    * Once by the right pointer
    * Once by the left pointer
* No nested loops in terms of total operations
* Optimal for this problem

---

#### 🧠 Space Complexity: O(k)

* `HashSet` stores at most `k` unique characters
* Here, `k ≤ 26` (only lowercase letters)
* Effectively constant space → **O(1)**

---

## 🧩 Pattern this problem belongs to:

```text
Sliding Window + HashSet + Reset Condition
```

---

### 🔑 Key Patterns Used:

1. **Sliding Window (Expand + Shrink)**
2. **Two Pointers (left, right)**
3. **Duplicate Handling using HashSet**
4. **Segment Reset (on '*')**

---

## 🚀 Core Algorithm Idea:

```text
Expand window → if duplicate → shrink  
Encounter '*' → reset window  
Track max length continuously
```

---

## ⚠️ Edge Cases to Remember:

* Empty string → return 0
* Only '*' → return 0
* Single character → return 1
* Multiple '*' → treat each segment independently

---

## Where I stuck:

---

### 🧠 Your Biggest Conceptual Confusion (Most Important)

> You were mixing **window reset vs window shrink**

You initially thought:

* On duplicate → reset everything ❌
* Instead of shrinking gradually

And later:

* You handled duplicates correctly
* But missed handling `'*'` as a reset boundary

---

### ⚠️ Specific Confusions:

1. Using `set.clear()` for duplicates ❌
2. Not maintaining a proper **left pointer**
3. Using `if` instead of `while` for duplicate removal
4. Not recognizing `'*'` splits independent problems
5. Confusion between:

    * “restart logic” vs
    * “adjust window logic”

---

## 📊 Final Skill Assessment (Honest)

### Strengths you showed:

* Correct identification of sliding window pattern
* Good use of HashSet for duplicate detection
* Proper understanding of two pointers
* Strong debugging and iterative refinement
* Eventually handled session reset correctly

---

### Actual mistakes:

* Resetting instead of shrinking window
* Misunderstanding duplicate handling
* Missing segment-based logic (`'*'`)
* Edge case handling (`"*"` case)
* Small logical condition mistakes (`while` vs `if`)

---

# 🎯 One-Line Root Cause of All Your Confusion

> You understood sliding window mechanics, but not the invariant:
> **“The window always contains unique characters within a single session.”**

Once this invariant is clear:

* You won’t reset unnecessarily
* You’ll shrink correctly
* You’ll handle `'*'` as a boundary naturally

---

# 🧠 Final Mental Model (VERY IMPORTANT)

```text
Window = valid substring with unique chars
Duplicate → shrink window
'*' → reset window completely
```

---

# 🔥 Pattern Recognition Trigger

Whenever you see:

```text
substring + unique characters + longest
```

👉 Immediately think:

```text
Sliding Window + Set
```

If you also see:

```text
separator like '*'
```

👉 Add:

```text
Reset logic
```

---

# 🚀 Upgrade Path

Next problems you should practice:

1. Longest Substring Without Repeating Characters
2. Longest Substring with At Most K Distinct Characters
3. Minimum Window Substring
4. Sliding Window Maximum
5. Longest Repeating Character Replacement

---
