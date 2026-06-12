---
title: "Interfaces in Java and Kotlin: SDK Contracts, Abstraction, and Callbacks"
date: 2026-06-08
slug: "interfaces-java-kotlin-sdk-contracts-callbacks"
tags: [ "Interfaces", "Java", "Kotlin", "OOP", "Backend Engineering" ]
summary: "Practical guide to interfaces in Java and Kotlin: learn how interfaces define contracts, support abstraction and polymorphism, enable SDK design, default/custom implementations, facades, and callback patterns."
categories: Backend Engineering
readTime: 8
---

# Interfaces

Interfaces are a cornerstone of modern software development, enabling developers to define contracts that classes must
adhere to. They promote flexibility, abstraction, and polymorphism, making code more modular and maintainable. In this
blog, we'll explore how interfaces are used in SDKs, why they are essential, and how they can function as callbacks.

## Interface Definition

An `interface` is a contract that defines a set of **abstract methods** that a class or component must implement. It
specifies what operations an object can perform without specifying how they are implemented.

Through interfaces, classes can achieve **multiple inheritance of types**, meaning a class can implement multiple
interfaces, each defining a different set of behaviors. This concept is also known as **subtyping**.

### Key Points:

- Interfaces are a fundamental tool for abstraction, allowing developers to focus on defining the essential behaviors of
  objects without concerning themselves with the specific implementation details.
- The `interface` provides a clear and standardized way to define common behavior across different classes, promoting
  code consistency and flexibility.
- `Interface` allows us to **treat different objects uniformly** based on their **common behaviour** .It emphasizes the
  idea of **polymorphism**, where different objects can be treated uniformly based on a shared interface.

### Example

```java
interface Drawable {
    void draw();
}

class Circle implements Drawable {
    public void draw() {
        System.out.println("Drawing a Circle");
    }
}

class Rectangle implements Drawable {
    public void draw() {
        System.out.println("Drawing a Rectangle");
    }
}

public class Main {
    public static void main(String[] args) {
        List<Drawable> shapes = new ArrayList<>();
        shapes.add(new Circle());
        shapes.add(new Rectangle());

        for (Drawable shape : shapes) {
            shape.draw();
        }
    }
}
```

#### Explanation

In this example, the `Drawable` interface defines a single method, `draw()`, which is implemented by both `Circle` and
`Rectangle`. The Main class demonstrates **polymorphism** by treating both shapes uniformly through the `Drawable`
interface. i.e. code interacts with `Circle` and `Rectangle` objects through the `Drawable` interface
This allows the code to call the `draw()` method on any `Drawable` object without needing to know the specific
implementation details of the shape. This demonstrates how **interfaces** promote a _separation between the interface
definition and the implementation_.

---
**Question we had:** When companies release an SDK or a library, they will give interfaces for us to utilize the
SDKs) right? So in that case, how the interface works, why they release it like that, can you explain that?

## Interfaces in SDKs

- Why do SDKs and libraries typically provide interfaces for developers? What are the key reasons behind this design
  choice?
- What is the role of interfaces in enabling developers to interact with these tools?

### Why SDKs Provide Interfaces

1. **Abstraction, Encapsulation and API Contract:**
    - Interfaces define a clear contract, or API (Application Programming Interface), that developers must adhere to
      when interacting with the SDK.
    - This provides abstraction, focusing on what the SDK offers rather than how it achieves it
    - This effectively hides the internal implementation details and ensures that developers interact with the SDK in a
      standardized way.
    - This allows SDK developers to make changes internally without affecting external code that relies on the defined
      API contract.
    - This ensures backward compatibility, modularity, and versioning, as changes to the internal implementation do not
      affect the external API contract.
2. **Flexibility and Extensibility**:
    - Developers can extend or customize the SDK's functionality by implementing the provided interfaces.
    - This allows for tailored solutions that fit specific needs.
    - This also allows for plug-and-play functionality, where new features or components can be added without disrupting
      existing code.
3. **Testability**:
    - Interfaces enable the creation of mock implementations, which are crucial for efficient unit testing.
    - This allows developers to isolate and test specific components of their code without relying on the complete SDK.
    - This also allows for Isolation and Dependency Injection.
