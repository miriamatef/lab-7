/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Course;
import models.User;
import models.Student;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonDatabaseManager {

    private static final Logger logger = Logger.getLogger(JsonDatabaseManager.class.getName());

    private final File usersFile;
    private final File coursesFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonDatabaseManager(String usersPath, String coursesPath) {
        this.usersFile = new File(usersPath);
        this.coursesFile = new File(coursesPath);
        ensureExists(usersFile, "[]");
        ensureExists(coursesFile, "[]");
    }

    private void ensureExists(File f, String defaultContent) {
        try {
            if (!f.exists()) {
                try (FileWriter fw = new FileWriter(f)) {
                    fw.write(defaultContent);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create file: " + f.getName(), e);
            throw new RuntimeException("Failed to create file: " + f.getName(), e);
        }
    }

    public List<User> loadUsers() {
        try (FileReader fr = new FileReader(usersFile)) {
            Type generic = new TypeToken<List<Object>>() {}.getType();
            List<Object> raw = gson.fromJson(fr, generic);
            List<User> users = new ArrayList<>();

            if (raw != null) {
                for (Object o : raw) {
                    String json = gson.toJson(o);
                    com.google.gson.JsonObject jo = gson.fromJson(json, com.google.gson.JsonObject.class);
                    String role = jo.has("role") ? jo.get("role").getAsString() : "STUDENT";

                    if ("STUDENT".equalsIgnoreCase(role)) {
                        Student s = gson.fromJson(json, Student.class);
                        users.add(s);
                    } else {
                        User u = gson.fromJson(json, User.class); // other roles
                        users.add(u);
                    }
                }
            }

            return users;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load users from JSON", e);
            return new ArrayList<>(); // return empty list if failed
        }
    }

    public void saveUsers(List<User> users) {
        try (FileWriter fw = new FileWriter(usersFile)) {
            gson.toJson(users, fw);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save users to JSON", e);
            throw new RuntimeException("Failed to save users to JSON", e);
        }
    }

    public List<Course> loadCourses() {
        try (FileReader fr = new FileReader(coursesFile)) {
            Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
            List<Course> courses = gson.fromJson(fr, listType);
            return courses != null ? courses : new ArrayList<>();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load courses from JSON", e);
            return new ArrayList<>();
        }
    }

    public void saveCourses(List<Course> courses) {
        try (FileWriter fw = new FileWriter(coursesFile)) {
            gson.toJson(courses, fw);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save courses to JSON", e);
            throw new RuntimeException("Failed to save courses to JSON", e);
        }
    }

    public Optional<Course> findCourseById(String courseId) {
        return loadCourses().stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst();
    }

    public Optional<User> findUserById(String userId) {
        return loadUsers().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }
}
