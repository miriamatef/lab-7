/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7.isa;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 *
 * @author Miriam
 */

public class InstructorDashboardFrame extends JFrame {
    private final String instructorId;
    private final CourseService courseService;
    private final DefaultListModel<Course> listModel;
    private final JList<Course> courseJList;
    private final UserService userService;

    public InstructorDashboardFrame(String instructorId, UserService userService) {
    this.instructorId = instructorId;
    this.userService = userService;
    this.courseService = new CourseService(userService.db);
    

        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        courseJList = new JList<>(listModel);
        courseJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        initUI();
        loadCourses();
        setVisible(true);
    }

    private void initUI() {
        // Top bar with Logout
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame(new UserService(JsonDatabaseManager.getInstance())).setVisible(true);
            dispose();
        });
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.add(logoutBtn);
        add(topBar, BorderLayout.NORTH);

        // Buttons
        JButton create = new JButton("Create");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton lessons = new JButton("Manage Lessons");

        create.addActionListener(e -> new CreateCourseFrame(instructorId, this));
        edit.addActionListener(e -> {
            Course c = courseJList.getSelectedValue();
            if (c == null) { JOptionPane.showMessageDialog(this, "Select a course."); return; }
            new EditCourseFrame(c, this);
        });
        del.addActionListener(e -> {
            Course c = courseJList.getSelectedValue();
            if (c == null) { JOptionPane.showMessageDialog(this, "Select a course."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete " + c.getTitle() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean ok = courseService.deleteCourse(c.getCourseId());
                if (ok) { JOptionPane.showMessageDialog(this, "Deleted."); loadCourses(); }
                else JOptionPane.showMessageDialog(this, "Delete failed.");
            }
        });
        lessons.addActionListener(e -> {
            Course c = courseJList.getSelectedValue();
            if (c == null) { JOptionPane.showMessageDialog(this, "Select a course."); return; }
            new LessonManagementFrame(c);
        });

        JPanel bottom = new JPanel();
        bottom.add(create);
        bottom.add(edit);
        bottom.add(del);
        bottom.add(lessons);

        add(new JScrollPane(courseJList), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void loadCourses() {
        listModel.clear();
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        for (Course c : courses) listModel.addElement(c);
    }
}
