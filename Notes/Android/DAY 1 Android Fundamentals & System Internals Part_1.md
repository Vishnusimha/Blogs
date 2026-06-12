---
title: "Android Fundamentals and System Internals Part 1"
date: 2026-06-12
slug: "android-fundamentals-system-internals-part-1"
tags: [ "Notes", "Android", "Interview Preparation", "Android Fundamentals", "Activities", "Permissions", "Content Providers" ]
summary: "Personal Android interview notes covering Android basics, app components, activity lifecycle, fragments, manifest, permissions, intents, assets, resources, and ContentProvider concepts."
categories: Notes
readTime: 20
---

# Technical interview questions

[DAY 1 Android_Manifest_File_Guide.md](DAY%201%20Android_Manifest_File_Guide.md)
[DAY 1 App_Bundles_and_APK_Structure.md](DAY%201%20App_Bundles_and_APK_Structure.md)

#### DAY 1 Android Fundamentals & System Internals

1. What is Android and what are its key features?
2. Explain the Android application components.
3. What is the activity lifecycle in Android?
4. What is the purpose of an Android Manifest file?
5. Explain the concept of Fragments in Android.
6. What is the purpose of the ContentProvider class in Android?
7. Explain the concept of Activities and Tasks in Android.
8. Describe the Android Support Library.
9. How do you handle screen orientation changes in Android?
10. Explain the purpose of the Android Asset Packaging Tool (AAPT).
11. What is the purpose of the Android App Bundle format?
12. Explain the concept of the Android Application Sandbox.
13. What are the differences between Serializable and Parcelable interfaces in Android?
14. How do you handle app permissions in Android?
15. What are the differences between startActivityForResult() and onActivityResult() methods?
16. What are the main components of an Android project structure?
17. What is the purpose of the `AndroidManifest.xml` file?
18. How are assets different from resources in an Android project?
19. Explain the difference between implicit and explicit intents.
20. What is the role of `AsyncTask` in Android?
21. Content Provider example with kotlin and to query media images in phone?

## Answers:

#### Set 1 Answers

### ✅ 1. What is Android and what are its key features?

**Android** is an **open-source, Linux-based mobile operating system** developed by Google. It provides a complete platform for building and running apps across phones, tablets, TVs, wearables, and automotive devices. Android apps run inside the **Android Runtime (ART)** and the OS manages resources like memory, CPU, storage, and permissions.

#### Key features of Android:

* **Open-source & customizable**: Built on Linux and AOSP, manufacturers can customize Android for different devices.
* **Component-based app framework**: Apps are built using **Activities, Services, BroadcastReceivers, and ContentProviders**.
* **Multitasking & process management**: Runs multiple apps with lifecycle control and background execution management.
* **Rich UI support**: Supports multiple screen sizes, densities, and modern UI frameworks like **Jetpack Compose**.
* **Connectivity**: Built-in support for Wi-Fi, Bluetooth, NFC, GPS, cellular networks, etc.
* **Security**: App sandboxing, permissions, secure app signing, SELinux, and verified boot.
* **App ecosystem**: Distribution through **Google Play** (AAB/APK delivery, updates, Play services integration).
* **Notifications & system integration**: Powerful notification system, deep links, widgets, and system-level integrations.

✅ Interview one-liner:

> “Android is a Linux-based OS + app framework that provides runtime, security sandboxing, and system services for building scalable mobile applications.”

### 2. Explain the Android application components.

Android applications are built using four key components:

- **Activities**: An activity represents a single screen with a user interface. For example, a login screen, a settings
  screen, etc.
- **Services**: A service is a background process that can run for a long time. Examples include playing music,
  downloading files, etc.
- **Content Providers**: Content providers manage access to a structured set of data. They encapsulate the data and
  provide mechanisms for defining data security.
- **Broadcast Receivers**: Broadcast receivers respond to broadcast messages from other applications or the system. For
  example, they can be used to trigger actions when the battery is low or the internet connection changes.

