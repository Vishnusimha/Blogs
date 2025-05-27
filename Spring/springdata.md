# Spring Data

https://github.com/LinkedInLearning/spring-spring-data-2-2508603

<img alt="Spring Data" src="/Spring/springimages/query_Methods.png" />
<img alt="Spring Data" src="/Spring/springimages/img_2.png" />
<img alt="Spring Data" src="/Spring/springimages/img_3.png" />

# JPA Query Methods

## Spring Data Property Expression Query Methods

Overview

Spring Data allows you to create custom query methods using property expressions. These methods let you query data
sources without writing SQL. Instead, Spring handles the implementation using reflection and BeanUtils.
Key Concepts

    Method Naming:
        Begin with findBy, followed by the property name in camel case.
        Example: findByLastName, findByAge.

    Return Types:
        For single results: Use the actual class or Optional<Class>.
        For multiple results: Use a collection type (e.g., List<Class>).

    Count Queries:
        Start with countBy and follow the same property expression rules.
        Example: countByInstructor, countByCredits.

    Validation:
        Queries are validated at application startup.
        Spring throws exceptions for invalid property names, ensuring fast failure.

Example
Student Repository

    Query Methods:

    java

    List<Student> findByFullTime(boolean fullTime);
    List<Student> findByAge(int age);
    List<Student> findByAttendeeLastName(String lastName);

    Usage:
        These methods will automatically generate the appropriate JPQL/HQL based on the method name and property.

Course Repository

    Query Methods:

    java

    List<Course> findByName(String name);
    List<Course> findByPrerequisite(Course prerequisite);
    List<Course> findByCredits(int credits);
    List<Course> findByDepartmentChairMemberLastName(String lastName);

    Usage:
        Similar to the Student repository, these methods generate the necessary JPQL/HQL.

Advantages

    Simplicity: No need to write SQL or HQL manually.
    Consistency: Methods are checked at startup, preventing runtime errors due to invalid queries.
    Efficiency: Easy to understand and maintain.

Example Code Refactor

    StudentDAO to StudentRepository:
        Convert query methods:

        java

    // From StudentDAO
    List<Student> findFullTimeStudents();
    List<Student> findStudentsByAge(int age);
    List<Student> findStudentsByLastName(String lastName);

    // To StudentRepository
    List<Student> findByFullTime(boolean fullTime);
    List<Student> findByAge(int age);
    List<Student> findByAttendeeLastName(String lastName);

CourseDAO to CourseRepository:

    Convert query methods:

    java

        // From CourseDAO
        List<Course> findCoursesByName(String name);
        List<Course> findCoursesByPrerequisite(Course prerequisite);
        List<Course> findCoursesByCredits(int credits);
        List<Course> findCoursesByChairLastName(String lastName);

        // To CourseRepository
        List<Course> findByName(String name);
        List<Course> findByPrerequisite(Course prerequisite);
        List<Course> findByCredits(int credits);
        List<Course> findByDepartmentChairMemberLastName(String lastName);

Error Handling

    Example of validation:

    java

    // Invalid method causing startup error
    List<Staff> findByNothing();

    Spring will throw an exception at startup, ensuring that only valid property expressions are used.

Summary

Using Spring Data property expression query methods simplifies database access, ensures consistency, and reduces
boilerplate code. This approach leverages Spring's capabilities to create dynamic queries based on method names,
offering a powerful and efficient way to interact with your data layer.

<img alt="springimages" src="/Spring/springimages/img_4.png" />
<img alt="springimages" src="/Spring/springimages/img_5.png" />
<img alt="springimages" src="/Spring/springimages/img_6.png" />
<img alt="springimages" src="/Spring/springimages/img_7.png" />

## Spring Data Property Expression Query Methods with Clauses

Overview

Spring Data allows you to create more complex queries using property expressions and logical operators. These methods
extend the basic findBy functionality to include conditions, sorting, and limiting results.
Key Concepts

    Logical Expressions:
        Combine conditions using And, Or, etc.
        Example: findByFullTimeOrAge(boolean fullTime, int age)

    Comparison Operators:
        Use operators like GreaterThan, LessThan, etc.
        Example: findByAgeGreaterThan(int age), findByFullTimeOrAgeLessThan(boolean fullTime, int age)

    Case Insensitivity:
        Use IgnoreCase to perform case-insensitive searches.
        Example: findByAttendeeLastNameIgnoreCase(String lastName)

    Sorting and Limiting:
        Sort results using OrderBy followed by the property and sort direction (Asc or Desc).
        Limit the number of results with findTop or findFirst.
        Example: findFirstByOrderByAttendeeLastNameAsc(), findTopByOrderByAgeDesc(), findTop3ByOrderByAgeDesc()

