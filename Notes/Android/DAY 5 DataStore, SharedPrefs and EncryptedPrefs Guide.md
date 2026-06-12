---
title: "DataStore, SharedPreferences and EncryptedSharedPreferences Guide"
date: 2026-06-12
slug: "datastore-sharedpreferences-encryptedsharedpreferences-guide"
tags: [ "Notes", "Android", "DataStore", "SharedPreferences", "EncryptedSharedPreferences", "Secure Storage", "Preferences" ]
summary: "Personal Android storage notes explaining SharedPreferences, Preferences DataStore, Proto DataStore, EncryptedSharedPreferences, corruption handling, testing, migration from SharedPreferences, and secure key-value storage choices."
categories: Notes
readTime: 18
---


# DataStore, SharedPreferences & EncryptedSharedPreferences — Complete Android Guide

This guide explains **modern and legacy key-value storage in Android**, when to use each, and how to implement them properly for production apps.

---

## 📑 Table of Contents

- [1. SharedPreferences (Legacy)](#1-sharedpreferences-legacy)
- [2. DataStore (Modern Replacement)](#2-datastore-modern-replacement)
  - [Preferences DataStore](#preferences-datastore)
  - [Proto DataStore (Type-Safe)](#proto-datastore-type-safe)
- [3. EncryptedSharedPreferences (Secure Storage)](#3-encryptedsharedpreferences-secure-storage)
- [4. When to Use What?](#4-when-to-use-what)
- [5. Interview Summary](#5-interview-summary)

---

## 1. SharedPreferences (Legacy)

SharedPreferences is Android’s **older key-value storage system**.

### Characteristics
- Stores primitive types (String, Int, Boolean, etc.)
- Synchronous by default (`commit()` blocks thread)
- Not type-safe
- Not safe for large data
- Can cause ANR if misused

### Setup

```kotlin
val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
```

### Writing Data

```kotlin
sharedPrefs.edit()
    .putString("username", "Vishnu")
    .putBoolean("is_logged_in", true)
    .apply() // async write
```

### Reading Data

```kotlin
val username = sharedPrefs.getString("username", null)
val isLoggedIn = sharedPrefs.getBoolean("is_logged_in", false)
```

### Problems with SharedPreferences
- Runs on main thread
- No structured schema
- Hard to scale
- No Flow/LiveData support

---

## 2. DataStore (Modern Replacement)

DataStore is the **Jetpack replacement** for SharedPreferences.

### Why DataStore is better
- Asynchronous (Coroutines)
- Thread-safe
- Supports Kotlin Flow
- No ANR risk
- Type-safe with Proto DataStore

---

### Preferences DataStore

Key-value storage like SharedPreferences, but modern.

### Setup

```kotlin
val Context.dataStore by preferencesDataStore(name = "settings")
```

### Define Keys

```kotlin
object PreferencesKeys {
    val USERNAME = stringPreferencesKey("username")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}
```

### Write Data

```kotlin
suspend fun saveUser(context: Context, name: String) {
    context.dataStore.edit { prefs ->
        prefs[PreferencesKeys.USERNAME] = name
        prefs[PreferencesKeys.IS_LOGGED_IN] = true
    }
}
```

### Read Data

```kotlin
val usernameFlow: Flow<String?> = context.dataStore.data
    .map { prefs -> prefs[PreferencesKeys.USERNAME] }
```

---

### Proto DataStore (Type-Safe)

Used when you want **structured, strongly-typed data**.

### Step 1: Add Proto Schema (`user_prefs.proto`)

```proto
syntax = "proto3";

option java_package = "com.example.datastore";
option java_multiple_files = true;

message UserPreferences {
  string username = 1;
  bool is_logged_in = 2;
}
```

### Step 2: Serializer

```kotlin
object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            UserPreferences.parseFrom(input)
        } catch (e: Exception) {
            defaultValue
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        t.writeTo(output)
}
```

### Step 3: DataStore Instance

```kotlin
val Context.userPrefsDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer
)
```

### Write Data

```kotlin
suspend fun updateLogin(context: Context, name: String) {
    context.userPrefsDataStore.updateData { prefs ->
        prefs.toBuilder()
            .setUsername(name)
            .setIsLoggedIn(true)
            .build()
    }
}
```

### Read Data

```kotlin
val userFlow: Flow<UserPreferences> = context.userPrefsDataStore.data
```

---

## 3. EncryptedSharedPreferences (Secure Storage)

Used for **storing sensitive data** like:
- Tokens
- Passwords
- API keys

Uses **Android Keystore** under the hood.

### Setup

```kotlin
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val encryptedPrefs = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

### Writing Secure Data

```kotlin
encryptedPrefs.edit()
    .putString("auth_token", "secret_token_here")
    .apply()
```

### Reading Secure Data

```kotlin
val token = encryptedPrefs.getString("auth_token", null)
```

---

## 4. When to Use What?

| Use Case | Recommended |
|----------|------------|
| Simple settings | DataStore Preferences |
| Structured config | Proto DataStore |
| Secure tokens | EncryptedSharedPreferences |
| Legacy code | SharedPreferences (avoid new use) |

---

## 5. Interview Summary

> “DataStore replaces SharedPreferences with coroutine-based, thread-safe storage. Preferences DataStore handles simple key-values, Proto DataStore provides type safety, and EncryptedSharedPreferences secures sensitive data using Android Keystore.”


---

## 6. DataStore Error Handling & Corruption Handling (Advanced)

DataStore can encounter **IO errors** or **file corruption**. Unlike SharedPreferences, it provides built-in mechanisms to handle these safely.

### Handling IO Exceptions

```kotlin
val safeFlow = context.dataStore.data
    .catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences()) // fallback safely
        } else {
            throw exception
        }
    }
```

This prevents app crashes due to temporary file read errors.

---

### Handling Corruption (Proto DataStore)

When using Proto DataStore, file corruption can be handled with a **CorruptionHandler**.

```kotlin
val Context.userPrefsDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        UserPreferences.getDefaultInstance()
    }
)
```

This replaces corrupted data with a default instance instead of crashing.

---

## 7. Testing DataStore (Advanced)

Testing DataStore requires **TestCoroutineDispatcher** and **temporary file storage**.

### Rule for Unit Testing

```kotlin
@get:Rule
val coroutineRule = MainDispatcherRule()
```

### Creating Test DataStore

```kotlin
val testDataStore = PreferenceDataStoreFactory.create(
    scope = TestScope(),
    produceFile = { File("test_prefs.preferences_pb") }
)
```

### Testing Write & Read

```kotlin
@Test
fun testSaveUsername() = runTest {
    testDataStore.edit { it[stringPreferencesKey("username")] = "TestUser" }

    val value = testDataStore.data.first()[stringPreferencesKey("username")]
    assertEquals("TestUser", value)
}
```

---

## 8. Security Comparison — Encrypted DataStore vs EncryptedSharedPreferences

| Feature | EncryptedSharedPreferences | Encrypted DataStore |
|--------|----------------------------|----------------------|
| Encryption | Built-in | Requires manual implementation |
| Keystore Support | Yes | Only if custom encryption added |
| Coroutine Support | No | Yes |
| Flow Support | No | Yes |
| Recommended For | Tokens & secrets | Structured secure settings |
| Ease of Use | Easy | Advanced setup |

### Key Insight

EncryptedSharedPreferences is **simpler for storing secrets**, while encrypted DataStore requires custom encryption layers but integrates better with Flow and modern architecture.

---

## 9. DataStore Migration from SharedPreferences

DataStore supports automatic migration.

```kotlin
val Context.dataStore by preferencesDataStore(
    name = "settings",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "old_prefs"))
    }
)
```

This moves existing SharedPreferences data into DataStore seamlessly.

---

## 🔚 Final Advanced Interview Summary

> “In modern Android, DataStore replaces SharedPreferences with coroutine-based, safe storage. Preferences DataStore handles simple settings, Proto DataStore gives type safety, EncryptedSharedPreferences protects secrets, and advanced apps implement corruption handling, testing strategies, and secure storage comparisons.”
