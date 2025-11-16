/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import model.Course;
import model.Lesson;
import org.json.JSONArray;
import org.json.JSONObject;
import database.JsonDatabaseManager;
import java.util.*;

/**
 *
 * @author Miriam
 */
public class CourseService {
    private JsonDatabaseManager db;

    public CourseService(JsonDatabaseManager db) {
        this.db = db;
    }

    public JsonDatabaseManager getDb() {
        return db;
    }

    public void setDb(JsonDatabaseManager db) {
        this.db = db;
    }
    
    public Course createCourse (int instructorId, String title, String description){
        int id=db.generateCourseId;
        Course course = new Course (id, title, description, instructorId, new ArrayList<>(), new ArrayList<>());
        saveCourseToJson(course);
        return course;
    }
    
    public Course getCourseById (int courseId){
        JSONObject root = db.loadCourses();
        JSONArray arr = root.getJSONArray("courses");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject jsonCourse = arr.getJSONObject(i);

            if (jsonCourse.getInt("courseId") == courseId) {
                return db.jsonToCourse(jsonCourse);
            }
        }
        return null;
    }
    
    public boolean editCourse(int courseId, String title, String description){
        Course course = getCourseById (courseId);
        
        if (course == null)
            return false;
        
        course.setTitle(title);
        course.setDescription(description);
        return true;
    }
    
    public boolean deleteCourse (int courseId){
        JSONObject root = db.loadCourses();
        JSONArray arr = root.getJSONArray("courses");
        
        for (int i=0; i<arr.length(); i++){
            JSONObject c = arr.getJSONObject(i);

            if (c.getInt("courseId") == courseId) {
                arr.remove(i);
                db.saveCourses(root);
                return true;
            }
        }
        
        return false;
    }
    
    private void saveCourseToJson(Course course) {

        JSONObject root = db.loadCourses();

        JSONArray arr = root.getJSONArray("courses");

        // Remove older copy if exists
        for (int i = 0; i < arr.length(); i++) {
            JSONObject jsonCourse = arr.getJSONObject(i);

            if (jsonCourse.getInt("courseId") == course.getCourseId()) {
                arr.remove(i);
                break;
            }
        }

        // Add updated course
        arr.put(db.courseToJson(course));

        db.saveCourses(root);
    }
    
    public Lesson addLesson (int courseId, String title, String content, List<String>resources){
        Course course = getCourseById(courseId);
        if (course == null)
            return null;
        
        int lessonId = db.generateLessonId();
        Lesson lesson = new Lesson (lessonId, title, content, resources);
        course.addLesson(lesson);
        saveCourseToJson(course);
        return lesson;
    }
    
    public boolean editLesson (int courseId, int lessonId, String nTitle, String nContent, List<String>nResources){
        Course course = getCourseById (courseId);
        
        if (course ==null)
            return false;
        
        for (Lesson lesson: course.getLessons()){
            if (lesson.getLessonId()==lessonId){
                lesson.setTitle(nTitle);
                lesson.setContent(nContent);
                lesson.setResources(nResources);
                saveCourseToJson(course);
                return true;
            }
        }
        
        return false;
    }
    
    public boolean deleteLesson (int courseId, int lessonId){
        Course course = getCourseById(courseId);
        if (course == null)
            return false;
        
        course.removeLesson(lessonId);
        saveCourseToJson(course);
        return true;
    }
    
    public List<Course> getCoursesByInstructor(int instructorId) {

        List<Course> list = new ArrayList<>();

        JSONObject root = db.loadCourses();
        JSONArray arr = root.getJSONArray("courses");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject jsonCourse = arr.getJSONObject(i);

            if (jsonCourse.getInt("instructorId") == instructorId) {
                list.add(db.jsonToCourse(jsonCourse));
            }
        }

        return list;
    }
}
