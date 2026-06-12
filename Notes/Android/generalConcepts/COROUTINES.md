## Job

A cancelable unit of work, such as one created with the launch() function.

## CoroutineScope

Functions used to create new coroutines such as launch() and async() extend CoroutineScope.

## Dispatcher

Determines the thread the coroutine will use. The Main dispatcher will always run coroutines on the main thread, while
dispatchers like Default, IO, or Unconfined will use other threads.