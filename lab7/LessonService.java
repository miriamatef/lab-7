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

public class LessonService {
    private JsonDatabaseManager db;

    public LessonService(JsonDatabaseManager db) {
        this.db = db;
    }
    
    public void addLesson(String courseId, String title, String content) {
        JSONArray courses = db.readCourses();
        for(Object obj:courses){
            JSONObject course=(JSONObject)obj;
            if(course.getString("id").equals(courseId)){
                JSONArray lessons = course.getJSONArray("lesson");
                for (Object s : lessons) {
                    if (s.equals(title)) {
                        System.out.println("This lesson is already in the course");
                        return;
                    }   
                } 
                JSONObject newLesson = new JSONObject();
                newLesson.put("id", db.generateId(lessons));
                newLesson.put("title", title);
                newLesson.put("content", content);
                newLesson.put("completedBy", new JSONArray());
                lessons.put(newLesson);
                db.writeCourses(courses);
                return;
            }
        }
    }
    
    public void markLessonCompleted(String courseId, String lessonId, String studentId) {
        JSONArray courses = db.readCourses();
        for(Object obj :courses){
            JSONObject course = (JSONObject)obj;
            if(course.getString("course id").equals(courseId)){
                JSONArray lessons = course.getJSONArray("lessons");
                for(Object objLesson: lessons){
                    JSONObject lesson = (JSONObject)objLesson;
                    if(lesson.getString("lesson id").equals(lessonId)){
                        JSONArray completedBy = lesson.getJSONArray("completedBy");
                        if (!completedBy.toList().contains(studentId)) {
                            completedBy.put(studentId);
                            db.writeCourses(courses);
                        }
                        return;
                    }
                }
            }
        }
    }
}

