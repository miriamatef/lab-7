/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import service.CourseService;
import database.JsonDatabaseManager;
import model.Course;
import java.util.List;

/**
 *
 * @author Miriam
 */


public class InstructorDashboardFrame extends JFrame {
    private CourseService courseService;
    private JList<Course> courseList;
    private DefaultListModel<Course> courseListModel;
    private int instructorId;

    public InstructorDashboardFrame(int instructorId) {
        this.instructorId = instructorId;
        this.courseService = new CourseService(JsonDatabaseManager.getInstance());

        setTitle("Instructor Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadCourses();

        setVisible(true);
    }

    private void initUI() {
        courseListModel = new DefaultListModel<>();
        courseList = new JList<>(courseListModel);
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton createBtn = new JButton("Create Course");
        JButton editBtn = new JButton("Edit Course");
        JButton lessonBtn = new JButton("Manage Lessons");

        createBtn.addActionListener(e -> new CreateCourseFrame(instructorId, this));
        editBtn.addActionListener(e -> {
            Course selected = courseList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a course to edit.");
                return;
            }
            new EditCourseFrame(selected, this);
        });
        lessonBtn.addActionListener(e -> {
            Course selected = courseList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a course to manage lessons.");
                return;
            }
            new LessonManagementFrame(selected);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(lessonBtn);

        add(new JScrollPane(courseList), "Center");
        add(buttonPanel, "South");
    }

    public void loadCourses() {
        courseListModel.clear();
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        for (Course c : courses) {
            courseListModel.addElement(c);
        }
    }
}