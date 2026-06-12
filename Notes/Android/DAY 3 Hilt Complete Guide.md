---
title: "DAY 3 Hilt Dependency Injection: Complete Senior-Level Guide for Android"
date: 2026-06-20
slug: "hilt-dependency-injection-android-guide"
tags: [ "Notes", "Android", "Hilt", "Dependency Injection", "Dagger", "Architecture" ]
summary: "An exhaustive guide to Hilt DI in Android, covering everything from basic setup to advanced concepts like qualifiers, assisted injection, and testing."
categories: Notes
readTime: 12
---

# Hilt Dependency Injection — Complete Senior-Level Guide

This guide covers **everything you need to know about Hilt** for Android interviews and real-world
projects.

---

## 📑 Table of Contents

- [What is Hilt?](#what-is-hilt)
- [Why Dependency Injection?](#why-dependency-injection)
- [Hilt vs Dagger](#hilt-vs-dagger)
- [Hilt Setup](#hilt-setup)
- [Core Hilt Concepts](#core-hilt-concepts)
- [Providing Dependencies](#providing-dependencies)
- [Injecting Dependencies](#injecting-dependencies)
- [Hilt Scopes](#hilt-scopes)
- [Qualifiers](#qualifiers)
- [Assisted Injection](#assisted-injection)
- [Entry Points](#entry-points)
- [Hilt + ViewModel](#hilt--viewmodel)
- [Hilt + WorkManager](#hilt--workmanager)
- [Hilt + Navigation](#hilt--navigation)
- [Testing with Hilt](#testing-with-hilt)
- [Common Mistakes](#common-mistakes)
- [Interview Q&A](#interview-qa)

---

## What is Hilt?

Hilt is **Google’s official dependency injection library for Android**, built on top of Dagger.  
It reduces boilerplate and integrates with Android components like **Activity, Fragment, ViewModel,
Service, and WorkManager**.

> Hilt manages object creation and lifecycle automatically using generated Dagger components.

---

## Why Dependency Injection?

Without DI:

- Classes create their own dependencies
- Hard to test
- Tight coupling

With DI:

- Dependencies are provided from outside
- Easier testing
- Better architecture

---

## Hilt vs Dagger

| Feature                   | Dagger | Hilt     |
|---------------------------|--------|----------|
| Boilerplate               | High   | Low      |
| Android lifecycle support | Manual | Built-in |
| Learning curve            | Steep  | Easier   |
| Recommended today         | ❌      | ✅        |

---

## Hilt Setup

### 1️⃣ Gradle Setup

```gradle
plugins {
    id 'dagger.hilt.android.plugin'
    id 'kotlin-kapt'
}

dependencies {
    implementation "com.google.dagger:hilt-android:2.50"
    kapt "com.google.dagger:hilt-compiler:2.50"
}
```

### 2️⃣ Application Class

```kotlin
@HiltAndroidApp
class MyApp : Application()
```

---

## Core Hilt Concepts

| Concept   | Meaning                          |
|-----------|----------------------------------|
| Module    | Provides dependencies            |
| Component | Container of dependencies        |
| Scope     | Lifetime of dependency           |
| Qualifier | Distinguishes same type bindings |

---

## Providing Dependencies

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.example.com")
            .build()

    @Provides
    fun provideApi(retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)
}
```

---

## Injecting Dependencies

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var api: Api
}
```

---

## Hilt Scopes

| Scope            | Lifetime                |
|------------------|-------------------------|
| Singleton        | App lifetime            |
| ActivityRetained | Survives config changes |
| ViewModelScoped  | ViewModel lifetime      |
| ActivityScoped   | Activity lifecycle      |
| FragmentScoped   | Fragment lifecycle      |

---

## Qualifiers

Used when multiple bindings of same type exist.

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit
```

---

## Assisted Injection

Used when runtime parameters are needed.

```kotlin
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repo: Repo
) : ViewModel()
```

---

## Entry Points

Used when Android class not supported by Hilt needs injection.

```kotlin
@EntryPoint
@InstallIn(SingletonComponent::class)
interface MyEntryPoint {
    fun repo(): Repo
}
```

---

## Hilt + ViewModel

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel()
```

---

## Hilt + WorkManager

```kotlin
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: Repo
) : CoroutineWorker(context, params)
```

---

## Testing with Hilt

```kotlin
@HiltAndroidTest
class MyTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
}
```

---

## Common Mistakes

❌ Forgetting @AndroidEntryPoint  
❌ Using wrong scope  
❌ Not using qualifiers when needed

---

## Interview Q&A

**Q: Why use Hilt?**  
A: Reduces boilerplate, lifecycle aware, easier DI for Android apps.

**Q: Difference between @Inject and @Provides?**  
A: `@Inject` constructor = class knows how to build itself.  
`@Provides` = used when we don’t control class creation.

---

## Final Senior Summary

Hilt is the modern DI framework for Android that simplifies Dagger usage by generating components
tied to Android lifecycles, enabling scalable, testable, and maintainable applications.
