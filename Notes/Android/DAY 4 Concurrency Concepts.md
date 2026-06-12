---
title: "Android Concurrency Concepts: Coroutines, Flow and Background Execution"
date: 2026-06-12
slug: "android-concurrency-concepts-coroutines-flow-background-execution"
tags: [ "Notes", "Android", "Concurrency", "Coroutines", "Flow", "StateFlow", "ANR" ]
summary: "Personal Android concurrency notes covering AsyncTask, HandlerThread, Loaders, Kotlin Coroutines, Flow, StateFlow, SharedFlow, backpressure, RxJava, ANR causes, and main-thread performance best practices."
categories: Notes
readTime: 24
---

# DAY 4 — Concurrency, Coroutines, Services & Background Execution

This document is a **complete, single-source guide** for Android concurrency and background
execution.  
If you master this, you can confidently answer **mid → senior-level interview questions**.

## Table of Contents

- [Legacy Concurrency (Conceptual Awareness)](#legacy-concurrency-conceptual-awareness)
    - [AsyncTask (and why it’s deprecated)](#asynctask-and-why-its-deprecated)
    - [Handler & HandlerThread](#handler--handlerthread)
    - [Loaders](#loaders)
- [Modern Concurrency](#modern-concurrency)
    - [Kotlin Coroutines](#kotlin-coroutines)
    - [Coroutines vs Threads](#coroutines-vs-threads)
    - [Flow](#flow)
    - [RxJava (Conceptual Understanding)](#rxjava-conceptual-understanding)
- [Performance](#performance)
    - [ANR Causes & Prevention](#anr-causes--prevention)
    - [Main Thread Blocking Scenarios](#main-thread-blocking-scenarios)

---

## Legacy Concurrency (Conceptual Awareness)

> These are **important to know for interviews**, but **not recommended for new code**.

---

### AsyncTask (and why it’s deprecated)

**AsyncTask** was used to run background work and post results to the main thread.

```text
onPreExecute() → doInBackground() → onPostExecute()
```

#### Why it’s deprecated ❌

- Lifecycle unaware → memory leaks
- Tied to Activity lifecycle incorrectly
- Poor cancellation support
- Hard to handle configuration changes
- Encourages bad threading practices

✅ Interview one-liner:
> “AsyncTask is deprecated because it’s lifecycle-unaware and leak-prone; coroutines are the modern
> replacement.”

---

### Handler & HandlerThread

#### Handler

- Posts work to a **Looper’s MessageQueue**
- Often used for main-thread communication

```kotlin
Handler(Looper.getMainLooper()).post {
    // runs on main thread
}
```

#### HandlerThread

- Background thread with its own Looper
- Useful for serial background tasks

```kotlin
val handlerThread = HandlerThread("Worker")
handlerThread.start()
val handler = Handler(handlerThread.looper)
handler.post {
    // background work
}
```

#### Limitations

- Manual thread & lifecycle management
- Easy to leak Activities
- No structured cancellation

---

### Loaders

- Used to load data asynchronously
- Lifecycle-aware (unlike AsyncTask)
- Mostly used for DB / Cursor loading

❌ Deprecated because:

- Complex API
- Tightly coupled to UI
- Replaced by **ViewModel + LiveData / Flow**

---

## Modern Concurrency

> **This is where interviews focus heavily.**

---

### Kotlin Coroutines

**Coroutines** are a lightweight, structured concurrency framework for async programming.

#### Key ideas

- Suspends without blocking threads
- Structured concurrency (scope-based)
- Lifecycle-aware via scopes

```kotlin
viewModelScope.launch {
    val data = repository.fetchData()
    _state.value = data
}
```

#### Common Coroutine Scopes

- `viewModelScope` → ViewModel lifecycle
- `lifecycleScope` → Activity/Fragment lifecycle
- `GlobalScope` ❌ (avoid)

#### Dispatchers

- `Dispatchers.Main` → UI
- `Dispatchers.IO` → disk/network
- `Dispatchers.Default` → CPU work

---

### Coroutines vs Threads

| Threads             | Coroutines           |
|---------------------|----------------------|
| Heavyweight         | Lightweight          |
| Expensive to create | Cheap to create      |
| Manual lifecycle    | Structured lifecycle |
| Blocking            | Suspending           |
| Error-prone         | Safer & readable     |

✅ Interview one-liner:
> “Coroutines are not threads; they run on threads but suspend without blocking.”

---

### Flow

**Flow** represents a **cold asynchronous stream of values** built on top of Kotlin coroutines.  
It emits multiple values **over time** and integrates naturally with structured concurrency.

---

#### Why Flow exists (intuition)

Before Flow, Android developers relied on:

- callbacks (hard to manage)
- LiveData (UI-focused, limited)
- RxJava (powerful but complex)

**Flow provides:**

- coroutine-friendly async streams
- structured concurrency
- built-in cancellation
- **backpressure handling** (Backpressure is the situation where a producer emits data faster than
  the consumer can process it.)

**Real-life analogy 🚰**

Imagine:

a tap (producer) pouring water

a bucket (consumer) collecting water

If the tap flows faster than the bucket can drain → overflow.

That overflow problem = backpressure problem.

Interview line:
> “Flow is a coroutine-based replacement for callbacks, LiveData, and much of RxJava.”

---

#### Basic Flow example

```kotlin
val dataFlow = flow {
    emit(api.fetch())
}
```

⚠️ Nothing runs yet.  
Flow is **lazy**.

---

#### Cold Stream (very important)

Flow is **cold by default**, meaning:

- execution starts only when collected
- every collector triggers a fresh execution

```kotlin
val flow = flow {
    println("Fetching data")
    emit(1)
}

flow.collect { }   // prints "Fetching data"
flow.collect { }   // prints "Fetching data" again
```

Interview line:
> “Flow is cold — execution starts on collect.”

---

#### Collecting Flow

```kotlin
lifecycleScope.launch {
    viewModel.dataFlow.collect { value ->
        updateUI(value)
    }
}
```

Notes:

- `collect` is a **suspending function**
- blocks the coroutine, not the thread
- collection stops automatically when scope is cancelled

---

#### Threading with Flow (`flowOn`)

```kotlin
flow {
    emit(api.fetch())
}.flowOn(Dispatchers.IO)
```

Rules:

- `flowOn()` changes **upstream execution**
- `collect {}` runs on **collector’s context**

Typical flow:

```
emit() → IO thread
collect() → Main thread
```

Interview line:
> “flowOn affects upstream; collect runs downstream.”

---

#### Cancellation

Flow is **cooperatively cancellable**.

```kotlin
lifecycleScope.launch {
    flow.collect { }
}
```

- Cancels automatically when lifecycle ends
- No manual unsubscribe needed

---

<div id="Backpressure"></div>
<details>
<summary><b>🔴 <font color="red">Backpressure (ELABORATED)</font> 🔴</b></summary>
<br>
<blockquote>

## Backpressure

Flow is **backpressure-aware by default**.

If the collector is slow:

- emission suspends
- no buffer overflow
- no crashes

Interview line:
> “Flow handles backpressure naturally by suspending emitters.”

#### Backpressure In Android terms

* **Producer** → API, database, sensor, flow emitter
* **Consumer** → UI, collector, ViewModel

Example:

```kotlin
flow {
    repeat(1000) {
        emit(it)
    }
}.collect { value ->
    delay(100) // slow UI work
}
```

Producer is fast
Consumer is slow
👉 Backpressure scenario

## How **Flow handles backpressure** (key point)

### ✅ Flow is **backpressure-aware by default**

Meaning:

* `emit()` **suspends**
* producer waits until consumer is ready
* no uncontrolled memory growth
* no crashes

```kotlin
emit(value)  // suspends until collector processes previous value
```

✅ In short line:

> “Flow handles backpressure by suspending the producer instead of buffering infinitely.”

## What happens internally?

* Flow uses **suspension**, not buffering
* Producer and consumer cooperate
* No need for manual control in most cases

## When buffering is needed (advanced)

Sometimes you **don’t want producer to wait**.

```kotlin
flow
    .buffer()
    .collect { value ->
        delay(100)
    }
```

### Operators that affect backpressure:

* `buffer()` → allow producer to run ahead
* `conflate()` → skip intermediate values
* `collectLatest()` → cancel previous work

## Practical examples

### UI typing scenario

```kotlin
searchFlow
    .debounce(300)
    .collectLatest { query ->
        search(query)
    }
```

Here:

* fast typing → producer
* slow API → consumer
* `collectLatest` cancels old requests

## Backpressure vs RxJava (interview gold)

| RxJava                            | Flow              |
|-----------------------------------|-------------------|
| Must handle backpressure manually | Built-in          |
| Can crash if misused              | Safe by default   |
| Complex operators                 | Simple suspension |

## One-line answer (perfect)

> “Backpressure is when data is produced faster than it’s consumed. Kotlin Flow handles it naturally
> by suspending the producer until the consumer is ready.”
>
---

> [⬆️ Back to Top / Close](#Backpressure)
</blockquote>
</details>

#### Common Flow operators

```kotlin
flow
    .map { it * 2 }
    .filter { it > 5 }
    .onEach { log(it) }
    .catch { emit(-1) }
    .collect { }
```

---

#### Error handling

```kotlin
flow {
    emit(api.fetch())
}.catch { error ->
    emit(defaultValue)
}
```

⚠️ Do NOT rely on try-catch inside `collect`.

---

#### StateFlow (UI State)

**StateFlow** is:

- hot stream
- always holds a value
- designed for UI state

```kotlin
private val _state = MutableStateFlow(0)
val state: StateFlow<Int> = _state
```

Characteristics:

- new collectors get latest value immediately
- replaces LiveData in modern apps

Interview line:
> “StateFlow represents state, not events.”

---

#### SharedFlow (Events)

**SharedFlow** is:

- hot stream
- no default value required
- designed for **one-time events**

```kotlin
private val _events = MutableSharedFlow<String>()
```

Use cases:

- navigation
- snackbars
- one-time actions

---

#### StateFlow vs SharedFlow

| Feature       | StateFlow | SharedFlow   |
|---------------|-----------|--------------|
| Hot           | Yes       | Yes          |
| Holds value   | Always    | Optional     |
| Default value | Required  | Not required |
| UI state      | Yes       | No           |
| Events        | No        | Yes          |

---

#### Hot vs Cold streams

| Type              | Cold       | Hot                    |
|-------------------|------------|------------------------|
| Example           | Flow       | StateFlow / SharedFlow |
| Starts on collect | Yes        | No                     |
| Shared emissions  | No         | Yes                    |
| Use case          | Data fetch | State / Events         |

---

#### Flow vs LiveData

| LiveData          | Flow            |
|-------------------|-----------------|
| Lifecycle-aware   | Coroutine-based |
| UI-focused        | General-purpose |
| Limited operators | Rich operators  |
| Main thread only  | Any dispatcher  |

---

#### Flow vs RxJava (conceptual)

| RxJava               | Flow                   |
|----------------------|------------------------|
| Complex              | Simple                 |
| Heavy                | Lightweight            |
| Manual disposal      | Automatic cancellation |
| Steep learning curve | Kotlin-native          |

---

#### Best Practices (Senior-level)

✅ Use:

- `StateFlow` for UI state
- `SharedFlow` for events
- `flowOn(Dispatchers.IO)` for IO work
- lifecycle-aware scopes

❌ Avoid:

- collecting in `GlobalScope`
- using Flow for single-shot results
- emitting UI events via StateFlow

<div id="stateflow-vs-sharedflow"></div>
<details>
<summary><b>🔴 <font color="red">Why should we AVOID emitting UI events via StateFlow?🔴(ELABORATED)</font> 🔴</b></summary>
<br>
<blockquote>

## Why should we **avoid emitting UI events via StateFlow**?

Because **StateFlow is designed for *state***, not *events*.

Let’s break it down cleanly.

## First: State vs Event (core idea)

### ✅ State

* Represents **what the UI looks like now**
* Persistent
* Can be re-emitted safely

Examples:

* loading = true / false
* user data
* screen UI state

### ❌ Event

* Represents **something that should happen once**
* Should **not repeat**
* Not persistent

Examples:

* navigation
* toast/snackbar
* showing dialog
* one-time error message

---

## What StateFlow does (important behavior)

StateFlow:

* is **hot**
* **always holds the latest value**
* **replays the last value to new collectors**

This is the key problem 👇

---

## The problem: Events get re-triggered ❌

### Bad example (DON’T do this)

```kotlin
val uiState = MutableStateFlow<UiState>(UiState.Idle)

uiState.value = UiState.NavigateToHome
```

Then in UI:

```kotlin
uiState.collect { state ->
    if (state is NavigateToHome) {
        navController.navigate("home")
    }
}
```

### What goes wrong?

* Rotate screen
* Fragment recreated
* New collector starts
* **StateFlow re-emits last value**
* 🚨 Navigation happens AGAIN

That’s a bug.

---

## Why this happens

Because StateFlow:

* assumes state is **always valid**
* assumes replay is **desirable**

But for events:

* replay is **dangerous**
* repetition causes crashes / bad UX

---

## Why SharedFlow is correct for events ✅

SharedFlow:

* does **not require a value**
* does **not replay by default**
* emits events **only when they happen**

```kotlin
val events = MutableSharedFlow<UiEvent>()
```

Emit:

```kotlin
events.emit(UiEvent.NavigateHome)
```

Collect:

```kotlin
events.collect { event ->
    handle(event)
}
```

Rotate screen → ❌ no re-trigger
New collector → ❌ no replay

Perfect for events.

---

## Interview one-liner (memorize this)

> “StateFlow replays the latest value, so UI events get re-triggered on configuration changes.
> That’s why events should be sent via SharedFlow, not StateFlow.”

---

## Quick comparison (very interview-friendly)

| Aspect                    | StateFlow | SharedFlow      |
|---------------------------|-----------|-----------------|
| Purpose                   | UI state  | UI events       |
| Holds value               | Always    | Optional        |
| Replays value             | Yes       | No (by default) |
| Safe for navigation/toast | ❌ No      | ✅ Yes           |

---

## When StateFlow *is* OK

Only when the “event” is actually state.

Example:

* showing/hiding error banner
* showing loading spinner

These are **UI states**, not events.

---

## Final senior summary (10 seconds)

> “StateFlow is for persistent UI state. Events must not be replayed, so using StateFlow for
> navigation or toasts causes duplicate triggers. SharedFlow is the correct tool for events.”

> [⬆️ Back to Top / Close](#stateflow-vs-sharedflow)
</blockquote>
</details>

#### Final Interview Summary (15 seconds)

> “Flow is a cold, coroutine-based asynchronous stream. It’s lazy, cancellable, and
> backpressure-aware. StateFlow is used for state, SharedFlow for events, and Flow replaces
> callbacks,
> LiveData, and most RxJava use cases in modern Android.”

### RxJava (Conceptual Understanding)

RxJava is a **reactive programming library** based on streams.

#### Why it existed

- Before coroutines
- For complex async chains

#### Why less preferred today

- Steep learning curve
- Harder debugging
- Coroutines + Flow are simpler

✅ Interview answer:
> “RxJava is still used in legacy codebases, but coroutines + Flow are preferred today.”

---

## Performance

> This section often decides **senior vs mid-level** perception.

---

### ANR Causes & Prevention

**ANR (Application Not Responding)** happens when the main thread is blocked too long.

#### Time limits

- **5s** for input events
- **10s** for BroadcastReceiver
- **20s** for Service startup

#### Common ANR causes

- Network on main thread
- Disk IO on main thread
- Heavy computation on main thread
- Long `onReceive()` work
- Deadlocks

#### Prevention

- Move work to `Dispatchers.IO`
- Use WorkManager / coroutines
- Keep `onReceive()` minimal
- Avoid blocking calls (`Thread.sleep()`)

---

### Main Thread Blocking Scenarios

❌ Blocking examples:

```kotlin
Thread.sleep(3000)
runBlocking { }
File.readText()
Room DB query on main thread
        Network call without coroutine
```

✅ Correct approach:

```kotlin
withContext(Dispatchers.IO) {
    // background work
}
```

---

## Final Senior Interview Summary (30 seconds)

> “Legacy concurrency like AsyncTask, Handlers, and Loaders exist but are deprecated or limited.
> Modern Android uses Kotlin coroutines for structured concurrency, Flow for reactive streams, and
> lifecycle-aware scopes. Coroutines suspend without blocking threads, improving performance and
> safety. ANRs occur due to main thread blocking, so all IO, network, and heavy work must be
> offloaded
> properly.”

---
