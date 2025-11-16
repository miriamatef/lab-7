/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7;

/**
 *
 * @author carol
 */
import org.json.JSONArray;
import org.json.JSONObject;

public class CourseService {
    private JsonDatabaseManager db;

    public CourseService(JsonDatabaseManager db) {
        this.db = db;
    }
    
    public void addCourse(String title, String description, String instructorId) {
        JSONArray courses = db.readCourses();
        for(Object obj : courses){
            JSONObject course = (JSONObject)obj;
            if(course.getString("title").equals(title)){
                System.out.println("this course already exists");
                return;
            }      
        }
        JSONObject newCourse =new JSONObject();
        newCourse.put("id", db.generateId(courses));
        newCourse.put("title",title);
        newCourse.put("description",description);
        newCourse.put("instructor id",instructorId);
        newCourse.put("lessons", new JSONArray());
        newCourse.put("students", new JSONArray());
        courses.put(newCourse);
        db.writeCourses(courses);
    }
    
    public boolean deleteCourse(String courseId){
        JSONArray courses = db.readCourses();
        for(int i=0;i< courses.length();i++){
            JSONObject course = courses.getJSONObject(i);
            if(course.getString("course id").equals(courseId)){
                courses.remove(i);
                System.out.println("course deleted successfully");
                return true;
            }      
        }
        System.out.println("course not found");
        return false;
    }  
    
    public boolean editCourse(String courseId, String newTitle, String newDescription) {
        JSONArray courses = db.readCourses();
        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            if (course.getString("course id").equals(courseId)) {
                course.put("title", newTitle);
                course.put("description", newDescription);
                db.writeCourses(courses);
                System.out.println("Course updated successfully");
                return true;
            }
        }
        System.out.println("Course not found");
        return false;
    }

    public boolean enrollStudent(String courseId, String studentId) {
        JSONArray courses = db.readCourses();
        for(Object obj : courses){
            JSONObject course = (JSONObject)obj;
            if (course.getString("course id").equals(courseId)) {
                JSONArray students = course.getJSONArray("students");
                for (Object s : students) {
                    if (s.equals(studentId)) {
                        System.out.println("This student is already enrolled in the course");
                        return false;
                    }
                }
            students.put(studentId);
            db.writeCourses(courses);
            System.out.println("Student enrolled successfully");
            return true;
            }
        }
        System.out.println("course not found");
        return false;
    }
            
    public void viewEnrolledStudents(String courseId){
        JSONArray courses = db.readCourses();
        for(Object obj: courses){
            JSONObject course = (JSONObject)obj;
            if (course.getString("course id").equals(courseId)) {
            JSONArray students = course.getJSONArray("students");
                for (Object s : students) {
                    System.out.println(s.toString());
                }
                return;
            }
        }
        System.out.println("course not found");
    }
    
    public JSONArray getAllCourses() {
        return db.readCourses();
    }
}

