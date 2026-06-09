---
title: "SQL Roadmap for Developers: From Basics to Advanced Queries"
date: 2026-06-08
slug: "sql-roadmap-developers-basics-advanced"
tags: [ "SQL", "Database", "Queries", "Indexes", "Transactions" ]
summary: "Complete SQL learning guide for developers covering CRUD operations, joins, grouping, subqueries, indexes, transactions, normalization, and real-world database optimization."
categories: Database
readTime: 21
---

# SQL Roadmap for Developers: From Basics to Advanced Queries

SQL is one of those skills that quietly follows a developer everywhere.

If you are building a backend API, you will need SQL.  
If you are creating reports, dashboards, or admin panels, you will need SQL.  
If you are debugging production data issues, you will need SQL.  
If you are preparing for software engineering interviews, you will definitely need SQL.

This guide is written as a practical learning roadmap. The goal is not only to memorize commands, but to understand what
SQL can do, how queries are usually written, and what topics a developer should study step by step.

---

## Table of Contents

1. [What is SQL?](#what-is-sql)
2. [Why Developers Should Learn SQL](#why-developers-should-learn-sql)
3. [SQL and Relational Databases](#sql-and-relational-databases)
4. [Basic SQL Rules to Remember](#basic-sql-rules-to-remember)
5. [Core SQL Command Categories](#core-sql-command-categories)
6. [Creating Databases and Tables](#creating-databases-and-tables)
7. [Inserting Data](#inserting-data)
8. [Reading Data with SELECT](#reading-data-with-select)
9. [Filtering Data with WHERE](#filtering-data-with-where)
10. [Sorting and Limiting Results](#sorting-and-limiting-results)
11. [Working with NULL Values](#working-with-null-values)
12. [Updating and Deleting Data](#updating-and-deleting-data)
13. [Aggregate Functions](#aggregate-functions)
14. [GROUP BY and HAVING](#group-by-and-having)
15. [SQL Joins](#sql-joins)
16. [Subqueries](#subqueries)
17. [Common Table Expressions](#common-table-expressions)
18. [CASE Statements](#case-statements)
19. [String, Date, and Numeric Functions](#string-date-and-numeric-functions)
20. [Indexes](#indexes)
21. [Constraints](#constraints)
22. [Normalization](#normalization)
23. [Views](#views)
24. [Transactions](#transactions)
25. [Stored Procedures and Functions](#stored-procedures-and-functions)
26. [SQL for Backend Developers](#sql-for-backend-developers)
27. [SQL Performance Tips](#sql-performance-tips)
28. [Common SQL Mistakes](#common-sql-mistakes)
29. [SQL Practice Questions](#sql-practice-questions)
30. [How to Study SQL Properly](#how-to-study-sql-properly)

---

## What is SQL?

SQL stands for **Structured Query Language**.

It is used to communicate with relational databases. With SQL, we can create tables, insert data, read data, update
records, delete records, filter results, join multiple tables, create reports, and manage database structures.

In simple words:

> SQL helps us ask questions from a database and manage the data stored inside it.

Example:

```sql
SELECT name, email
FROM users
WHERE status = 'ACTIVE';
```

This query asks the database:

> Give me the name and email of users whose status is active.

---

## Why Developers Should Learn SQL

SQL is useful for many types of developers.

For a **backend developer**, SQL is needed to design tables, write queries, debug API issues, and optimize database
performance.

For an **Android developer**, SQL knowledge helps when working with local databases like SQLite or Room. Even if Room
generates some queries, understanding SQL makes debugging and database design much easier.

For a **cloud or DevOps-focused developer**, SQL helps when working with managed databases like Amazon RDS, PostgreSQL,
MySQL, Aurora, or SQL Server.

For a **data-focused role**, SQL is one of the most important skills for reporting, analytics, filtering, aggregation,
and business insights.

---

## SQL and Relational Databases

A relational database stores data in tables.

A table contains rows and columns.

Example: `users` table

| id | name   | email              | status   |
|---:|--------|--------------------|----------|
|  1 | ella   | ella@example.com   | ACTIVE   |
|  2 | Rahul  | rahul@example.com  | INACTIVE |
|  3 | austin | austin@example.com | ACTIVE   |

Here:

- `users` is the table.
- `id`, `name`, `email`, and `status` are columns.
- Each record is a row.

Popular relational databases include:

- MySQL
- PostgreSQL
- SQLite
- SQL Server
- Oracle Database
- MariaDB

---

## Basic SQL Rules to Remember

SQL keywords are usually written in uppercase for readability.

```sql
SELECT *
FROM users;
```

But SQL keywords are generally not case-sensitive:

```sql
select *
from users;
```

Both can work, but uppercase keywords are easier to read.

However, string values can be case-sensitive depending on the database and collation.

```sql
WHERE name = 'ella'
```

may not always behave the same as:

```sql
WHERE name = 'ella'
```

A semicolon is commonly used to end a SQL statement.

```sql
SELECT *
FROM users;
```

Some tools require it, some tools do not, but it is a good habit to use it.

---

## Core SQL Command Categories

SQL commands are commonly grouped into different categories.

| Category | Meaning                    | Examples                              |
|----------|----------------------------|---------------------------------------|
| DDL      | Defines database structure | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML      | Modifies data              | `INSERT`, `UPDATE`, `DELETE`          |
| DQL      | Reads data                 | `SELECT`                              |
| DCL      | Controls access            | `GRANT`, `REVOKE`                     |
| TCL      | Manages transactions       | `COMMIT`, `ROLLBACK`, `SAVEPOINT`     |

As a developer, you should be comfortable with all of them, but `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `CREATE TABLE`,
`ALTER TABLE`, `JOIN`, `GROUP BY`, and indexes are especially important.

---

## Creating Databases and Tables

A database contains tables. A table stores structured data.

Example:

```sql
CREATE
DATABASE stock_app;
```

After creating a database, we can create a table.

```sql
CREATE TABLE users
(
    id         INT PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    status     VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);
```

This table has:

- `id` as the primary key
- `name` as a required field
- `email` as a unique required field
- `status` with a default value
- `created_at` with the current timestamp

---

## Inserting Data

To insert a new row:

```sql
INSERT INTO users (id, name, email, status)
VALUES (1, 'ella', 'ella@example.com', 'ACTIVE');
```

To insert multiple rows:

```sql
INSERT INTO users (id, name, email, status)
VALUES (2, 'Rahul', 'rahul@example.com', 'INACTIVE'),
       (3, 'austin', 'austin@example.com', 'ACTIVE');
```

A good practice is to mention column names explicitly. This makes the query safer and easier to understand.

---

## Reading Data with SELECT

The `SELECT` statement is used to read data.

Read all columns:

```sql
SELECT *
FROM users;
```

Read specific columns:

```sql
SELECT name, email
FROM users;
```

In real projects, avoid using `SELECT *` everywhere. It is okay for quick testing, but in application code, selecting
only required columns is usually better.

---

## Filtering Data with WHERE

The `WHERE` clause filters rows based on a condition.

```sql
SELECT *
FROM users
WHERE status = 'ACTIVE';
```

Common comparison operators:

| Operator      | Meaning               |
|---------------|-----------------------|
| `=`           | Equal                 |
| `<>` or `!=`  | Not equal             |
| `>`           | Greater than          |
| `<`           | Less than             |
| `>=`          | Greater than or equal |
| `<=`          | Less than or equal    |
| `BETWEEN`     | Between a range       |
| `IN`          | Match multiple values |
| `LIKE`        | Pattern matching      |
| `IS NULL`     | Check NULL values     |
| `IS NOT NULL` | Check non-NULL values |

Example with `AND`:

```sql
SELECT *
FROM users
WHERE status = 'ACTIVE'
  AND name = 'ella';
```

Example with `OR`:

```sql
SELECT *
FROM users
WHERE status = 'ACTIVE'
   OR status = 'PENDING';
```

Example with `IN`:

```sql
SELECT *
FROM users
WHERE status IN ('ACTIVE', 'PENDING');
```

Example with `BETWEEN`:

```sql
SELECT *
FROM orders
WHERE total_amount BETWEEN 100 AND 500;
```

---

## Pattern Matching with LIKE

`LIKE` is used to search using patterns.

| Query         | Meaning                                         |
|---------------|-------------------------------------------------|
| `LIKE 'A%'`   | Starts with A                                   |
| `LIKE '%a'`   | Ends with a                                     |
| `LIKE '%or%'` | Contains or                                     |
| `LIKE '_r%'`  | Second character is r                           |
| `LIKE 'A_%'`  | Starts with A and has at least two characters   |
| `LIKE 'A__%'` | Starts with A and has at least three characters |

Example:

```sql
SELECT *
FROM customers
WHERE customer_name LIKE 'A%';
```

This returns customers whose names start with `A`.

---

## Sorting and Limiting Results

`ORDER BY` sorts query results.

Ascending order:

```sql
SELECT *
FROM users
ORDER BY name ASC;
```

Descending order:

```sql
SELECT *
FROM users
ORDER BY created_at DESC;
```

Multiple sort conditions:

```sql
SELECT *
FROM users
ORDER BY status ASC, name DESC;
```

Limiting records depends on the database.

MySQL and PostgreSQL:

```sql
SELECT *
FROM users LIMIT 10;
```

SQL Server:

```sql
SELECT TOP 10 *
FROM users;
```

Oracle:

```sql
SELECT *
FROM users
    FETCH FIRST 10 ROWS ONLY;
```

---

## Working with NULL Values

`NULL` means missing or unknown value.

You should not check NULL using `=`.

Wrong:

```sql
SELECT *
FROM users
WHERE phone = NULL;
```

Correct:

```sql
SELECT *
FROM users
WHERE phone IS NULL;
```

For non-NULL values:

```sql
SELECT *
FROM users
WHERE phone IS NOT NULL;
```

This is a common beginner mistake, so remember it clearly.

---

## Updating and Deleting Data

Use `UPDATE` to modify existing records.

```sql
UPDATE users
SET status = 'INACTIVE'
WHERE id = 1;
```

Very important:

> Always check the `WHERE` clause before running an `UPDATE`.

If you forget `WHERE`, all rows may be updated.

Dangerous:

```sql
UPDATE users
SET status = 'INACTIVE';
```

Use `DELETE` to remove records.

```sql
DELETE
FROM users
WHERE id = 1;
```

Again, be careful.

Dangerous:

```sql
DELETE
FROM users;
```

This can delete all rows from the table.

A safer habit is to first run a `SELECT` with the same condition:

```sql
SELECT *
FROM users
WHERE id = 1;
```

Then run the `UPDATE` or `DELETE` after confirming the affected rows.

---

## Aggregate Functions

Aggregate functions perform calculations on multiple rows.

| Function  | Purpose             |
|-----------|---------------------|
| `COUNT()` | Counts rows         |
| `SUM()`   | Adds values         |
| `AVG()`   | Calculates average  |
| `MIN()`   | Finds minimum value |
| `MAX()`   | Finds maximum value |

Examples:

```sql
SELECT COUNT(*)
FROM users;
```

```sql
SELECT AVG(total_amount)
FROM orders;
```

```sql
SELECT SUM(total_amount)
FROM orders;
```

```sql
SELECT MIN(total_amount), MAX(total_amount)
FROM orders;
```

---

## GROUP BY and HAVING

`GROUP BY` is used when we want results grouped by a column.

Example: count users by status.

```sql
SELECT status, COUNT(*) AS total_users
FROM users
GROUP BY status;
```

Output:

| status   | total_users |
|----------|------------:|
| ACTIVE   |          20 |
| INACTIVE |           5 |
| PENDING  |           3 |

`HAVING` filters grouped results.

```sql
SELECT status, COUNT(*) AS total_users
FROM users
GROUP BY status
HAVING COUNT(*) > 5;
```

Difference between `WHERE` and `HAVING`:

| Clause   | Used For                      |
|----------|-------------------------------|
| `WHERE`  | Filters rows before grouping  |
| `HAVING` | Filters groups after grouping |

---

## SQL Joins

Joins are used to combine data from multiple tables.

Example tables:

`users`

| id | name  |
|---:|-------|
|  1 | ella  |
|  2 | Rahul |

`orders`

|  id | user_id | total_amount |
|----:|--------:|-------------:|
| 101 |       1 |          250 |
| 102 |       1 |          500 |
| 103 |       3 |          100 |

### INNER JOIN

Returns only matching records from both tables.

```sql
SELECT users.name, orders.total_amount
FROM users
         INNER JOIN orders
                    ON users.id = orders.user_id;
```

Result:

| name | total_amount |
|------|-------------:|
| ella |          250 |
| ella |          500 |

### LEFT JOIN

Returns all records from the left table and matching records from the right table.

```sql
SELECT users.name, orders.total_amount
FROM users
         LEFT JOIN orders
                   ON users.id = orders.user_id;
```

This returns all users, even users without orders.

### RIGHT JOIN

Returns all records from the right table and matching records from the left table.

```sql
SELECT users.name, orders.total_amount
FROM users
         RIGHT JOIN orders
                    ON users.id = orders.user_id;
```

### FULL OUTER JOIN

Returns records when there is a match in either table.

```sql
SELECT users.name, orders.total_amount
FROM users
         FULL OUTER JOIN orders
                         ON users.id = orders.user_id;
```

Note: Some databases, like MySQL, do not directly support `FULL OUTER JOIN`. You may need to combine `LEFT JOIN` and
`RIGHT JOIN` using `UNION`.

---

## Subqueries

A subquery is a query inside another query.

Example: find users who have placed orders.

```sql
SELECT *
FROM users
WHERE id IN (SELECT user_id
             FROM orders);
```

Example: find orders greater than the average order amount.

```sql
SELECT *
FROM orders
WHERE total_amount > (SELECT AVG(total_amount)
                      FROM orders);
```

Subqueries are useful, but sometimes joins or common table expressions can be more readable.

---

## Common Table Expressions

A Common Table Expression, or CTE, helps make complex queries easier to read.

```sql
WITH active_users AS (SELECT *
                      FROM users
                      WHERE status = 'ACTIVE')
SELECT *
FROM active_users;
```

CTEs are especially useful when a query has multiple steps.

Example:

```sql
WITH user_order_totals AS (SELECT user_id, SUM(total_amount) AS total_spent
                           FROM orders
                           GROUP BY user_id)
SELECT users.name, user_order_totals.total_spent
FROM users
         JOIN user_order_totals
              ON users.id = user_order_totals.user_id;
```

---

## CASE Statements

`CASE` is used to create conditional output.

```sql
SELECT name,
       status,
       CASE
           WHEN status = 'ACTIVE' THEN 'Allowed'
           WHEN status = 'INACTIVE' THEN 'Blocked'
           ELSE 'Review Needed'
           END AS access_status
FROM users;
```

This is useful for reports and dashboards.

---

## String, Date, and Numeric Functions

SQL has many built-in functions. Function names can differ between databases, but the ideas are similar.

### String Examples

```sql
SELECT UPPER(name)
FROM users;
```

```sql
SELECT LOWER(email)
FROM users;
```

```sql
SELECT LENGTH(name)
FROM users;
```

Some databases use `LEN()` instead of `LENGTH()`.

### Date Examples

```sql
SELECT CURRENT_DATE;
```

```sql
SELECT *
FROM orders
WHERE created_at >= CURRENT_DATE;
```

### Numeric Examples

```sql
SELECT ROUND(total_amount, 2)
FROM orders;
```

---

## Indexes

An index helps the database find rows faster.

Example:

```sql
CREATE INDEX idx_users_email
    ON users (email);
```

Indexes are useful for columns frequently used in:

- `WHERE`
- `JOIN`
- `ORDER BY`
- `GROUP BY`

But indexes are not free. They take extra storage and can slow down inserts and updates because the index also needs to
be maintained.

Good index candidate:

```sql
SELECT *
FROM users
WHERE email = 'ella@example.com';
```

Bad index candidate:

A column with very few unique values, such as `status`, may not always benefit from an index unless the table is large
and the query pattern needs it.

---

## Constraints

Constraints protect the quality of data.

| Constraint    | Purpose                      |
|---------------|------------------------------|
| `PRIMARY KEY` | Uniquely identifies each row |
| `FOREIGN KEY` | Links one table to another   |
| `UNIQUE`      | Prevents duplicate values    |
| `NOT NULL`    | Requires a value             |
| `DEFAULT`     | Provides default value       |
| `CHECK`       | Validates a condition        |

Example:

```sql
CREATE TABLE orders
(
    id           INT PRIMARY KEY,
    user_id      INT NOT NULL,
    total_amount DECIMAL(10, 2) CHECK (total_amount >= 0),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
```

---

## Normalization

Normalization is the process of organizing data to reduce duplication and improve consistency.

Bad design:

| order_id | customer_name | customer_email   | product_name |
|---------:|---------------|------------------|--------------|
|        1 | ella          | ella@example.com | Laptop       |
|        2 | ella          | ella@example.com | Mouse        |

Here, customer details are repeated.

Better design:

`customers`

| id | name | email            |
|---:|------|------------------|
|  1 | ella | ella@example.com |

`orders`

| id | customer_id |
|---:|------------:|
|  1 |           1 |
|  2 |           1 |

This structure reduces duplication and makes updates safer.

Common normalization levels:

- **1NF**: Each column should contain atomic values.
- **2NF**: Remove partial dependency.
- **3NF**: Remove transitive dependency.

For most developers, understanding practical 1NF, 2NF, and 3NF is enough.

---

## Views

A view is a saved query.

```sql
CREATE VIEW active_users AS
SELECT id, name, email
FROM users
WHERE status = 'ACTIVE';
```

Now you can query the view like a table:

```sql
SELECT *
FROM active_users;
```

Views are useful for:

- Hiding complex queries
- Improving readability
- Creating report-friendly data
- Restricting access to selected columns

---

## Transactions

A transaction is a group of SQL operations that should succeed or fail together.

Example: money transfer.

```sql
BEGIN;

UPDATE accounts
SET balance = balance - 100
WHERE id = 1;

UPDATE accounts
SET balance = balance + 100
WHERE id = 2;

COMMIT;
```

If something goes wrong:

```sql
ROLLBACK;
```

Transactions follow ACID properties:

| Property    | Meaning                                   |
|-------------|-------------------------------------------|
| Atomicity   | All operations succeed or none succeed    |
| Consistency | Data remains valid                        |
| Isolation   | Transactions do not interfere incorrectly |
| Durability  | Committed data is saved permanently       |

---

## Stored Procedures and Functions

Stored procedures are reusable SQL blocks stored in the database.

Example syntax differs by database, but the idea is:

```sql
CREATE PROCEDURE GetActiveUsers()
BEGIN
SELECT *
FROM users
WHERE status = 'ACTIVE';
END;
```

Stored procedures can be useful, but many modern backend teams prefer keeping business logic in the application layer
and using SQL mainly for data access. The right choice depends on the project and team standards.

---

## SQL for Backend Developers

Backend developers use SQL in many practical situations.

Common backend tasks:

- Design database tables
- Write queries for APIs
- Add indexes for performance
- Debug slow queries
- Handle pagination
- Manage transactions
- Create migrations
- Ensure data integrity

Example API query:

```sql
SELECT id, name, email
FROM users
WHERE status = 'ACTIVE'
ORDER BY created_at DESC LIMIT 20
OFFSET 0;
```

This is commonly used for pagination.

Example Spring Boot repository query:

```sql
SELECT *
FROM products
WHERE category = ?
ORDER BY created_at DESC;
```

When using Spring Data JPA or Hibernate, SQL may be generated automatically, but understanding SQL is still important
because you need to know what is happening under the hood.

---

## SQL for Android Developers

Android developers may use SQL through SQLite or Room.

Example SQLite table:

```sql
CREATE TABLE stock_items
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL,
    quantity    INTEGER NOT NULL,
    expiry_date TEXT
);
```

Example Room query:

```kotlin
@Query("SELECT * FROM stock_items WHERE quantity <= :lowStockLimit")
fun getLowStockItems(lowStockLimit: Int): Flow<List<StockItem>>
```

SQL helps Android developers understand:

- Local database design
- Room queries
- Filtering and sorting local data
- Debugging app data issues
- Writing migrations safely

---

## SQL Performance Tips

Good SQL is not only about getting the correct result. It should also perform well.

Important tips:

1. Select only the columns you need.

```sql
SELECT id, name
FROM users;
```

Instead of:

```sql
SELECT *
FROM users;
```

2. Add indexes to frequently searched columns.

```sql
CREATE INDEX idx_orders_user_id
    ON orders (user_id);
```

3. Avoid functions on indexed columns in `WHERE` when possible.

Less efficient:

```sql
SELECT *
FROM users
WHERE LOWER(email) = 'ella@example.com';
```

Better approach: store normalized email or use database-supported functional indexes if available.

4. Use pagination for large result sets.

```sql
SELECT *
FROM orders
ORDER BY created_at DESC LIMIT 20
OFFSET 0;
```

5. Check query plans.

PostgreSQL:

```sql
EXPLAIN
ANALYZE
SELECT *
FROM orders
WHERE user_id = 1;
```

MySQL:

```sql
EXPLAIN
SELECT *
FROM orders
WHERE user_id = 1;
```

---

## Common SQL Mistakes

### Forgetting WHERE in UPDATE

```sql
UPDATE users
SET status = 'INACTIVE';
```

This updates every row.

### Forgetting WHERE in DELETE

```sql
DELETE
FROM users;
```

This deletes every row.

### Using = NULL

```sql
WHERE phone = NULL
```

Use:

```sql
WHERE phone IS NULL
```

### Using SELECT * in production code everywhere

It may fetch unnecessary data and make APIs slower.

### Not understanding joins

Many wrong reports come from incorrect joins or duplicate rows after joins.

### Not using transactions for multi-step operations

If two related updates should succeed together, use a transaction.

---

## SQL Practice Questions

### Basic Level

1. Select all users from the `users` table.
2. Select only `name` and `email` from the `users` table.
3. Find users whose status is `ACTIVE`.
4. Find users whose name starts with `A`.
5. Sort users by `created_at` in descending order.
6. Count the total number of users.
7. Find users whose phone number is missing.

Example answer:

```sql
SELECT *
FROM users
WHERE phone IS NULL;
```

---

### Intermediate Level

1. Count users by status.
2. Find the total order amount for each user.
3. Find users who have placed at least one order.
4. Find users who have not placed any orders.
5. Get the top 5 highest-value orders.
6. Find the average order amount.
7. Join users and orders to show user name and order amount.

Example answer:

```sql
SELECT users.name, SUM(orders.total_amount) AS total_spent
FROM users
         JOIN orders
              ON users.id = orders.user_id
GROUP BY users.name;
```

---

### Advanced Level

1. Find users whose total order amount is greater than the average total order amount.
2. Use a CTE to calculate monthly sales.
3. Find duplicate emails in a users table.
4. Find the second highest salary from an employees table.
5. Write a transaction for transferring money between accounts.
6. Optimize a slow query using indexes.
7. Use `EXPLAIN` to understand a query plan.

Example: duplicate emails

```sql
SELECT email, COUNT(*) AS count
FROM users
GROUP BY email
HAVING COUNT (*) > 1;
```

Example: second highest salary

```sql
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (SELECT MAX(salary)
                FROM employees);
```

---

## Common Interview Questions

### What is the difference between WHERE and HAVING?

`WHERE` filters rows before grouping.  
`HAVING` filters groups after `GROUP BY`.

### What is the difference between INNER JOIN and LEFT JOIN?

`INNER JOIN` returns only matching rows from both tables.  
`LEFT JOIN` returns all rows from the left table and matching rows from the right table.

### What is a primary key?

A primary key uniquely identifies each row in a table.

### What is a foreign key?

A foreign key links one table to another table.

### What is an index?

An index is a database structure that helps queries find data faster, especially on large tables.

### What is normalization?

Normalization is organizing database tables to reduce duplication and improve consistency.

### What is a transaction?

A transaction is a group of operations that should succeed together or fail together.

---

## How to Study SQL Properly

A good SQL learning path should be practical.

### Step 1: Learn Basic Queries

Start with:

- `SELECT`
- `WHERE`
- `ORDER BY`
- `LIMIT`
- `INSERT`
- `UPDATE`
- `DELETE`

### Step 2: Learn Aggregation

Practice:

- `COUNT`
- `SUM`
- `AVG`
- `MIN`
- `MAX`
- `GROUP BY`
- `HAVING`

### Step 3: Learn Joins

Focus heavily on:

- `INNER JOIN`
- `LEFT JOIN`
- `RIGHT JOIN`
- `FULL OUTER JOIN`
- Self joins

Joins are one of the most important parts of SQL.

### Step 4: Learn Subqueries and CTEs

These help you write cleaner queries for complex problems.

### Step 5: Learn Database Design

Study:

- Primary keys
- Foreign keys
- Constraints
- Normalization
- Relationships
- Indexes

### Step 6: Practice Real Problems

Use sample tables like:

- `users`
- `orders`
- `products`
- `employees`
- `departments`
- `stock_items`

Write queries from real scenarios instead of only reading theory.

---

## Mini Project Idea: Inventory Database

If you want to practice SQL properly, create a small inventory database.

Tables:

```sql
CREATE TABLE categories
(
    id   INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE stock_items
(
    id              INT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    category_id     INT,
    quantity        INT          NOT NULL,
    low_stock_limit INT DEFAULT 5,
    expiry_date     DATE,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);
```

Practice queries:

```sql
SELECT *
FROM stock_items
WHERE quantity <= low_stock_limit;
```

```sql
SELECT categories.name, COUNT(stock_items.id) AS total_items
FROM categories
         LEFT JOIN stock_items
                   ON categories.id = stock_items.category_id
GROUP BY categories.name;
```

```sql
SELECT *
FROM stock_items
WHERE expiry_date <= CURRENT_DATE;
```

This kind of project makes SQL much easier to remember because it connects theory with a real app idea.

---

## Final Thoughts

SQL is not just a database language. It is a core developer skill.

You do not need to learn every advanced database feature on day one. Start with basic queries, then slowly move into
joins, grouping, indexes, transactions, and query optimization.

For most developers, the confidence comes from writing queries again and again.

If you understand:

- How tables are designed
- How data is inserted and updated
- How to filter and sort data
- How to join related tables
- How to group and summarize data
- How to use indexes and transactions

then you already have a strong SQL foundation.

After that, the best way to improve is simple:

> Build small database examples, write queries daily, make mistakes, debug them, and keep improving.
