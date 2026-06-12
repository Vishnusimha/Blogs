---
title: "Object-Oriented Programming Concepts"
date: 2026-06-08
slug: "object-oriented-programming-concepts-java-kotlin"
tags: [ "OOP", "Java", "Kotlin", "Software Engineering", "Backend Engineering" ]
summary: "Beginner-friendly guide to core object-oriented programming concepts including encapsulation, abstraction, inheritance, polymorphism, classes, objects, constructors, composition, and aggregation."
categories: Backend Engineering
readTime: 7
---

# Object-Oriented programming concepts 
**OOPs concepts include:**

    Encapsulation
    Abstraction
    Inheritance
    Polymorphism

## Definitions

**_Encapsulation_**:
Encapsulation is the bundling of the state and behaviour of an object within a class. It restricts direct access to the internal components of the class and provides controlled access through well-defined interfaces.

**_Encapsulation is achieved through:_**
- Private Access Modifiers
- Getters and Setters

**_Abstraction_**:
Abstraction is the process of simplifying complex systems by modelling classes based on their essential features and hiding unnecessary details.

_**Abstraction is achieved through the use of:**_
- **Abstract classes and Interfaces** -> Here we focus on defining what an object does rather than how it achieves its functionality.
  
**How do we hide unnecessary details?**

**Ans:** Through (Access Modifiers, Encapsulation, Abstraction Layers)

**Abstraction Layers:**
In complex systems, **abstraction** occurs in layers, promoting a **'separation of concerns.'** 
Each layer provides a simplified interface to the layer above, hiding the underlying complexities. This modular approach allows each layer to focus on its specific responsibilities, improving maintainability and reducing dependencies.

```kotlin
// Higher-level module
public class OrderProcessor {
    private PaymentProcessor paymentProcessor;

    public void processOrder(Order order) {
        // Interaction through an abstracted interface
        paymentProcessor.processPayment(order.getTotalAmount());
    }
}
```

**Abstract Class**: An abstract class is a blueprint and serves as a base class for other classes also any class cannot instantiate it, it can only be extended to provide a common structure and behaviour for derived classes.
It can contain abstract or non-abstract methods, i.e., with or without method implementation. Subclasses must provide implementations for all abstract methods.

**Interface**: An interface is a contract that defines a set of abstract methods that a class or component must implement.
It specifies what operations an object can perform without specifying how they are implemented.
Through interfaces, classes can achieve multiple inheritance of method signatures.

**Class**:
A class is a blueprint or template for creating objects. It defines the properties (attributes) and behaviours (methods) that objects of the class will have.
A Class is a static entity

 **Object**:
An object is an instance of a class. It represents a real-world entity and encapsulates data (attributes) and functionality (methods). 
An object is a dynamic entity, created during runtime.

**Inheritance**:
Inheritance is a mechanism that allows a class (subclass or derived class) to inherit properties and behaviours from another class (superclass or base class).
1. Single Inheritance
2. Multilevel Inheritance
3. Hierarchical Inheritance: Hierarchical inheritance involves multiple subclasses inheriting from the same superclass.
4. Multiple Type Inheritance (through Interfaces): Java does not support multiple inheritance of classes (i.e., a class cannot directly extend more than one class). However, a class can implement multiple interfaces, enabling it to fulfill multiple contracts and achieve multiple type inheritance. This allows a class to be treated as instances of multiple interfaces, defining a set of behaviors without inheriting implementation details from multiple classes.

**Polymorphism**:
Polymorphism allows objects of different types to be treated as objects of a common interface or a common base class. 

It contains two main concepts:

-  **Method Overloading**:
Method overloading allows a class to have multiple methods with the same name but different parameters. The compiler determines which method to invoke based on the method signature.

-  **Method Overriding**:
Method overriding occurs when a subclass provides a specific implementation for a method that is already defined in its superclass. It allows a subclass to provide a specialized version of a method.

**Constructor**:
A constructor is a special method called when an object is instantiated. It is used to initialize the object's state.

**Destructor**:
In some programming languages, a destructor is a special method called when an object is about to be destroyed. It is used for cleanup operations.

Java uses automatic garbage collection for memory management, and therefore does not have traditional destructors like in C++. While the
`finalize()` method exists, its use is strongly discouraged. Instead, resources should be managed using try-with-resources blocks or explicit close methods.

**Composition**:
Composition is a design concept where a class contains an object of another class, and the contained object's lifecycle is dependent on the containing object. It represents a strong 'has-a' relationship. 
For example, a car 'has-a' engine, and the engine typically cannot exist without the car.

**Aggregation**: Aggregation is another form of 'has-a' relationship, but it represents a weaker association.
In aggregation, the contained object can exist **independently** of the containing object. 
For example, a department 'has-a' students, but students can exist outside of a specific department.