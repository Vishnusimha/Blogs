---
title: "DAY 3 Android Architecture, ViewModel and Dependency Injection Interview Q&A"
date: 2026-06-12
slug: "android-architecture-viewmodel-dependency-injection-interview-qa"
tags: [ "Notes", "Android", "ViewModel", "LiveData", "MVVM", "Dependency Injection", "Navigation Component" ]
summary: "Personal Android interview Q&A notes covering Dependency Injection, Hilt and Dagger, Data Binding, ViewModel, LiveData, MVVM, ViewModelFactory, Navigation Component, deprecated ViewModelProviders, and ViewModelStoreOwner."
categories: Notes
readTime: 16
---

# Technical interview questions

#### DAY 3 Architecture, State & Dependency Management

1. Explain the concept of Dependency Injection and how it is implemented in Android.
2. How do you implement data binding in Android?
3. Explain the purpose of the ViewModel class in Android Architecture Components.
4. What is the purpose of LiveData and how is it used in Android development?
5. Describe the purpose of the Android Data Binding Library.
6. Explain the concept of ViewModels in the MVVM (Model-View-ViewModel) architecture pattern.
7. Describe the purpose and usage of the ViewModelFactory in Android Architecture Components.
8. What are the advantages of using LiveData over traditional observables in Android?
9. Explain the purpose of the Navigation Component in Android.
10. Explain the concept of dependency injection and how it can be achieved in Android using Dagger 2
11. Describe the purpose of the `ViewModelProviders ⚠️ *Deprecated*` class in Android.
12. What is the purpose of the `ViewModelStoreOwner` interface in Android ViewModel architecture?

## Answers:

#### Day 3 Answers

### 1. Explain the concept of Dependency Injection and how it is implemented in Android.

**Dependency Injection (DI)** is a design pattern used to achieve Inversion of Control (IoC) by
decoupling object
creation from object usage. In Android, DI can be implemented using frameworks like Dagger or *
*Hilt (modern recommended)**. ⚠️ *Pure Dagger setup is now mostly legacy for Android apps.*

