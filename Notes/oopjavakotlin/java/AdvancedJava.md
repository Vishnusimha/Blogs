## Streams API and Handling Data Sets

Introduction to Streams

    Definition: A Stream is a sequence of elements that can be processed sequentially or in parallel.
    Source Creation:
        From a List: Use the .stream() method.
        From an Array: Use Arrays.stream(array).
        Using Stream.of: Create a Stream with the Stream.of method.

Types of Stream Operations

    Intermediate Operations: Return a new Stream and can be chained; they are lazy and do not execute until a terminal operation is called.
        filter: Filters elements based on a predicate.
        map: Transforms elements using a given function.
        flatMap: Transforms each element into a Stream and then flattens the resulting Streams into a single Stream.

    Terminal Operations: End the Stream and no further operations can be performed.
        forEach: Applies an action to each element in the Stream.
        collect: Collects elements into a collection such as a List or Set.
        reduce: Reduces the Stream to a single value using an accumulator function.

Examples of Stream Operations

    Example Code:
        Filter, Map, Collect:

        java

        List<String> names = Arrays.asList("Ali", "Bob", "Adam");
        List<String> filteredNames = names.stream()
                                          .filter(name -> name.startsWith("A"))
                                          .map(String::toUpperCase)
                                          .collect(Collectors.toList());
        System.out.println(filteredNames);  // Output: [ALI, ADAM]

Key Points

    Streams simplify code handling datasets using a functional programming approach.
    Intermediate operations are lazy and only execute when a terminal operation is called.
    Functional programming style can often replace traditional loops and if statements.

Conclusion

    This overview provides the foundational knowledge to start writing code in the functional programming style using Streams API.
![1](/oopjavakotlin/javaImages/8.png)
![1](/oopjavakotlin/javaImages/9.png)

## Concurrency

Java Concepts for Concurrency
Overview

    Java provides numerous tools and APIs for concurrency and multithreading.
    This overview covers key concepts and practical examples.

Thread Class and Runnable Interface

    Thread Class: Extends the Object class and implements the Runnable interface.
        Provides methods for starting, stopping, and managing threads.
        Example:

        java

        class MyThread extends Thread {
            public void run() {
                System.out.println("Hello from thread " + getId());
            }
        }
        // Usage:
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();
        thread1.start();
        thread2.start();

            Output: "Hello from thread X" and "Hello from thread Y" (X and Y are different thread IDs).

Synchronization and Locks

    Ensures safe and consistent access to shared resources by multiple threads.
    Synchronized Keyword: Creates synchronized methods or blocks.
        Only one thread can access the synchronized resource at a time.
        Example:

        java

        public synchronized void increment() {
            // critical section
        }

Executor Framework

    Simplifies thread creation and management.
    ExecutorService: Manages a pool of threads to execute tasks concurrently.
        Example:

        java

        ExecutorService executor = Executors.newFixedThreadPool(5);
        Runnable task = () -> System.out.println("Hello from Executor on thread " + Thread.currentThread().getId());
        for (int i = 0; i < 10; i++) {
            executor.execute(task);
        }
        executor.shutdown();

            Output: Tasks run using different thread IDs.

Concurrent Collections

    Designed for safe concurrent access and manipulation by multiple threads.
    Examples:
        ConcurrentHashMap
        ConcurrentLinkedQueue
        ConcurrentSkipListSet
        Example:

        java

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("key", 1);

Future and Callable Interfaces

    Handle responses that are not immediately available.
    Callable: Similar to Runnable but can return a value.
    Future: Represents the result of an asynchronous computation.
        Example:

        java

```java
ExecutorService executor = Executors.newFixedThreadPool(2);
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 123;
};
Future<Integer> future = executor.submit(task);
try{
Integer result = future.get();
System.out.

println("Result: "+result);
}catch(InterruptedException |
ExecutionException e){
        e.

printStackTrace();
}finally{
        executor.

shutdown();
}
```

Handling timeout:

java

        try {
            Integer result = future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

Key Concepts

    Thread Class and Runnable Interface: Basics of creating and managing threads.
    Synchronization and Locks: Ensuring safe access to shared resources.
    Executor Framework: Efficiently managing multiple threads and tasks.
    Concurrent Collections: Collections designed for concurrent access.
    Future and Callable: Managing asynchronous computations and results.

This high-level overview covers essential concepts and practical examples to help you start working with concurrency and
multithreading in Java.

## Singleton

Implementing the Singleton Pattern

Singleton pattern ensures a class has only one instance and provides a global point of access to it.
Common approaches: **Eager Initialization and Lazy Initialization.**

### Eager Initialization

1. [ ] Instance is created when the class is loaded.
2. [ ] Suitable for instances that are not resource-intensive or guaranteed to be used during the application's
   lifetime.

**Implementation:** Make the class final to prevent inheritance.
Private constructor to prevent instantiation from outside the class.
Static instance created at the time of class loading.

```java
public final class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {
        // Private constructor to prevent instantiation
    }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }

}
```

### Lazy Initialization

1. [ ] Instance is created only when needed.
2. [ ] Efficient for resource-intensive instances or instances not guaranteed to be used.

**Implementation:** Private constructor to prevent instantiation from outside the class.
Synchronized method to ensure thread safety.
Double-checked locking to minimize synchronization overhead.

```java
public final class LazySingleton {
    // Volatile variable to ensure visibility of changes to instance across threads
    private static volatile LazySingleton instance;

    // Private constructor prevents instantiation from other classes
    private LazySingleton() {
        // Prevent reflection from creating a second instance
        if (instance != null) {
            throw new IllegalStateException("Instance already initialized");
        }
    }

    // Method to return the singleton instance
    public static LazySingleton getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (LazySingleton.class) { // Synchronize on the class object
                if (instance == null) { // Second check (with locking)
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
```

Lazy Singleton with Double-Checked Locking:

1. The instance is created only when it is first requested by calling the **getInstance** method.
2. The **volatile** keyword ensures that multiple threads handle the instance variable correctly when it is being
   initialized to the LazySingleton instance.
3. The **double-checked locking mechanism** is used to reduce the overhead of acquiring a lock by first checking if the
   instance is already created before entering the synchronized block.
4. Inside the **synchronized** block, another check is performed to ensure that the instance is still null before
   creating the singleton instance. This ensures that only one thread initializes the instance.

Key Points

    Eager Initialization:
        Simple and straightforward.
        Instance created at class loading time.
        Suitable for non-resource-intensive instances.

    Lazy Initialization:
        Instance created only when needed.
        Uses synchronized block with double-checked locking to ensure thread safety.
        More code but efficient for resource-intensive instances.

    Thread Safety:
        Eager initialization is inherently thread-safe due to class loading guarantees.
        Lazy initialization requires explicit synchronization to ensure only one instance is created in a multithreaded environment.

    Advantages and Drawbacks:
        Eager Initialization:
            Advantages: Simple, thread-safe by default.
            Drawbacks: Instance created even if never used.
        Lazy Initialization:
            Advantages: Instance created on demand, resource-efficient.
            Drawbacks: More complex implementation, potential performance hit due to synchronization.

Understanding the benefits and drawbacks of each approach helps in choosing the best implementation for a specific use
case.