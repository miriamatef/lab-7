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

public class CreateCourseFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton createButton;
    private int instructorId;
    private CourseService courseService;
    private InstructorDashboardFrame parent;

    public CreateCourseFrame(int instructorId, InstructorDashboardFrame parent) {
        this.instructorId = instructorId;
        this.parent = parent;
        this.courseService = new CourseService(JsonDatabaseManager.getInstance());

        setTitle("Create New Course");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));

        titleField = new JTextField();
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        form.add(new JLabel("Course Title:"));
        form.add(titleField);
        form.add(new JLabel("Description:"));
        form.add(new JScrollPane(descriptionArea));

        createButton = new JButton("Create");
        createButton.addActionListener(e -> createCourse());

        panel.add(form, BorderLayout.CENTER);
        panel.add(createButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void createCourse() {
        String title = titleField.getText().trim();
        String desc = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty");
            return;
        }

        Course course = courseService.createCourse(instructorId, title, desc);
        JOptionPane.showMessageDialog(this, "Course created with ID: " + course.getCourseId());
        parent.loadCourses(); // refresh dashboard
        dispose();
    }
}