### 3. What is the activity lifecycle in Android?

The activity lifecycle describes the stages an activity goes through from creation to destruction. Key methods include:

- `onCreate()`: Called when the activity is first created.
- `onStart()`: Called when the activity becomes visible to the user.
- `onResume()`: Called when the activity starts interacting with the user.
- `onPause()`: Called when the system is about to resume another activity.
- `onStop()`: Called when the activity is no longer visible.
- `onDestroy()`: Called before the activity is destroyed.
- `onRestart()`: Called after the activity has been stopped, prior to it being started again.

### 4. What is the purpose of an Android Manifest file?

The **AndroidManifest.xml** file provides essential information about your app to the Android system. This includes:

- **Permissions**: What permissions the app requires.
- **Components**: Declares all activities, services, broadcast receivers, and content providers.
- **Intents**: Specifies intent filters for activities and services.
- **App Metadata**: Provides application-level metadata like the app's name, icon, and theme.
- **Hardware and Software Features**: Declares which hardware and software features the app uses.

### 5. Explain the concept of Fragments in Android.

**Fragments** are reusable components representing a portion of the user interface within an activity. They can be
combined to create a multi-pane UI and reused in different activities. Fragments have their own lifecycle, which is
closely tied to the lifecycle of the containing activity.

### 6. What is the purpose of the ContentProvider class in Android?

**ContentProvider** manages access to a central repository of data. It encapsulates the data and provides mechanisms for
defining data security. Commonly used to share data between applications.

---

### 7. Explain the concept of Activities and Tasks in Android.

- **Activities**: Single screens with a user interface that allow users to interact with the app.
- **Tasks**: Collections of activities that users interact with **when performing a specific job**. Tasks are managed by the
  **back stack**, allowing users to move back to previous activities.

Your answer is correct historically ✅ but for **modern (2026) senior interviews**, you should update the terminology:

### 8. Describe the Android Support Library (Updated for modern Android)

> **Android Support Library is deprecated** and replaced by **AndroidX + Jetpack libraries**.

The **Android Support Library** was a set of libraries created by Google to provide **backward-compatible UI components and APIs**, allowing developers to use modern features on older Android versions.

Over time, the Support Library evolved and was replaced by **AndroidX**, which is the current standard library ecosystem used in Android development today.

#### What it provided (historically):

* **AppCompat**: Material-style UI widgets and theme support on older Android versions.
* **Support v4/v7 libraries**: Backward compatibility for features like fragments, notifications, etc.
* **RecyclerView/CardView**: Efficient list rendering and UI components.
* **Design Support Library**: Material components like **NavigationView, TabLayout, Snackbar, FloatingActionButton**.

#### Modern replacement (important interview point):

* Today we use **AndroidX + Jetpack**, which includes:

    * `androidx.appcompat`
    * `androidx.recyclerview`
    * Material Components (`com.google.android.material`)
    * Lifecycle, ViewModel, Navigation, Room, WorkManager, Compose, etc.

✅ Interview one-liner:

> “Support Library enabled backward compatibility, but it has been replaced by AndroidX and Jetpack, which are the modern recommended libraries.”

### 9. How do you handle screen orientation changes in Android? ✅

By default, when the device orientation changes, Android treats it as a **configuration change** and **recreates the Activity**:

`onPause() → onStop() → onDestroy() → onCreate() → onStart() → onResume()`

To handle this properly, we preserve UI state using:

#### 1) `onSaveInstanceState()` (short-lived UI state)

Used to save lightweight UI state before recreation (like text input, scroll position).

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("key", "value")
}

