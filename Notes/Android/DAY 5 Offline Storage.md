---
title: "Android Offline Storage Notes"
date: 2026-06-12
slug: "android-offline-storage-notes"
tags: [ "Notes", "Android", "Offline Storage", "SQLite", "Room", "File Storage", "Caching" ]
summary: "Personal Android notes covering offline storage options such as SQLite, SharedPreferences, file storage, Room, caching, data syncing, security considerations, and Kotlin examples for saving and retrieving local data."
categories: Notes
readTime: 12
---

# Offline Storage

#### I as android developer, what I should know about offline storage?

As an Android developer, understanding offline storage is crucial for creating robust and efficient applications. Here
are some key aspects you should know:

1. **SQLite Database**: Knowledge of implementing and managing local databases using SQLite, which allows for efficient
   storage and retrieval of data on the device even when offline.

2. **Shared Preferences**: Understanding how to utilize shared preferences for storing small amounts of data, such as
   user preferences or settings, locally on the device.

3. **File Storage**: Familiarity with storing files locally on the device's internal or external storage, including
   methods for managing file directories and permissions.

4. **Room Persistence Library**: Experience with Room, an SQLite object mapping library that provides an abstraction
   layer over SQLite, making it easier to work with databases and perform CRUD operations.

5. **Data Caching**: Implementing data caching mechanisms to store frequently accessed data locally, reducing the need
   for repeated network requests and improving app performance.

6. **Data Syncing**: Strategies for syncing local data with remote servers when the device is back online, ensuring
   consistency and data integrity across devices.

7. **Security Considerations**: Awareness of security best practices for storing sensitive data locally, such as
   encryption and secure storage techniques, to protect user information from unauthorized access.

By mastering these aspects of offline storage, you can build Android applications that provide seamless user
experiences, even in scenarios where network connectivity is limited or unavailable.

#### Give example for `SQLite` Database using Kotlin code, just show example to save a string and an Int Value.

Sure, here's an example of how you can use SQLite in Android with Kotlin to save a string and an integer value:

```kotlin
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SampleDatabase"
        private const val TABLE_NAME = "SampleTable"
        private const val COLUMN_STRING = "sampleString"
        private const val COLUMN_INT = "sampleInt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_STRING TEXT,"
                + "$COLUMN_INT INTEGER)"
                )
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(sampleString: String, sampleInt: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_STRING, sampleString)
        contentValues.put(COLUMN_INT, sampleInt)
        return db.insert(TABLE_NAME, null, contentValues)
    }
}

// Usage example
fun main() {
    val context: Context = TODO() // Replace TODO() with your application's context
    val dbHelper = DatabaseHelper(context)

    // Inserting data
    val insertedRowId = dbHelper.insertData("Hello, SQLite!", 42)
    if (insertedRowId != -1L) {
        println("Data inserted successfully with ID: $insertedRowId")
    } else {
        println("Failed to insert data")
    }
}
```

In this example:

- `DatabaseHelper` class is responsible for creating and managing the SQLite database.
- `onCreate()` method is called when the database is created for the first time, where you define the structure of your
  database table.
- `insertData()` method inserts data into the database table.
- Usage example demonstrates how to create a `DatabaseHelper` instance and insert data into the SQLite database.
  Replace `TODO()` with your application's context.

##### how to get the data from it in the same way ?

To retrieve data from the SQLite database, you can add a method in the `DatabaseHelper` class to query the database and
return the results. Here's an example of how you can modify the `DatabaseHelper` class to include a method for
retrieving data:

```kotlin
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SampleDatabase"
        private const val TABLE_NAME = "SampleTable"
        private const val COLUMN_STRING = "sampleString"
        private const val COLUMN_INT = "sampleInt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_STRING TEXT,"
                + "$COLUMN_INT INTEGER)"
                )
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(sampleString: String, sampleInt: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_STRING, sampleString)
        contentValues.put(COLUMN_INT, sampleInt)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllData(): ArrayList<Pair<String, Int>> {
        val dataList = ArrayList<Pair<String, Int>>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val sampleString = it.getString(it.getColumnIndex(COLUMN_STRING))
                    val sampleInt = it.getInt(it.getColumnIndex(COLUMN_INT))
                    dataList.add(Pair(sampleString, sampleInt))
                } while (it.moveToNext())
            }
        }
        return dataList
    }
}
```

