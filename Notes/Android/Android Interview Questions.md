
---
title: "Android Developer Interview Questions: Master List"
date: 2026-06-28
slug: "android-interview-questions-master-list"
tags: [ "Notes", "Android", "Interview", "Questions", "Fundamentals", "UI", "Architecture" ]
summary: "A massive collection of Android interview questions categorized by day and topic, ranging from basic system internals to advanced architectural patterns and testing."
categories: Notes
readTime: 15
---

## DAY 1 Android Fundamentals & System Internals Part 1

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

## DAY 1 Android Fundamentals & System Internals Part 2

1. Explain Android architecture layers (Linux Kernel, HAL, Native Libraries, ART, Framework, Apps).
2. What is the difference between **Dalvik** and **ART** runtime?
3. What is the **Zygote** process in Android and why is it important?
4. What is the **System Server** process and what does it do?
5. What happens internally during an Android app **cold start vs warm start**?
6. What is an Android **process**, and how does Android prioritize/kills processes (OOM adjustment)?
7. What is **ANR** and what are the common triggers?
8. What is the **Looper**, **MessageQueue**, and **Handler** in Android main thread model?
9. What is the difference between **Application class** and **Activity class**?
10. What is **Context** in Android and what are the types of context?
11. Difference between **Activity context vs Application context**?
12. What is the difference between **Intent**, **PendingIntent**, and **Broadcast**?
13. What is the difference between **Service**, **BroadcastReceiver**, and **ContentProvider** at system level?
14. What are the different types of **Services** in Android (started/bound/foreground)?
15. Explain **BroadcastReceiver** types (manifest-registered vs dynamic).
16. What is an **explicit broadcast vs implicit broadcast**?
17. What is Android **IPC** and what mechanisms exist for IPC?
18. What is the **Binder** framework in Android?
19. What is **AIDL** and when would you use it?
20. What is the role of **PackageManager** and **ActivityManager**?
21. Explain Android **APK structure** (classes.dex, resources.arsc, manifest, assets, lib).
22. What is **DEX** and why is it needed?
23. What is **MultiDex** and when does it happen?
24. What are **launchModes** in Android (standard, singleTop, singleTask, singleInstance)?
25. What is the difference between **taskAffinity** and launchMode?
26. What is the Android **back stack** and how is it managed?
27. What is the difference between **finish()** and **finishAffinity()**?
28. Explain how **configuration changes** work internally.
29. What is `android:exported` and why did it become mandatory (Android 12+)?
30. What are **intent-filters** matching rules (action/category/data/priority)?
31. Explain `onSaveInstanceState()` and what Android saves automatically.
32. What is the difference between **resources in res/** and **assets/** at build/runtime?
33. What is **R.java / R class**, and how it is generated?
34. Explain **AAPT vs AAPT2** (what changed and why).
35. What is the difference between **APK** and **AAB** in delivery model?

---

## DAY 2 — UI Layer (Views → XML, Jetpack Compose, RecyclerView, Material Design) Part 1

1. What is the purpose of the ViewHolder pattern in RecyclerView?
2. Explain the difference between ListView and RecyclerView.
3. Explain the concept of Material Design in Android.
4. Explain the concept of ViewBinding in Android.
5. What are the different types of layouts available in Android?
6. Describe the difference between RelativeLayout and ConstraintLayout.
7. Explain the concept of RecyclerView animations.
8. How do you implement custom views in Android?
9. What are the different ways to handle user input in Android?
10. How do you implement responsive design in Android?
11. How do you implement pagination in RecyclerView?
12. What is the purpose of the View.OnClickListener interface in Android?
13. How do you implement a custom theme in an Android application?
14. How do you implement swipe-to-refresh functionality in an Android app?
15. What are resource files in Android, and how are they organized?
16. How do you implement custom animations in Android?

## DAY 2 — UI Layer (Views → XML, Jetpack Compose, RecyclerView, Material Design) Part 2

#### Views and ViewGroups

1. What is the difference between a View and a ViewGroup?
2. Can you explain the View lifecycle in Android?
3. How do you handle user interactions with views?
4. What are the different types of ViewGroups available in Android?
5. How do you optimize view rendering and improve performance?
6. What is the purpose of the onDraw method in a View?

#### Layouts (ConstraintLayout, LinearLayout, RelativeLayout)

1. When would you choose ConstraintLayout over other layout types?
2. How do you create a complex UI using ConstraintLayout?
3. What are the performance implications of using nested LinearLayouts?
4. How do you align views in RelativeLayout?
5. What are the key attributes of ConstraintLayout?
6. How do you chain views in ConstraintLayout?
7. What are the differences between ConstraintLayout, LinearLayout, and RelativeLayout?

#### Custom Views

1. How do you create a custom view in Android?
2. What methods must be overridden when creating a custom view?
3. How do you handle custom attributes for custom views?
4. Can you explain how to draw on a Canvas in a custom view?
5. How do you make a custom view responsive to user interactions?
6. What are some use cases for custom views?

## DAY 2 — UI Layer (Views → XML, Jetpack Compose, RecyclerView, Material Design) Part 3

#### Material Design principles

1. What are the core principles of Material Design?
2. How do you implement Material Design components in an Android app?
3. What is the purpose of the Material Design guidelines?
4. How do you use the Material Design color system in an Android app?
5. What are some common Material Design components available in Android?
6. How do you implement shadows and elevations in Material Design?
7. What is the significance of the Material Theming in Material Design?

#### RecyclerView and Adapters

1. What is a RecyclerView and how does it differ from ListView?
2. How do you set up a RecyclerView with an Adapter?
3. What is the purpose of the ViewHolder pattern in RecyclerView?
4. How do you handle item click events in a RecyclerView?
5. Can you explain the different types of LayoutManagers available for RecyclerView?
6. How do you implement pagination in a RecyclerView?
7. What are ItemDecorations and how do you use them in RecyclerView?
8. How do you update the data in a RecyclerView Adapter?

---

---

## DAY 3 Architecture, State & Dependency Management

1. Explain the concept of Dependency Injection and how it is implemented in Android.
2. How do you implement data binding in Android?
3. Explain the purpose of the ViewModel class in Android Architecture Components.
4. What is the purpose of LiveData and how is it used in Android development?
5. Describe the purpose of the Android Data Binding Library.
6. Explain the concept of ViewModels in the MVVM (Model-View-ViewModel) architecture pattern.
7. Describe the purpose and usage of the ViewModelFactory in Android Architecture Components.
8. What are the advantages of using LiveData over traditional observables in Android?
9. Explain the purpose of the Navigation Component in Android.
10. Explain the concept of dependency injection and how it can be achieved in Android using Dagger 2.
11. Describe the purpose of the `ViewModelProviders` class in Android.
12. What is the purpose of the `ViewModelStoreOwner` interface in Android ViewModel architecture?

---

## DAY 4 — Concurrency, Coroutines, Services & Background Execution
1. Explain the difference between services and threads in Android.
2. What are the different threading models available in Android?
3. What are the differences between Services and IntentService?
4. What is the purpose of the AsyncTaskLoader in Android?
5. What is the purpose of the JobScheduler API in Android?
6. How do you handle background tasks efficiently in Android?

---

## DAY 5 Data, Networking, Background Work & Platform Capabilities

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

---

## DAY 6 Testing, Debugging, Performance, Tooling, Build & Compatibility

1. How do you optimize Android app performance?
2. What is the role of ADB (Android Debug Bridge) in Android development?
3. What is the purpose of ProGuard in Android development?
4. How do you handle memory leaks in Android?
5. How do you handle backward compatibility in Android development?
6. What is the purpose of the Android Gradle plugin?
7. How do you implement unit testing in Android?
8. Describe the purpose of the Android Jetpack library.
9. What is the `build.gradle` file, and how is it used in an Android project?
10. How do you manage dependencies in an Android project?

---

## DAY 7 Localization, Security & Release Readiness

1. How do you implement localization in Android?
2. What are some security best practices for Android app development?
3. Describe the steps involved in publishing an app on the Google Play Store.
4. How do you implement encryption in Android?
5. How do you implement biometric authentication in an Android app?
6. What is the purpose of the `proguard-rules.pro` file in an Android project?

---

## DAY 8 Kotlin, System Thinking & Interview Readiness

1. What are the advantages of using Kotlin for Android development?
2. `open` class in Kotlin meaning?
3. Companion Object vs Object in Kotlin

---


## Set 4 Android project structure and its files:

1. What are the main components of an Android project structure?
2. What is the purpose of the AndroidManifest.xml file?
3. What is the build.gradle file, and how is it used in an Android project?
4. What are resource files in Android, and how are they organized?
5. How are assets different from resources in an Android project?
6. What is the purpose of the proguard-rules.pro file in an Android project?
7. How do you manage dependencies in an Android project?

#### Set 5 Storage, intents, and background tasks

1. What are the different storage options available in Android?
2. Explain the difference between implicit and explicit intents.
3. What is the role of AsyncTask in Android?
4. How do you handle background tasks efficiently in Android?
5. Describe the purpose of the ViewModelProviders class in Android.
6. How do you implement custom animations in Android?
7. What is the purpose of the ViewModelStoreOwner interface in Android ViewModel architecture?
8. Content provider example with kotlin and to query media images in phone?
9. Data store and encrypted shared preferences in android?

#### Set 6 Views and ViewGroups

1. What is the difference between a View and a ViewGroup?
2. Can you explain the View lifecycle in Android?
3. How do you handle user interactions with views?
4. What are the different types of ViewGroups available in Android?
5. How do you optimize view rendering and improve performance?
6. What is the purpose of the onDraw method in a View?

#### Set 7 Layouts (ConstraintLayout, LinearLayout, RelativeLayout)

1. When would you choose ConstraintLayout over other layout types?
2. How do you create a complex UI using ConstraintLayout?
3. What are the performance implications of using nested LinearLayouts?
4. How do you align views in RelativeLayout?
5. What are the key attributes of ConstraintLayout?
6. How do you chain views in ConstraintLayout?
7. What are the differences between ConstraintLayout, LinearLayout, and RelativeLayout?

#### Set 8 Custom Views

1. How do you create a custom view in Android?
2. What methods must be overridden when creating a custom view?
3. How do you handle custom attributes for custom views?
4. Can you explain how to draw on a Canvas in a custom view?
5. How do you make a custom view responsive to user interactions?
6. What are some use cases for custom views?

#### Set 9 Material Design principles

1. What are the core principles of Material Design?
2. How do you implement Material Design components in an Android app?
3. What is the purpose of the Material Design guidelines?
4. How do you use the Material Design color system in an Android app?
5. What are some common Material Design components available in Android?
6. How do you implement shadows and elevations in Material Design?
7. What is the significance of the Material Theming in Material Design?

#### Set 10 RecyclerView and Adapters

1. What is a RecyclerView and how does it differ from ListView?
2. How do you set up a RecyclerView with an Adapter?
3. What is the purpose of the ViewHolder pattern in RecyclerView?
4. How do you handle item click events in a RecyclerView?
5. Can you explain the different types of LayoutManagers available for RecyclerView?
6. How do you implement pagination in a RecyclerView?
7. What are ItemDecorations and how do you use them in RecyclerView?
8. How do you update the data in a RecyclerView Adapter?

#### Set 11 Animations and Transitions

1. What are the different types of animations available in Android?
2. How do you create a simple animation in Android?
3. What are property animations and how do they differ from view animations?
4. Can you explain the concept of transitions in Android?
5. How do you implement shared element transitions between activities?
6. What is the purpose of the AnimatorSet class?
7. How do you animate a view’s property using ObjectAnimator?
8. What are keyframe animations and how do you use them?

---
