# 🧠 Array Pattern Code Snippet Quiz (Based on Your Mistakes)

These questions target the exact logic areas where you got stuck:
- Pointer roles
- Loop stopping conditions
- Compaction vs swapping
- Stable vs unstable partition
- Boundary errors

Try to predict the output or fill the blank before expanding the answer.

---

## 1️⃣ What is the loop condition to correctly reverse an array?

```java
while ( _____ ) {
    int temp = arr[start];
    arr[start] = arr[end];
    arr[end] = temp;
    start++;
    end--;
}
```

<details>
<summary>Answer</summary>

`start < end`  
We stop when pointers meet or cross.
</details>

---

## 2️⃣ What will be the output?

```java
int[] arr = {1,2,3,4,5};
int start = 0, end = arr.length-1;
while(start <= end){
    int temp = arr[start];
    arr[start] = arr[end];
    arr[end] = temp;
    start++;
    end--;
}
System.out.println(Arrays.toString(arr));
```

<details>
<summary>Answer</summary>

`[1, 2, 3, 4, 5]`  
Because swaps continue after midpoint and undo the reverse.

YESSS great question — this is exactly the kind of boundary bug interviewers love 🔥

You are correct about the **problem**:
The loop runs **one step too far** and starts undoing the reverse.

---

## ❌ What’s Wrong in Your Code

```java
while(start <= end)
```

When `start == end` (middle element), you swap the element **with itself** — harmless.

But after that iteration:

```
start becomes greater than end
```

The previous swaps have already fully reversed the array, and the loop should have stopped earlier.

---

## ✅ FIX

Change the loop condition to:

```java
while (start < end)
```

---

## ✔ Correct Version

```java
int[] arr = {1,2,3,4,5};
int start = 0, end = arr.length-1;

while(start < end){
    int temp = arr[start];
    arr[start] = arr[end];
    arr[end] = temp;
    start++;
    end--;
}

System.out.println(Arrays.toString(arr));
```

### Output:

```
[5, 4, 3, 2, 1]
```

---

## 🧠 Rule to Remember

| Condition      | Meaning                         |
|----------------|---------------------------------|
| `start < end`  | Process pairs only              |
| `start == end` | Middle element (no swap needed) |
| `start > end`  | Already crossed, stop           |

Reversal is a **pair operation**, so only process while pairs exist.

---

🔥 This exact bug shows up in palindrome, reverse string, and mirror matrix problems too.

</details>

---

## 3️⃣ Fill in the blank (Move zeros compaction)

```java
for(int i=0;i<arr.length;i++){
    if(arr[i]!=0){
        arr[start] = arr[i];
        ______++;
    }
}
```

<details>
<summary>Answer</summary>

`start`
</details>

---

## 4️⃣ Why is this wrong?

```java
for(int i=0;i<arr.length;i++){
    arr[start] = arr[i];
    start++;
}
```

<details>
<summary>Answer</summary>

This moves ALL elements, not just non-zero ones. Compaction requires a condition.
</details>

---

## 5️⃣ What pattern is this?

```java
if(arr[i] < 0){
    int temp = arr[i];
    int j = i;
    while(j > start){
        arr[j] = arr[j-1];
        j--;
    }
    arr[start] = temp;
    start++;
}
```

<details>
<summary>Answer</summary>

Stable partition using insertion + shifting.
</details>

---

## 6️⃣ Output?

```java
int[] arr = {1,2,3,4};
for(int i=0;i<arr.length;i++){
    int temp = arr[0];
    arr[0] = arr[arr.length-1];
    arr[arr.length-1] = temp;
}
System.out.println(Arrays.toString(arr));
```

<details>
<summary>Answer</summary>

`[1, 2, 3, 4]` — swapping same elements repeatedly undoes changes.
</details>

---

## 7️⃣ Fill in blank (Palindrome)

```java
while(start < end){
    if(arr[start] != arr[end]) return false;
    start++;
    ______--;
}
```

<details>
<summary>Answer</summary>

`end`
</details>

---

## 8️⃣ What mistake exists?

```java
while(start > end){
    if(arr[start] != arr[end]) return false;
}
```

<details>
<summary>Answer</summary>

Loop never runs. Condition should be `start < end`.
</details>

---

## 9️⃣ Pattern type?

```java
if(arr[i] != arr[start]){
    start++;
    arr[start] = arr[i];
}
```

<details>
<summary>Answer</summary>

Two-pointer compaction (remove duplicates).
</details>

---

## 🔟 Why initialize max with Integer.MIN_VALUE?

<details>
<summary>Answer</summary>

So negative numbers are handled correctly.
</details>

---

## 11️⃣ Fill blank

```java
if(num > largest){
    secondLargest = largest;
    ______ = num;
}
```

<details>
<summary>Answer</summary>

`largest`
</details>

---

## 12️⃣ Output?

```java
int[] arr = {1,2,3,2,1};
boolean flag = true;
for(int i=0;i<arr.length/2;i++){
    if(arr[i]!=arr[arr.length-1-i]) flag=false;
}
System.out.println(flag);
```

<details>
<summary>Answer</summary>

true
</details>

---

## 13️⃣ Which pattern?

```java
for(int i=0;i<arr.length-1;i++){
    if(arr[i] > arr[i+1]) return false;
}
```

<details>
<summary>Answer</summary>

Neighbor comparison pattern (sorted check)
</details>

---

## 14️⃣ Output?

```java
int start=0,end=4;
while(start<end){
    start++;
}
System.out.println(end-start);
```

<details>
<summary>Answer</summary>

0 — loop stops when start reaches end.
</details>

---

## 15️⃣ Fill blank

Stable partition preserves ______.

<details>
<summary>Answer</summary>

relative order
</details>

---

## 16️⃣ Swap partition is stable? (Yes/No)

<details>
<summary>Answer</summary>

No
</details>

---

## 17️⃣ Output?

```java
int[] arr={0,1,0,3,12};
int start=0;
for(int i=0;i<arr.length;i++){
    if(arr[i]!=0){
        int temp=arr[start];
        arr[start]=arr[i];
        arr[i]=temp;
        start++;
    }
}
System.out.println(Arrays.toString(arr));
```

<details>
<summary>Answer</summary>

`[1,3,12,0,0]` but order of non-zeros preserved only by luck; it's unstable.
</details>

---

## 18️⃣ Fill blank

Compaction problems are usually solved in ______ pass.

<details>
<summary>Answer</summary>

single
</details>

---

## 19️⃣ Output?

```java
int[] arr={1,2,3,4,5};
for(int i=0;i<arr.length/2;i++){
    int temp=arr[i];
    arr[i]=arr[arr.length-1-i];
    arr[arr.length-1-i]=temp;
}
System.out.println(Arrays.toString(arr));
```

<details>
<summary>Answer</summary>

`[5,4,3,2,1]`
</details>

---

## 20️⃣ Fill blank

Before coding, first identify the ______.

<details>
<summary>Answer</summary>

pattern
</details>

---

🎯 These mirror your past confusion points. Practice daily for speed & clarity!