In this modified `DatabaseHelper` class:

- Added a `getAllData()` method that queries the database and retrieves all rows from the `SampleTable`. It returns
  an `ArrayList<Pair<String, Int>>` containing the retrieved data.
- The `Cursor` object is used to iterate through the query results, and data is extracted from it.

Usage example to retrieve data:

```kotlin
fun main() {
    val context: Context = TODO() // Replace TODO() with your application's context
    val dbHelper = DatabaseHelper(context)

    // Retrieving data
    val dataList = dbHelper.getAllData()
    for ((sampleString, sampleInt) in dataList) {
        println("Sample String: $sampleString, Sample Int: $sampleInt")
    }
}
```

Replace `TODO()` with your application's context in the main function.

---

#### File Storage Example

Certainly! Below is an example in Kotlin demonstrating how to store a file locally on the device's internal storage:

```kotlin
import android.content.Context
import java.io.File
import java.io.FileOutputStream

class FileManager(private val context: Context) {

    fun saveToFile(fileName: String, data: String): Boolean {
        return try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFileContents(fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            val fileContents = file.readText()
            fileContents
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// Usage example
fun main() {
    val context: Context = TODO() // Replace TODO() with your application's context
    val fileManager = FileManager(context)

    val fileName = "example.txt"
    val data = "This is an example text."

    // Save data to file
    if (fileManager.saveToFile(fileName, data)) {
        println("Data saved to file successfully.")
    } else {
        println("Failed to save data to file.")
    }

    // Retrieve data from file
    val retrievedData = fileManager.getFileContents(fileName)
    if (retrievedData != null) {
        println("Retrieved data from file: $retrievedData")
    } else {
        println("Failed to retrieve data from file.")
    }
}
```

In this example:

- `FileManager` class manages file operations such as saving and retrieving data.
- `saveToFile()` function saves the provided data to a file in the internal storage of the app.
- `getFileContents()` function retrieves the contents of the specified file from the internal storage.
- Usage example demonstrates how to use `FileManager` to save data to a file and retrieve it afterward.

Make sure to replace `TODO()` with your application's context in the `main()` function. Additionally, remember to handle
permissions for accessing internal storage if your application targets Android API level 23 or higher.

##### What else functionalities i can add here? what else i can do with file manager ?

Your `FileManager` class already provides basic functionalities for saving data to files and retrieving data from files.
However, you can extend its functionality by adding additional methods to enhance file management capabilities. Here are
some suggestions:

1. **Delete File**: Add a method to delete a file from the device's storage.

```kotlin
fun deleteFile(fileName: String): Boolean {
    return try {
        val file = File(context.filesDir, fileName)
        file.delete()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
```

2. **Check File Existence**: Implement a method to check if a file exists in the device's storage.

```kotlin
fun doesFileExist(fileName: String): Boolean {
    return File(context.filesDir, fileName).exists()
}
```

3. **List Files**: Create a method to list all files stored in the app's internal storage directory.

```kotlin
fun listFiles(): Array<String> {
    return context.fileList()
}
```

4. **Move or Rename File**: Add functionality to move or rename a file within the device's storage.

```kotlin
fun moveFile(oldFileName: String, newFileName: String): Boolean {
    return try {
        val oldFile = File(context.filesDir, oldFileName)
        val newFile = File(context.filesDir, newFileName)
        oldFile.renameTo(newFile)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
```

5. **File Size**: Implement a method to get the size of a file in bytes.

```kotlin
fun getFileSize(fileName: String): Long {
    val file = File(context.filesDir, fileName)
    return file.length()
}
```

6. **File Metadata**: Extend functionality to retrieve metadata such as last modified time, permissions, etc., for a
   file.

```kotlin
fun getFileMetadata(fileName: String): FileMetadata? {
    return try {
        val file = File(context.filesDir, fileName)
        FileMetadata(
            lastModified = file.lastModified(),
            isDirectory = file.isDirectory,
            size = file.length(),
            permissions = file.canRead() to file.canWrite()
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

data class FileMetadata(
    val lastModified: Long,
    val isDirectory: Boolean,
    val size: Long,
    val permissions: Pair<Boolean, Boolean>
)
```

These additional functionalities enhance the versatility of your `FileManager` class, allowing for more comprehensive
file management within your application. You can choose to implement all or select the ones that align with your
application's requirements.