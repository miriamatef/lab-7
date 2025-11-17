package lab7.isa;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;

public class LoginFrame extends JFrame {
    private final UserService userService;

    public LoginFrame(UserService userService) {
        super("SkillForge - Login");
        this.userService = userService;
        init();
        checkLoggedInUser(); // auto-login if someone is already logged in
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 220);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel le = new JLabel("Email:");
        JTextField te = new JTextField(18);
        JLabel lp = new JLabel("Password:");
        JPasswordField tp = new JPasswordField(18);
        JButton btnLogin = new JButton("Login");
        JButton btnSignup = new JButton("Signup");

        // Layout components
        c.gridx = 0; c.gridy = 0; panel.add(le, c);
        c.gridx = 1; panel.add(te, c);
        c.gridx = 0; c.gridy = 1; panel.add(lp, c);
        c.gridx = 1; panel.add(tp, c);
        c.gridx = 1; c.gridy = 2; panel.add(btnLogin, c);
        c.gridx = 1; c.gridy = 3; panel.add(btnSignup, c);

        // Login action using email
        btnLogin.addActionListener(e -> {
            String email = te.getText().trim();
            String password = new String(tp.getPassword());
            JSONObject user = userService.loginByEmail(email, password); // <-- updated
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            openDashboard(user);
        });

        // Signup action
        btnSignup.addActionListener(e -> {
            new SignupFrame(userService).setVisible(true);
            dispose();
        });

        add(panel);
    }

    // Open appropriate dashboard
    private void openDashboard(JSONObject user) {
        String role = user.optString("role", "");
        String uid = user.optString("id", "");
        SwingUtilities.invokeLater(() -> {
            if ("instructor".equalsIgnoreCase(role)) {
                new InstructorDashboardFrame(uid, userService).setVisible(true);

            } else {
                new StudentDashboardFrame(uid).setVisible(true);
            }
        });
        dispose();
    }

    // Auto-login if a user is already logged in
    private void checkLoggedInUser() {
        JSONObject user = userService.getLoggedInUser();
        if (user != null) {
            openDashboard(user);
        }
    }
}