override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    val value = savedInstanceState.getString("key")
}
```

#### 2) ViewModel (recommended for data state)

For non-UI business data (API results, cached state), use **ViewModel** because it survives configuration changes.

**Best practice:**

* UI state → `SavedInstanceState` / `rememberSaveable`
* Data state → `ViewModel`

#### 3) SavedStateHandle (best modern approach)

For restoring state after process death + rotation:

```kotlin
class MyViewModel(private val state: SavedStateHandle) : ViewModel() {
    var name: String?
        get() = state["name"]
        set(value) { state["name"] = value }
}
```

#### ⚠️ 4) `android:configChanges` (use only in special cases)

You can prevent recreation:

```xml
android:configChanges="orientation|screenSize"
```

But this is **not recommended in general** because:

* you must manually update UI resources/layouts
* may cause UI bugs
* defeats Android’s standard resource reloading

Use only when required:

* camera preview
* video player
* heavy real-time rendering apps

**1-liner:**

> “Orientation change recreates Activity by default, so I handle it using ViewModel for data persistence and onSaveInstanceState / SavedStateHandle (or rememberSaveable in Compose) for UI state. I avoid configChanges unless it’s a special case like camera/video.”

**the modern best practice**:

> Don’t rely only on `onSaveInstanceState()` for app state. Use **ViewModel + SavedStateHandle** (or Compose `rememberSaveable`) and avoid `configChanges` unless necessary.

### ✅ Handling Orientation Changes in Jetpack Compose

In Compose, rotation triggers **Activity recreation**, so the UI recomposes again. We handle state using:

## 1) `rememberSaveable` (Best for UI state )

Use it for values that must survive **rotation** (configuration change).

```kotlin
@Composable
fun ProfileScreen() {
    var name by rememberSaveable { mutableStateOf("") }

    TextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") }
    )
}
```

**survives:**

* screen rotation
* configuration changes

## 2) `remember` (Not safe for rotation )

`remember` survives recomposition, but **not Activity recreation**.

```kotlin
var name by remember { mutableStateOf("") }   // ❌ lost on rotation
```

## 3) ViewModel (Best for data/business state )

Use ViewModel for:

* API results
* DB data
* shared screen state

```kotlin
class MyViewModel : ViewModel() {
    var userName by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        userName = newName
    }
}

@Composable
fun Screen(vm: MyViewModel = viewModel()) {
    Text(text = vm.userName)
}
```

**survives rotation automatically.**

## 4) `SavedStateHandle` in ViewModel (Rotation + process death )

Best modern approach if you want state restored even after process kill.

```kotlin
class MyViewModel(private val state: SavedStateHandle) : ViewModel() {
    var name: String
        get() = state["name"] ?: ""
        set(value) { state["name"] = value }
}

@Composable
fun Screen(vm: MyViewModel = viewModel()) {
    TextField(
        value = vm.name,
        onValueChange = { vm.name = it }
    )
}
```

### ✅ Compose Interview One-liner

> “In Compose, I use `rememberSaveable` for UI state across rotation, and ViewModel (SavedStateHandle if needed) for data and process-death-safe state.”

### Compose State: `remember` vs `rememberSaveable` vs `ViewModel`.

| Option                         | Survives Recomposition? | Survives Rotation? | Survives Process Death?           | Best For                                                    |
|--------------------------------|-------------------------|--------------------|-----------------------------------|-------------------------------------------------------------|
| `remember`                     | ✅ Yes                   | ❌ No               | ❌ No                              | Temporary UI state (tab selection, animation state)         |
| `rememberSaveable`             | ✅ Yes                   | ✅ Yes              | ⚠️ Sometimes (if system restores) | UI state across rotation (TextField input, scroll position) |
| `ViewModel`                    | ✅ Yes                   | ✅ Yes              | ❌ No (by default)                 | Screen/business state (API/DB data, shared state)           |
| `ViewModel + SavedStateHandle` | ✅ Yes                   | ✅ Yes              | ✅ Yes                             | Critical state that must restore after process kill         |

---

### 10. Explain the purpose of the Android Asset Packaging Tool (AAPT).

**AAPT** is a tool that handles packaging resources into an APK. It compiles and packages resources, such as images and
XML files, and generates the `R.java` file, which is used to access resources programmatically.

### 11. What is the purpose of the Android App Bundle format?

The **Android App Bundle (AAB)** format is a new publishing format that includes all your app's compiled code and
resources, but defers APK generation and signing to Google Play. This results in smaller, optimized APKs for each device
configuration, improving download times and reducing storage usage.

### 12. Explain the concept of the Android Application Sandbox.

The **Android Application Sandbox** isolates app data and code execution from other apps. Each app runs in its own
process and has a unique user ID. The sandbox provides a secure environment by restricting access to app data and system
resources.

### 13. Differences between `Serializable` and `Parcelable`

#### **Serializable**

* Java standard mechanism
* uses **reflection**
* **slow**, more memory overhead
* creates more GC pressure
* easiest to implement

✅ Use when:

* not performance critical
* small objects, internal usage only

#### **Parcelable**

* Android-specific serialization
* designed for **IPC / Bundles / Intents**
* avoids reflection → **fast + memory efficient**
* recommended for passing data between Activities/Fragments

✅ Use when:

* passing objects via `Intent`, `Bundle`, `SavedStateHandle`
* performance matters

Example:

```kotlin
@Parcelize
data class User(val name: String, val age: Int) : Parcelable
```

✅ Senior one-liner:

> “Parcelable is the Android-optimized way for Bundles/IPC; Serializable is slower because of reflection.”

⚠️ Senior note:

> Don’t pass huge objects in intents. Prefer ID + fetch from DB.

### 14. How do you handle app permissions in Android?

Permissions are handled by declaring them in the `AndroidManifest.xml` and requesting them at runtime for dangerous
permissions.

Example:

```xml

