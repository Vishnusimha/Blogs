# What Can SQL do?

- SQL can execute queries against a database
- SQL can retrieve data from a database
- SQL can insert records in a database
- SQL can update records in a database
- SQL can delete records from a database
- SQL can create new databases
- SQL can create new tables in a database
- SQL can create stored procedures in a database
- SQL can create views in a database
- SQL can set permissions on tables, procedures, and views

**Keep in Mind That...**

- SQL keywords are NOT case sensitive:  `select`  is the same as  `SELECT`
- Strings are case sensitive

### Semicolon after SQL Statements?

Some database systems require a semicolon at the end of each SQL statement.

Semicolon is the standard way to separate each SQL statement in database systems that allow more than one SQL statement
to be executed in the same tutorial.call to the server.

### Some of The Most Important SQL Commands

- `SELECT`  - extracts data from a database
- `UPDATE`  - updates data in a database
- `DELETE`  - deletes data from a database
- `INSERT INTO`  - inserts new data into a database
- `CREATE DATABASE`  - creates a new database
- `ALTER DATABASE`  - modifies a database
- `CREATE TABLE`  - creates a new table
- `ALTER TABLE`  - modifies a table
- `DROP TABLE`  - deletes a table
- `CREATE INDEX`  - creates an index (search key)
- `DROP INDEX`  - deletes an index
- `DISTINCT`  - selects the unique element
- `WHERE` - filters based on specified condition
- `ORDER BY` - To sort the result-set in `ASC|DESC` (ascending or descending order).
- `TOP` - is used to specify the number of records to return.(The SELECT TOP clause is useful on large tables with
  thousands of records. Returning a large number of records can impact performance.)
- SQL MIN() and MAX()
- COUNT(), AVG() and SUM()

filters for `WHERE`

- Equal =
- Greater than >
- Less than <
- `BETWEEN` - Between a certain range
- `LIKE` - Search for a pattern
- `IN` - To specify multiple possible values for a column
- How to Test for NULL Values?
  It is not possible to test for NULL values with comparison operators, such as =, <, or <>. We will have to use
  the `IS NULL` and `IS NOT NULL` operators instead.

| LIKE Operator                      | Description                                                                  |
|---------------------------------|------------------------------------------------------------------------------|
| WHERE CustomerName LIKE 'a%'    |    Finds any values that start with "a"                                        |
| WHERE CustomerName LIKE '%a'       | Finds any values that end with "a"                                           |
| WHERE CustomerName LIKE '%or%'  |    Finds any values that have "or" in any position                             |
| WHERE CustomerName LIKE '_r%'   |    Finds any values that have "r" in the second position                       |
| WHERE CustomerName LIKE 'a_%'      | Finds any values that start with "a" and are at least 2 characters in length |
| WHERE CustomerName LIKE 'a__%'     | Finds any values that start with "a" and are at least 3 characters in length |
| WHERE ContactName LIKE 'a%o'       | Finds any values that start with "a" and ends with "o"                       |

Regex - link (https://www.computerhope.com/jargon/r/regex.htm)

**Wildcard Characters in SQL Server**
|Symbol | Description Example |
| ------------- | ------------- |
| % Represents zero or more characters | bl% finds bl, black, blue, and blob |
| _ Represents a single character | h_t finds hot, hat, and hit |
| []    Represents any single character within the brackets | h[oa]t finds hot and hat, but not hit |
| ^ Represents any character not in the brackets | h[^oa]t finds hit, but not hot and hat |
| - Represents a range of characters | c[a-b]t finds cat and cbt |

**Note: Be careful when updating records in a table! Notice the `WHERE` clause in the UPDATE statement. The `WHERE`
clause specifies which record(s) that should be updated. If you omit the `WHERE` clause, all records in the table will
be updated!**

**Note: Be careful when deleting records in a table! Notice the `WHERE` clause in the `DELETE` statement. The `WHERE`
clause specifies which record(s) should be deleted. If you omit the `WHERE` clause, all records in the table will be
deleted!
Delete All Records
It is possible to delete all rows in a table without deleting the table. This means that the table structure,
attributes, and indexes will be intact:
`DELETE` FROM table_name;**

**Note: Not all database systems support the SELECT TOP clause. MySQL supports the LIMIT clause to select a limited
number of records, while Oracle uses FETCH FIRST n ROWS ONLY and ROWNUM.**

**Note: Not all database systems support the SELECT TOP clause. MySQL supports the LIMIT clause to select a limited
number of records, while Oracle uses FETCH FIRST n ROWS ONLY and ROWNUM.**

Examples:

- SELECT * FROM Customers
  WHERE tutorial.Country="Austria";
- SELECT COUNT(DISTINCT tutorial.Country) FROM Customers;
- SELECT * FROM Customers WHERE tutorial.Country LIKE "UK";
- SELECT * FROM Customers WHERE tutorial.Country = "UK" or tutorial.Country = "Austria" or tutorial.Country = "Germany";
- SELECT * FROM Customers WHERE NOT tutorial.Country='Germany';
- SELECT * FROM Customers WHERE tutorial.Country='Germany' AND (City='Berlin' OR City='München');
- SELECT * FROM Customers WHERE NOT tutorial.Country='Germany' AND NOT tutorial.Country='USA';
- SELECT * FROM Customers ORDER BY tutorial.Country DESC;
- SELECT * FROM Customers ORDER BY tutorial.Country ASC, CustomerName DESC;
- SELECT * FROM Customers ORDER BY City DESC; ............................................................<- Important
  in order
- INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, tutorial.Country)
  VALUES (' d cksd kdsk','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
- SELECT CustomerName, ContactName, Address FROM Customers WHERE Address IS NOT NULL;
- UPDATE Customers
  SET ContactName='Vishnu', City='Frankfurt'
  WHERE CustomerID=1;
- DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
- SELECT COUNT(column_name) FROM table_name WHERE condition;
- SELECT AVG(column_name) FROM table_name WHERE condition;
- SELECT SUM(column_name) FROM table_name WHERE condition;
- select distinct CITY from STATION
  WHERE city REGEXP "^[aeiou].*"; //city names start with vowels
- SELECT DISTINCT CITY FROM STATION
  WHERE CITY REGEXP '[aeiou]$'; //city names end with vowels
- select distinct CITY from STATION
  WHERE(city REGEXP "^[aeiou]") and (city REGEXP "[aeiou]$") ;
  -SELECT DISTINCT CITY FROM STATION
  WHERE CITY NOT REGEXP '^[AEIOU]' OR CITY NOT REGEXP '[AEIOU]$'; //Query the list of CITY names from STATION that
  either do not start with vowels or do not end with vowels. Your result cannot contain duplicates.
- Select Name from STUDENTS
  where marks > 75
  order by Right(name,3), ID asc; // Query the Name of any student in STUDENTS who scored higher than Marks. Order your
  output by the last three characters of each name. If two or more students both have names ending in the same last
  three characters (i.e.: Bobby, Robby, etc.), secondary sort them by ascending ID.


  
