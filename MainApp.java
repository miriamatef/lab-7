/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lab6;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
/**
 *
 * @author Mariam Elshamy
 */


public class MainApp {
    public static void main(String[] args) {
        // ensure users.json exists in working dir
        String usersFile = "users.json";
        try {
            if (!Files.exists(Paths.get(usersFile))) {
                Files.write(Paths.get(usersFile), "{}".getBytes());
            }
            UsersJsonManager manager = new UsersJsonManager(usersFile);
            UserService userService = new UserService(manager);

            SwingUtilities.invokeLater(() -> {
                LoginFrame lf = new LoginFrame(userService);
                lf.setVisible(true);
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize users.json: " + e.getMessage());
        }
    }
}