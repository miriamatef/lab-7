/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Lesson;
import service.CourseService;

/**
 *
 * @author Miriam
 */

public class AddLessonFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextField resourcesField;

    private Course course;
    private CourseService courseService;
    private LessonManagementFrame parent;

    public AddLessonFrame(Course course, CourseService courseService, LessonManagementFrame parent) {
        this.course = course;
        this.courseService = courseService;
        this.parent = parent;

        setTitle("Add Lesson");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel form = new JPanel(new GridLayout(6, 1, 10, 10));

        titleField = new JTextField();
        contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        resourcesField = new JTextField();

        form.add(new JLabel("Lesson Title:"));
        form.add(titleField);
        form.add(new JLabel("Content:"));
        form.add(new JScrollPane(contentArea));
        form.add(new JLabel("Resources (comma separated):"));
        form.add(resourcesField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> addLesson());

        add(form, BorderLayout.CENTER);
        add(addBtn, BorderLayout.SOUTH);
    }

    private void addLesson() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        String resourcesText = resourcesField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lesson title cannot be empty");
            return;
        }

        List<String> resources = new ArrayList<>();
        if (!resourcesText.isEmpty()) {
            for (String r : resourcesText.split(",")) {
                resources.add(r.trim());
            }
        }

        Lesson lesson = courseService.addLesson(course.getCourseId(), title, content, resources);
        if (lesson != null) {
            course.getLessons().add(lesson);
            parent.refreshList();
            JOptionPane.showMessageDialog(this, "Lesson added!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding lesson");
        }
    }
}