Examples

    Logical Expressions:

    java

List<Student> findByFullTimeOrAge(boolean fullTime, int age);
List<Student> findByAttendeeFirstNameAndAttendeeLastName(String firstName, String lastName);

Comparison Operators:

java

List<Student> findByAgeGreaterThan(int age);
List<Student> findByFullTimeOrAgeLessThan(boolean fullTime, int age);

Case Insensitivity:

java

List<Student> findByAttendeeLastNameIgnoreCase(String lastName);

Sorting and Limiting:

java

    Student findFirstByOrderByAttendeeLastNameAsc();
    Student findTopByOrderByAgeDesc();
    List<Student> findTop3ByOrderByAgeDesc();

Practical Refactoring

    From StudentDAO to StudentRepository:

    java

    // Old DAO methods
    List<Student> findFullTimeStudents();
    List<Student> findStudentsByAge(int age);
    List<Student> findStudentsByFirstNameAndLastName(String firstName, String lastName);
    List<Student> findStudentsWithSimilarLastName(String lastNamePattern);
    Student findOldestStudent();
    List<Student> findTop3OldestStudents();

    // New Repository methods
    List<Student> findByFullTime(boolean fullTime);
    List<Student> findByAge(int age);
    List<Student> findByAttendeeFirstNameAndAttendeeLastName(String firstName, String lastName);
    List<Student> findByAttendeeLastNameLike(String lastNamePattern);
    Student findTopByOrderByAgeDesc();
    List<Student> findTop3ByOrderByAgeDesc();

Summary

Using Spring Data property expressions with clauses allows for powerful and flexible query creation without writing SQL.
You can combine logical expressions, comparison operators, and sorting/limiting features to handle complex queries
efficiently and intuitively. This approach ensures clean, maintainable code and leverages Spring's ability to handle
queries dynamically.

### @Query- Annotated Method

<img alt="Annotated Method" src="/Spring/springimages/img_8.png" />
<img alt="Annotated Method" src="/Spring/springimages/img_9.png" />
<img alt="Annotated Method" src="/Spring/springimages/img_10.png" />
<img alt="Annotated Method" src="/Spring/springimages/img_11.png" />

Using the @Query Annotation in Spring Data
Overview

The @Query annotation in Spring Data allows you to define custom queries using JPQL or native SQL. This approach offers
flexibility and simplicity, especially for complex queries that are cumbersome to write using property expressions.
Benefits of @Query

    Method Simplification:
        Simplifies method names compared to complex property expressions.
        Example: findByChairLastName is simpler than findByDepartmentChairMemberLastName.

    Parameter Substitution:
        Use @Param to substitute parameters in the query.
        Example: @Query("SELECT s FROM Student s WHERE s.firstName = :firstName AND s.lastName = :lastName")

    Complex Queries:
        Handle complex queries not easily expressed with property expressions.
        Example: JPQL joins or SELECT NEW queries for non-entity results.

    Native Queries:
        Use native SQL queries by setting nativeQuery to true.
        Useful for database-specific features like result limiting.
        Example: @Query(value = "SELECT * FROM Student ORDER BY age DESC LIMIT 3", nativeQuery = true)

Examples

    JPQL Query with @Param:

    java

@Query("SELECT s FROM Student s WHERE s.firstName = :firstName AND s.lastName = :lastName")
List<Student> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

JPQL Query without @Param:

java

@Query("SELECT s FROM Student s WHERE s.age < ?1")
List<Student> findByAgeLessThan(int age);

Native Query:

java

@Query(value = "SELECT * FROM Student ORDER BY age DESC LIMIT 3", nativeQuery = true)
List<Student> findTop3OldestStudents();

Case-Insensitive Query:

java

@Query("SELECT s FROM Student s WHERE LOWER(s.lastName) = LOWER(:lastName)")
List<Student> findByLastNameIgnoreCase(@Param("lastName") String lastName);

Join Query:

java

    @Query("SELECT c FROM Course c JOIN c.prerequisites p WHERE p.id = :prerequisiteId")
    List<Course> findByPrerequisite(@Param("prerequisiteId") Long prerequisiteId);

Refactoring Example

    Original DAO Methods:

    java

List<Student> findFullTimeStudents();
List<Student> findStudentsByAge(int age);
List<Student> findStudentsByFirstNameAndLastName(String firstName, String lastName);
List<Student> findStudentsWithSimilarLastName(String lastNamePattern);
Student findOldestStudent();
List<Student> findTop3OldestStudents();

Refactored Repository Methods with @Query:

