/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.HashSet;
import java.util.Set;

public class Progress {
    private String courseId;
    private Set<String> completedLessonIds = new HashSet<>();

    public Progress() {}

    public Progress(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public Set<String> getCompletedLessonIds() { return completedLessonIds; }
    public void setCompletedLessonIds(Set<String> completedLessonIds) { this.completedLessonIds = completedLessonIds; }

    public void markLessonCompleted(String lessonId) { completedLessonIds.add(lessonId); }
    public boolean isLessonCompleted(String lessonId) { return completedLessonIds.contains(lessonId); }
}

