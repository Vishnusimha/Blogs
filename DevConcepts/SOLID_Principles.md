---
title: "SOLID Principles"
date: 2026-06-08
slug: "solid-principles-object-oriented-design-kotlin"
tags: [ "SOLID", "OOP", "Kotlin", "Java", "Backend Engineering" ]
summary: "Clear guide to the five SOLID principles: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion with practical Kotlin examples."
categories: Backend Engineering
readTime: 8
---

# SOLID Principles

SOLID is an acronym that represents five fundamental principles of object-oriented programming and design. These
principles are aimed at making software designs more understandable, flexible, and maintainable. Let's break down each
principle and provide examples in Kotlin:

## Single Responsibility Principle (SRP):

This principle states that a class should have only one reason to change, meaning it should have only one
responsibility.

Example in Kotlin:

#### Before applying SRP:

```kotlin
// Before applying SRP
class FileManager {
    fun readFromFile() {
        // Code to read from file
    }

    fun writeToFile() {
        // Code to write to file
    }

    fun manipulateFileData() {
        // Code to manipulate file data
    }
}
```

#### After applying SRP:

```kotlin

class FileReader {
    fun readFromFile() {
// Code to read from file
    }
}

class FileWriter {
    fun writeToFile() {
// Code to write to file
    }
}

class DataManipulator {
    fun manipulate(data: String) {
// Code to manipulate data
    }
}
```
This separation ensures that if the logic for reading/writing files changes, it won't affect the data manipulation logic, and vice versa. If the original
FileManager class had both responsibilities, a change in either would necessitate modifying the same class, violating SRP.

## Open/Closed Principle (OCP):

This principle states that classes should be open for extension but closed for modification. It encourages the use of
abstraction and polymorphism to allow new functionality to be added without changing existing code.

Example in Kotlin:

```kotlin
interface Shape {
    fun area(): Double
}

class Rectangle(val width: Double, val height: Double) : Shape {
    override fun area(): Double {
        return width * height
    }
}

class Circle(val radius: Double) : Shape {
    override fun area(): Double {
        return Math.PI * radius * radius
    }
}
```

### Understanding the OCP Example:

In the example provided, we have an interface called Shape, which defines a method area() to calculate the area of
different shapes. We then have two concrete classes Rectangle and Circle, both implementing the Shape interface and
providing their own implementations of the area() method.
How OCP is Applied:

The key aspect of OCP is that once a class is written and tested, it should not be modified except to correct errors.
Instead, new functionality should be added by writing new code.
Furthermore, code that utilizes the `Shape` interface, such as the printArea function in the LSP example, does not need to be modified when new shapes are added.


#### Open for Extension:

We can easily introduce new shapes without modifying existing code. For example, if we want to add a Triangle class, we
simply create a new class implementing the Shape interface and provide its own implementation of the area() method.

#### Closed for Modification:

Once the Rectangle and Circle classes are defined and tested, we don't need to modify them even when introducing new
shapes. This ensures that existing code remains stable and unchanged.

#### Abstraction and Polymorphism:

The Shape interface serves as an abstraction, defining a common behavior (area()) that all shapes must implement.
Polymorphism is achieved through the use of interfaces and inheritance. Different shapes (Rectangle, Circle, etc.) can
be treated uniformly through their common interface, allowing for code that operates on shapes without needing to know
their specific types.

#### Benefits of OCP:

1. **Maintainability**: Existing code remains stable, reducing the risk of introducing bugs when new functionality is
   added.
2. **Scalability**: New functionality can be easily introduced without impacting existing code, making the system more
   adaptable to changing requirements.
3. **Flexibility**: The use of abstraction and polymorphism allows for more flexible and modular code, facilitating
   easier maintenance and extension.

In essence, the **Open/Closed Principle** encourages developers to design software components that are open for
extension
through abstraction and polymorphism while being closed for modification to ensure stability and maintainability.

## Liskov Substitution Principle (LSP):

This principle states that objects of a superclass should be replaceable with objects of its subclasses without
affecting the correctness of the program.

Example in Kotlin:

```kotlin
open class Shape {
    open fun area(): Double {
        return 0.0
    }
}

class Rectangle(val width: Double, val height: Double) : Shape() {
    override fun area(): Double {
        return width * height
    }
}

class Square(val side: Double) : Shape() {
    override fun area(): Double {
        return side * side
    }
}
```

### How LSP is Applied:

#### Substitutability:

Anywhere in the code where an object of type Shape is expected, we can substitute it with objects of type Rectangle or
Square without breaking the functionality of the program.
For example, if a function expects a Shape object as a parameter, we can safely pass either a Rectangle or a Square
object to that function.

