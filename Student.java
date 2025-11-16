/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lab6;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mariam Elshamy
 */
public class Student extends User {
    @SerializedName("enrolledCourses")
    private List<String> enrolledCourses;
    @SerializedName("progress")
    private List<String> progress; // lessons completed (el id bta3hom)

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new ArrayList<>();
    }

    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public List<String> getProgress() { return progress; }

    public void enrollCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

    public void markLessonCompleted(String lessonId) {
        if (!progress.contains(lessonId)) {
            progress.add(lessonId);
        }
    }
}
