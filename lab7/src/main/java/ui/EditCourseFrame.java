/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import service.CourseService;
import database.JsonDatabaseManager;
import model.Course;
import java.awt.*;

/**
 *
 * @author Miriam
 */

public class EditCourseFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton saveButton;
    private Course course;
    private CourseService courseService;
    private InstructorDashboardFrame parent;

    public EditCourseFrame(Course course, InstructorDashboardFrame parent) {
        this.course = course;
        this.parent = parent;
        this.courseService = new CourseService(JsonDatabaseManager.getInstance());

        setTitle("Edit Course (ID: " + course.getCourseId() + ")");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));

        titleField = new JTextField(course.getTitle());
        descriptionArea = new JTextArea(course.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        form.add(new JLabel("Course Title:"));
        form.add(titleField);
        form.add(new JLabel("Description:"));
        form.add(new JScrollPane(descriptionArea));

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> saveEdit());

        panel.add(form, BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void saveEdit() {
        String newTitle = titleField.getText().trim();
        String newDesc = descriptionArea.getText().trim();

        if (newTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty");
            return;
        }

        boolean ok = courseService.editCourse(course.getCourseId(), newTitle, newDesc);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Course updated!");
            parent.loadCourses();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating course");
        }
    }
}
