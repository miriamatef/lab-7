/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Course;
import model.Lesson;
import service.CourseService;
import database.JsonDatabaseManager;


/**
 *
 * @author Miriam
 */


public class LessonManagementFrame extends JFrame {
    private Course course;
    private CourseService courseService;

    private DefaultListModel<Lesson> lessonListModel;
    private JList<Lesson> lessonList;
    private JButton addButton, editButton, deleteButton;

    public LessonManagementFrame(Course course) {
        this.course = course;
        this.courseService = new CourseService(JsonDatabaseManager.getInstance());

        setTitle("Manage Lessons for Course: " + course.getTitle());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadLessons();

        setVisible(true);
    }

    private void initUI() {
        lessonListModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonListModel);
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton = new JButton("Add Lesson");
        editButton = new JButton("Edit Lesson");
        deleteButton = new JButton("Delete Lesson");

        addButton.addActionListener(e -> new AddLessonFrame(course, courseService, this));
        editButton.addActionListener(e -> {
            Lesson selected = lessonList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a lesson");
                return;
            }
            new EditLessonFrame(course, selected, courseService, this);
        });
        deleteButton.addActionListener(e -> deleteSelectedLesson());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(lessonList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadLessons() {
        lessonListModel.clear();
        List<Lesson> lessons = course.getLessons();
        for (Lesson l : lessons) {
            lessonListModel.addElement(l);
        }
    }

    private void deleteSelectedLesson() {
        Lesson selected = lessonList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a lesson to delete");
            return;
        }

        boolean ok = courseService.deleteLesson(course.getCourseId(), selected.getLessonId());
        if (ok) {
            course.getLessons().remove(selected);
            loadLessons();
            JOptionPane.showMessageDialog(this, "Lesson deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting lesson");
        }
    }

    public void refreshList() {
        loadLessons();
    }
}
