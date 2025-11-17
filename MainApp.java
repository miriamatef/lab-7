package lab7.isa;

import javax.swing.*;
import java.nio.file.*;

public class MainApplication {
    public static void main(String[] args) {
        try {
            // ensure files exist
            Files.write(Paths.get("users.json"), "[]".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            Files.write(Paths.get("courses.json"), "[]".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception ignored) {}

        JsonDatabaseManager db = JsonDatabaseManager.getInstance();
        UserService userService = new UserService(db);
        SwingUtilities.invokeLater(() -> {
            LoginFrame f = new LoginFrame(userService);
            f.setVisible(true);
        });
    }
}
