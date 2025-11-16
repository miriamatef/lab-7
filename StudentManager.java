/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import models.Course;
import models.Student;
import models.Lesson;
import models.User;
import storage.JsonDatabaseManager;
import util.SecurityUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentManager {
    private final JsonDatabaseManager db;

    public StudentManager(JsonDatabaseManager db) {
        this.db = db;
    }

   
    public List<Course> browseCourses() {
        return db.loadCourses();
    }

  
    public List<Course> getEnrolledCourses(String studentId) {
        List<User> users = db.loadUsers();
        Optional<User> ou = users.stream().filter(u -> u.getUserId().equals(studentId)).findFirst();
        if (ou.isEmpty() || !(ou.get() instanceof Student)) return List.of();

        Student s = (Student) ou.get();
        List<Course> courses = db.loadCourses();
        return courses.stream()
                .filter(c -> s.getEnrolledCourses().contains(c.getCourseId()))
                .collect(Collectors.toList());
    }

    
    public List<Lesson> getLessonsForCourse(String courseId) {
        Optional<Course> oc = db.findCourseById(courseId);
        return oc.map(Course::getLessons).orElse(List.of());
    }

    
    public boolean enroll(String studentId, String courseId) {
        List<User> users = db.loadUsers();
        Optional<User> ou = users.stream().filter(u -> u.getUserId().equals(studentId)).findFirst();
        if (ou.isEmpty() || !(ou.get() instanceof Student)) return false;
        Student s = (Student) ou.get();

        List<Course> courses = db.loadCourses();
        Optional<Course> oc = courses.stream().filter(c -> c.getCourseId().equals(courseId)).findFirst();
        if (oc.isEmpty()) return false;
        Course c = oc.get();

        // Update student and course
        s.enrollCourse(courseId);
        c.addStudent(studentId);

        // Save changes
        db.saveUsers(users);
        db.saveCourses(courses);
        return true;
    }

    
    public boolean markLessonCompleted(String studentId, String courseId, String lessonId) {
        List<User> users = db.loadUsers();
        Optional<User> ou = users.stream().filter(u -> u.getUserId().equals(studentId)).findFirst();
        if (ou.isEmpty() || !(ou.get() instanceof Student)) return false;
        Student s = (Student) ou.get();

        var prog = s.getProgressForCourse(courseId);
        if (prog == null) return false; // not enrolled

        prog.markLessonCompleted(lessonId);

        db.saveUsers(users);
        return true;
    }

    
    public Student registerStudent(String username, String email, String password) {
        List<User> users = db.loadUsers();
        String uid = UUID.randomUUID().toString();
        String hash = SecurityUtil.sha256(password);
        Student s = new Student(uid, username, email, hash);
        users.add(s);
        db.saveUsers(users);
        return s;
    }
}
