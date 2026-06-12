# Technical interview questions

#### DAY 8 Kotlin, System Thinking & Interview Readiness

1. What are the advantages of using Kotlin for Android development?
2. `open` class in Kotlin meaning?
3. Companion Object vs Object in Kotlin

## Answers:

#### Day 7 Answers

### 1. What are the advantages of using Kotlin for Android development?

- **Concise Syntax**: Reduces boilerplate code.
- **Null Safety**: Helps prevent null pointer exceptions.
- **Coroutines**: Simplifies asynchronous programming.
- **Extension Functions**: Adds functionality to existing classes without inheriting.
- **Interoperability**: Fully interoperable with Java.

### 2. `open` class in Kotlin meaning?

In Kotlin, `open` is a keyword that allows a class to be inherited. By default, all classes in Kotlin are final (cannot
be inherited), so you need to mark them as `open` to allow inheritance.

Example:

```kotlin
open class BaseClass {
    open fun greet() {
        println("Hello from BaseClass")
    }
}

class DerivedClass : BaseClass() {
    override fun greet() {
        println("Hello from DerivedClass")
    }
}
```

By marking `BaseClass` as `open`, it can be inherited by `DerivedClass`, and the `greet` method can be overridden.

### 3. Companion Object vs Object in Kotlin

In Kotlin, both `object` and `companion object` are used to define singletons, but they serve different purposes and
have different scopes.

#### 🔹 `object`

1. **Singletons**:
    - Declaring an `object` creates a single instance of that class. This is a true singleton, meaning there's only *
      *one instance** of the object throughout the application.
    - Used for **utility classes**, **holding constants**, or **thread-safe singletons** without extra boilerplate.

   ```kotlin
   object NetworkManager {
       fun makeRequest() {
           // Implementation
       }
   }

   // Usage
   NetworkManager.makeRequest()
   ```

2. **Initialization**:
    - An `object` is instantiated the **first time** it is accessed.

3. **Properties and Methods**:
    - Properties and methods defined inside an `object` can be accessed directly using the object name.

---

#### 🔹 `companion object`

1. **Static-like Members**:
    - A `companion object` is used **inside a class** to define members that belong to the class itself rather than to
      instances. This behaves similarly to **static members in Java**.
    - Useful for **factory methods, constants, or other utilities** related to the class.

   ```kotlin
   class MyClass {
       companion object {
           const val CONSTANT = "constant"
           fun create(): MyClass = MyClass()
       }
   }

   // Usage
   val instance = MyClass.create()
   val value = MyClass.CONSTANT
   ```

2. **Initialization**:
    - The `companion object` is instantiated when the **containing class** is first loaded, similar to **static
      initialization in Java**.

3. **Naming**:
    - You can optionally name a companion object. If unnamed, it is accessed using the class name.

   ```kotlin
   class MyClass {
       companion object Factory {
           fun create(): MyClass = MyClass()
       }
   }

   // Usage
   val instance = MyClass.Factory.create()
   ```

4. **Multiple Companion Objects?**
    - A **class can have only one `companion object`**.

---

#### 🔥 Differences at a Glance

| Feature           | `object`                            | `companion object`                  |
|-------------------|-------------------------------------|-------------------------------------|
| **Scope**         | Top-level or nested within a class  | Only inside a class                 |
| **Usage**         | Global, application-wide singletons | Static-like members inside a class  |
| **Instantiation** | First access                        | When the containing class is loaded |

---

#### 🔹 Example to Highlight Differences

```kotlin
object Logger {
    fun log(message: String) {
        println("Log: $message")
    }
}

class Example {
    companion object {
        const val CONSTANT = "ExampleConstant"
        fun create(): Example = Example()
    }

    fun instanceMethod() {
        println("Instance method called")
    }
}

// Usage
Logger.log("This is a log message") // Singleton object usage

val example = Example.create() // Companion object usage
example.instanceMethod()

println(Example.CONSTANT) // Access companion object constant
```

#### **Explanation**

- `Logger` is a **singleton object**, accessible globally.
- `Example` has a **companion object**, which provides a factory method and a constant specific to the class.

---
