package lab7.isa;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {
    private final String studentId;
    private final StudentManager studentManager;
    private final UserService userService;

    private JList<String> coursesList;
    private DefaultListModel<String> coursesListModel;
    private JList<String> lessonsList;
    private DefaultListModel<String> lessonsListModel;
    private JTextArea lessonContentArea;
    private JButton enrollButton;
    private JButton markCompleteButton;
    private final LoginFrame loginFrame;
    private JProgressBar progressBar;
    private JLabel progressLabel;

    private List<Course> availableCourses;

    
    public StudentDashboardFrame(String studentId, LoginFrame loginFrame) {
        super("Student Dashboard - " + studentId);
        this.studentId = studentId;
        this.studentManager = new StudentManager(JsonDatabaseManager.getInstance());
        this.userService = new UserService(JsonDatabaseManager.getInstance());
        this.loginFrame = loginFrame;

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        loadCourses();
        setVisible(true);
    }

    private void initUI() {
    
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
        loginFrame.setVisible(true); 
        dispose();                  
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.add(logoutBtn);
        add(topBar, BorderLayout.NORTH);

       
        coursesListModel = new DefaultListModel<>();
        coursesList = new JList<>(coursesListModel);
        coursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane coursesScroll = new JScrollPane(coursesList);
        coursesScroll.setPreferredSize(new Dimension(300, 0));

        enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(e -> onEnrollClicked());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Available Courses", JLabel.CENTER), BorderLayout.NORTH);
        leftPanel.add(coursesScroll, BorderLayout.CENTER);
        leftPanel.add(enrollButton, BorderLayout.SOUTH);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        
        lessonsListModel = new DefaultListModel<>();
        lessonsList = new JList<>(lessonsListModel);
        lessonsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane lessonsScroll = new JScrollPane(lessonsList);

        markCompleteButton = new JButton("Mark Completed");
        markCompleteButton.addActionListener(e -> onMarkCompletedClicked());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Lessons", JLabel.CENTER), BorderLayout.NORTH);
        centerPanel.add(lessonsScroll, BorderLayout.CENTER);
        centerPanel.add(markCompleteButton, BorderLayout.SOUTH);

       
        lessonContentArea = new JTextArea();
        lessonContentArea.setEditable(false);
        lessonContentArea.setLineWrap(true);
        lessonContentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(lessonContentArea);
        contentScroll.setPreferredSize(new Dimension(350, 0));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Lesson Content", JLabel.CENTER), BorderLayout.NORTH);
        rightPanel.add(contentScroll, BorderLayout.CENTER);

     
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressLabel = new JLabel("Progress: 0% (0/0 lessons)", JLabel.CENTER);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

       
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(progressPanel, BorderLayout.SOUTH);

       
        coursesList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int idx = coursesList.getSelectedIndex();
            if (idx < 0 || availableCourses == null) return;
            Course c = availableCourses.get(idx);
            lessonsListModel.clear();
            for (Lesson l : c.getLessons()) {
                lessonsListModel.addElement(l.getLessonId() + " — " + l.getTitle());
            }
            lessonContentArea.setText("");
            updateProgress(c); 
        });

        lessonsList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int cidx = coursesList.getSelectedIndex();
            int lidx = lessonsList.getSelectedIndex();
            if (cidx < 0 || lidx < 0 || availableCourses == null) return;
            Lesson l = availableCourses.get(cidx).getLessons().get(lidx);
            lessonContentArea.setText(l.getContent());
        });
    }

    private void onEnrollClicked() {
        int idx = coursesList.getSelectedIndex();
        if (idx < 0 || availableCourses == null) {
            JOptionPane.showMessageDialog(this, "Select a course first");
            return;
        }
        int courseId = availableCourses.get(idx).getCourseId();
        boolean ok = studentManager.enroll(studentId, courseId);
        JOptionPane.showMessageDialog(this, ok ? "Enrolled successfully" : "Enroll failed (maybe already enrolled)");
        loadCourses();
    }

    private void onMarkCompletedClicked() {
        int cidx = coursesList.getSelectedIndex();
        int lidx = lessonsList.getSelectedIndex();
        if (cidx < 0 || lidx < 0 || availableCourses == null) {
            JOptionPane.showMessageDialog(this, "Select course and lesson first");
            return;
        }
        int courseId = availableCourses.get(cidx).getCourseId();
        int lessonId = availableCourses.get(cidx).getLessons().get(lidx).getLessonId();

        List<Course> enrolled = studentManager.getEnrolledCourses(studentId);
        boolean isEnrolled = false;
        for (Course c : enrolled) if (c.getCourseId() == courseId) { isEnrolled = true; break; }
        if (!isEnrolled) {
            JOptionPane.showMessageDialog(this, "You must enroll in the course before marking lessons.");
            return;
        }

        boolean markOk = studentManager.markLessonCompleted(studentId, courseId, lessonId);
        new LessonService(JsonDatabaseManager.getInstance()).markCompleted(courseId, lessonId, studentId);
        JOptionPane.showMessageDialog(this, "Lesson marked completed");

        
        Course selectedCourse = availableCourses.get(cidx);
        updateProgress(selectedCourse);
    }

    private void loadCourses() {
        availableCourses = studentManager.browseCourses();
        coursesListModel.clear();
        if (availableCourses != null) {
            for (Course c : availableCourses) {
                coursesListModel.addElement(c.getCourseId() + " — " + c.getTitle());
            }
        }
        lessonsListModel.clear();
        lessonContentArea.setText("");
        progressBar.setValue(0);
        progressLabel.setText("Progress: 0% (0/0 lessons)");
    }

    private void updateProgress(Course course) {
        List<Integer> completedLessons = studentManager.getCompletedLessons(studentId, course.getCourseId());
        int total = course.getLessons().size();
        int done = completedLessons != null ? completedLessons.size() : 0;

        int percent = total > 0 ? (done * 100 / total) : 0;
        progressBar.setValue(percent);
        progressLabel.setText("Progress: " + percent + "% (" + done + "/" + total + " lessons)");
    }
}
