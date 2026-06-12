---
title: "Room Database Migrations and CRUD Complete Guide"
date: 2026-06-12
slug: "room-database-migrations-crud-complete-guide"
tags: [ "Notes", "Android", "Room", "SQLite", "Migrations", "CRUD", "Database" ]
summary: "Personal Android database notes covering Room migrations, schema upgrades, destructive migration, CRUD implementation with Entity, DAO, Database, Repository, ViewModel, and migration testing strategies."
categories: Notes
readTime: 20
---

# Room Database Migrations & CRUD — Complete Advanced Guide

This guide covers **Room migrations + full CRUD implementation** in a real-world Android
architecture.  
Perfect for **interview prep** and **production-ready understanding**.

---

## 📑 Table of Contents

- [Room Database Migrations \& CRUD — Complete Advanced Guide](#room-database-migrations--crud--complete-advanced-guide)
    - [📑 Table of Contents](#-table-of-contents)
    - [What Are Room Migrations?](#what-are-room-migrations)
    - [When Is Migration Required?](#when-is-migration-required)
    - [Migration Basics](#migration-basics)
    - [Adding a Column](#adding-a-column)
    - [Removing a Column (Tricky)](#removing-a-column-tricky)
    - [Which column did we drop here?](#which-column-did-we-drop-here)
        - [Original table (assumed before migration)](#original-table-assumed-before-migration)
        - [New table to be created in migration](#new-table-to-be-created-in-migration)
    - [Why were they dropped?](#why-were-they-dropped)
    - [What actually happens step-by-step](#what-actually-happens-step-by-step)
    - [⚠️ Interview Trap (Important)](#️-interview-trap-important)
    - [Renaming a Column](#renaming-a-column)
    - [Changing Column Type](#changing-column-type)
    - [Adding a New Table](#adding-a-new-table)
    - [Registering Migrations](#registering-migrations)
        - [📍 Where do we add this migration code?](#-where-do-we-add-this-migration-code)
            - [🗂 **`DatabaseModule.kt` (Hilt module)**](#-databasemodulekt-hilt-module)
        - [✅ Correct File: `DatabaseModule.kt`](#-correct-file-databasemodulekt)
        - [🧠 Why here?](#-why-here)
        - [🔄 When does migration actually run?](#-when-does-migration-actually-run)
    - [❌ Where NOT to put it](#-where-not-to-put-it)
    - [🎯 Interview-Ready Answer](#-interview-ready-answer)
    - [Destructive Migration (When \& Why)](#destructive-migration-when--why)
- [Room CRUD Implementation (Complete)](#room-crud-implementation-complete)
    - [Entity](#entity)
    - [DAO](#dao)
    - [Database](#database)
    - [Repository](#repository)
    - [ViewModel](#viewmodel)
    - [Testing Migrations](#testing-migrations)
    - [Real-World Migration Strategy](#real-world-migration-strategy)
    - [Final Interview Summary](#final-interview-summary)

---

## What Are Room Migrations?

Room migrations allow your app to **upgrade the database schema without losing user data** when you
release a new app version.

---

## When Is Migration Required?

| Change             | Migration Needed |
|--------------------|------------------|
| Add column         | ✅                |
| Remove column      | ✅                |
| Rename column      | ✅                |
| Change column type | ✅                |
| Add table          | ✅                |
| Rename table       | ✅                |

---

## Migration Basics

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // SQL commands
    }
}
```

---

## Adding a Column

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE beerentity ADD COLUMN rating INTEGER NOT NULL DEFAULT 0"
        )
    }
}
```

---

## Removing a Column (Tricky)

SQLite doesn’t support DROP COLUMN — You must recreate the table.

**Steps:**

1. Create new table
2. Copy old data
3. Drop old table
4. Rename new table

<div id="deletecolumn"></div>
<details>
<summary><b>🔴 <font color="red">Removing a Column</font> 🔴</b></summary>
<br>
<blockquote>

## Which column did we drop here?

### Original table (assumed before migration)

```kotlin
@Entity
data class BeerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val tagline: String,
    val description: String
)
```

### New table to be created in migration

```sql
CREATE TABLE beerentity_new (
    id INTEGER PRIMARY KEY NOT NULL,
    name TEXT NOT NULL
)
```

👉 **Dropped columns:**

* `tagline`
* `description`

Only these columns are preserved:

* `id`
* `name`

---

## Why were they dropped?

Because in SQLite:

❌ There is **no** `ALTER TABLE DROP COLUMN`

So to remove columns:

1. Create a new table **without** those columns
2. Copy only the needed columns
3. Delete old table
4. Rename new table

This migration is effectively saying:

> “From version 2 → 3, we no longer need `tagline` and `description`.”

---

## What actually happens step-by-step

```sql
CREATE TABLE beerentity_new (...)
```

➡ New schema (only id, name)

```sql
INSERT INTO beerentity_new (id, name)
SELECT id, name FROM beerentity
```

➡ Copies **only these two columns**

```sql
DROP TABLE beerentity
```

➡ Old table (with extra columns) is deleted

```sql
ALTER TABLE beerentity_new RENAME TO beerentity
```

➡ New table becomes the main table

---

## ⚠️ Interview Trap (Important)

If you forget to include a column in the `INSERT INTO … SELECT …` statement:

👉 **That column’s data is permanently lost**

So migrations must be written **very carefully**.

> “In this migration, we dropped the `tagline` and `description` columns by recreating the table
> without them, because SQLite doesn’t support dropping columns directly.”

> [⬆️ Back to Top / Close](#deletecolumn)
</blockquote>
</details>

```kotlin
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE beerentity_new (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL)")
        db.execSQL("INSERT INTO beerentity_new (id, name) SELECT id, name FROM beerentity")
        db.execSQL("DROP TABLE beerentity")
        db.execSQL("ALTER TABLE beerentity_new RENAME TO beerentity")
    }
}
```

---

## Renaming a Column

Also requires table recreation using same method as above.

---

## Changing Column Type

Same recreate + copy strategy, with conversion if needed.

---

## Adding a New Table

```kotlin
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY NOT NULL, beerId INTEGER NOT NULL)")
    }
}
```

---

## Registering Migrations

```kotlin
Room.databaseBuilder(context, BeerDatabase::class.java, "beers.db")
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
    .build()
```

<div id="registeringmigration"></div>
<details>
<summary><b>🔴 <font color="red">📍 Where do we add this migration code?</font> 🔴</b></summary>
<br>
<blockquote>

### 📍 Where do we add this migration code?

```kotlin
Room.databaseBuilder(context, BeerDatabase::class.java, "beers.db")
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
    .build()
```

👉 This goes in the place **where your Room database instance is created**.

In modern apps, that is usually:

#### 🗂 **`DatabaseModule.kt` (Hilt module)**

Because we want:

* One single DB instance (Singleton)
* Injected everywhere

---

### ✅ Correct File: `DatabaseModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(
        @ApplicationContext context: Context
    ): BeerDatabase {

        return Room.databaseBuilder(
            context,
            BeerDatabase::class.java,
            "beers.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // 👈 HERE
            .build()
    }

    @Provides
    fun provideBeerDao(db: BeerDatabase): BeerDao = db.dao
}
```

---

### 🧠 Why here?

Because:

* Room runs migrations **when the database is opened**
* This builder is what creates/opens the DB
* So this is where Room checks the version and applies migrations

---

### 🔄 When does migration actually run?

Room compares:

| Installed DB Version | App DB Version | Action                       |
|----------------------|----------------|------------------------------|
| 1                    | 4              | Run 1→2, 2→3, 3→4 migrations |

This happens automatically when:

```kotlin
Room.databaseBuilder(...).build()
```

is executed and the DB is first accessed.

---

## ❌ Where NOT to put it

🚫 Not in DAO
🚫 Not in Repository
🚫 Not in ViewModel
🚫 Not in Activity

Because migration is a **database configuration concern**, not a UI or data logic concern.

---

## 🎯 Interview-Ready Answer

> “Room migrations are registered where the database is built — typically inside a Hilt
> DatabaseModule using Room.databaseBuilder(). Room automatically runs migrations when opening the
> database if the schema version has changed.”

### After i migrate in the next release and update should i remove that migration code because i already migrated??

Short answer:

> ❌ **NO — you should NOT remove old migrations**

Let’s break down why 👇

---

## 🧠 Why migrations must stay forever

Users don’t all update your app version-by-version.

### Example release history

| App Version | DB Version |
|-------------|------------|
| v1          | DB v1      |
| v2          | DB v2      |
| v3          | DB v3      |
| v4 (latest) | DB v4      |

Now imagine a user:

👉 Installs **v1**
👉 Skips v2 & v3
👉 Updates directly to **v4**

Room must run:

```
1 → 2
2 → 3
3 → 4
```

If you deleted `MIGRATION_1_2` or `MIGRATION_2_3`, the app will crash.

---

## ❌ What happens if you remove old migration

Room will throw:

```
IllegalStateException:
A migration from 1 to 4 was required but not found.
```

And the app **won’t start**.

---

## 📦 Think of migrations like version history

Each migration is a **step in a ladder**.

If you remove a middle step, users stuck on older versions can’t climb to the latest schema.

---

## ✅ What you SHOULD do

✔ Keep **all migrations** from the first public release
✔ Chain them like:

```kotlin
.addMigrations(
    MIGRATION_1_2,
    MIGRATION_2_3,
    MIGRATION_3_4,
    MIGRATION_4_5
)
```

---

## 🧹 When CAN you remove old migrations?

Only if:

1. You bump DB version and
2. Use destructive migration intentionally:

```kotlin
.fallbackToDestructiveMigration()
```

But this **deletes user data** — usually unacceptable in production.

---

## 🏢 Real-world industry practice

| Scenario                             | Action                      |
|--------------------------------------|-----------------------------|
| Public production app                | Keep all migrations forever |
| Internal app with no data importance | Can use destructive         |
| Major DB redesign                    | Create new DB name instead  |

---

## 🎯 Interview-Ready Answer

> “Migrations must be kept permanently because users can skip app versions. Room needs the full
> migration path from any older version to the latest. Removing old migrations will crash the app
> for users updating from older builds.”

---

> [⬆️ Back to Top / Close](#registeringmigration)
</blockquote>
</details>

---

## Destructive Migration (When & Why)

```kotlin
.fallbackToDestructiveMigration()
```

Deletes DB — only safe in development.

---

# Room CRUD Implementation (Complete)

## Entity

```kotlin
@Entity(tableName = "beerentity")
data class BeerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val tagline: String,
    val description: String
)
```

---

## DAO

```kotlin
@Dao
interface BeerDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer: BeerEntity)

    // READ (single)
    @Query("SELECT * FROM beerentity WHERE id = :id")
    suspend fun getBeerById(id: Int): BeerEntity?

    // READ (all)
    @Query("SELECT * FROM beerentity")
    fun observeAllBeers(): Flow<List<BeerEntity>>

    // UPDATE
    @Update
    suspend fun updateBeer(beer: BeerEntity)

    // DELETE (single)
    @Delete
    suspend fun deleteBeer(beer: BeerEntity)

    // DELETE (all)
    @Query("DELETE FROM beerentity")
    suspend fun clearAll()
}
```

---

## Database

```kotlin
@Database(entities = [BeerEntity::class], version = 4)
abstract class BeerDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao
}
```

---

## Repository

```kotlin
class BeerRepository @Inject constructor(private val dao: BeerDao) {

    val beersFlow = dao.observeAllBeers()

    suspend fun addBeer(beer: BeerEntity) = dao.insertBeer(beer)

    suspend fun updateBeer(beer: BeerEntity) = dao.updateBeer(beer)

    suspend fun deleteBeer(beer: BeerEntity) = dao.deleteBeer(beer)
}
```

---

## ViewModel

```kotlin
@HiltViewModel
class BeerViewModel @Inject constructor(private val repo: BeerRepository) : ViewModel() {

    val beers = repo.beersFlow.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addBeer(beer: BeerEntity) = viewModelScope.launch {
        repo.addBeer(beer)
    }
}
```

---

## Testing Migrations

Use `MigrationTestHelper` from Room testing library to validate schema.

---

## Real-World Migration Strategy

| Stage               | Strategy                       |
|---------------------|--------------------------------|
| Dev                 | Destructive OK                 |
| Production          | Always migrate                 |
| Major schema change | Write recreate-table migration |
| Sensitive data      | Backup before migration        |

---

## Final Interview Summary

> “Room migrations safely upgrade the database without data loss. Simple changes use ALTER TABLE,
> while complex changes require recreating the table and copying data. CRUD operations are handled
> via DAO, and Flow makes Room reactive for UI updates.”
