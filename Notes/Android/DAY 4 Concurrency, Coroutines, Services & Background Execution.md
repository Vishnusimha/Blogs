# Technical interview questions

#### DAY 4 — Concurrency, Coroutines, Services & Background Execution

1. Explain the difference between services and threads in Android.
2. What are the different threading models available in Android?
3. What are the differences between Services and IntentService?
4. What is the purpose of the AsyncTaskLoader in Android?
5. What is the purpose of the JobScheduler API in Android?
6. How do you handle background tasks efficiently in Android?

## Answers:

#### Day 4 Answers

### 1. Explain the difference between services and threads in Android.

- **Services**: Used for long-running operations that don't require user interaction. They run on
  the main thread unless
  otherwise specified.
- **Threads**: Used for performing tasks off the main thread to prevent UI blocking. They should be
  used for shorter
  tasks or tasks that need to interact with the UI.

### 2. What are the different threading models available in Android?

* **Main (UI) Thread**
  Handles UI updates, user interactions, and lifecycle callbacks.
  ❗ Must not perform long-running or blocking operations.

* **Worker Threads**
  Used for long-running operations like network calls, disk IO, and heavy computation to avoid
  blocking the UI thread.

* **HandlerThread** ⚠️ *(Legacy / Limited Use)*
  A background thread with its own `Looper`, useful for sequential background tasks.
  Still works, but largely replaced by **coroutines** and **WorkManager**.

* **ThreadPoolExecutor** ⚠️ *(Low-level / Rarely used directly)*
  Manages a pool of worker threads.
  Powerful but complex; typically abstracted by libraries or coroutines.

* **AsyncTask** ❌ **(Deprecated)**
  Previously used for simple background tasks with UI callbacks.
  Deprecated due to lifecycle issues, memory leaks, and poor cancellation support.

* **Kotlin Coroutines** ✅ **(Recommended / Modern)**
  Modern, lightweight, lifecycle-aware approach for asynchronous and concurrent tasks.
  Uses structured concurrency and dispatchers (`Main`, `IO`, `Default`).

#### Quick Highlights 🚀

* ❌ **AsyncTask** → Deprecated
* ⚠️ **HandlerThread / ThreadPoolExecutor** → Legacy / low-level
* ✅ **Kotlin Coroutines** → Modern standard

Here’s a **perfect one-liner answer** you can confidently say 👇

> **“Threads are low-level, heavyweight, and manually managed, while coroutines are lightweight, structured, lifecycle-aware, and suspend without blocking threads, making them the preferred modern approach in Android.”**

If you want an even **shorter punch version**:

> **“Coroutines are not threads; they run on threads but suspend without blocking, making async code safer and simpler.”**

### Threads vs Coroutines

| Aspect             | Threads                | Coroutines                    |
|--------------------|------------------------|-------------------------------|
| Level              | Low-level OS construct | High-level Kotlin abstraction |
| Weight             | Heavyweight            | Lightweight                   |
| Creation cost      | Expensive              | Very cheap                    |
| Count              | Limited (hundreds)     | Can be thousands              |
| Blocking           | Blocks the thread      | Suspends without blocking     |
| Lifecycle handling | Manual                 | Structured & scope-based      |
| Cancellation       | Hard & unsafe          | Cooperative & safe            |
| Error handling     | Complex                | Built-in (try/catch)          |
| Readability        | Callback-heavy         | Sequential, readable code     |
| Android usage      | Legacy / low-level     | Modern standard               |

### Interview-ready takeaway (memorize this)

> **“Threads are heavyweight and manually managed, while coroutines are lightweight, lifecycle-aware, and suspend without blocking threads.”**

---

### **runBlocking vs launch vs async**

| Feature        | runBlocking | launch          | async              |
|----------------|-------------|-----------------|--------------------|
| Blocks thread  | ✅ Yes       | ❌ No            | ❌ No               |
| Returns value  | ❌ No        | ❌ No            | ✅ Yes (`Deferred`) |
| Returns handle | ❌ No        | `Job`           | `Deferred<T>`      |
| Cancellation   | ❌ Manual    | ✅ Yes           | ✅ Yes              |
| Android usage  | ❌ Never     | ✅ Yes           | ✅ Yes              |
| Typical use    | tests       | fire-and-forget | parallel results   |


### 3. What are the differences between Services and IntentService?

