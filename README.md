# FencicngSchool

Client-server application for creating, modifying, deleting records of the administrator of the fencing school.

Stack: Java EE, Apache Tomcat, Servlet API, MySQL DB, JDBC, Java FX, Gson.

## Server part of the application:

REST API that generates JSON responses. For each entity of the data model, develop a repository for accessing the database in accordance with the API of the server application. Server Application Classes:
### 1. RegistrationServlet
#### Methods:
• post - receives data and registers a new user-administrator of the school in the system. Handles user existence in the database correctly
### 2. UserServlet
#### Methods:
• post - checks if the username and password match in the database

• get - displays the user with the given id

• delete - removes the user with the given id from the database
### 3. ApprenticeServlet
#### Methods:
• post - adds a new student to the database

• get - retrieves all students, as well as a student by his id

• put - updates the student by his id

• delete - deletes the student and all records associated with him
### 4. TrainerServlet
#### Methods:
• post - adds a new trainer to the database

• get - retrieves all trainers, as well as a trainer by his id

• put - updates the trainer by his id

• delete - deletes a trainer from the database by his id
### 5. TrainerScheduleServlet
#### Methods:
• post - adds a schedule for a specific trainer

• get - retrieves the schedule for the trainer with the specified id

• put - updates the trainer's schedule with the specified id

• delete - deletes the trainer's schedule with the specified id
### 6. TrainingServlet
#### Methods:
• post - adds a new workout for the specified user id and for the specified trainer id

• get - retrieves a workout by its id, workouts by user id or trainer id

• delete - delete a workout by its id

