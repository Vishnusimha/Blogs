---
title: "DAY 5 Android Networking, Data Storage and Background Work Interview Q&A"
date: 2026-06-12
slug: "android-networking-data-storage-background-work-interview-qa"
tags: [ "Notes", "Android", "Networking", "Data Storage", "WorkManager", "Room", "DataStore", "Deep Linking" ]
summary: "Personal Android interview Q&A notes covering data storage options, push notifications, NDK, networking, location services, SharedPreferences, SQLite, PendingIntent, Room, WorkManager, connectivity changes, deep linking, and DataStore."
categories: Notes
readTime: 18
---

# Technical interview questions

#### DAY 5 Data, Networking, Background Work & Platform Capabilities

1. What are the different ways to store and retrieve data in Android?
2. How do you implement push notifications in Android?
3. What is the Android NDK and when would you use it?
4. How do you handle network operations in Android?
5. How do you implement location-based services in Android?
6. What are the differences between SharedPreferences and SQLite for data storage?
7. Explain the concept of PendingIntent in Android.
8. Explain the concept of Room Persistence Library and its advantages over SQLiteOpenHelper.
9. Describe the purpose of the Android WorkManager API.
10. What are the different ways to handle network connectivity changes in Android?
11. How do you handle deep linking in Android?
12. What are the different storage options available in Android?
13. Data store and encrypted shared preferences in android

## Answers:

#### Day 5 Answers

### 1. What are the different ways to store and retrieve data in Android?

- **SharedPreferences**: For storing small

key-value pairs.

- **Internal/External Storage**: For storing files.
- **SQLite Database**: For structured data storage.
- **Room Database**: A higher-level abstraction over SQLite.
- **ContentProviders**: For sharing data between apps.
- **Network Storage**: For cloud storage solutions.

### 2. How do you implement push notifications in Android?

Push notifications can be implemented using Firebase Cloud Messaging (FCM):

- Add Firebase to the Android project.
- Implement Firebase messaging service to handle messages.
- Configure and send notifications from the Firebase console or server.

Example:

```kotlin
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle message
    }

    override fun onNewToken(token: String) {
        // Handle token refresh
    }
}
```

### 3. What is the Android NDK and when would you use it?

The **Android NDK (Native Development Kit)** allows you to implement parts of your app using native code languages such
as C and C++. It is used when:

- Performance is critical and can't be achieved with Java/Kotlin.
- You need to use existing C/C++ libraries.
- You need to perform low-level hardware operations.

### 4. How do you handle network operations in Android?

Network operations in Android should be handled in a background thread to avoid blocking the main UI thread. Common
approaches include:

- **AsyncTask**: Deprecated, previously used for simple background tasks.
- **HandlerThread**: For long-running tasks in a separate thread.
- **ThreadPoolExecutor**: For managing a pool of threads.
- **RxJava**: For composing asynchronous operations.
- **Kotlin Coroutines**: For managing asynchronous tasks in a more readable way.
- **WorkManager**: For deferrable, guaranteed background work.

Example using Kotlin Coroutines:

```kotlin
GlobalScope.launch(Dispatchers.IO) {
    val response = makeNetworkRequest()
    withContext(Dispatchers.Main) {
        // Update UI with response
    }
}
```

### 5. How do you implement location-based services in Android?

Location-based services can be implemented using the `LocationManager` or `FusedLocationProviderClient` for improved
accuracy and efficiency.

Example using FusedLocationProviderClient:

```kotlin
val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

fusedLocationClient.lastLocation
    .addOnSuccessListener { location: Location? ->
        // Use location data
    }
```

### 6. What are the differences between SharedPreferences and SQLite for data storage?

- **SharedPreferences**: Used for storing simple key-value pairs, typically for app settings and preferences.
- **SQLite**: A full-fledged relational database for storing structured data. Suitable for more complex data storage
  needs.

### 7. Explain the concept of PendingIntent in Android.
✔️ PendingIntent stays here — it’s a system service bridge, not UI or architecture.

**PendingIntent** is a token that allows a foreign application to execute a predefined piece of code with the same
permissions as the application that created it. It is commonly used for notifications and alarms.
**PendingIntent** is a token that you give to another application (like NotificationManager, AlarmManager, etc.) to
allow it to perform an action on your application's behalf. It is typically used for notifications and alarms.

Example:

```kotlin
val intent = Intent(this, MyActivity::class.java)
val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
```
### 8. Explain the concept of Room Persistence Library and its advantages over SQLiteOpenHelper.

The **Room Persistence Library** is part of the Android Jetpack and provides an abstraction layer over SQLite to allow
fluent database access while harnessing the full power of SQLite.

### 9. Describe the purpose of the Android WorkManager API.

The **WorkManager API** is part of Android Jetpack and is used for managing deferrable, guaranteed background work. It
simplifies the scheduling and execution of background tasks that need to be guaranteed to run, even if the app exits or
the device restarts.

### 10. What are the different ways to handle network connectivity changes in Android?

- **BroadcastReceiver**: Register a `BroadcastReceiver` to listen for connectivity changes.
- **ConnectivityManager**: Use `ConnectivityManager` to check network status.

Example using `BroadcastReceiver`:

```kotlin
class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        // Handle network change
    }
}

val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
registerReceiver(NetworkChangeReceiver(), filter)
```

### 11. How do you handle deep linking in Android?

Deep linking allows you to launch specific activities in your app via URLs.

1. **Define deep link in `AndroidManifest.xml`:**