<uses-permission android:name="android.permission.CAMERA"/>
```

Requesting permissions at runtime:

```kotlin
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
}
```

### 15. `startActivityForResult()` vs `onActivityResult()` (Modern answer)

#### Old way (Deprecated ❌)

* `startActivityForResult()` starts an Activity expecting a result.
* `onActivityResult()` receives the result.

⚠️ Both are **deprecated**.

#### ✅ Modern way (Recommended in 2026): Activity Result API

Use:

* `registerForActivityResult()`
* `ActivityResultContracts`

Example:

```kotlin
private val launcher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // handle result
        }
    }

fun openSecondActivity() {
    val intent = Intent(this, SecondActivity::class.java)
    launcher.launch(intent)
}
```

#### Why Activity Result API is better?

* lifecycle-aware (prevents leaks/crashes)
* works cleanly with fragments
* handles config changes better
* easier contracts (`GetContent`, `PickContact`, `RequestPermission`)

✅ Senior one-liner:

> “startActivityForResult/onActivityResult is deprecated; we use Activity Result API with contracts which is lifecycle-aware.”


### 16. What are the main components of an Android project structure?

**Answer:**
An Android project structure consists of several directories and files, each serving a specific purpose. The main
components are:

- **`app/`**: Contains the application's source files and resources.
    - **`src/`**: Contains the Java/Kotlin source files.
        - **`main/`**: The main source set.
            - **`java/`**: Contains Java/Kotlin source code.
            - **`res/`**: Contains resources such as layouts, strings, and images.
            - **`AndroidManifest.xml`**: The manifest file describing the application.
    - **`test/`**: Contains unit tests.
    - **`androidTest/`**: Contains Android instrumented tests.
- **`build/`**: Contains build outputs.
- **`gradle/`**: Contains Gradle scripts and configurations.
- **`.gradle/`**: Contains Gradle's local cache.
- **`build.gradle`**: The project-level build configuration file.
- **`app/build.gradle`**: The app-level build configuration file.
- **`settings.gradle`**: Specifies the settings for the Gradle build.
- **`gradle.properties`**: Configures properties for the Gradle build.

### 17. What is the purpose of the `AndroidManifest.xml` file?

**Answer:**
The `AndroidManifest.xml` file is a critical part of every Android application. It provides essential information to the
Android build tools, the Android operating system, and Google Play. The primary purposes of the `AndroidManifest.xml`
include:

- **Declaring application components**: Lists all activities, services, broadcast receivers, and content providers.
- **Permissions**: Specifies permissions that the app requires, such as internet access or reading contacts.
- **Hardware and software features**: Declares the hardware and software features the app uses.
- **App metadata**: Provides metadata about the application, such as its name, icon, and theme.
- **Intent filters**: Specifies how the app handles intents to allow deep linking and other interactions.

### 18. How are assets different from resources in an Android project?

**Answer:**
Assets and resources are both used to include non-code content in an Android application, but they have different
purposes and ways of accessing them:

- **Resources**:
    - Stored in the `res/` directory.
    - Accessed using the `R` class (e.g., `R.layout.activity_main`).
    - Can be automatically localized and handled by the Android framework.

- **Assets**:
    - Stored in the `assets/` directory.
    - Accessed using the `AssetManager` class.
    - Not processed or managed by the Android framework, allowing for arbitrary files like HTML, text files, or bundled
      data.

Example of accessing an asset file:

```kotlin
val assetManager = context.assets
val inputStream = assetManager.open("myfile.txt")
val content = inputStream.bufferedReader().use { it.readText() }
```

### 19. Explain the difference between implicit and explicit intents.

**Answer:**

| Feature    | Explicit Intent                                             | Implicit Intent                                                                |
|------------|-------------------------------------------------------------|--------------------------------------------------------------------------------|
| Definition | Specifies the exact component to start.                     | Specifies an action to be performed without specifying the target component.   |
| Usage      | Used to start a specific activity or service within an app. | Used to request an action from another app (e.g., opening a URL in a browser). |
| Example    | `Intent(context, SecondActivity::class.java)`               | `Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))`                  |

**Explicit Intent Example:**

```kotlin
val intent = Intent(context, SecondActivity::class.java)
startActivity(intent)
```

**Implicit Intent Example:**

```kotlin
val intent = Intent(Intent.ACTION_VIEW)
intent.data = Uri.parse("https://google.com")
startActivity(intent)
```

---

### **Additional Implicit Intent Examples:**

#### 1. Viewing a Web Page

```kotlin
val webIntent = Intent(Intent.ACTION_VIEW)
webIntent.data = Uri.parse("https://www.example.com")
startActivity(webIntent)
```

#### 2. Dialing a Phone Number

```kotlin
val dialIntent = Intent(Intent.ACTION_DIAL)
dialIntent.data = Uri.parse("tel:1234567890")
startActivity(dialIntent)
```

#### 3. Sending an Email

```kotlin
val emailIntent = Intent(Intent.ACTION_SENDTO)
emailIntent.data = Uri.parse("mailto:example@example.com")
emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of the email")
startActivity(emailIntent)
```

#### 4. Sending a Text Message

```kotlin
val smsIntent = Intent(Intent.ACTION_SENDTO)
smsIntent.data = Uri.parse("smsto:1234567890")
smsIntent.putExtra("sms_body", "Hello, how are you?")
startActivity(smsIntent)
```

#### 5. Viewing a Location on a Map

```kotlin
val mapIntent = Intent(Intent.ACTION_VIEW)
mapIntent.data = Uri.parse("geo:37.7749,-122.4194")
startActivity(mapIntent)
```

#### 6. Capturing a Photo

```kotlin
val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
```

#### 7. Choosing a Contact

```kotlin
val pickContactIntent = Intent(Intent.ACTION_PICK)
pickContactIntent.type = ContactsContract.Contacts.CONTENT_TYPE
startActivityForResult(pickContactIntent, REQUEST_SELECT_CONTACT)
```

#### 8. Playing Media

```kotlin
val playMediaIntent = Intent(Intent.ACTION_VIEW)
playMediaIntent.setDataAndType(Uri.parse("file://path/to/media/file"), "audio/*")
startActivity(playMediaIntent)
```

#### 9. Sharing Content

```kotlin
val shareIntent = Intent(Intent.ACTION_SEND)
shareIntent.type = "text/plain"
shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text to share.")
startActivity(Intent.createChooser(shareIntent, "Share via"))
```

#### 10. Setting an Alarm

```kotlin
val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM)
alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, 7)
alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, 30)
startActivity(alarmIntent)
```

---

### **When to Use Implicit Intents**

Use implicit intents when you want to:

- Perform an action but let the system choose the appropriate application.
- Launch a common app feature, such as viewing a map or sending an email, without specifying the exact app.
- Promote flexibility and reusability in your app, allowing users to use their preferred apps for certain actions.

**Always check if an app is available to handle the intent:**

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
if (intent.resolveActivity(packageManager) != null) {
    startActivity(intent)
} else {
    // Handle the case where no suitable app is found
    Toast.makeText(this, "No application can handle this request.", Toast.LENGTH_SHORT).show()
}
```

