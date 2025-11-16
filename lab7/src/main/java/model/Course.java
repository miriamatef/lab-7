/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.*;

/**
 *
 * @author Miriam
 */
public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<Lesson>lessons;
    private List<Integer>students;

    public Course(int courseId, String title, String description, int instructorId, List<Lesson> lessons, List<Integer> students) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = lessons;
        this.students = students;
        
        //to avoid empty lists
        this.lessons = (lessons != null) ? lessons : new ArrayList<>();
        this.students = (students != null) ? students : new ArrayList<>();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Integer> getStudents() {
        return students;
    }

    public void setStudents(List<Integer> students) {
        this.students = students;
    }
    
    public void addLesson (Lesson lesson){
        lessons.add(lesson);
    }
    
    public void removeLesson (int lessonId){
        lessons.removeIf(lesson ->lesson.getLessonId()==lessonId);
    }
    
    public void enrollStudent (int studentId){
        if (!students.contains(studentId)){
            students.add(studentId);
        }
        else{
            System.out.println("This student is already enrolled in the course");
        }
    }
    
    @Override
    public String toString() {
        return title;
    }
}

