/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7;

/**
 *
 * @author carol
 */
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        JsonDatabaseManager db = new JsonDatabaseManager();
        UserService userService = new UserService(db);
        CourseService courseService = new CourseService(db);
        LessonService lessonService = new LessonService(db);

        JSONObject loggedInUser = null;

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Add Course");
            System.out.println("4. Enroll in Course");
            System.out.println("5. Add Lesson");
            System.out.println("6. Mark Lesson Completed");
            System.out.println("7. Show All Courses");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: // Signup
                    System.out.print("Role (Student/Instructor): ");
                    String role = scanner.nextLine();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    boolean signupSuccess = userService.signup(role, username, email, password);
                    System.out.println(signupSuccess ? "Signup successful!" : "Email already exists.");
                    break;

                case 2: // Login
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Password: ");
                    String loginPassword = scanner.nextLine();

                    loggedInUser = userService.login(loginEmail, loginPassword);
                    if (loggedInUser != null) {
                        System.out.println("Login successful! Welcome " + loggedInUser.getString("username"));
                    } else {
                        System.out.println("Login failed.");
                    }
                    break;

                case 3: // Add Course
                    if (loggedInUser == null) {
                        System.out.println("You must login first.");
                        break;
                    }
                    System.out.print("Course Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Course Description: ");
                    String description = scanner.nextLine();

                    courseService.addCourse(title, description, loggedInUser.getString("id"));
                    System.out.println("Course added!");
                    break;

                case 4: // Enroll in Course
                    if (loggedInUser == null) {
                        System.out.println("You must login first.");
                        break;
                    }
                    System.out.print("Course ID: ");
                    String courseId = scanner.nextLine();

                    boolean enrolled = courseService.enrollStudent(courseId, loggedInUser.getString("id"));
                    System.out.println(enrolled ? "Enrolled successfully!" : "Enrollment failed.");
                    break;

                case 5: // Add Lesson
                    System.out.print("Course ID: ");
                    String lessonCourseId = scanner.nextLine();
                    System.out.print("Lesson Title: ");
                    String lessonTitle = scanner.nextLine();
                    System.out.print("Lesson Content: ");
                    String lessonContent = scanner.nextLine();

                    lessonService.addLesson(lessonCourseId, lessonTitle, lessonContent);
                    System.out.println("Lesson added!");
                    break;

                case 6: // Mark Lesson Completed
                    if (loggedInUser == null) {
                        System.out.println("You must login first.");
                        break;
                    }
                    System.out.print("Course ID: ");
                    String compCourseId = scanner.nextLine();
                    System.out.print("Lesson ID: ");
                    String compLessonId = scanner.nextLine();

                    lessonService.markLessonCompleted(compCourseId, compLessonId, loggedInUser.getString("id"));
                    System.out.println("Lesson marked as completed!");
                    break;

                case 7: // Show All Courses
                    JSONArray courses = courseService.getAllCourses();
                    System.out.println("Courses: " + courses.toString(2)); // pretty print
                    break;

                case 0: // Exit
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