### 20. What is the role of `AsyncTask` in Android?

**Answer:**
`AsyncTask` was used for background tasks that update the UI, but it's deprecated in favor of Kotlin Coroutines and
WorkManager.

**Limitations of `AsyncTask`:**

- Not lifecycle-aware.
- Causes memory leaks if not handled properly.

**Example using `AsyncTask` (deprecated):**

```kotlin
class MyTask : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String {
        return "Background Task Completed"
    }

    override fun onPostExecute(result: String) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
    }
}
```

**Alternative using Coroutines:**

```kotlin
CoroutineScope(Dispatchers.IO).launch {
    val result = "Background Task Completed"
    withContext(Dispatchers.Main) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
    }
}
```

### 21. Content Provider example with kotlin and to query media images in phone?
### Query Media Images in Phone (Modern Kotlin - 2026)

This guide shows the **modern and correct way** to query images from the phone using **MediaStore + ContentResolver**, with **Android 13+ permission handling** and an optional **Photo Picker** approach.

✅ Key interview note:
> In most real apps, you **do not** implement your own ContentProvider to query photos. You query the **system MediaStore provider** using **ContentResolver**.


#### Table of Contents
- [1) Permissions (Android 13+ vs 12 and below)](#1-permissions-android-13-vs-12-and-below)
- [2) Permission Request (Activity Result API)](#2-permission-request-activity-result-api)
- [3) Query Images using MediaStore (Recommended)](#3-query-images-using-mediastore-recommended)
- [4) Full Activity Example (End-to-end)](#4-full-activity-example-end-to-end)
- [5) Loading Images (Coil / Glide)](#5-loading-images-coil--glide)
- [6) Best UX Option: Photo Picker (Android 13+)](#6-best-ux-option-photo-picker-android-13)
- [7) Interview Notes / Common Mistakes](#7-interview-notes--common-mistakes)

#### 1) Permissions (Android 13+ vs 12 and below)

##### AndroidManifest.xml
```xml
<!-- Android 13+ (API 33+) -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- Android 12 and below (API <= 32) -->
<uses-permission
    android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

Notes:
- `READ_MEDIA_IMAGES` is required from **Android 13+**
- `READ_EXTERNAL_STORAGE` is only needed for **Android 12 and below**
- ❌ Do NOT use `WRITE_EXTERNAL_STORAGE` in modern apps (scoped storage)

#### 2) Permission Request (Activity Result API)

##### Decide correct permission based on OS version
```kotlin
// Returns correct permission depending on device Android version
private fun imagePermission(): String =
    if (android.os.Build.VERSION.SDK_INT >= 33)
        android.Manifest.permission.READ_MEDIA_IMAGES
    else
        android.Manifest.permission.READ_EXTERNAL_STORAGE
```

##### Request permission using Activity Result API
```kotlin
// Modern permission request launcher (lifecycle-aware)
private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            // Permission granted → proceed to query images
            queryImages()
        } else {
            // Permission denied → show message / fallback to Photo Picker if possible
        }
    }
