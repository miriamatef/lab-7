/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledCourses = new ArrayList<>();
    private List<Progress> progress = new ArrayList<>();

    public Student() { super(); }

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, "STUDENT", username, email, passwordHash);
    }

    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }

    public List<Progress> getProgress() { return progress; }
    public void setProgress(List<Progress> progress) { this.progress = progress; }

    public void enrollCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            progress.add(new Progress(courseId));
        }
    }

    public Progress getProgressForCourse(String courseId) {
        return progress.stream().filter(p -> p.getCourseId().equals(courseId)).findFirst().orElse(null);
    }
}
