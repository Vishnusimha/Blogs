# Kotlin Syntax Cheat Sheet

Here's a concise cheat sheet covering various Kotlin syntaxes:

### 1. Variables and Constants:

```kotlin
// Variable
var variableName: DataType = value

// Constant (read-only variable)
val constantName: DataType = value
```

### 2. Basic Types:

```kotlin
val integer: Int = 42
val longNumber: Long = 123456789L
val floatingPoint: Float = 3.14f
val doublePrecision: Double = 3.141592653589793238
val text: String = "Hello, Kotlin!"
val character: Char = 'A'
val boolean: Boolean = true
```

### 3. Conditional Statements:

```kotlin
// If-else statement
if (condition) {
    // Code block
} else {
    // Code block
}

// When expression (Kotlin's switch statement)
when (variable) {
    value1 -> // Code block
        value2
    -> // Code block
    else -> // Code block
}
```

### 4. Loops:

```kotlin
// For loop
for (item in collection) {
    // Code block
}

// While loop
while (condition) {
    // Code block
}
```

### 5. Functions:

```kotlin
fun functionName(parameter1: Type, parameter2: Type): ReturnType {
    // Code block
    return returnValue
}

// Single-expression function
fun functionName(parameter: Type): ReturnType = expression
```

### 6. Nullable Types and Safe Calls:

```kotlin
val nullableVariable: Type? = null

// Safe call operator
val length = nullableVariable?.length
```

### 7. Extension Functions:

```kotlin
fun String.customFunction() {
    // Code block
}

// Usage
"example".customFunction()
```

### 8. Classes and Objects:

```kotlin
class ClassName {
    // Properties
    // Constructor
    // Methods
}

// Object declaration (Singleton)
object SingletonObject {
    // Properties
    // Methods
}
```

### 9. Inheritance and Interfaces:

```kotlin
// Inheritance
class SubClass : SuperClass() {
    // Properties
    // Methods
}

// Interface
interface InterfaceName {
    // Properties
    // Methods
}
```

### 10. Collections:

```kotlin
// List
val list: List<Type> = listOf(element1, element2, element3)

// Mutable List
val mutableList: MutableList<Type> = mutableListOf()

// Map
val map: Map<KeyType, ValueType> = mapOf(key1 to value1, key2 to value2)

// Mutable Map
val mutableMap: MutableMap<KeyType, ValueType> = mutableMapOf()
```

This cheat sheet provides a quick reference for common Kotlin syntaxes. However, keep in mind that Kotlin has many more
features and functionalities not covered here. Always refer to the Kotlin documentation for detailed information.

Great! Here are some additional Kotlin syntaxes that are useful for intermediate to advanced developers:

---

## **Advanced Kotlin Syntax Cheat Sheet**

### **11. Higher-Order Functions & Lambda Expressions**

```kotlin
// Higher-order function: takes function as parameter
fun operateOnNumbers(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Lambda expression
val sum: (Int, Int) -> Int = { x, y -> x + y }

// Calling the function
val result = operateOnNumbers(5, 10, sum)
```

### **12. Inline Functions (Performance Optimization)**

```kotlin
inline fun inlineFunction(operation: () -> Unit) {
    operation()
}
```

### **13. Scope Functions (`let`, `run`, `with`, `apply`, `also`)**

```kotlin
val name: String? = "John"

// `let` (returns last statement, used for null-checks)
val length = name?.let { it.length }

// `run` (returns last statement, used for single object operations)
val length2 = name?.run { length }

// `with` (same as `run`, but takes object as parameter)
val formattedString = with(name) { uppercase() }

// `apply` (returns the object itself, used for initializing objects)
val person = Person().apply { age = 25 }

// `also` (returns the object itself, used for additional operations)
person.also { println(it) }
```

### **14. Sealed Classes & Enum Classes**

```kotlin
// Enum Class (Fixed Set of Constants)
enum class Direction { NORTH, SOUTH, EAST, WEST }

// Sealed Class (Restricted Hierarchy)
sealed class Result {
    class Success(val data: String) : Result()
    class Error(val message: String) : Result()
}
```

### **15. Delegation & Delegated Properties**

```kotlin
// Delegation using `by`
interface Logger {
    fun log(message: String)
}

class FileLogger : Logger {
    override fun log(message: String) {
        println("Logging: $message")
    }
}

class Application(logger: Logger) : Logger by logger

val app = Application(FileLogger())
app.log("Started") // Delegates to FileLogger

// Lazy property
val lazyValue: String by lazy {
    println("Computed!")
    "Hello, Kotlin!"
}

// Observable property
var observedValue: Int by Delegates.observable(0) { _, old, new ->
    println("Value changed from $old to $new")
}
```

### **16. Coroutines & Concurrency**

```kotlin
suspend fun fetchData(): String {
    delay(1000) // Simulates a network call
    return "Data retrieved"
}

fun main() = runBlocking {
    val data = async { fetchData() }
    println(data.await())
}
```

### **17. Type Aliases**

```kotlin
typealias UserMap = Map<String, List<String>>

val users: UserMap = mapOf("admin" to listOf("Alice", "Bob"))
```

### **18. Operator Overloading**

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