4. **Consistency and Predictability:**
    - Interfaces establish a standardized way to interact with the SDK, ensuring predictable behavior across different
      implementations.
    - This helps maintain consistency and reduces the likelihood of unexpected errors and ease of use.
5. **Clear Documentation and Discoverability:**
    - Interfaces act as a form of documentation, clearly outlining the methods and properties that developers can use.
    - This makes it easier to understand and utilize the SDK.
    - This also improves Discoverability and Documentation Generation.

The practical application of interfaces in SDKs is evident in platforms like Android. The Android SDK heavily relies on
interfaces for its various components, such as `OnClickListener` for handling user interactions. Similarly, networking
libraries like `Retrofit` also use interfaces to define **API endpoints**, demonstrating the widespread and effective
use of interfaces in software development.

Question we had: So here, User can either create an object for the first-order class and access the default
implementation of the SDK, or else they can again implement the interface given by us and access them with their own
class. So in both cases, if they call a method the default implementation which is there in the SDK will get triggered
and works. Only if they add additional implementation to their overriden methods, it will be the customized
implementation.)

### Example: SDK Interface Implementation

#### SDK Module

In this example, the Calculator interface defines the basic arithmetic operations. The DefaultCalculator class provides
a default implementation, while the CalculatorFacade class acts as a simplified interface for users. Users can either
use the default implementation or provide their own custom implementation by implementing the Calculator interface.

#### Calculator interface:

```kotlin
interface Calculator {
    fun add(a: Double, b: Double): Double

    fun subtract(a: Double, b: Double): Double

    fun multiply(a: Double, b: Double): Double

    fun divide(a: Double, b: Double): Double
}
```

#### Default Implementation (DefaultCalculator.kt):

```kotlin
internal class DefaultCalculator : Calculator {
    // Default calculator implementation
    override fun add(a: Double, b: Double): Double {
        return a + b
    }

    override fun subtract(a: Double, b: Double): Double {
        return a - b
    }

    override fun multiply(a: Double, b: Double): Double {
        return a * b
    }

    override fun divide(a: Double, b: Double): Double {
        if (b != 0.0) {
            return a / b
        } else {
            throw IllegalArgumentException("Cannot divide by zero")
        }
    }
}
```

#### Facade (`CalculatorFacade.kt`):

```kotlin
// Facade for the Calculator subsystem
class CalculatorFacade {
    private val calculator: Calculator = DefaultCalculator()

    fun add(a: Double, b: Double): Double = calculator.add(a, b)
    fun subtract(a: Double, b: Double): Double = calculator.subtract(a, b)
    fun multiply(a: Double, b: Double): Double = calculator.multiply(a, b)
    fun divide(a: Double, b: Double): Double = calculator.divide(a, b)
}
```

#### User's Code

```kotlin
fun main() {
    val calculatorFacade = CalculatorFacade()

    // Users can only access the simplified interface provided by CalculatorFacade
    println("Addition: ${calculatorFacade.add(5.0, 3.0)}") // Output: Addition: 8.0
    println("Subtraction: ${calculatorFacade.subtract(7.0, 2.0)}") // Output: Subtraction: 5.0
    println("Multiplication: ${calculatorFacade.multiply(4.0, 6.0)}") // Output: Multiplication: 24.0

    try {
        println("Division: ${calculatorFacade.divide(8.0, 2.0)}") // Output: Division: 4.0
        println("Division: ${calculatorFacade.divide(6.0, 0.0)}") // Throws IllegalArgumentException
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}") // Output: Error: Cannot divide by zero
    }
}
```

### **How Users Can Choose Between Default and Custom Implementations**

- The facade always calls `DefaultCalculator` unless users explicitly provide a custom implementation.
- The default implementation is used unless users explicitly provide a custom implementation by overriding the methods.

---

### Choosing Between Default and Custom Implementations

### **1. Direct Usage of Default Implementation**

- Users can create an object of the first-order class/entry-point class (in this case, `CalculatorFacade`) and directly
  use the default implementation provided by the SDK.
- This approach allows for quick integration without the need for additional implementation work.

### **2. Custom Implementation by Implementing the Interface**

