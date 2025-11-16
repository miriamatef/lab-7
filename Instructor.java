/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lab6;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {
    @SerializedName("createdCourses")
    private List<String> createdCourses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "instructor");
        this.createdCourses = new ArrayList<>();
    }

    public List<String> getCreatedCourses() { return createdCourses; }

    public void addCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }
}
