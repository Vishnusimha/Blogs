---
title: "Android Architecture, State and Dependency Management Guide"
date: 2026-06-12
slug: "android-architecture-state-dependency-management-guide"
tags: [ "Notes", "Android", "Architecture", "MVVM", "Clean Architecture", "State Management", "Dependency Injection" ]
summary: "Personal senior Android architecture notes covering MVC, MVP, MVVM, Clean Architecture, Repository Pattern, Single Source of Truth, MVI, unidirectional data flow, modular architecture, state management, dependency injection, navigation, and testing."
categories: Notes
readTime: 30
---

# DAY 3 — Architecture, State & Dependency Management

## Senior-Level Android Interview Preparation Guide

This document is a **deep-dive, industry-level architecture guide** designed for **mid-to-senior
Android engineers**. It covers architectural thinking, trade-offs, real-world implementations, and
interview-ready explanations.

---

## 📑 Table of Contents

- [Architecture Patterns](#architecture-patterns)
    - [MVC, MVP, MVVM](#mvc-mvp-mvvm)
    - [Clean Architecture](#clean-architecture)
    - [Repository Pattern](#repository-pattern)
    - [Single Source of Truth](#single-source-of-truth)
    - [MVI (Model–View–Intent)](#mvi-modelviewintent)
    - [Unidirectional Data Flow (UDF)](#unidirectional-data-flow-udf)
    - [Modular Architecture](#modular-architecture)
- [State Management](#state-management)
- [Android Architecture Components](#android-architecture-components)
- [Repository & Data Layer](#repository--data-layer)
- [Dependency Injection](#dependency-injection)
- [Navigation & App Structure](#navigation--app-structure)
- [Testing (Architecture)](#testing-architecture)

---

# Architecture Patterns

## MVC, MVP, MVVM

### Overview

These patterns represent the **evolution of UI architecture** in Android:

| Pattern | Problem Solved             | Limitation                     |
|---------|----------------------------|--------------------------------|
| MVC     | Separates UI from data     | Controller becomes massive     |
| MVP     | Improves testability       | Too many interfaces            |
| MVVM    | Lifecycle-aware + reactive | Requires good state management |

* **MVC (Model–View–Controller)**
  Early Android followed MVC loosely:

    * Activity/Fragment = View + Controller
    * Model = data layer
      ❌ Problem: Activities become *Massive Controllers*.

* **MVP (Model–View–Presenter)**
  Introduced to improve testability:

    * View = passive (Activity/Fragment)
    * Presenter = UI logic
    * Model = data
      ✅ Better separation
      ❌ Too many interfaces, lifecycle handling is manual.

* **MVVM (Model–View–ViewModel)**
  Modern standard for Android:

    * ViewModel holds UI state
    * View observes state
    * Model = repository/domain
      ✅ Lifecycle-aware
      ✅ Works naturally with LiveData, Flow, Compose
      ❌ Requires proper state/event separation.

<div id=“architecturecomparisons></div>
<details>
<summary><b>🔴 <font color="red">Architecture Comparisons</font> 🔴</b></summary>
<br>
<blockquote>

Here’s the same section rewritten with **MVVM as the baseline reference model** 👇

## Architecture Patterns (Compared Using MVVM as Base)

### 🧠 Start Point: MVVM (Your Reference Model)

Before comparing others, remember MVVM:

| Layer         | Responsibility              |
|---------------|-----------------------------|
| **View**      | Displays UI, observes state |
| **ViewModel** | Holds UI state & logic      |
| **Model**     | Repository / domain / data  |

MVVM advantages:

* Lifecycle-aware (ViewModel survives rotation)
* Reactive (Flow/LiveData)
* View does not hold logic
* Easy to test ViewModel

Now let’s compare older patterns **relative to MVVM**.

---

### MVC (Model–View–Controller) — *Primitive MVVM*

#### How it maps compared to MVVM

| MVVM      | MVC Equivalent | Problem                  |
|-----------|----------------|--------------------------|
| View      | View           | Same                     |
| ViewModel | ❌ Missing      | Logic goes to Controller |
| Model     | Model          | Same                     |

In Android:

* Activity/Fragment acts as **View + Controller**
* There is **no ViewModel equivalent**

#### What MVVM improves over MVC

| MVC Problem      | MVVM Solution               |
|------------------|-----------------------------|
| Fat Activity     | Move logic to ViewModel     |
| Hard to test     | ViewModel is testable       |
| Lifecycle issues | ViewModel survives rotation |

#### Interview Line

> “MVC collapses View and Controller into Activities, leading to massive classes. MVVM separates
> state into ViewModel and improves lifecycle handling.”

---

### MVP (Model–View–Presenter) — *Manual MVVM*

#### How it maps compared to MVVM

| MVVM      | MVP Equivalent   |
|-----------|------------------|
| View      | View (interface) |
| ViewModel | Presenter        |
| Model     | Model            |

MVP introduced a **Presenter**, which is conceptually similar to ViewModel.

#### Key Differences from MVVM

| MVP                          | MVVM                        |
|------------------------------|-----------------------------|
| View is passive              | View observes state         |
| Presenter calls View methods | View observes reactive data |
| Manual lifecycle handling    | Lifecycle-aware ViewModel   |
| Uses interfaces heavily      | Less boilerplate            |

#### What MVVM improves over MVP

| MVP Limitation             | MVVM Benefit                |
|----------------------------|-----------------------------|
| Too many interfaces        | No View interfaces required |
| Presenter dies on rotation | ViewModel survives          |
| Manual state restore       | Built-in lifecycle handling |

#### Interview Line

> “MVP was an attempt to fix MVC by introducing Presenter, but MVVM improved it further by adding
> lifecycle awareness and reactive state observation.”

---

### MVVM (Modern Standard)

#### Why MVVM became dominant

MVVM combines the **best parts of MVP** with Android lifecycle support.

| Feature                     | Why It Matters        |
|-----------------------------|-----------------------|
| ViewModel survives rotation | No UI data loss       |
| Reactive streams            | Automatic UI updates  |
| No direct View reference    | Prevents memory leaks |
| Works with Compose          | State-driven UI       |

#### Real-World MVVM Stack

```
Compose UI / Fragment
        ↓
     ViewModel
        ↓
   Repository
        ↓
 DB + Network
```

---

### Senior-Level Summary Comparison

| Pattern | What It Misses That MVVM Solves                          |
|---------|----------------------------------------------------------|
| MVC     | No lifecycle-aware state holder                          |
| MVP     | Manual lifecycle & heavy interfaces                      |
| MVVM    | Modern solution, but needs proper state/event separation |

---

### Final Interview-Ready Summary

> “I use MVVM as the baseline. MVC puts too much responsibility on Activities. MVP improves
> separation but requires manual lifecycle handling. MVVM modernizes this with lifecycle-aware
> ViewModels and reactive state, making it the standard architecture in Android today.”

---

### ELABORATION:

When we say **“MVP requires manual lifecycle handling”**, we mean:

> **The Presenter does NOT automatically survive configuration changes or know about Android
lifecycle states. You have to manage it yourself.**

Let’s compare with MVVM to make it crystal clear.

## 🔁 Problem Scenario: Screen Rotation

### 📱 In MVVM

```kotlin
class MyViewModel : ViewModel() {
    var counter = 0
}
```

* Activity rotates
* Activity is destroyed & recreated
* **ViewModel survives automatically**
* `counter` value is preserved

✅ Lifecycle handled by Android framework

---

### 📱 In MVP

You have:

```kotlin
class MyPresenter {
    var counter = 0
}
```

Presenter is usually created like this:

```kotlin
class MainActivity : AppCompatActivity(), MyView {
    private val presenter = MyPresenter()
}
```

When screen rotates:

* Activity is destroyed ❌
* New Activity created ❌
* New Presenter created ❌
* `counter` resets to 0 ❌

👉 **Presenter does NOT survive rotation**

---

## 🧠 So what must you do in MVP?

You must manually handle:

### 1️⃣ Saving state

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    outState.putInt("counter", presenter.counter)
}
```

### 2️⃣ Restoring state

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    presenter = MyPresenter()
    presenter.counter = savedInstanceState?.getInt("counter") ?: 0
}
```

### 3️⃣ Handling View attach/detach

Presenter must avoid memory leaks:

```kotlin
override fun onStart() {
    presenter.attachView(this)
}

override fun onStop() {
    presenter.detachView()
}
```

If you forget:
💥 Memory leaks
💥 Crashes
💥 Lost state

---

## 🔥 Why MVVM is better here

ViewModel is lifecycle-aware:

```kotlin
class MyViewModel : ViewModel()^
```

Android automatically:

* Keeps it across rotation
* Clears it when Activity finishes
* No need to attach/detach View manually

---

## 🎯 Interview One-Liner

> “In MVP, the Presenter doesn’t know about Android lifecycle, so we must manually save state,
> restore it, and manage view attachment to avoid memory leaks. MVVM solves this by using
> lifecycle-aware ViewModels.”

---

Want me to explain **memory leak risk in MVP** next? That’s another common follow-up.

> [⬆️ Back to Top / Close](#architecturecomparisons)
</blockquote>
</details>

### Deep Dive

MVVM is dominant today because:

- ViewModel survives configuration changes
- Works well with LiveData, Flow, and Compose state
- Removes tight coupling between UI and business logic

### Real-World Implementation

```kotlin
class ProfileViewModel(private val repo: UserRepository) : ViewModel() {
    val uiState = repo.getUser().stateIn(viewModelScope)
}
```

### Senior Interview Q&A

**Q: Why is MVVM preferred over MVP in modern Android?**  
A: MVVM integrates with lifecycle-aware components and reactive streams, reducing boilerplate and
improving state handling.

**Q: Where should business logic live?**  
A: In the domain layer or use cases, not inside Activities or Fragments.

---

## Clean Architecture

### Overview

Separates app into **presentation, domain, and data layers**.

### Deep Dive

- Domain layer must be independent of Android framework
- Enables scalability and easier testing
- Trade-off: more boilerplate

### Real-World Implementation

```kotlin
class GetUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(id: String) = repo.getUser(id)
}
```

### Senior Interview Q&A

**Q: When is Clean Architecture overkill?**  
A: For small apps or prototypes with limited business logic.

---

## Repository Pattern

### Overview

Acts as **abstraction layer** between ViewModel and data sources.

### Deep Dive

Repository coordinates:

- Network
- Database
- Cache
- Conflict resolution

### Real-World Example

```kotlin
class UserRepository(
    private val api: ApiService,
    private val dao: UserDao
) {
    fun getUsers() = dao.observeUsers()
}
```

---

## Single Source of Truth

### Overview

**Single Source of Truth (SSOT)** is an architectural principle where **one reliable data source is
considered the authoritative version of data** in the app.

In modern Android apps, this is typically the **local database (Room)**.

Instead of the UI directly consuming:

- API responses
- In-memory caches
- Multiple repositories

👉 All data flows through **one central source**, ensuring **consistency and reliability**.

**Key idea:**
> The UI should observe data from the database, not directly from the network.

---

### Deep Dive

SSOT is critical in real-world apps because mobile environments are:

- Unreliable (network drops)
- Concurrent (multiple requests at once)
- Lifecycle-driven (rotations, background/foreground)

Without SSOT, you get:

❌ Race conditions  
❌ Stale UI data  
❌ Multiple conflicting states  
❌ Difficult debugging

---

#### 🚨 Problem Without SSOT

Imagine:

1. Screen A loads user profile → API call
2. Screen B updates user profile → API call
3. Screen A still holds old API response

Now:

- Screen A shows outdated data
- Screen B shows updated data

💥 UI inconsistency

---

#### ✅ With Single Source of Truth

Flow becomes:

```
Network → Save to DB → UI observes DB
```

Steps:

1. Repository fetches from API
2. Repository stores data in **Room database**
3. UI observes **Flow from Room**
4. Any update → DB changes → UI auto-updates

Now:
✔ All screens show the same data  
✔ No stale state  
✔ Offline works automatically

---

### Real-World Example

```kotlin
class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {
    fun observeUser(id: String) = dao.observeUser(id) // UI observes DB

    suspend fun refreshUser(id: String) {
        val response = api.getUser(id)
        dao.insert(response.toEntity()) // DB becomes the source of truth
    }
}
```

UI:

```kotlin
viewModelScope.launch {
    repository.observeUser("123").collect { user ->
        _uiState.value = user
    }
}
```

Notice:

- UI never uses API result directly
- API only **updates the DB**
- DB drives UI

---

### Why SSOT Prevents Race Conditions

If two API calls return in different order:

Without SSOT:

- UI might display older response last

With SSOT:

- Last DB write wins
- All observers receive the same latest data

---

### SSOT + Offline Support

Because UI reads from DB:

- App works offline automatically
- Network only enhances data freshness
- No special offline logic needed in UI

---

### Senior-Level Insight

> SSOT shifts responsibility from UI to data layer. The database becomes the state manager of the
> app, ensuring consistency across screens, lifecycles, and network states.

---

### Interview One-Liner

> “Single Source of Truth means the UI observes only one authoritative data source—usually the local
> database—while the network acts only as a data updater. This prevents race conditions, stale UI,
> and
> ensures consistency across the app.”

---

## MVI (Model–View–Intent)

### Overview

UI emits intents → Reducer produces new immutable state.

### Deep Dive

Great for Compose because state is immutable and predictable.

---

## Unidirectional Data Flow (UDF)

Data flows in one direction:
UI → Event → ViewModel → State → UI

Prevents circular updates and bugs.

---

## Modular Architecture

### Overview

Split app into modules: core, feature, data, domain.

### Benefits

- Faster builds
- Parallel development
- Clear boundaries

---

# State Management

### State vs Event

State = long-lived UI data  
Event = one-time action (navigation, toast)

### StateFlow vs SharedFlow

StateFlow = UI state  
SharedFlow = events

### remember vs rememberSaveable

remember → survives recomposition  
rememberSaveable → survives process death (via Bundle)

### SavedStateHandle

Used inside ViewModel to restore state after process death.

---

# Android Architecture Components

### ViewModel

Stores UI state across rotations.

### LiveData

Legacy observable; Flow preferred now.

Great question — this is a **very common interview confusion**. Let’s clear it properly.

---

## 📌 What is Data Binding in Android? ⚠️ It is no longer the preferred modern approach.

| Technology             | Status                            |
|------------------------|-----------------------------------|
| **Data Binding (XML)** | Still supported, maintenance mode |
| **View Binding**       | Preferred for XML projects        |
| **Jetpack Compose**    | Official future direction         |

**Data Binding** is a **View-based UI feature** (XML world) that allows you to bind UI components
directly to data sources (like ViewModel) **without writing boilerplate `findViewById` or setter
code**.

It is part of **Android View System**, not Compose.

### Example (XML + MVVM)

```xml

<layout>
    <data>
        <variable name="viewModel" type="com.example.UserViewModel" />
    </data>

    <TextView android:text="@{viewModel.username}" />
</layout>
```

ViewModel:

```kotlin
class UserViewModel : ViewModel() {
    val username = MutableLiveData("Vishnu")
}
```

👉 XML observes ViewModel directly.

---

## 🧠 Is Data Binding part of MVVM?

**Yes — historically.**

Data Binding was introduced to support **MVVM in XML-based UIs**:

| MVVM Layer | Data Binding Role         |
|------------|---------------------------|
| View       | XML layout                |
| ViewModel  | Exposed LiveData          |
| Binding    | Connects View ↔ ViewModel |

So Data Binding was the **bridge** between XML UI and ViewModel.

---

## ❓ Is Data Binding used in Jetpack Compose?

**❌ No. Not needed.**

Jetpack Compose **replaces Data Binding entirely**.

Compose already works like MVVM + Data Binding combined:

```kotlin
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val name by viewModel.name.collectAsState()

    Text(text = name)
}
```

No XML. No binding expressions.
State automatically updates UI.

---

## ⚖️ Data Binding vs Compose

| Feature      | Data Binding (XML) | Jetpack Compose    |
|--------------|--------------------|--------------------|
| UI           | XML                | Kotlin code        |
| Binding      | Expressions in XML | Direct state usage |
| MVVM support | Yes                | Native             |
| Boilerplate  | Medium             | Low                |
| Future-proof | ⚠️ Legacy          | ✅ Modern           |

---

## 🎯 Interview-Ready Answer

> “Data Binding is part of the XML View system and was used to connect ViewModel with layouts in
> MVVM architecture. Jetpack Compose does not use Data Binding because Compose is already
> state-driven
> and directly observes state from ViewModels, making Data Binding unnecessary.”

---

### 🔹 Data Binding vs View Binding

Both are **XML-based view access tools**, but they solve **different problems**.

---

## 🧩 What is View Binding?

**View Binding** generates a binding class for each XML layout to give **type-safe access to views
**.

It **does NOT connect UI to ViewModel**.
It only replaces `findViewById`.

```kotlin
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.titleText.text = "Hello"
}
```

👉 Just safe view references.

---

## 🧠 What is Data Binding?

**Data Binding** allows **binding UI directly to data (usually ViewModel)** using XML expressions.

```xml

<TextView android:text="@{viewModel.username}" />
```

It can:

* Observe LiveData automatically
* Call methods from XML
* Use logic inside XML

👉 It connects **UI ↔ ViewModel**.

---

## ⚖️ Key Differences

| Feature               | View Binding           | Data Binding           |
|-----------------------|------------------------|------------------------|
| Purpose               | Replace `findViewById` | Bind UI to data (MVVM) |
| XML expressions       | ❌ Not supported        | ✅ Supported            |
| LiveData auto observe | ❌ No                   | ✅ Yes                  |
| Two-way binding       | ❌ No                   | ✅ Yes                  |
| Compile speed         | ⚡ Fast                 | 🐢 Slower              |
| Complexity            | Simple                 | Complex                |
| Debugging             | Easy                   | Hard                   |
| Recommended today     | ✅ Yes (for XML)        | ⚠️ Only for legacy     |
| Used in Compose       | ❌ No                   | ❌ No                   |

---

## 🚩 Why View Binding replaced Data Binding in XML projects

* No annotation processing delays
* No hidden XML logic
* Easier debugging
* Clear separation of UI & logic

Teams prefer writing logic in Kotlin, not XML.

---

## 🏢 Real-World Industry Trend (2026)

| Scenario         | Recommended                             |
|------------------|-----------------------------------------|
| Old MVVM XML app | Continue Data Binding (if already used) |
| New XML project  | View Binding                            |
| Modern apps      | Jetpack Compose (no binding needed)     |

---

## 🎯 Interview-Ready Answer

> “View Binding is a lightweight tool that only gives type-safe access to XML views. Data Binding is
> more powerful and supports binding UI directly to ViewModel data using expressions in XML. However,
> Data Binding adds complexity and build overhead, so most modern XML projects prefer View Binding,
> while Jetpack Compose eliminates both.”

---

### Navigation Component

Handles navigation graph, back stack, deep links.

### Lifecycle-aware coroutines

```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) { }
}
```

---

# Repository & Data Layer

### Offline-first Architecture

DB is source of truth. Network updates DB.

### Caching Strategies

- Memory cache (fast)
- Disk cache (persistent)

### Error Handling

Robust error handling is a **core responsibility of the data and domain layers** in a production
Android app.  
Instead of throwing exceptions directly to the UI, modern Android apps **model errors explicitly**
using sealed classes.

This leads to:

- predictable UI states
- better testability
- clearer business logic
- fewer crashes

---

### Why NOT use exceptions directly?

Using `try/catch` everywhere or letting exceptions bubble up causes:

❌ UI crashes  
❌ Unclear error origin  
❌ Tight coupling between UI and data layer  
❌ Difficult testing

Example of **bad practice**:

```kotlin
val data = api.getUsers() // throws exception → crash
````

---

### Industry-Standard Approach: Sealed Result Wrapper

We represent **all possible outcomes** of an operation explicitly.

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val exception: Throwable,
        val message: String? = null
    ) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

Now every operation can be:

* Success
* Error
* Loading

Nothing is hidden.

---

### Repository-Level Error Handling (Best Practice)

Repositories **catch exceptions and convert them into domain-safe results**.

```kotlin
class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {

    suspend fun fetchUsers(): Result<List<User>> {
        return try {
            val response = api.getUsers()
            dao.insertAll(response.toEntities())
            Result.Success(response.map { it.toDomain() })
        } catch (e: IOException) {
            Result.Error(e, "Network error")
        } catch (e: HttpException) {
            Result.Error(e, "Server error ${e.code()}")
        } catch (e: Exception) {
            Result.Error(e, "Unknown error")
        }
    }
}
```

Key points:

* Repository **owns error mapping**
* UI never sees raw exceptions
* Network & DB errors are abstracted

---

### UI State Mapping (MVVM / MVI Friendly)

ViewModel converts `Result` into UI state.

```kotlin
fun loadUsers() {
    viewModelScope.launch {
        _uiState.value = Result.Loading
        _uiState.value = repository.fetchUsers()
    }
}
```

UI reacts safely:

```kotlin
when (val result = uiState) {
    is Result.Loading -> showLoading()
    is Result.Success -> showData(result.data)
    is Result.Error -> showError(result.message)
}
```

---

### Advanced: Domain-Specific Errors (Senior Level)

Instead of generic `Throwable`, large apps define **explicit error types**.

```kotlin
sealed class AppError {
    object Network : AppError()
    object Unauthorized : AppError()
    object NotFound : AppError()
    data class Unknown(val throwable: Throwable) : AppError()
}
```

Result becomes:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: AppError) : Result<Nothing>()
}
```

Benefits:

* UI can react differently to each error
* Better analytics & logging
* Cleaner retry logic

---

### Error Handling + Offline-First Architecture

With **Single Source of Truth**:

* Network failure ≠ UI failure
* UI still shows cached data

```kotlin
fun observeUsers(): Flow<Result<List<User>>> =
    dao.observeUsers()
        .map { Result.Success(it.map { user -> user.toDomain() }) }
        .catch { emit(Result.Error(it)) }
```

---

### Error Handling with Paging 3

Paging handles errors via `LoadState`:

```kotlin
when (loadState.refresh) {
    is LoadState.Error -> showRetry()
    is LoadState.Loading -> showLoader()
}
```

But **business errors** should still be modeled using sealed classes.

---

### Logging & Monitoring (Production Reality)

In real apps:

* Errors are logged (Crashlytics / Sentry)
* User-friendly messages are shown
* Technical details stay in logs

```kotlin
Result.Error(e, "Something went wrong")
```

---

### Senior-Level Best Practices Summary

✅ Use sealed classes for results
✅ Convert exceptions at repository layer
✅ Never expose raw exceptions to UI
✅ Separate technical error from user message
✅ Support offline + cached data
✅ Make error states testable

---

### Interview One-Liner (Strong)

> “In production apps, errors are modeled explicitly using sealed classes instead of thrown
> exceptions. Repositories convert failures into domain-safe results, enabling predictable UI
> states,
> better testing, and resilient offline-first behavior.”

---

<div id=“errorhandling></div>
<details>
<summary><b>🔴 <font color="red">Login & Registration with Sealed Result Error Handling</font> 🔴</b></summary>
<br>
<blockquote>

### 🔴 Login & Registration with Sealed Result Error Handling 🔴

Modern Android apps **do not throw raw exceptions to the UI**.  
Instead, they **convert failures into structured result types** so the UI can react predictably.

This example shows the **complete flow**:

UI → ViewModel → Repository → API  
with **proper error mapping** at the repository layer.

---

## 1️⃣ Sealed Result Wrapper

We model all possible outcomes of an API call.

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: AppError) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
````

---

## 2️⃣ Domain-Level Error Types (Industry Standard)

Instead of exposing exceptions, we define **app-specific errors**.

```kotlin
sealed class AppError {
    object Network : AppError()
    object InvalidCredentials : AppError()
    object UserAlreadyExists : AppError()
    object Server : AppError()
    data class Unknown(val throwable: Throwable) : AppError()
}
```

This allows UI to handle errors **intelligently**.

---

## 3️⃣ API Interface

```kotlin
interface AuthApi {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDto>
}
```

---

## 4️⃣ Repository Layer (Where Errors Are Handled)

This is the **MOST IMPORTANT layer for error handling**.

```kotlin
class AuthRepository(private val api: AuthApi) {

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val user = response.body()!!.toDomain()
                Result.Success(user)
            } else {
                when (response.code()) {
                    401 -> Result.Failure(AppError.InvalidCredentials)
                    in 500..599 -> Result.Failure(AppError.Server)
                    else -> Result.Failure(AppError.Unknown(HttpException(response)))
                }
            }

        } catch (e: IOException) {
            Result.Failure(AppError.Network)
        } catch (e: Exception) {
            Result.Failure(AppError.Unknown(e))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))

            if (response.isSuccessful) {
                Result.Success(response.body()!!.toDomain())
            } else {
                when (response.code()) {
                    409 -> Result.Failure(AppError.UserAlreadyExists)
                    in 500..599 -> Result.Failure(AppError.Server)
                    else -> Result.Failure(AppError.Unknown(HttpException(response)))
                }
            }

        } catch (e: IOException) {
            Result.Failure(AppError.Network)
        } catch (e: Exception) {
            Result.Failure(AppError.Unknown(e))
        }
    }
}
```

### 🔎 What we are handling here

| Situation                | Error Type                    |
|--------------------------|-------------------------------|
| No internet              | `AppError.Network`            |
| Wrong password           | `AppError.InvalidCredentials` |
| Email already registered | `AppError.UserAlreadyExists`  |
| Server crash             | `AppError.Server`             |
| Unexpected issue         | `AppError.Unknown`            |

---

## 5️⃣ ViewModel Layer

ViewModel exposes UI-safe state.

```kotlin
class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<User>>(Result.Loading)
    val uiState: StateFlow<Result<User>> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = Result.Loading
            _uiState.value = repo.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = Result.Loading
            _uiState.value = repo.register(name, email, password)
        }
    }
}
```

---

## 6️⃣ UI Layer (Fragment / Compose)

UI reacts **without knowing about exceptions**.

```kotlin
when (val state = uiState) {
    is Result.Loading -> showLoading()
    is Result.Success -> navigateToHome(state.data)
    is Result.Failure -> when (state.error) {
        AppError.Network -> showMessage("Check your internet connection")
        AppError.InvalidCredentials -> showMessage("Wrong email or password")
        AppError.UserAlreadyExists -> showMessage("Email already registered")
        AppError.Server -> showMessage("Server error. Try again later")
        is AppError.Unknown -> showMessage("Something went wrong")
    }
}
```

---

## 🧠 Why This Is Industry Standard

| Benefit                    | Why It Matters                                |
|----------------------------|-----------------------------------------------|
| No crashes from API errors | Exceptions are caught early                   |
| UI is simple               | UI reacts to states, not exceptions           |
| Testable                   | You can fake `Result.Failure` easily          |
| Scalable                   | Add new error types without changing UI logic |
| Offline ready              | Can fallback to cached data if needed         |

---

## 🎯 Senior-Level Interview One-Liner

> “In production apps, login and registration errors are handled in the repository layer by mapping
> exceptions and HTTP codes into sealed result types. The ViewModel exposes these as UI state,
> allowing the UI to react predictably without ever handling raw exceptions.”


If you'd like, next know about:

- 🔁 **Retry mechanism implementation**
- 📡 **Login with token refresh error handling**
- 🧪 **Unit testing this login flow**

---

## 🧪 Unit Testing Login Flow (Sealed Result Pattern)

We want to test **ViewModel + Repository logic** without:

- Real network calls
- Android framework dependencies

We use:

- **Fake repository**
- **Coroutine test dispatcher**
- **StateFlow testing**

---

### 1️⃣ Test Dependencies

```gradle
testImplementation "junit:junit:4.13.2"
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
testImplementation "app.cash.turbine:turbine:1.0.0"
````

---

### 2️⃣ Fake Repository for Tests

We simulate different outcomes.

```kotlin
class FakeAuthRepository : AuthRepository {

    var resultToReturn: Result<User> = Result.Success(User("1", "Test"))

    override suspend fun login(email: String, password: String): Result<User> {
        return resultToReturn
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return resultToReturn
    }
}
```

---

### 3️⃣ Test Coroutine Rule

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    private val dispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

---

### 4️⃣ ViewModel Test Class

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepo: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        fakeRepo = FakeAuthRepository()
        viewModel = AuthViewModel(fakeRepo)
    }
```

---

### 5️⃣ Test: Successful Login

```kotlin
@Test
fun `login success emits Success state`() = runTest {
        val user = User("1", "Vishnu")
        fakeRepo.resultToReturn = Result.Success(user)

        viewModel.login("test@mail.com", "1234")

        viewModel.uiState.test {
            assert(awaitItem() is Result.Loading)
            val result = awaitItem()
            assert(result is Result.Success && result.data == user)
            cancelAndIgnoreRemainingEvents()
        }
    }
```

---

### 6️⃣ Test: Network Error

```kotlin
@Test
fun `login network error emits Failure Network`() = runTest {
        fakeRepo.resultToReturn = Result.Failure(AppError.Network)

        viewModel.login("test@mail.com", "1234")

        viewModel.uiState.test {
            assert(awaitItem() is Result.Loading)
            val result = awaitItem()
            assert(result is Result.Failure && result.error is AppError.Network)
            cancelAndIgnoreRemainingEvents()
        }
    }
```

---

### 7️⃣ Test: Invalid Credentials

```kotlin
@Test
fun `login invalid credentials emits Failure InvalidCredentials`() = runTest {
        fakeRepo.resultToReturn = Result.Failure(AppError.InvalidCredentials)

        viewModel.login("test@mail.com", "wrong")

        viewModel.uiState.test {
            assert(awaitItem() is Result.Loading)
            val result = awaitItem()
            assert(result is Result.Failure && result.error is AppError.InvalidCredentials)
            cancelAndIgnoreRemainingEvents()
        }
    }
```

---

## 🧠 What We Just Tested

| Layer      | Tested? | How                     |
|------------|---------|-------------------------|
| API        | ❌       | Replaced by fake repo   |
| Repository | ❌       | Simulated output        |
| ViewModel  | ✅       | Observed emitted states |
| UI         | ❌       | Not needed in unit test |

---

## 🎯 Senior Interview One-Liner

> “We unit test ViewModels by injecting a fake repository and verifying emitted UI states using Flow
> testing tools like Turbine. This keeps tests fast, deterministic, and independent of network or
> Android framework.”

---

### 🔥 Bonus Interview Tip

If interviewer asks:

**“How would you test the repository?”**

Answer:

> “I’d mock the API service and verify that repository maps HTTP codes and exceptions into correct
> sealed error types.”

---

If you want next, Know about:

* 🧪 Repository unit tests
* 🔁 Retry logic testing
* 🧪 Testing with Hilt Test Modules

> [⬆️ Back to Top / Close](#errorhandling)
</blockquote>
</details>

---

# Dependency Injection

### Concepts

Inversion of control → dependencies provided, not created.

### Hilt Example

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideApi(): Api = Retrofit.Builder().build().create(Api::class.java)
}
```

### Scopes

- Singleton
- ActivityRetained
- ViewModelScoped

### Interview Q&A

**Q: Why Hilt over Dagger?**  
A: Less boilerplate, lifecycle integration.

**MUST READ->** [DAY 3 Hilt Complete Guide.md](DAY%203%20Hilt%20Complete%20Guide.md)

---

# Navigation & App Structure

### Single Activity Architecture

One activity hosts NavHost; screens are fragments or composables.

### Shared ViewModels

Used for shared state across screens.

---

# Testing (Architecture)

### ViewModel Testing

Use fake repository and runTest.

### Hilt Testing

Use HiltAndroidRule and replace modules.

---

# Final Senior Summary

Modern Android architecture prioritizes **unidirectional data flow, modularization, clean separation
of concerns, and dependency injection** to ensure maintainability, scalability, and testability in
large codebases.