Example with Hilt:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideRepository(): Repository {
        return RepositoryImpl()
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

### 2. How do you implement data binding in Android?

**Data Binding** allows you to bind UI components ⚠️ *Still supported but no longer preferred for
new projects. View Binding or Jetpack Compose are modern alternatives.* to data sources in a
declarative manner.

Steps to implement data binding:

1. **Enable Data Binding**: Add data binding to your `build.gradle`:

    ```groovy
    android {
        ...
        viewBinding {  // ⚠️ This enables View Binding, not Data Binding. For Data Binding you should use dataBinding { enabled = true } (XML-based apps only).
            enabled = true
        }
    }
    ```

2. **Layout File**: Wrap your XML layout file in a `<layout>` tag.

    ```xml
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
        <data>
            <variable
                name="user"
                type="com.example.User" />
        </data>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            <TextView
                android:id="@+id/nameTextView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{user.name}" />
        </LinearLayout>
    </layout>
    ```

3. **Bind Data in Activity**:

    ```kotlin
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            val user = User("John Doe")
            binding.user = user
        }
    }
    ```

### 3. Explain the purpose of the ViewModel class in Android Architecture Components.

The **ViewModel** class is part of Android Architecture Components and is designed to store and
manage UI-related data
in a lifecycle-conscious way. This ensures that data survives configuration changes such as screen
rotations. ViewModels
help separate UI data from UI controller logic, adhering to the MVVM (Model-View-ViewModel)
architecture pattern.

Example in Kotlin:

```kotlin
class MyViewModel : ViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()

    fun loadUserData() {
        // Load user data asynchronously
    }
}
```

### 4. What is the purpose of LiveData and how is it used in Android development?

**LiveData** is an observable data holder class ⚠️ *Still used, but Flow/StateFlow is preferred in
modern apps (especially with Compose).* that is lifecycle-aware, meaning it respects the lifecycle
of other app
components such as activities, fragments, or services. LiveData updates only observers that are in
an active lifecycle
state.

Example in Kotlin:

```kotlin
class MyViewModel : ViewModel() {
    val userData: LiveData<User> = MutableLiveData()

    fun loadUser() {
        // Load user data asynchronously
    }
}

// In an Activity or Fragment
myViewModel.userData.observe(
    this,
    Observer { user ->  // ⚠️ LiveData observer. In modern apps, Flow + collect is preferred.
        // Update UI with user data
    })
```

### 5. Describe the purpose of the Android Data Binding Library.

The **Data Binding Library** allows you to bind UI components ⚠️ *Considered legacy for new
projects. Compose replaces this approach.* in your layouts directly to data sources, reducing
boilerplate code and improving code readability.

Example:

```xml

<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="user" type="com.example.User" />
    </data>
    <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent">
        <TextView android:id="@+id/nameTextView" android:layout_width="wrap_content"
            android:layout_height="wrap_content" android:text="@{user.name}" />
    </LinearLayout>
</layout>
```

### 6. Explain the concept of ViewModels in the MVVM (Model-View-ViewModel) architecture pattern.

In MVVM, the **ViewModel** acts as a bridge between the View and the Model. It holds UI data and
business logic,
exposing data to the View via observable properties. The ViewModel ensures that the View is only
responsible for
rendering data and user interactions.

### 7. Describe the purpose and usage of the ViewModelFactory in Android Architecture Components.

The **ViewModelFactory** is used to create instances of ViewModel with specific constructor
parameters. It allows for
dependency injection into ViewModels.

Example:

```kotlin
class MyViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

### 8. What are the advantages of using LiveData over traditional observables in Android?

- **Lifecycle-aware**: Automatically manages subscription based on the lifecycle state of UI
  components.
- **No memory leaks**: Observers are bound to lifecycle objects, reducing the risk of leaks.
- **No crashes due to stopped activities**: Updates are only sent to active observers.
- **Ease of use**: Simple API for handling updates.
- **Integration with ViewModel**: Works seamlessly with ViewModel to manage UI-related data.

### 9. Explain the purpose of the Navigation Component in Android.

The **Navigation Component** simplifies ⚠️ *Still valid for Fragment-based apps. Compose Navigation
is used in modern Compose apps.* the implementation of navigation between app screens. It manages
fragment
transactions, handles deep linking, and provides a consistent navigation experience.

Example:

```kotlin
val navController = findNavController(R.id.nav_host_fragment)
navController.navigate(R.id.action_mainFragment_to_detailFragment)
```

### 10. Explain the concept of dependency injection and how it can be achieved in Android using Dagger 2.

**Dependency Injection (DI)** is a design pattern that helps to reduce the coupling between classes
and their
dependencies. It allows dependencies to be injected at runtime rather than being hardcoded.

**Dagger 2** is a popular DI framework ⚠️ *Now mostly replaced by Hilt in Android projects.* in
Android:

1. **Add dependencies:**

```gradle
dependencies {
    implementation 'com.google.dagger:dagger:2.37'
    kapt 'com.google.dagger:dagger-compiler:2.37'
}
```

---

### 11. Describe the purpose of the `ViewModelProviders ⚠️ *Deprecated*` class in Android.

**Answer:**

- The `ViewModelProviders ⚠️ *Deprecated*` class was used to create and retrieve `ViewModel`
  instances.
- It was deprecated in favor of `ViewModelProvider`.

**Example using `ViewModelProvider`:**

```kotlin
val viewModel: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
```

**Why use ViewModel?**

- It retains data across configuration changes (e.g., screen rotation).
- Prevents unnecessary reloading of data.

### 12. What is the purpose of the`ViewModelStoreOwner` interface in Android ViewModel architecture?

**Answer:**

- The `ViewModelStoreOwner` interface is used to provide `ViewModelStore`, which stores ViewModels.
- It is typically implemented by `FragmentActivity` and `Fragment`.

**Use Case:**

- Ensures `ViewModel` instances survive configuration changes.
- Allows sharing `ViewModel` between fragments in the same activity.

**Example of `ViewModelStoreOwner`:**

```kotlin
val viewModel = ViewModelProvider(viewModelStoreOwner).get(MyViewModel::class.java)
```