- **Service**: Can perform long-running operations and runs on the main thread by default. Needs
  explicit management of
  threading.
- **IntentService**: A subclass of Service that handles asynchronous requests on a separate worker
  thread. Automatically
  stops itself when the task is completed.

---

<div id="ServiceLifecycleCallbacks"></div>
<details>
<summary><b>🔴 <font color="red">Service Lifecycle Callbacks (ELABORATED)</font> 🔴</b></summary>
<br>
<blockquote>

## Service Lifecycle Callbacks

The lifecycle of a Service in Android is crucial to understand as it dictates how the service is
created, started,
stopped, and destroyed. Below is a detailed look at the lifecycle of a Service, including callbacks
and their typical
usage.

### 1. **onCreate()**

- **Called:** When the service is first created.
- **Purpose:** Initialize the service. This method is called only once during the entire lifecycle
  of the service.
- **Typical Usage:** Set up resources such as threads, receivers, etc.

### 2. **onStartCommand(Intent intent, int flags, int startId)**

- **Called:** Every time a client starts the service using `startService(Intent intent)`.
- **Purpose:** Defines what happens when the service is started. It handles each start request by
  executing the
  corresponding task.
- **Typical Usage:** Perform the task, then stop the service if necessary using `stopSelf()` or
  `stopSelf(startId)`.

### 3. **onBind(Intent intent)**

- **Called:** When a client binds to the service using
  `bindService(Intent service, ServiceConnection conn, int flags)`.
- **Purpose:** Return an `IBinder` that clients use to communicate with the service.
- **Typical Usage:** Return a binder object to allow the client to make calls to the service.

### 4. **onUnbind(Intent intent)**

- **Called:** When all clients have unbound from a particular interface published by the service.
- **Purpose:** Clean up resources related to the binding.
- **Typical Usage:** Handle cleanup of resources related to the binding.

### 5. **onRebind(Intent intent)**

- **Called:** When new clients bind to the service after it had previously been unbound.
- **Purpose:** Re-establish connections with new clients.
- **Typical Usage:** Reinitialize resources needed for the clients.

### 6. **onDestroy()**

- **Called:** When the service is no longer used and is being destroyed.
- **Purpose:** Clean up resources and perform any necessary finalization.
- **Typical Usage:** Release any resources that were created in `onCreate()`.

---

## Service Lifecycle Diagram

Here's a simplified diagram to help visualize the lifecycle:

```
       onCreate() ----> [Service Created]
           ↓
   onStartCommand() ----> [Service Running] ----> onDestroy() ----> [Service Destroyed]
           ↓
       onBind() ----> [Service Bound] ----> onUnbind() ----> onDestroy()
           ↓
       onRebind() (if new clients bind after onUnbind)
```

---

## Example of a Service Implementation

Here is an example of a simple `Service` implementation:

```kotlin
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private val TAG = "MyService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
        // Initialize resources
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service Started")
        // Handle start request
        // Perform task here, e.g., start a new thread for background processing
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "Service Bound")
        // Return binder object for clients to communicate with the service
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Service Unbound")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "Service Rebound")
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destroyed")
        // Clean up resources
    }
}
```

## Usage Scenarios

- **Foreground Service:** Use `startForeground()` in `onStartCommand()` to run a service in the
  foreground.
- **Background Service:** Use `startService()` to start the service and `stopSelf()` to stop it when
  done.
- **Bound Service:** Use `bindService()` to bind to the service and `unbindService()` to unbind.

By understanding these lifecycle callbacks and their typical usage, you can implement and manage
services in your
Android applications effectively.

