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

public class EditLessonFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextField resourcesField;

    private Course course;
    private Lesson lesson;
    private CourseService courseService;
    private LessonManagementFrame parent;

    public EditLessonFrame(Course course, Lesson lesson, CourseService courseService, LessonManagementFrame parent) {
        this.course = course;
        this.lesson = lesson;
        this.courseService = courseService;
        this.parent = parent;

        setTitle("Edit Lesson (ID: " + lesson.getLessonId() + ")");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel form = new JPanel(new GridLayout(6, 1, 10, 10));

        titleField = new JTextField(lesson.getTitle());
        contentArea = new JTextArea(lesson.getContent(), 5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        StringBuilder sb = new StringBuilder();
        if (lesson.getResources() != null) {
            for (String r : lesson.getResources()) sb.append(r).append(", ");
        }
        resourcesField = new JTextField(sb.toString());

        form.add(new JLabel("Lesson Title:"));
        form.add(titleField);
        form.add(new JLabel("Content:"));
        form.add(new JScrollPane(contentArea));
        form.add(new JLabel("Resources (comma separated):"));
        form.add(resourcesField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> saveLessonEdit());

        add(form, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);
    }

    private void saveLessonEdit() {
        String newTitle = titleField.getText().trim();
        String newContent = contentArea.getText().trim();
        String resText = resourcesField.getText().trim();

        if (newTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lesson title cannot be empty");
            return;
        }

        List<String> resources = new ArrayList<>();
        if (!resText.isEmpty()) {
            for (String r : resText.split(",")) resources.add(r.trim());
        }

        boolean ok = courseService.editLesson(course.getCourseId(), lesson.getLessonId(), newTitle, newContent, resources);
        if (ok) {
            lesson.setTitle(newTitle);
            lesson.setContent(newContent);
            lesson.setResources(resources);
            parent.refreshList();
            JOptionPane.showMessageDialog(this, "Lesson updated!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating lesson");
        }
    }
}