java

    @Query("SELECT s FROM Student s WHERE s.fullTime = true")
    List<Student> findFullTimeStudents();

    @Query("SELECT s FROM Student s WHERE s.age < ?1")
    List<Student> findByAgeLessThan(int age);

    @Query("SELECT s FROM Student s WHERE s.firstName = :firstName AND s.lastName = :lastName")
    List<Student> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT s FROM Student s WHERE s.lastName LIKE %:lastNamePattern%")
    List<Student> findStudentsWithSimilarLastName(@Param("lastNamePattern") String lastNamePattern);

    @Query(value = "SELECT * FROM Student ORDER BY age DESC LIMIT 1", nativeQuery = true)
    Student findOldestStudent();

    @Query(value = "SELECT * FROM Student ORDER BY age DESC LIMIT 3", nativeQuery = true)
    List<Student> findTop3OldestStudents();

Summary

Using the @Query annotation in Spring Data simplifies method signatures, supports complex queries, and allows for the
use of native SQL. It enhances the flexibility of query methods, making your code cleaner and more maintainable

<img alt="springimages" src="/Spring/springimages/img_12.png" />
<img alt="springimages" src="/Spring/springimages/img_13.png" />
<img alt="springimages" src="/Spring/springimages/img_14.png" />
<img alt="springimages" src="/Spring/springimages/img_15.png" />
<img alt="springimages" src="/Spring/springimages/img_16.png" />

## Paging and Sorting in Spring Data

Overview

Spring Data provides a robust and straightforward approach to implement paging and sorting using built-in repository
methods, making it easier than manually handling these operations in your code.
Key Concepts

    PagingAndSortingRepository Interface:
        Extends CrudRepository.
        Provides methods for pagination and sorting.
        Already included in JpaRepository, so all repositories extending JpaRepository have paging and sorting capabilities by default.

    Paging and Sorting Parameters:
        Sort: Used to define sorting criteria.
        Pageable: Used to define pagination (page number, page size, and optionally, sorting).

Creating Sort and Pageable Objects

    Creating a Sort Object:

    java

Sort sort = Sort.by(Sort.Direction.ASC, "lastName");

    Sort A: Sort.by(Sort.Direction.ASC, "lastName")
    Sort B: Sort.by(new Sort.Order(Sort.Direction.ASC, "lastName"))
    Sort C: Sort.by("lastName") (default direction is ascending)
    Sort D: Sort.by("lastName").and(Sort.by("firstName")) (sub-sorting)

Creating a Pageable Object:

java

    Pageable pageable = PageRequest.of(0, 5, Sort.by("lastName").and(Sort.by("firstName")));

        PageRequest.of(pageNumber, pageSize): First page is 0.
        Optional Sort Parameter: Add sort criteria.

Using Pageable in Repository Methods

    findAll with Pageable:

    java

Page<Staff> staffPage = staffRepo.findAll(pageable);

    Returns a Page object containing the results and metadata.

Accessing Page Results:

java

    List<Staff> staffList = staffPage.getContent();
    long totalElements = staffPage.getTotalElements();
    int totalPages = staffPage.getTotalPages();

Example Refactoring

Old Method in StaffDao:

java

public List<Staff> findStaffPage(int pageNumber, int pageSize) {
int firstResult = (pageNumber - 1) * pageSize;
Query query = entityManager.createQuery("SELECT s FROM Staff s ORDER BY s.lastName");
query.setFirstResult(firstResult);
query.setMaxResults(pageSize);
return query.getResultList();
}

New Method Using StaffRepo:

java

// Define Sort and Pageable
Sort sort = Sort.by("lastName").and(Sort.by("firstName"));
Pageable pageable = PageRequest.of(0, 5, sort);

// Use StaffRepo's findAll method
Page<Staff> staffPage = staffRepo.findAll(pageable);

// Extract results
List<Staff> staffList = staffPage.getContent();

JUnit Test for Pagination:

java

@Test
public void testPagingAndSorting() {
Pageable pageable = PageRequest.of(0, 5, Sort.by("lastName").and(Sort.by("firstName")));
Page<Staff> staffPage = staffRepo.findAll(pageable);
List<Staff> staffList = staffPage.getContent();
assertEquals(5, staffList.size()); // assuming there are at least 5 staff members
}

Summary

    Use PagingAndSortingRepository to enable out-of-the-box pagination and sorting.
    Create Sort and Pageable objects to define sorting and pagination parameters.
    Use findAll(Pageable pageable) to get paginated and sorted results.
    Access the results and metadata from the Page object.

<img alt="1" src="/Spring/springimages/img_17.png" />
<img alt="1" src="/Spring/springimages/img_18.png" />
<img alt="1" src="/Spring/springimages/img_19.png" />
<img alt="1" src="/Spring/springimages/img_20.png" />
<img alt="1" src="/Spring/springimages/img_21.png" />

