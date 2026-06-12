---
title: "Android UI Layer Guide: Views, XML, Jetpack Compose, RecyclerView and Material Design"
date: 2026-06-12
slug: "android-ui-layer-views-xml-compose-recyclerview-material"
tags: [ "Notes", "Android", "UI Layer", "Jetpack Compose", "RecyclerView", "Material Design", "XML Views" ]
summary: "Personal Android UI layer notes covering the View system, XML layouts, RecyclerView, custom views, Material Design, Jetpack Compose state, recomposition, side effects, lifecycle awareness, and Compose vs XML trade-offs."
categories: Notes
readTime: 28
---

# DAY 2 — UI Layer (Views, XML, Jetpack Compose, RecyclerView, Material)

## Table of Contents

- [View System](#view-system)
    - [Views and ViewGroups](#views-and-viewgroups)
    - [Layouts](#layouts)
        - [ConstraintLayout (Recommended)](#constraintlayout-recommended)
        - [LinearLayout](#linearlayout)
        - [RelativeLayout (Legacy)](#relativelayout-legacy)
    - [RecyclerView & Adapters](#recyclerview--adapters)
    - [Custom Views](#custom-views)
    - [Animations & Transitions](#animations--transitions)
    - [Material Design Principles](#material-design-principles)

- [Material Design Principles — Complete Interview Guide](#material-design-principles--complete-interview-guide)
    - [Introduction to Material Design](#introduction-to-material-design)
    - [Core Principles](#core-principles)
        - [Elevation & Shadows](#elevation--shadows)
        - [Motion for Meaning](#motion-for-meaning)
        - [Responsive Layouts](#responsive-layouts)
        - [Consistent Theming](#consistent-theming)
    - [Material Design 3 (Material You)](#material-design-3-material-you)
    - [Material Components in Android (Views)](#material-components-in-android-views)
    - [Material Components in Jetpack Compose](#material-components-in-jetpack-compose)
    - [Typography & Color System](#typography--color-system)
    - [Dark Theme & Dynamic Color](#dark-theme--dynamic-color)
    - [Accessibility in Material Design](#accessibility-in-material-design)
    - [Real-World Implementation Tips](#real-world-implementation-tips)
    - [Common Interview Questions](#common-interview-questions)

- [Jetpack Compose](#jetpack-compose)
    - [Compose Basics](#compose-basics)
    - [State & Recomposition](#state--recomposition)
        - [State](#state)
        - [Types of State in Compose](#types-of-state-in-compose)
        - [Recomposition](#recomposition)
        - [What Triggers Recomposition?](#what-triggers-recomposition)
        - [Avoiding Unnecessary Recomposition](#avoiding-unnecessary-recomposition)
        - [State Hoisting (Interview Favorite)](#state-hoisting-interview-favorite)

- [Types of State in Compose (Detailed)](#types-of-state-in-compose-detailed)
    - [`remember { mutableStateOf() }`](#remember--mutablestateof)
    - [`rememberSaveable`](#remembersaveable)
    - [`StateFlow.collectAsState()`](#stateflowcollectasstate)
    - [`derivedStateOf`](#derivedstateof)
    - [Differences Between State Types in Compose](#differences-between-state-types-in-compose)
    - [When to Use What](#when-to-use-what)

- [🧠 State Hoisting vs State Ownership in Jetpack Compose](#-state-hoisting-vs-state-ownership-in-jetpack-compose)
- [🔹 Event vs State in Jetpack Compose](#-event-vs-state-in-jetpack-compose)

- [Advanced Compose Concepts (Runtime & Lifecycle)](#advanced-compose-concepts-runtime--lifecycle)
    - [Side Effects in Compose](#side-effects-in-compose)
    - [Performance Optimization in Compose](#performance-optimization-in-compose)
    - [Compose Lifecycle Awareness](#compose-lifecycle-awareness)

- [Full Compose Screen Architecture Example](#full-compose-screen-architecture-example)

- [Compose vs XML Trade-offs](#compose-vs-xml-trade-offs)
- [Final Interview Summary](#final-interview-summary)

---

# View System

## Views and ViewGroups

### Overview

The Android View system is a hierarchical tree of UI components.

- View → Basic UI element (TextView, Button, ImageView)
- ViewGroup → Container that holds other Views (LinearLayout, ConstraintLayout)

### Deep Dive

- Every View goes through: measure → layout → draw
- Rendering happens on the UI thread
- Deep nesting can cause performance issues

### Real-World Tip

Use flat hierarchies (ConstraintLayout) instead of deeply nested layouts.

---

## Layouts

### ConstraintLayout (Recommended)

- Flat layout hierarchy
- Powerful positioning system
- Best for complex screens

### LinearLayout

- Simple row/column layouts
- Use weight carefully

### RelativeLayout (Legacy)

- Mostly replaced by ConstraintLayout

---

## RecyclerView & Adapters

### Overview

RecyclerView efficiently displays large lists using view recycling.

### Key Components

- Adapter → Binds data
- ViewHolder → Holds item view
- LayoutManager → Controls layout

### Example

```kotlin
class UserAdapter : RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}
```

### Best Practice

Use ListAdapter + DiffUtil.

---

## Custom Views

### When Needed

- Complex drawing
- Reusable widgets
- Performance-critical UI

### Example

```kotlin
class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, 100f, Paint())
    }
}
```

---

## Animations & Transitions

### Types

- View Animation
- Property Animation
- MotionLayout

### Example

```kotlin
ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
    duration = 300
    start()
}
```

---

## Material Design Principles

### Core Ideas

- Elevation & shadows
- Motion for meaning
- Responsive layouts
- Consistent theming

Use Material3 components.

<div id=“materialdesign></div>
<details>
<summary><b>🔴 <font color="red">Material Design</font> 🔴</b></summary>
<br>
<blockquote>

# Material Design Principles — Complete Interview Guide

## 📑 Table of Contents

- [Introduction to Material Design](#introduction-to-material-design)
- [Core Principles](#core-principles)
    - [Elevation & Shadows](#elevation--shadows)
    - [Motion for Meaning](#motion-for-meaning)
    - [Responsive Layouts](#responsive-layouts)
    - [Consistent Theming](#consistent-theming)
- [Material Design 3 (Material You)](#material-design-3-material-you)
- [Material Components in Android (Views)](#material-components-in-android-views)
- [Material Components in Jetpack Compose](#material-components-in-jetpack-compose)
- [Typography & Color System](#typography--color-system)
- [Dark Theme & Dynamic Color](#dark-theme--dynamic-color)
- [Accessibility in Material Design](#accessibility-in-material-design)
- [Real-World Implementation Tips](#real-world-implementation-tips)
- [Common Interview Questions](#common-interview-questions)

---

## Introduction to Material Design

Material Design is Google’s design system that provides guidelines for creating beautiful, usable,
and consistent UI across Android apps.

It focuses on:

- Visual hierarchy
- Meaningful motion
- Consistent components
- Adaptive layouts

---

## Core Principles

### Elevation & Shadows

Elevation represents how high a surface appears relative to others.

Higher elevation → casts bigger shadow → appears “on top”.

Examples:

- Floating Action Button (FAB) has higher elevation than cards
- Dialogs appear above the main content

Android XML example:

```xml

<com.google.android.material.card.MaterialCardView android:elevation="8dp" />
```

Compose example:

```kotlin
Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
    Text("Elevated Card")
}
```

---

### Motion for Meaning

Motion explains:

- Where content comes from
- What changed
- What action just happened

Examples:

- Snackbar slides from bottom
- Shared element transitions between screens

Bad motion = distracting  
Good motion = communicates state changes

Compose animation example:

```kotlin
AnimatedVisibility(visible = isVisible) {
    Text("Hello")
}
```

---

### Responsive Layouts

Material layouts must adapt to:

- Phones
- Tablets
- Foldables
- Landscape

Best practices:

- Use ConstraintLayout or Compose adaptive layouts
- Avoid fixed sizes
- Use window size classes (Compose)

Compose example:

```kotlin
if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
    TabletLayout()
} else {
    PhoneLayout()
}
```

---

### Consistent Theming

Apps should follow:

- Same color system
- Same typography scale
- Same spacing rules

Use Material theme system instead of custom styling.

XML theme:

```xml

<style name="Theme.MyApp" parent="Theme.Material3.DayNight.NoActionBar" />
```

Compose theme:

```kotlin
MaterialTheme(
    colorScheme = lightColorScheme(),
    typography = Typography(),
    shapes = Shapes()
) {
    AppContent()
}
```

---

## Material Design 3 (Material You)

Material 3 introduces:

- Dynamic color
- New typography scale
- Updated components
- Personalized UI based on wallpaper

Modern apps should use Material 3.

---

## Material Components in Android (Views)

Examples:

- MaterialButton
- TextInputLayout
- BottomNavigationView
- MaterialCardView

Dependency:

```gradle
implementation "com.google.android.material:material:1.11.0"
```

---

## Material Components in Jetpack Compose

Compose Material3 library:

```gradle
implementation "androidx.compose.material3:material3"
```

Example:

```kotlin
Button(onClick = {}) {
    Text("Material3 Button")
}
```

---

## Typography & Color System

Material uses a type scale:

- Display
- Headline
- Title
- Body
- Label

Color roles:

- Primary
- Secondary
- Surface
- Background
- Error

---

## Dark Theme & Dynamic Color

Material supports dark theme automatically.

Compose dynamic color:

```kotlin
val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= 31) {
    dynamicDarkColorScheme(context)
} else {
    darkColorScheme()
}
```

---

## Accessibility in Material Design

Best practices:

- Minimum 48dp touch targets
- Sufficient contrast ratio
- Support TalkBack
- Content descriptions for icons

---

## Real-World Implementation Tips

- Don’t hardcode colors → use theme
- Don’t create custom buttons unless needed
- Follow Material spacing guidelines
- Use Material motion instead of custom animations

---

## Common Interview Questions

Q: Why use Material Design?  
Ensures consistency, usability, and accessibility across Android apps.

Q: What changed in Material 3?  
Dynamic color, new components, updated typography, personalization.

Q: How does elevation affect UI?  
Indicates visual hierarchy using shadows.

---

## Final Interview Summary

Material Design is a design system focused on visual hierarchy, motion, and adaptability. Modern
Android apps should use Material 3 components and follow theming, accessibility, and responsive
layout principles to deliver a consistent user experience.

> [⬆️ Back to Top / Close](#materialdesign)
</blockquote>
</details>

---

## Jetpack Compose

### Compose Basics

Jetpack Compose is Android’s **modern declarative UI toolkit**. Instead of describing *how* to build
UI step-by-step (like XML + imperative code), you describe **what the UI should look like for a
given state**. Compose then takes care of updating the screen when state changes.

In traditional Views:

* You inflate layouts
* Find views
* Manually update them

In Compose:

* UI is written as **Kotlin functions**
* UI automatically reacts to state changes

This leads to:

* Less boilerplate
* Easier UI updates
* Better state-driven architecture
* Strong integration with Kotlin, coroutines, and Flow

Basic example:

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name")
}
```

Here:

* `@Composable` marks a function that emits UI
* `Text()` is itself a composable
* UI is just a function of input data (`name`)

Compose encourages **small, reusable UI functions** instead of large XML layouts.

---

## State & Recomposition

### State

In Compose, **state drives UI**. When state changes, Compose automatically updates the parts of the
UI that depend on that state.

State is usually held using `remember` and `mutableStateOf`.

```kotlin
var count by remember { mutableStateOf(0) }

Button(onClick = { count++ }) {
    Text("$count")
}
```

What happens here:

* `count` is observable state
* When `count` changes, Compose re-runs any composables that read `count`
* The `Text` composable updates automatically

This eliminates:
❌ Manual `findViewById`
❌ Manual `setText()` calls
❌ UI synchronization bugs

### Types of State in Compose

| Type                            | Use Case                       |
|---------------------------------|--------------------------------|
| `remember { mutableStateOf() }` | UI-local state                 |
| `rememberSaveable`              | Survives configuration changes |
| `StateFlow.collectAsState()`    | ViewModel state                |
| `derivedStateOf`                | Computed state                 |

Example with ViewModel state:

```kotlin
val uiState by viewModel.uiState.collectAsState()

Text(text = uiState.username)
```

Compose seamlessly integrates with **Flow and StateFlow**, which is now the **recommended state
holder** over LiveData.

<div id=“composestates></div>
<details>
<summary><b>🔴<font color="red">Types of State in Compose (Detailed)</font>🔴</b></summary>
<br>
<blockquote>

### Types of State in Compose (Detailed)

| Type                            | Use Case                       |
|---------------------------------|--------------------------------|
| `remember { mutableStateOf() }` | UI-local state                 |
| `rememberSaveable`              | Survives configuration changes |
| `StateFlow.collectAsState()`    | ViewModel state                |
| `derivedStateOf`                | Computed state                 |

---

### 🔹 `remember { mutableStateOf() }`

This is the **most basic state holder** in Compose.

It:

* Stores state **inside the composable**
* Survives **recomposition**
* Does **NOT survive configuration changes** (rotation, process death)

```kotlin
var text by remember { mutableStateOf("") }

TextField(
    value = text,
    onValueChange = { text = it }
)
```

**When to use:**

* ✔ Temporary UI state
* ✔ Animation state
* ✔ Toggle visibility
* ✔ Form inputs that don’t need to persist after rotation

**Not for:**

* ❌ Business logic
* ❌ Long-lived state
* ❌ Data that should survive screen recreation

Think of it as **“UI memory” during the current composition lifecycle**.

---

### 🔹 `rememberSaveable`

`rememberSaveable` is like `remember`, but it **persists state across configuration changes** using
`SavedInstanceState`.

It automatically saves values that can be stored in a `Bundle` (primitives, String, etc.).

```kotlin
var username by rememberSaveable { mutableStateOf("") }
```

Now if the screen rotates:

* `username` value is restored

**When to use:**

* ✔ Text field inputs
* ✔ Scroll positions
* ✔ Tab selections
* ✔ UI state that must survive rotation

**Custom objects?**
You can provide a `Saver`:

```kotlin
val saver = Saver<User, String>(
    save = { it.name },
    restore = { User(it) }
)

var user by rememberSaveable(stateSaver = saver) {
    mutableStateOf(User("Vishnu"))
}
```

Think of it as **“UI state that should survive device rotation but not app restarts”**.

---

### 🔹 `StateFlow.collectAsState()`

This is how **Compose observes ViewModel state**.

Compose collects a `StateFlow` and converts it into Compose state.

```kotlin
val uiState by viewModel.uiState.collectAsState()

Text(uiState.username)
```

Every time the `StateFlow` emits:
➡ Compose recomposes affected UI

**Why this is modern best practice:**

* ✔ Lifecycle-aware
* ✔ Works seamlessly with coroutines
* ✔ Replaces LiveData in modern apps
* ✔ Ideal for MVVM architecture

**When to use:**

* ✔ Screen state
* ✔ Business logic data
* ✔ API responses
* ✔ DB data

Think of it as **“UI observing the ViewModel’s source of truth”**.

---

### 🔹 `derivedStateOf`

`derivedStateOf` is used to create **computed state** based on other states.
It recalculates **only when dependencies change**, avoiding unnecessary recomposition.

```kotlin
val isButtonEnabled by remember {
    derivedStateOf { username.isNotBlank() && password.length > 6 }
}
```

This prevents recalculating logic on every recomposition.

**When to use:**

* ✔ Expensive computations
* ✔ Filtering lists
* ✔ Combining multiple states
* ✔ Derived UI flags

**Example:**

```kotlin
val filteredList by remember(searchQuery, fullList) {
    derivedStateOf {
        fullList.filter { it.contains(searchQuery) }
    }
}
```

Without `derivedStateOf`, filtering would run on every recomposition.

Think of it as **“memoized computed state”**.

---

### 🧠 Summary

> `remember` is for temporary UI state, `rememberSaveable` survives rotation, `collectAsState()`
> connects ViewModel state to UI, and `derivedStateOf` is used for efficient computed state. Proper
> state selection is key to avoiding unnecessary recompositions and performance issues in Compose
> apps.

---

## 🔍 Differences Between State Types in Compose

| Feature                                      | `remember { mutableStateOf() }` | `rememberSaveable`                     | `StateFlow.collectAsState()`    | `derivedStateOf`                    |
|----------------------------------------------|---------------------------------|----------------------------------------|---------------------------------|-------------------------------------|
| **Scope**                                    | Inside composable               | Inside composable                      | ViewModel → UI                  | Inside composable                   |
| **Survives recomposition**                   | ✅ Yes                           | ✅ Yes                                  | ✅ Yes                           | ✅ Yes                               |
| **Survives configuration change (rotation)** | ❌ No                            | ✅ Yes                                  | ✅ Yes (ViewModel survives)      | ❌ No                                |
| **Survives process death**                   | ❌ No                            | ⚠️ Sometimes (if saved)                | ❌ No (unless restored manually) | ❌ No                                |
| **Where state lives**                        | UI layer                        | UI layer (saved in Bundle)             | ViewModel layer                 | Derived in UI layer                 |
| **Used for business logic?**                 | ❌ No                            | ❌ No                                   | ✅ Yes                           | ❌ No                                |
| **Best for**                                 | Temporary UI state              | UI state that must persist on rotation | Screen data / API / DB          | Computed UI values                  |
| **Example use**                              | Toggle visibility               | TextField input                        | User profile data               | Button enabled logic                |
| **Triggers recomposition?**                  | Yes                             | Yes                                    | Yes (on flow emission)          | Yes (only when dependencies change) |
| **Performance impact**                       | Low                             | Low                                    | Depends on flow emissions       | Optimized (avoids extra recompute)  |

---

## 🧭 When to Use What

### 🟢 Use `remember { mutableStateOf() }` when:

* State is **purely visual**
* Doesn’t matter if lost on rotation
  **Examples:** animation progress, snackbar visibility, temporary toggles

---

### 🟢 Use `rememberSaveable` when:

* State must survive **configuration changes**
* But is still **UI-only**
  **Examples:** form inputs, scroll positions, tab selection

---

### 🟢 Use `StateFlow.collectAsState()` when:

* State comes from **ViewModel**
* Backed by **business logic / repository / DB**
  **Examples:** user data, API responses, UI screen state

---

### 🟢 Use `derivedStateOf` when:

* State is **calculated from other states**
* You want to **avoid unnecessary recomposition**
  **Examples:** filtered lists, form validation flags, expensive computations

---

## 🎯 One-Liner

> "`remember` is for temporary UI state, `rememberSaveable` persists UI state across rotation,
`collectAsState()` connects ViewModel state to UI, and `derivedStateOf` optimizes computed state to
> avoid unnecessary recompositions."

---

> [⬆️ Back to Top / Close](#composestates)
</blockquote>
</details>

---

### Recomposition

**Recomposition** is the process where Compose re-executes composable functions when state changes.

Key idea:

> Compose only recomposes functions that read the changed state.

Example:

```kotlin
@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")   // Recomposition happens here
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
```

When `count` changes:

* Only `Text("Count: $count")` is recomposed
* The entire screen is NOT redrawn

This makes Compose highly efficient.

---

### What Triggers Recomposition?

Recomposition happens when:

* `mutableStateOf` value changes
* A collected Flow/StateFlow emits a new value
* A parameter passed to a composable changes

Compose tracks **state reads** automatically.

---

### Avoiding Unnecessary Recomposition

Advanced Compose apps must minimize unnecessary recompositions.

Best practices:

* ✔ Keep state as small as possible
* ✔ Hoist state to parent composables
* ✔ Use `remember` for expensive objects
* ✔ Use `key()` when rendering lists
* ✔ Avoid creating new objects inside composables unnecessarily

Example:

```kotlin
val formattedName = remember(name) {
    name.uppercase()
}
```

---

### State Hoisting (Interview Favorite)

State should be owned by the **lowest common ancestor** that needs to modify it.

Bad:

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) } // Hard to reuse
}
```

Good:

```kotlin
@Composable
fun Counter(count: Int, onIncrement: () -> Unit) {
    Button(onClick = onIncrement) {
        Text("$count")
    }
}
```

State is managed outside and passed in → **more reusable and testable UI**.

<div id=“statehoistingvsstateownership></div>
<details>
<summary><b>🔴 <font color="red">🧠 State Hoisting vs State Ownership in Jetpack Compose</font> 🔴</b></summary>
<br>
<blockquote>

## 🧠 State Hoisting vs State Ownership in Jetpack Compose

Compose follows a **state-driven UI model**, and understanding **who owns state** vs **who just
displays it** is critical for scalable architecture.

---

## 🔹 What is State Ownership?

**State ownership** means the component is **responsible for holding and modifying the state**.

That component:

* Creates the state
* Updates the state
* Decides how state changes

Example (state owned inside composable):

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) } // State owned here

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

Here:

* `Counter` **owns** the state
* Not reusable
* Hard to test
* Parent can’t control the value

---

## 🔹 What is State Hoisting?

**State hoisting** means **moving state up to a parent** and passing it down as parameters.

The composable becomes **stateless** and only **renders UI + sends events upward**.

Example (state hoisted):

```kotlin
@Composable
fun Counter(count: Int, onIncrement: () -> Unit) {
    Button(onClick = onIncrement) {
        Text("Count: $count")
    }
}
```

Parent owns the state:

```kotlin
@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }

    Counter(
        count = count,
        onIncrement = { count++ }
    )
}
```

Now:

* Parent owns state
* Child only displays it
* Easier to reuse & test

---

## ⚖️ Key Differences

| Concept           | State Ownership   | State Hoisting        |
|-------------------|-------------------|-----------------------|
| Where state lives | Inside composable | In parent / ViewModel |
| Reusability       | ❌ Low             | ✅ High                |
| Testability       | ❌ Hard            | ✅ Easy                |
| Architecture      | Local             | Scalable              |
| Best practice     | Small widgets     | Screens & reusable UI |

---

## 🧩 Why State Hoisting Matters

State hoisting:

* ✔ Encourages **unidirectional data flow**
* ✔ Makes composables **reusable**
* ✔ Makes UI **predictable**
* ✔ Works naturally with **ViewModel + Flow**

This matches modern Android architecture.

---

## 🏗️ Real-World Pattern (MVVM + Compose)

ViewModel owns state:

```kotlin
val uiState by viewModel.uiState.collectAsState()

ProfileScreen(
    state = uiState,
    onNameChanged = viewModel::updateName
)
```

Composable only renders:

```kotlin
@Composable
fun ProfileScreen(state: ProfileUiState, onNameChanged: (String) -> Unit) {
    TextField(
        value = state.name,
        onValueChange = onNameChanged
    )
}
```

UI doesn’t manage business logic — ViewModel does.

---

## 🎯 Interview-Ready Summary

> “State ownership means a composable manages its own state. State hoisting moves the state up to a
> parent or ViewModel, making the composable stateless and reusable. Modern Compose apps follow
> state
> hoisting with unidirectional data flow.”

---

<div id="statevsevent"></div>
<details>
<summary><b>🔴 <font color="red">🔹 Event vs State in Jetpack Compose</font> 🔴</b></summary>
<br>
<blockquote>

## 🔹 Event vs State in Jetpack Compose

Compose is **state-driven**, but not everything should be treated as state.
A common mistake is storing **one-time events** as state, which leads to repeated actions.

---

## 🟢 What is State?

**State represents the current UI condition**.
It is **persistent** and can be safely re-rendered.

Examples of state:

* Username text
* Loading indicator
* Screen data
* Selected tab
* List of items

```kotlin
data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val errorMessage: String? = null
)
```

State:

* ✔ Can be shown again after recomposition
* ✔ Survives configuration changes (via ViewModel)
* ✔ Represents what the screen **looks like**

---

## 🔴 What is an Event?

**Events are one-time actions**, not persistent UI conditions.

Examples of events:

* Show toast
* Navigate to another screen
* Show Snackbar
* Open dialog once
* Play animation once

Events:

* ❌ Should NOT be stored as normal state
* ❌ Should NOT re-trigger after recomposition
* ✔ Should be consumed once

---

## ❌ Common Mistake

Using state for events:

```kotlin
data class UiState(val showToast: Boolean = false)
```

Problem:

* Recomposition or rotation may trigger the toast again
* Leads to repeated navigation/snackbar bugs

---

## ✅ Correct Pattern: State + Event Flow

Use:

* **StateFlow** → for persistent UI state
* **SharedFlow / Channel** → for one-time events

### ViewModel

```kotlin
private val _events = MutableSharedFlow<UiEvent>()
val events = _events.asSharedFlow()

fun onLoginSuccess() {
    viewModelScope.launch {
        _events.emit(UiEvent.NavigateToHome)
    }
}

sealed class UiEvent {
    object NavigateToHome : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}
```

---

### Compose Screen

```kotlin
val eventFlow = viewModel.events

LaunchedEffect(Unit) {
    eventFlow.collect { event ->
        when (event) {
            is UiEvent.NavigateToHome -> navController.navigate("home")
            is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
```

This ensures:

* ✔ Event happens once
* ✔ No repeat after recomposition
* ✔ No duplicate navigation

---

## ⚖️ State vs Event Comparison

| Feature                   | State               | Event                       |
|---------------------------|---------------------|-----------------------------|
| Purpose                   | Represents UI       | Triggers action             |
| Lifetime                  | Persistent          | One-time                    |
| Replayed on recomposition | Yes                 | No                          |
| Stored in                 | StateFlow           | SharedFlow / Channel        |
| Example                   | Loading, data, text | Navigation, toast, snackbar |

---

## 🎯 Interview-Ready Summary

> “State represents what the UI looks like and can be safely recomposed. Events represent one-time
> actions like navigation or toasts and should be handled using SharedFlow or Channels, not stored
> as
> state.”

---

> [⬆️ Back to Top / Close](#statevsevent)
</blockquote>
</details>

---

> [⬆️ Back to Top / Close](#statehoistingvsstateownership)
</blockquote>
</details>

---

### Interview-Ready Summary

> Jetpack Compose is declarative and state-driven. UI is described as a function of state, and
> recomposition ensures only the parts of the UI that depend on changed state are updated. Proper
> state management and state hoisting are essential for building scalable and efficient Compose
> applications.

---

<div id="composeadvanced"></div>
<details>
<summary><b>🔴 <font color="red">Advanced Compose Concepts (Runtime & Lifecycle) </font> 🔴</b></summary>
<br>
<blockquote>
Perfect — these are **senior-level Compose topics**. Here’s a structured guide you can paste directly into your notes.

---

# 🔹 Side Effects in Compose

Compose is **declarative**, but sometimes you must perform **imperative work** like:

* Making API calls
* Starting animations
* Navigating
* Showing snackbars

These are called **side effects**.

---

## 🟢 `LaunchedEffect`

Runs a coroutine tied to the **Composable lifecycle**.
It restarts when its **key changes**.

```kotlin
LaunchedEffect(userId) {
    viewModel.loadUser(userId)
}
```

### When to use

✔ API calls
✔ Collecting flows
✔ One-time screen actions
✔ Event handling

It is **canceled automatically** when the composable leaves the composition.

---

## 🟢 `rememberCoroutineScope`

Provides a coroutine scope tied to the composable, but **does not automatically restart** like
`LaunchedEffect`.

```kotlin
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        snackbarHostState.showSnackbar("Saved")
    }
})
```

### When to use

✔ Button clicks
✔ User-triggered actions
✔ Snackbar/Toast calls
✔ Imperative event handling

---

## 🟡 `SideEffect`

Runs **after every successful recomposition**.

```kotlin
SideEffect {
    Log.d("Compose", "Recomposition happened")
}
```

### Use case

✔ Sync Compose state with external systems

---

## 🟡 `DisposableEffect`

Used when you need **setup + cleanup**.

```kotlin
DisposableEffect(Unit) {
    val listener = object : Listener {}
    registerListener(listener)

    onDispose {
        unregisterListener(listener)
    }
}
```

### Use case

✔ Register/unregister listeners
✔ Lifecycle observers
✔ Broadcast receivers

---

## 🎯 Interview Summary

> “Side effects in Compose handle work that doesn’t belong in UI rendering. `LaunchedEffect` is
> lifecycle-aware, `rememberCoroutineScope` is for user actions, and `DisposableEffect` handles
> cleanup logic.”

---

# 🔹 Performance Optimization in Compose

Compose is efficient, but misuse of state can cause **excess recomposition**.

---

## 🟢 Minimize Recomposition

Bad:

```kotlin
Text("${System.currentTimeMillis()}")
```

Good:

```kotlin
val time = remember { System.currentTimeMillis() }
Text("$time")
```

---

## 🟢 Use `remember`

Avoid recreating expensive objects:

```kotlin
val formatter = remember { DateTimeFormatter.ISO_DATE }
```

---

## 🟢 Use `derivedStateOf`

For computed values:

```kotlin
val isValid by remember {
    derivedStateOf { username.isNotBlank() && password.length > 6 }
}
```

Prevents recomputation on unrelated recompositions.

---

## 🟢 Stable & Immutable Data

Use `data class` with `val` fields.

```kotlin
@Immutable
data class UiState(val name: String)
```

---

## 🟢 Lazy Lists Optimization

Use keys:

```kotlin
LazyColumn {
    items(users, key = { it.id }) {
        UserRow(it)
    }
}
```

---

## 🎯 Interview Summary

> “Compose performance depends on controlling recomposition. We use `remember`, `derivedStateOf`,
> stable data models, and proper keys in Lazy lists to minimize unnecessary UI updates.”

---

# 🔹 Compose Lifecycle Awareness

Compose does not have the same lifecycle as Activities, but it integrates with them.

---

## 🟢 Collecting Flow with Lifecycle

```kotlin
val lifecycleOwner = LocalLifecycleOwner.current

LaunchedEffect(Unit) {
    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.flow.collect { /* update state */ }
    }
}
```

Prevents collecting when screen is backgrounded.

---

## 🟢 `collectAsStateWithLifecycle`

Modern best practice:

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

Lifecycle-aware and avoids leaks.

---

## 🟢 Remembering Across Lifecycle

Use `rememberSaveable` for configuration changes.

```kotlin
var text by rememberSaveable { mutableStateOf("") }
```

---

## 🟢 ViewModel Lifecycle

ViewModel survives:
✔ Rotation
❌ Process death

Compose simply observes ViewModel state.

---

## 🎯 Interview Summary

> “Compose itself is lifecycle-agnostic, so we use lifecycle-aware APIs like
`collectAsStateWithLifecycle` and `repeatOnLifecycle` to ensure flows are only collected when the UI
> is active.”

---

If you'd like next, I can give a **full screen architecture example combining all these concepts** (
ViewModel + State + Events + Effects).


> [⬆️ Back to Top / Close](#composeadvanced)
</blockquote>
</details>

<div id="fullcomposescreenarchitecture"></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">Full Compose Screen Architecture Example</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>
Perfect — this ties **everything together** into one **senior-level Compose architecture example**. You can paste this directly into your notes.

---

# 🧩 Full Compose Screen Architecture Example

**(ViewModel + State + Events + Side Effects)**

This example demonstrates a **real-world login screen** using:

✔ ViewModel
✔ StateFlow for UI state
✔ SharedFlow for one-time events
✔ Compose side effects
✔ State hoisting
✔ Unidirectional Data Flow

---

## 🧠 Architecture Flow

```
User Action → Composable → ViewModel Event → Business Logic → State Update
                                           ↘ One-time Event → Side Effect (Toast / Navigation)
```

---

## 🟦 1️⃣ UI State (Persistent)

Represents what the UI should look like.

```kotlin
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

---

## 🟨 2️⃣ UI Events (One-time)

Used for actions that should happen **once**.

```kotlin
sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
    data class ShowToast(val message: String) : LoginEvent()
}
```

---

## 🟩 3️⃣ ViewModel

Owns state and business logic.

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.login(_uiState.value.username, _uiState.value.password)

            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> _events.emit(LoginEvent.NavigateToHome)
                is Result.Error -> _events.emit(LoginEvent.ShowToast(result.message))
            }
        }
    }
}
```

---

## 🟪 4️⃣ Compose Screen

### Collect state

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

### Handle one-time events using side effects

```kotlin
val context = LocalContext.current

LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is LoginEvent.NavigateToHome -> navController.navigate("home")
            is LoginEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
```

---

### Stateless UI

```kotlin
LoginContent(
    state = uiState,
    onUsernameChange = viewModel::onUsernameChange,
    onPasswordChange = viewModel::onPasswordChange,
    onLoginClick = viewModel::login
)
```

---

## 🟫 5️⃣ Stateless Composable (State Hoisting)

```kotlin
@Composable
fun LoginContent(
    state: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        TextField(
            value = state.username,
            onValueChange = onUsernameChange,
            label = { Text("Username") }
        )

        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") }
        )

        Button(
            onClick = onLoginClick,
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Login")
            }
        }

        state.errorMessage?.let {
            Text(text = it, color = Color.Red)
        }
    }
}
```

---

## ⚙️ What Concepts Are Demonstrated?

| Concept                  | Where Used                                  |
|--------------------------|---------------------------------------------|
| State Hoisting           | LoginContent receives state + callbacks     |
| ViewModel State          | StateFlow → `collectAsStateWithLifecycle()` |
| One-time Events          | SharedFlow + LaunchedEffect                 |
| Side Effects             | Navigation + Toast inside `LaunchedEffect`  |
| Unidirectional Data Flow | UI → ViewModel → State → UI                 |
| Lifecycle Awareness      | `collectAsStateWithLifecycle()`             |

---

## 🎯 Interview-Ready Summary

> “In modern Compose architecture, ViewModel exposes UI state using StateFlow and one-time events
> using SharedFlow. The composable collects state and renders UI, while side effects like navigation
> and toasts are handled inside LaunchedEffect. State is hoisted, ensuring unidirectional data flow
> and a clean separation of UI and business logic.”

---

If you want next, I can also provide
**Error handling version of this flow with retry + loading states** — another common interview
scenario.


> [⬆️ Back to Top / Close](#fullcomposescreenarchitecture)
</blockquote>
</details>

## Compose vs XML Trade-offs

| Feature        | XML         | Compose             |
|----------------|-------------|---------------------|
| UI Definition  | XML         | Kotlin              |
| Learning Curve | Lower       | Higher              |
| Performance    | Good        | Excellent           |
| Future         | Maintenance | Strategic direction |

---

## Final Interview Summary

The View system is imperative and XML-driven, while Compose is declarative and state-driven. Modern
Android development is shifting toward Compose, but strong knowledge of RecyclerView and layouts is
still essential.