> [⬆️ Back to Top / Close](#ServiceLifecycleCallbacks)
</blockquote>
</details>

### 4. What is the purpose of the AsyncTaskLoader in Android?

**AsyncTaskLoader** is a subclass of `Loader` that performs asynchronous load operations in the
background. It is useful
for performing long-running operations and automatically handles the lifecycle of the data loading
process, including
caching results.

Example:

```kotlin
class MyLoader(context: Context) : AsyncTaskLoader<String>(context) {
    override fun loadInBackground(): String? {
        // Perform long-running task
    }
}
```

### 5. What is the purpose of the JobScheduler API in Android?

The **JobScheduler API** manages scheduled tasks across the whole device, optimizing battery usage.
It allows you to
schedule tasks to be run under specific conditions (e.g., when the device is charging or connected
to Wi-Fi).

Example:

```kotlin
val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
val jobInfo = JobInfo.Builder(jobId, ComponentName(this, MyJobService::class.java))
    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
    .build()
jobScheduler.schedule(jobInfo)
```

### 6. How do you handle background tasks efficiently in Android?

**Answer:**
There are multiple ways to handle background tasks efficiently in Android, depending on the nature
of the task. Below
are the recommended approaches:

---

#### **1. Coroutines (Recommended for lightweight tasks)**

- **Use Case:** Ideal for asynchronous operations that do not block the main thread.
- **Example:**
  ```kotlin
  CoroutineScope(Dispatchers.IO).launch {
      val data = fetchDataFromNetwork()
      withContext(Dispatchers.Main) {
          updateUI(data)
      }
  }
  ```

---

#### **2. WorkManager (Recommended for scheduled tasks)**

- **Use Case:** Best for persistent background tasks that need to run even if the app is closed or
  the device restarts.
- **Example:**
  ```kotlin
  val workRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
  WorkManager.getInstance(context).enqueue(workRequest)
  ```

---

#### **3. Foreground Services (For long-running tasks)**

- **Use Case:** Tasks that require continuous processing and need to be noticeable to the user (
  e.g., music playback,
  file downloads).
- **Example:**
  ```kotlin
  class MyForegroundService : Service() {
      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
          val notification = NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentTitle("Service Running")
              .setContentText("Your foreground service is running.")
              .setSmallIcon(R.drawable.ic_notification)
              .build()
          startForeground(1, notification)
          // Perform your task here
          return START_STICKY
      }

      override fun onBind(intent: Intent?): IBinder? {
          return null
      }
  }
  ```

---

#### **4. JobScheduler (For periodic background tasks)**

- **Use Case:** Tasks that need to run periodically, such as syncing data with a server.
- **Example:**
  ```kotlin
  val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
  val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(this, MyJobService::class.java))
      .setPeriodic(15 * 60 * 1000) // Run every 15 minutes
      .build()
  jobScheduler.schedule(jobInfo)
  ```

---

### **When to Use Each Service Type?**

| **Service Type**        | **Use Case**                                                                |
|-------------------------|-----------------------------------------------------------------------------|
| **Coroutines**          | Lightweight, short-lived tasks (e.g., fetching data from a network).        |
| **WorkManager**         | Persistent tasks that need to run even if the app is closed or restarted.   |
| **Foreground Services** | Long-running tasks that require user awareness (e.g., music playback).      |
| **JobScheduler**        | Periodic tasks that need to run at specific intervals (e.g., data syncing). |

---

### **Real-World Examples**

#### **Foreground Services**

1. **Music/Media Playback:** Playing music or video in the background.
2. **Location Tracking:** Real-time GPS tracking for fitness or delivery apps.
3. **File Upload/Download:** Uploading or downloading large files.
4. **VoIP Calls:** Managing voice over IP calls.

#### **Background Services**

1. **Data Synchronization:** Syncing app data with a server periodically.
2. **Data Backup:** Automatically backing up data to the cloud.
3. **Silent Push Notifications:** Handling push notifications silently.
4. **Pre-fetching Content:** Downloading content ahead of time.

#### **Bound Services**

1. **Media Player Control:** Allowing activities to control playback.
2. **Sensor Data Collection:** Collecting and providing sensor data to activities.
3. **Local Database Access:** Providing database access to activities.
4. **Network Communication:** Managing network requests for activities.

---

### **Examples**

#### **Foreground Service Example: Music Playback**

```kotlin
class MusicPlaybackService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Playing music")
            .setSmallIcon(R.drawable.ic_music_note)
            .build()
        startForeground(1, notification)
        // Play music
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
```

#### **Background Service Example: Data Synchronization**

```kotlin
class DataSyncService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform data sync in the background
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
```

#### **Bound Service Example: Local Database Access**

```kotlin
class LocalDatabaseService : Service() {
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): LocalDatabaseService = this@LocalDatabaseService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun queryData(query: String): Cursor {
        // Execute query on local database
    }

    fun insertData(data: ContentValues) {
        // Insert data into local database
    }
}
```
[DAY 4 Android Background Tasks.md](DAY%204%20Android%20Background%20Tasks.md)