```xml

<activity android:name=".MyActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:scheme="https" android:host="www.example.com" android:pathPrefix="/path"/>
    </intent-filter>
</activity>
```

2. **Handle the deep link in your activity:**

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val data: Uri? = intent?.data
    if (data != null) {
        // Handle the deep link
    }
}
```

### 12. What are the different storage options available in Android?

**Answer:**
Android provides several storage options for data persistence:

1. **SharedPreferences**:
    - Used for storing small amounts of key-value data.
    - Suitable for storing user preferences, settings, or flags.

   **Example:**
   ```kotlin
   val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
   val editor = sharedPreferences.edit()
   editor.putString("username", "Vishnu")
   editor.apply()
   ```

2. **Internal Storage**:
    - Used for private app data that should not be accessible by other apps.

   **Example:**
   ```kotlin
   val fileOutputStream = context.openFileOutput("myfile.txt", Context.MODE_PRIVATE)
   fileOutputStream.write("Hello, World!".toByteArray())
   fileOutputStream.close()
   ```

3. **External Storage**:
    - Used for storing large files such as media.
    - Requires **WRITE_EXTERNAL_STORAGE** permission (before Android 10).

4. **SQLite Database**:
    - Used for structured data storage.
    - Allows complex queries.

   **Example using Room:**
   ```kotlin
   @Entity
   data class User(@PrimaryKey val id: Int, val name: String)

   @Dao
   interface UserDao {
       @Insert
       fun insertUser(user: User)
   }

   @Database(entities = [User::class], version = 1)
   abstract class AppDatabase : RoomDatabase() {
       abstract fun userDao(): UserDao
   }
   ```

5. **Content Providers**:
    - Used for sharing data across apps.
    - Examples: Contacts, MediaStore.

6. **Cloud Storage (Firebase, AWS, Google Drive)**:
    - Used for syncing data with cloud services.

---

### 13. Data store and encrypted shared preferences in android

Jetpack DataStore is a modern and recommended data storage solution in Android for storing key-value pairs or typed
objects. It overcomes many limitations of `SharedPreferences` and provides a cleaner, more robust, and more efficient
API. DataStore is built using Kotlin coroutines and Flow, ensuring asynchronous and transactional operations.

#### DataStore Variants

1. **Preferences DataStore:** Stores key-value pairs similar to `SharedPreferences`.
2. **Proto DataStore:** Stores typed objects using Protocol Buffers (ideal for more complex data structures).

---

#### 1. Preferences DataStore (Key-Value Pair)

### How it Works

- Stores data as key-value pairs, like `SharedPreferences`.
- Type-safe and provides better performance by being fully asynchronous.

#### Setting Up DataStore

Add the necessary dependencies in your `build.gradle` file:

```groovy
implementation "androidx.datastore:datastore-preferences:1.0.0"
```

#### Creating a Preferences DataStore

```kotlin
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension function to create DataStore
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesManager(private val context: Context) {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] ?: "Default User" }

    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userName
        }
    }
}
```

#### Usage in an Activity/Fragment

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferencesManager = UserPreferencesManager(this)

        lifecycleScope.launch { userPreferencesManager.saveUserName("John Doe") }

        lifecycleScope.launch {
            userPreferencesManager.userNameFlow.collect { userName ->
                println("User Name: $userName")
            }
        }
    }
}
```

---

#### 2. Proto DataStore (Typed Objects)

#### How it Works

- Stores strongly typed objects using Protocol Buffers.
- Provides more structure and type safety compared to Preferences DataStore.

#### Setting Up Proto DataStore

#### Add Dependencies

```groovy
implementation "androidx.datastore:datastore:1.0.0"
```

#### Define a Protocol Buffers File (`user_prefs.proto`)

```protobuf
syntax = "proto3";
option java_package = "com.example.datastore";
option java_outer_classname = "UserPreferencesProto";

message UserPreferences {
  string user_name = 1;
  int32 user_age = 2;
}
```

#### Create the Proto DataStore

```kotlin
import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.datastore.UserPreferencesProto.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}

val Context.userPreferencesDataStore by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer
)

class UserPreferencesManager(private val context: Context) {
    val userPreferencesFlow: Flow<UserPreferences> = context.userPreferencesDataStore.data
        .map { preferences -> preferences ?: UserPreferences.getDefaultInstance() }

    suspend fun updateUserPreferences(userName: String, userAge: Int) {
        context.userPreferencesDataStore.updateData { preferences ->
            preferences.toBuilder().setUserName(userName).setUserAge(userAge).build()
        }
    }
}
```

#### Usage in an Activity/Fragment

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferencesManager = UserPreferencesManager(this)

        lifecycleScope.launch { userPreferencesManager.updateUserPreferences("Jane Doe", 28) }

        lifecycleScope.launch {
            userPreferencesManager.userPreferencesFlow.collect { preferences ->
                println("User Name: ${preferences.userName}, User Age: ${preferences.userAge}")
            }
        }
    }
}
```

---

#### Why Choose DataStore Over SharedPreferences?

- **Fully asynchronous API** (avoids blocking the main thread).
- **Type-safe** with better error handling.
- **Supports Kotlin Coroutines and Flow** for seamless data updates.
- **More robust** (transactional support, no risk of ANR).
- **Optimized performance** even for large data sets.

#### Summary

Jetpack DataStore provides a modern, scalable, and type-safe solution for storing data in Android applications. Whether
you need simple key-value storage (Preferences DataStore) or structured data (Proto DataStore), it has you covered.