#### Behavior Preservation:

Both Rectangle and Square classes provide their own implementations of the area() method, which calculate the area
specific to each shape.
Despite having different implementations, both subclasses adhere to the contract defined by the superclass (Shape),
ensuring that the behavior expected from a Shape object is preserved.

**Note:** It's important to note that adding new methods to a subclass that are not present in the superclass does not violate LSP. However, changing the
behavior of a method inherited from the superclass in a way that violates the superclass's contract does violate LSP.

### Benefits of LSP:

**Polymorphism**: LSP facilitates polymorphism, allowing objects of different subclasses to be treated interchangeably
based on their common superclass.

**Code Re-usability**: By adhering to LSP, we can reuse code written for the superclass (Shape) for its subclasses (
Rectangle, Square), reducing duplication and promoting a more maintainable codebase.

**Flexibility**: LSP promotes flexibility in software design by allowing for the substitution of objects at runtime,
making the system more adaptable to changing requirements.

```kotlin
fun printArea(shape: Shape) {
    println("Area: ${shape.area()}")
}

fun main() {
    val rectangle = Rectangle(5.0, 3.0)
    val square = Square(4.0)

    printArea(rectangle) // Outputs: Area: 15.0
    printArea(square)   // Outputs: Area: 16.0
}
```

In the main() function, we demonstrate substitutability by passing objects of Rectangle and Square to the printArea()
function, which expects a Shape object. Both objects are successfully substituted, and the correct area is calculated
and printed without affecting the correctness of the program.

## Interface Segregation Principle (ISP):

This principle states that clients should not be forced to depend on interfaces they don't use. It encourages the use of
smaller, cohesive interfaces.

Example in Kotlin:

```kotlin
interface Printer {
    fun print()
}

interface Scanner {
    fun scan()
}

class MultifunctionDevice : Printer, Scanner {
    override fun print() {
        // Code to print
    }

    override fun scan() {
        // Code to scan
    }
}

class SimplePrinter : Printer {
    override fun print() {
        // Code to print
    }
}
```
Without ISP, if we had a single `IODevice` interface with both `print()` and `scan()` methods, the `SimplePrinter` class would be forced to implement the `scan()` method, even though it doesn't support scanning. This would lead to **unnecessary code or potentially throwing exceptions** for unsupported operations.

## Dependency Inversion Principle (DIP):

This principle states that **high-level modules** should not depend on **low-level modules**; both should depend on
**abstractions**. It promotes **loose coupling** between classes by **decoupling** the implementation details from the high-level
modules.

Example in Kotlin:

```kotlin
interface Database {
    fun save(data: String)
}

class MySQLDatabase : Database {
    override fun save(data: String) {
        // Code to save data to MySQL database
    }
}

class OracleDatabase : Database {
    override fun save(data: String) {
        // Code to save data to Oracle database
    }
}

class DataManager(private val database: Database) {
    fun processData(data: String) {
        // Code to process data and save it to the database
        database.save(data)
    }
}

fun main() {
   val mysqlDatabase = MySQLDatabase()
   val dataManagerForMySQL = DataManager(mysqlDatabase)
   dataManagerForMySQL.processData("Data to be saved in MySQL")

   val oracleDatabase = OracleDatabase()
   val dataManagerForOracle = DataManager(oracleDatabase)
   dataManagerForOracle.processData("Data to be saved in Oracle")
}

```

#### How DIP is Applied:

The Dependency Inversion Principle states that high-level modules should not depend on low-level modules; both should
depend on abstractions. Let's understand how this principle is demonstrated in the example:

In this example, `DataManager` is a high-level module, as it defines the overall application logic, while `MySQLDatabase` and `OracleDatabase` are low-level modules, providing the specific database implementation details.

**Abstraction with Interface**:
By introducing the Database interface, we provide an abstraction that defines a contract (save() method) for saving
data.
High-level modules (e.g., DataManager) depend on this abstraction (interface) rather than on specific implementations (
MySQLDatabase or OracleDatabase).

**Decoupling High-level and Low-level Modules**:
DataManager class depends on the Database interface, which represents the abstraction. It doesn't directly depend on
specific database implementations.
This decouples the DataManager class from the details of how data is actually saved, promoting loose coupling.

**Flexibility and Extensibility**:
Because DataManager depends on an abstraction (Database interface), it can work with any class that implements this
interface, regardless of the specific database used.
Adding support for a new database involves creating a new class that implements the Database interface, without needing
to modify existing code.