## QueryDSL

§
<img alt="1" src="/Spring/springimages/img_22.png" />
<img alt="1" src="/Spring/springimages/img_23.png" />

### Query by Example
<img alt="1" src="/Spring/springimages/img_24.png" />
<img alt="1" src="/Spring/springimages/img_25.png" />
<img alt="1" src="/Spring/springimages/img_26.png" />
<img alt="1" src="/Spring/springimages/img_27.png" />
<img alt="1" src="/Spring/springimages/img_28.png" />
<img alt="1" src="/Spring/springimages/img_29.png" />

## Spring data rest feature

<img alt="1" src="/Spring/springimages/img_30.png" />
<img alt="1" src="/Spring/springimages/img_31.png" />
<img alt="1" src="/Spring/springimages/img_32.png" />
<img alt="1" src="/Spring/springimages/img_33.png" />
<img alt="1" src="/Spring/springimages/img_34.png" />
<img alt="1" src="/Spring/springimages/img_35.png" />
<img alt="1" src="/Spring/springimages/img_36.png" />

## Spring Data MongoDB
Overview

Spring Data supports various data sources through the commons CrudRepository interface. This section focuses on applying
Spring Data to MongoDB, using a simplified Spring Boot project.
Key Components

    Dependencies:
        spring-boot-starter-data-mongodb: Starter for MongoDB.
        de.flapdoodle.embed.mongo: Embedded MongoDB for in-memory instances.

    Domain Model:
        Documents: MongoDB equivalent to JPA entities.
        Annotations: @Document for MongoDB documents.

    Repositories:
        Extends PagingAndSortingRepository and CrudRepository.
        Supports property expressions and custom queries using the @Query annotation with MongoDB's JSON syntax.

Example Domain Model

    Staff Document:

    java

@Document
public class Staff {
@Id
private String id;
private Person member;
// Getters and setters
}

Person Class:

java

public class Person {
private String firstName;
private String lastName;
// Getters and setters
}

Department Document:

java

    @Document
    public class Department {
        @Id
        private String id;
        private String name;
        private Staff chair;
        // Getters and setters
    }

Repository Examples

    Staff Repository:

    java

public interface StaffRepo extends PagingAndSortingRepository<Staff, String> {
List<Staff> findByMemberLastName(String lastName);

    @Query("{ 'member.firstName' : ?0 }")
    List<Staff> findByFirstName(String firstName);

}

Department Repository:

java

    public interface DepartmentRepo extends PagingAndSortingRepository<Department, String> {
        List<Department> findByName(String name);
        
        @Query("{ 'name' : { '$regex' : ?0, '$options' : 'i' } }")
        List<Department> findNameByPattern(String pattern);
    }

Configuration

    application.properties:

    properties

spring.mongodb.embedded.version=3.5.5

Spring Boot Application Class:

java

    @SpringBootApplication
    public class UniversityApplication {
        public static void main(String[] args) {
            SpringApplication.run(UniversityApplication.class, args);
        }
    }

Testing with JUnit

    Helper Methods:

    java

private void createStaff(String firstName, String lastName) {
staffRepo.save(new Staff(new Person(firstName, lastName)));
}

private void createDepartment(String name, Staff chair) {
departmentRepo.save(new Department(name, chair));
}

JUnit Test:

java

    @Test
    public void testMongoQueries() {
        // Create sample data
        createStaff("John", "Doe");
        createStaff("Jane", "Smith");
        Staff chair = createStaff("John", "Jones");
        createDepartment("Humanities", chair);

        // Sorting example
        Sort sort = Sort.by("member.lastName").ascending();
        List<Staff> sortedStaff = staffRepo.findAll(sort);
        assertEquals("Doe", sortedStaff.get(0).getMember().getLastName());

        // Pagination example
        Pageable pageable = PageRequest.of(0, 2, sort);
        Page<Staff> staffPage = staffRepo.findAll(pageable);
        assertEquals(2, staffPage.getSize());

        // Property expression
        List<Staff> johns = staffRepo.findByFirstName("John");
        assertFalse(johns.isEmpty());

        // Custom query with regex
        List<Department> sciences = departmentRepo.findNameByPattern(".*Sciences");
        assertFalse(sciences.isEmpty());
    }

Summary

    Transitioning to MongoDB: Using Spring Data with MongoDB is seamless and similar to using JPA.
    Repository Interfaces: Leverage CrudRepository and PagingAndSortingRepository for basic CRUD and advanced operations.
    Custom Queries: Use @Query annotation for more complex MongoDB queries.
    Testing: Easily test with embedded MongoDB using dependencies like Flapdoodle.
