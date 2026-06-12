---
title: "WorkManager in Android: Practical Examples and Best Practices"
date: 2026-06-12
slug: "workmanager-android-practical-examples-best-practices"
tags: [ "Notes", "Android", "WorkManager", "Background Work", "Periodic Work", "Chained Work", "Constraints" ]
summary: "Personal Android background work notes explaining WorkManager setup, Worker classes, input data, constraints, work status observation, periodic work, chained work, and practical scheduling use cases."
categories: Notes
readTime: 12
---

# Work manager example in android

The example I provided is **a basic and
functional example**, but it can be improved by incorporating best practices and more realistic use cases. Let’s refine
it further for better **readability, error handling, and real-world applicability**.

---

### 🔹 **Improved WorkManager Example in Android (Kotlin)**

This example includes:
✅ **Worker with Input Data**  
✅ **Constraints (e.g., network requirement)**  
✅ **Observing Work Status**

#### **1️⃣ Add Dependencies** *(if not already added)*

```gradle
dependencies {
    implementation "androidx.work:work-runtime-ktx:2.7.0"
}
```

#### **2️⃣ Create a Worker Class**

```kotlin
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val inputData = inputData.getString("task_data") ?: "No input"
            Log.d("MyWorker", "Background work running: $inputData")

            // Simulate some work
            Thread.sleep(3000)

            Result.success()
        } catch (e: Exception) {
            Log.e("MyWorker", "Work failed", e)
            Result.failure()
        }
    }
}
```

---

#### **3️⃣ Enqueue Work with Constraints & Data**

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define constraints (e.g., require network)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Pass input data to worker
        val inputData = workDataOf("task_data" to "WorkManager Example")

        // Create a OneTime WorkRequest
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        // Enqueue the work
        WorkManager.getInstance(this).enqueue(workRequest)

        // Observe work status
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> println("Work Enqueued")
                        WorkInfo.State.RUNNING -> println("Work Running")
                        WorkInfo.State.SUCCEEDED -> println("Work Completed Successfully")
                        WorkInfo.State.FAILED -> println("Work Failed")
                        WorkInfo.State.CANCELLED -> println("Work Cancelled")
                        else -> {}
                    }
                }
            })
    }
}
```

---

### 🎯 **Why is this a Better Example?**

✔ Uses **Kotlin** (Modern Android Development)  
✔ Includes **Constraints** (Requires Network)  
✔ Handles **Input Data**  
✔ Uses **Error Handling** (`try-catch` in `doWork()`)  
✔ Implements **Live Observation** of Work Status

### 🚀 **Next Steps**

🔹 If you need **repeated work**, use `PeriodicWorkRequest` instead of `OneTimeWorkRequest`.  
🔹 You can also **chain works** using `WorkManager.beginWith()`.

## Examples of **chained work** or **periodic work**? 😊

Great! Let’s go step by step with both **Periodic Work** and **Chained Work** using WorkManager.

---

## ✅ **1️⃣ Periodic Work (Repeated Work)**

### 🔹 **Scenario:** Run a task every 15 minutes (minimum time allowed by WorkManager).

### **Steps:**

1️⃣ Create a `Worker` class  
2️⃣ Schedule a `PeriodicWorkRequest`

---

### **📌 Step 1: Create the Worker Class**

```kotlin
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodicWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("PeriodicWorker", "Periodic work is running...")
        return Result.success()
    }
}
```

---

### **📌 Step 2: Schedule Periodic Work**

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define constraints (e.g., requires charging)
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        // Schedule periodic work every 15 minutes
        val periodicWorkRequest = PeriodicWorkRequestBuilder<PeriodicWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}
```

### **🛠 Key Notes on Periodic Work**

✔ **Minimum interval is 15 minutes** (WorkManager restriction).  
✔ Uses **setRequiresCharging(true)** to run only when the device is charging.  
✔ Can be used for **background sync, logs, notifications, etc.**

---

## ✅ **2️⃣ Chained Work (Sequential Execution of Tasks)**

### 🔹 **Scenario:** Run multiple tasks sequentially (e.g., Step 1 → Step 2 → Step 3)

---

### **📌 Step 1: Create Worker Classes**

```kotlin
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class Step1Worker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("Step1Worker", "Step 1 is running...")
        return Result.success()
    }
}

class Step2Worker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("Step2Worker", "Step 2 is running...")
        return Result.success()
    }
}

class Step3Worker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("Step3Worker", "Step 3 is running...")
        return Result.success()
    }
}
```

---

### **📌 Step 2: Chain the Work Requests**

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create Work Requests
        val step1 = OneTimeWorkRequest.Builder(Step1Worker::class.java).build()
        val step2 = OneTimeWorkRequest.Builder(Step2Worker::class.java).build()
        val step3 = OneTimeWorkRequest.Builder(Step3Worker::class.java).build()

        // Chain work requests
        WorkManager.getInstance(this)
            .beginWith(step1)  // Start with Step 1
            .then(step2)       // Then execute Step 2
            .then(step3)       // Then execute Step 3
            .enqueue()
    }
}
```

---

### **🎯 Why Use Chained Work?**

✔ Ensures **sequential execution** of background tasks.  
✔ Useful for **uploading files in order, multi-step processes, etc.**  
✔ Can handle **dependent work** where one task needs the previous one to complete first.

---

## 🚀 **Final Thoughts**

- **Use `PeriodicWorkRequest` for repeated work** (e.g., syncing data every X minutes).
- **Use work chaining (`beginWith()`) for dependent background tasks** (e.g., Step 1 → Step 2 → Step 3).
- **You can combine both** (e.g., periodic work that executes a chain of tasks).

Would you like an example where both **chaining and periodic work** are combined? 😊