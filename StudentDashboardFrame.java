/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import managers.StudentManager;
import models.Course;
import models.Lesson;
import storage.JsonDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {

    private final StudentManager studentManager;
    private final String studentId;

    private JList<String> coursesList;
    private DefaultListModel<String> coursesListModel;
    private JList<String> lessonsList;
    private DefaultListModel<String> lessonsListModel;
    private JTextArea lessonContentArea;
    private JButton enrollButton;
    private JButton markCompleteButton;

    public StudentDashboardFrame(String usersPath, String coursesPath, String studentId) {
        this.studentId = studentId;
        JsonDatabaseManager db = new JsonDatabaseManager(usersPath, coursesPath);
        this.studentManager = new StudentManager(db);
        initUI();
        loadCourses();
        setTitle("Student Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

       
        coursesListModel = new DefaultListModel<>();
        coursesList = new JList<>(coursesListModel);
        JScrollPane coursesScroll = new JScrollPane(coursesList);
        coursesScroll.setPreferredSize(new Dimension(300, 0));

        enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(e -> onEnrollClicked());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(coursesScroll, BorderLayout.CENTER);
        leftPanel.add(enrollButton, BorderLayout.SOUTH);

       
        lessonsListModel = new DefaultListModel<>();
        lessonsList = new JList<>(lessonsListModel);
        JScrollPane lessonsScroll = new JScrollPane(lessonsList);

        markCompleteButton = new JButton("Mark Completed");
        markCompleteButton.addActionListener(e -> onMarkCompletedClicked());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(lessonsScroll, BorderLayout.CENTER);
        centerPanel.add(markCompleteButton, BorderLayout.SOUTH);

       
        lessonContentArea = new JTextArea();
        lessonContentArea.setEditable(false);
        JScrollPane contentScroll = new JScrollPane(lessonContentArea);

        main.add(leftPanel, BorderLayout.WEST);
        main.add(centerPanel, BorderLayout.CENTER);
        main.add(contentScroll, BorderLayout.EAST);

        add(main);
    }

    private void loadCourses() {
        final List<Course> courses = studentManager.browseCourses();
        coursesListModel.clear();
        for (Course c : courses) {
            coursesListModel.addElement(c.getCourseId() + " — " + c.getTitle());
        }

        coursesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) return;
            int selectedIndex = coursesList.getSelectedIndex();
            onCourseSelected(selectedIndex, courses);
        });

        lessonsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) return;
            int courseIdx = coursesList.getSelectedIndex();
            int lessonIdx = lessonsList.getSelectedIndex();
            if (courseIdx < 0 || lessonIdx < 0) return;
            Course c = courses.get(courseIdx);
            Lesson l = c.getLessons().get(lessonIdx);
            lessonContentArea.setText(l.getContent());
        });
    }

    private void onCourseSelected(int courseIdx, List<Course> courses) {
        if (courseIdx < 0) return;
        Course c = courses.get(courseIdx);
        lessonsListModel.clear();
        for (Lesson l : c.getLessons()) {
            lessonsListModel.addElement(l.getLessonId() + " — " + l.getTitle());
        }
    }

    private void onEnrollClicked() {
        int idx = coursesList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first");
            return;
        }

        String entry = coursesListModel.get(idx);
        String[] parts = entry.split(" — ", 2);
        if (parts.length < 1) {
            JOptionPane.showMessageDialog(this, "Invalid course entry");
            return;
        }

        String courseId = parts[0];
        boolean ok = studentManager.enroll(studentId, courseId);
        JOptionPane.showMessageDialog(this, ok ? "Enrolled successfully" : "Enrollment failed");

        loadCourses(); 
    }

    private void onMarkCompletedClicked() {
        int courseIdx = coursesList.getSelectedIndex();
        int lessonIdx = lessonsList.getSelectedIndex();
        if (courseIdx < 0 || lessonIdx < 0) {
            JOptionPane.showMessageDialog(this, "Select course and lesson first");
            return;
        }

        String courseEntry = coursesListModel.get(courseIdx);
        String lessonEntry = lessonsListModel.get(lessonIdx);

        String[] courseParts = courseEntry.split(" — ", 2);
        String[] lessonParts = lessonEntry.split(" — ", 2);
        if (courseParts.length < 1 || lessonParts.length < 1) {
            JOptionPane.showMessageDialog(this, "Invalid course or lesson entry");
            return;
        }

        String courseId = courseParts[0];
        String lessonId = lessonParts[0];

        boolean ok = studentManager.markLessonCompleted(studentId, courseId, lessonId);
        JOptionPane.showMessageDialog(this, ok ? "Lesson marked complete" : "Operation failed");
    }

    
}