- Users can choose to implement the `Calculator` interface in their own class (e.g., `MyCustomCalculator`).
- By doing so, they can customize the behavior of the `Calculator` methods according to their specific requirements.
- When users implement their own custom calculator by adhering to the `Calculator` interface, their custom
  implementation will be executed when the Facade calls those methods.
- The default implementation (`DefaultCalculator`) is only used when the user directly uses the `CalculatorFacade`.

In both cases, users benefit from a **consistent API**, leveraging the standard methods defined by the interface.  
The flexibility to either use the **default implementation** or create a **custom implementation** empowers developers
to tailor the SDK functionality to their needs while maintaining compatibility with the established interface.  
This approach strikes a balance between **ease of use** and **extensibility**.

---

### **Explanation**

#### **Internal Modifier**

- By using the `internal` modifier on the `DefaultCalculator` class, you make it accessible **only** within the same
  module (SDK module).
- This hides it from external modules.

#### **Facade Pattern**

- The `CalculatorFacade` acts as a simplified interface to the calculator subsystem, and it contains an instance of the
  actual calculator implementation (`DefaultCalculator`).

#### **User's Code**

- Users can only access the `CalculatorFacade`, which provides a clean and straightforward API without exposing the
  internal complexities of the SDK.

#### Conclusion:

- This design allows you to **encapsulate the implementation details** within your SDK and provide users with a *
  *simplified
  interface** through the `CalculatorFacade`.
- Users interact with the subsystem **only** through this facade, ensuring that the implementation details are **hidden
  and encapsulated**.

---

## How the interfaces works as a callbacks?

In programming, **callbacks** refer to a mechanism where you pass a function (or a reference to a function) to another
function or method, allowing it to be executed later. Interfaces in `Java` and `Kotlin` are often used to implement
callback patterns.
Let's go through a simple example in Kotlin to illustrate how interfaces can be used as callbacks:

### Callback interface

```kotlin
interface OnTaskCompleteListener {
    fun onTaskComplete(result: String)
}
```

### Class with Callback Registration:

```kotlin
class TaskPerformer {
    private var callback: OnTaskCompleteListener? = null

    //Method to register the callback
    fun setOnTaskCompleteListener(callback: OnTaskCompleteListener) {
        this.callback = callback
    }

    //Method that performs at ask and invokes the callback
    fun performTask() {
//Simulate a time-consuming task.
//Task complete, invoke the callback.
        GlobalScope.launch {
            delay(2000)
            callback?.onTaskComplete("Task executed successfully!")
        }

    }
}
```

### Usage in Main:

```kotlin
// Main function demonstrating callback usage
fun main() {
    // Create an instance of the TaskPerformer
    val taskPerformer = TaskPerformer()

    // Register a callback using an anonymous object
    taskPerformer.setOnTaskCompleteListener(object : OnTaskCompleteListener {
        override fun onTaskComplete(result: String) {
            println("Callback: $result")
        }
    })

    // Perform the task
    taskPerformer.performTask()

    // Continue with other operations while the task is being performed
    println("Main function continues...")
}
```

### Explanation:

1. **Callback Interface (`OnTaskCompleteListener`):**
    - Defines a callback interface with a method (onTaskComplete) that will be invoked when a task is complete.
2. **Class with Callback Registration (TaskPerformer):**
    - Contains a method (`setOnTaskCompleteListener`) to register a callback of type `OnTaskCompleteListener`.
    - Performs a task (`performTask`) and invokes the registered callback when the task is complete.
3. **Usage in Main:**
    - Creates an instance of `TaskPerformer`.
    - Registers a callback using an anonymous object that implements the `OnTaskCompleteListener` interface.
    - Performs a task asynchronously, allowing the main function to continue with other operations.
    - When the task is complete, the registered callback is invoked.
      This example showcases how interfaces can be used as callbacks. The callback interface defines a contract, and any
      class implementing that interface can be registered to receive notifications when a particular event or task is
      complete. This pattern is widely used in asynchronous programming and event-driven architectures, where it allows
      for non-blocking operations and efficient handling of events.

This pattern is particularly useful for asynchronous operations, allowing the program to continue executing other tasks
while waiting for a process to complete. By using interfaces as callbacks, developers can decouple the logic for
handling task completion from the logic for performing the task, making the code more modular and easier to maintain.