val p1 = Point(1, 2)
val p2 = Point(3, 4)
val p3 = p1 + p2 // Calls operator function
```

### **19. Extension Functions & Properties**

```kotlin
fun String.removeSpaces() = replace(" ", "")

val String.lastChar: Char
    get() = this[length - 1]

println("Hello World".removeSpaces()) // Output: HelloWorld
println("Hello".lastChar) // Output: o
```

### **20. Smart Casting & `is` Operator**

```kotlin
fun printLength(obj: Any) {
    if (obj is String) {
        println(obj.length) // Smart cast to String
    }
}
```

### **21. Companion Objects & Object Expressions**

```kotlin
// Companion Object
class Database {
    companion object {
        fun connect() = "Connected"
    }
}

println(Database.connect()) // Calls static-like function

// Object Expression (Anonymous Class)
val obj = object {
    val name = "Anonymous"
}
```

### **22. Try-Catch with Resources (`use` function)**

```kotlin
File("test.txt").bufferedReader().use {
    println(it.readText()) // Auto-closes resource
}
```

### **23. Destructuring Declarations**

```kotlin
data class User(val name: String, val age: Int)
val (userName, userAge) = User("Alice", 25)
```

### **24. Function References & Reflection**

```kotlin
fun greet(name: String) = "Hello, $name!"

val ref: (String) -> String = ::greet
println(ref("Alice")) // Calls greet function

// Reflection
println(User::class.simpleName) // Output: User
```

---

This extended Kotlin cheat sheet now includes essential and advanced topics, making it a well-rounded reference for an
intermediate-to-advanced Kotlin developer.
---

## **25. Generics**

```kotlin
// Generic Class
class Box<T>(val value: T) {
    fun get(): T = value
}

val intBox = Box(10)
val stringBox = Box("Hello")
```

```kotlin
// Generic Function
fun <T> identity(value: T): T = value

println(identity(42)) // 42
println(identity("Hello")) // Hello
```

```kotlin
// Generic Constraints
fun <T : Number> sum(a: T, b: T): Double {
    return a.toDouble() + b.toDouble()
}
```

---

## **26. Star Projections (`*`)**

```kotlin
fun printList(list: List<*>) {
    for (item in list) {
        println(item)
    }
}

printList(listOf(1, 2, 3))
printList(listOf("A", "B", "C"))
```

---

## **27. Variance in Generics (`in`, `out`)**

```kotlin
// Covariant (`out` - Read-Only)
interface Producer<out T> {
    fun produce(): T
}

// Contravariant (`in` - Write-Only)
interface Consumer<in T> {
    fun consume(value: T)
}
```

---

## **28. Reified Type Parameters (Only in Inline Functions)**

```kotlin
inline fun <reified T> getType(): String = T::class.java.simpleName

println(getType<Int>()) // Output: Integer
```

---

## **29. Tail Recursion (`tailrec`)**

```kotlin
tailrec fun factorial(n: Int, acc: Int = 1): Int {
    return if (n == 1) acc else factorial(n - 1, n * acc)
}
```

---

## **30. Annotation Classes**

```kotlin
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomAnnotation

@CustomAnnotation
class MyClass
```

---

## **31. Data Class Advanced Features**

```kotlin
// Copying with Modifications
data class Person(val name: String, val age: Int)

val person1 = Person("John", 30)
val person2 = person1.copy(age = 31)
```

```kotlin
// Data Class Component Functions
val (name, age) = person1
println(name) // John
```

---

## **32. SAM Conversions (Single Abstract Method)**

```kotlin
// Interface with a single method
fun interface Printer {
    fun printMessage(message: String)
}

// SAM Conversion (Lambda instead of Anonymous Class)
val printer: Printer = Printer { println(it) }

printer.printMessage("Hello Kotlin!")
```

---

## **33. Collections Advanced Features**

```kotlin
// Partitioning a list
val (even, odd) = listOf(1, 2, 3, 4, 5).partition { it % 2 == 0 }

// Chunked Processing
val chunkedList = listOf(1, 2, 3, 4, 5, 6).chunked(2) // [[1,2], [3,4], [5,6]]

// Zip Function
val letters = listOf("A", "B", "C")
val numbers = listOf(1, 2, 3)
val zipped = letters.zip(numbers) // [(A,1), (B,2), (C,3)]
```

---

## **34. Advanced Coroutine Builders**

```kotlin
// Launch (Fire & Forget)
launch {
    delay(1000)
    println("Hello from Coroutine")
}

// Async (Concurrent Execution)
val deferred = async { fetchData() }
val result = deferred.await()
```

```kotlin
// SupervisorScope (Prevent Failure Propagation)
supervisorScope {
    launch {
        throw RuntimeException("Error!") // Won't affect siblings
    }
    launch {
        println("Still Running")
    }
}
```

---

## **35. Inline Classes (Type-Safety Enhancements)**

```kotlin
@JvmInline
value class UserId(val id: String)

fun getUser(id: UserId) {
    println("Fetching user with id: ${id.id}")
}

val myId = UserId("1234")
getUser(myId)
```

---

### **Did I Miss Anything Now?**

This now includes **every critical Kotlin syntax** that an intermediate-to-advanced developer might need in a cheat
sheet.
