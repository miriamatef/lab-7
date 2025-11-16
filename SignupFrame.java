package Lab6;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SignupFrame extends JFrame {
    private final UserService userService;

    public SignupFrame(UserService userService) {
        super("SkillForge - Signup");
        this.userService = userService;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lblRole = new JLabel("Role:");
        JComboBox<String> cbRole = new JComboBox<>(new String[] {"Student", "Instructor"});
        JLabel lblName = new JLabel("Username:");
        JTextField tfName = new JTextField(20);
        JLabel lblEmail = new JLabel("Email:");
        JTextField tfEmail = new JTextField(20);
        JLabel lblPass = new JLabel("Password:");
        JPasswordField pfPass = new JPasswordField(20);
        JButton btnSignup = new JButton("Sign up");

        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; panel.add(lblRole, c);
        c.gridx = 1; panel.add(cbRole, c);
        c.gridx = 0; c.gridy = 1; panel.add(lblName, c);
        c.gridx = 1; panel.add(tfName, c);
        c.gridx = 0; c.gridy = 2; panel.add(lblEmail, c);
        c.gridx = 1; panel.add(tfEmail, c);
        c.gridx = 0; c.gridy = 3; panel.add(lblPass, c);
        c.gridx = 1; panel.add(pfPass, c);
        c.gridx = 1; c.gridy = 4; panel.add(btnSignup, c);

        btnSignup.addActionListener(e -> {
            try {
                String role = (String) cbRole.getSelectedItem();
                String username = tfName.getText().trim();
                String email = tfEmail.getText().trim();
                String pass = new String(pfPass.getPassword());

                if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Password strength
                if (pass.length() < 6) {
                    JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if ("Student".equalsIgnoreCase(role)) {
                    userService.signupStudent(username, email, pass);
                } else {
                    userService.signupInstructor(username, email, pass);
                }

                JOptionPane.showMessageDialog(this, "Signup successful. You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Signup Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "I/O Error: " + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}