/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Miriam
 */
package lab7.isa;

import java.util.*;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private String instructorId;
    private List<Lesson> lessons;
    private List<String> students;

    public Course(int courseId, String title, String description, String instructorId,
                  List<Lesson> lessons, List<String> students) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = lessons == null ? new ArrayList<>() : lessons;
        this.students = students == null ? new ArrayList<>() : students;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
    public List<String> getStudents() { return students; }
    public void setStudents(List<String> students) { this.students = students; }

    public void addLesson(Lesson lesson) { lessons.add(lesson); }
    public void removeLesson(int lessonId) { lessons.removeIf(l -> l.getLessonId() == lessonId); }
    public void enrollStudent(String studentId) { if (!students.contains(studentId)) students.add(studentId); }

    @Override
    public String toString() { return title; }
}
