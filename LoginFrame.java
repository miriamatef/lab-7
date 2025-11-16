package Lab6;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UserService userService;

    public LoginFrame(UserService userService) {
        super("SkillForge - Login");
        this.userService = userService;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 220);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField(18);
        JLabel lblPass = new JLabel("Password:");
        JPasswordField pfPass = new JPasswordField(18);
        JButton btnLogin = new JButton("Login");
        JButton btnSignup = new JButton("Go to Signup");

        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; panel.add(lblEmail, c);
        c.gridx = 1; panel.add(tfEmail, c);
        c.gridx = 0; c.gridy = 1; panel.add(lblPass, c);
        c.gridx = 1; panel.add(pfPass, c);
        c.gridx = 1; c.gridy = 2; panel.add(btnLogin, c);
        c.gridx = 1; c.gridy = 3; panel.add(btnSignup, c);

        // Add Enter key support - pressing Enter in password field triggers login
        pfPass.addActionListener(e -> btnLogin.doClick());

        btnLogin.addActionListener(e -> {
            try {
                String email = tfEmail.getText().trim();
                String pass = new String(pfPass.getPassword());
                if (email.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User user = userService.login(email, pass);
                JOptionPane.showMessageDialog(this, "Login successful. Role: " + user.getRole()
                        + "\nUsername: " + user.getUsername(), "Welcome", JOptionPane.INFORMATION_MESSAGE);

                // TODO: When dashboards are ready, replace this with:
                // if ("student".equals(user.getRole())) {
                //     new StudentDashboardFrame((Student) user, userService).setVisible(true);
                // } else {
                //     new InstructorDashboardFrame((Instructor) user, userService).setVisible(true);
                // }
                
                // For now, just close the login window after successful login
                dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
                pfPass.setText(""); // Clear password field for security
            }
        });

        btnSignup.addActionListener(e -> {
            SignupFrame sf = new SignupFrame(userService);
            sf.setVisible(true);
            dispose(); // Close login window when going to signup
        });

        add(panel);
    }
}