```

#### 3) Query Images using MediaStore (Recommended)

##### Model class
```kotlin
import android.net.Uri

// Data model representing one image entry from MediaStore
data class ImageItem(
    val id: Long,
    val name: String?,
    val uri: Uri,          // content:// Uri (modern safe way)
    val dateAdded: Long
)
```

##### Query function (ContentResolver + MediaStore)
```kotlin
import android.content.ContentUris
import android.provider.MediaStore

private fun queryImages(): List<ImageItem> {
    val images = mutableListOf<ImageItem>()

    // MediaStore collection URI for external images
    val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    // Columns we want from MediaStore
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )

    // Sort: newest first
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    // Query MediaStore using ContentResolver (system ContentProvider)
    contentResolver.query(
        collection,
        projection,
        null,   // selection (WHERE clause)
        null,   // selectionArgs
        sortOrder
    )?.use { cursor ->

        // Get column indexes
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

        // Iterate results
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idCol)
            val name = cursor.getString(nameCol)
            val dateAdded = cursor.getLong(dateCol)

            // Create content Uri for each image: content://media/external/images/media/<id>
            val contentUri = ContentUris.withAppendedId(collection, id)

            images.add(ImageItem(id, name, contentUri, dateAdded))
        }
    }

    return images
}
```

✅ Interview note:
> We use **content Uri** instead of file path. `MediaStore.Images.Media.DATA` is deprecated and unreliable.

#### 4) Full Activity Example (End-to-end)

```kotlin
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // Permission request launcher (modern replacement for requestPermissions())
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // If permission granted → query and use images
                val images = queryImages()

                // Example: print first 5 image URIs
                images.take(5).forEach { println(it.uri) }
            } else {
                // Permission denied → handle gracefully (show UI message / use Photo Picker)
                println("Permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Decide correct permission for this Android version
        val permission = imagePermission()

        // Check if already granted
        if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Already granted → query images directly
            val images = queryImages()
            images.take(5).forEach { println(it.uri) }
        } else {
            // Not granted → request permission
            requestPermissionLauncher.launch(permission)
        }
    }

    // Returns correct permission depending on device Android version
    private fun imagePermission(): String =
        if (android.os.Build.VERSION.SDK_INT >= 33)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else
            android.Manifest.permission.READ_EXTERNAL_STORAGE

    // Query MediaStore images (system ContentProvider)
    private fun queryImages(): List<ImageItem> {
        val images = mutableListOf<ImageItem>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        contentResolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val dateAdded = cursor.getLong(dateCol)

                // Convert MediaStore ID to content Uri
                val contentUri = ContentUris.withAppendedId(collection, id)

                images.add(ImageItem(id, name, contentUri, dateAdded))
            }
        }

        return images
    }
}
```

#### 5) Loading Images (Coil / Glide)

##### Coil (Jetpack Compose)
```kotlin
AsyncImage(
    model = image.uri,
    contentDescription = image.name
)
```

##### Glide (XML Views)
```kotlin
Glide.with(imageView)
    .load(image.uri)
    .into(imageView)
```

#### 6) Best UX Option: Photo Picker (Android 13+)

Android 13+ provides a **system Photo Picker** that does **not require storage permission**.

```kotlin
private val pickMedia =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            // User selected an image → uri is content://
            println("Selected image: $uri")
        } else {
            // User cancelled
            println("No image selected")
        }
    }

// Launch picker (images only)
fun openPhotoPicker() {
    pickMedia.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}
```

✅ Interview line:
> “For modern apps, prefer Photo Picker on Android 13+ since it avoids storage permission and improves privacy.”

#### 7) Interview Notes / Common Mistakes

✅ Do:
- Use `READ_MEDIA_IMAGES` for Android 13+
- Use `content://` URIs from MediaStore
- Use `ContentResolver.query()`
- Prefer Photo Picker when possible

❌ Avoid:
- `WRITE_EXTERNAL_STORAGE`
- `MediaStore.Images.Media.DATA` (deprecated file paths)
- attempting direct file path access without SAF/Uri permissions

---
