/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lab6;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author Mariam Elshamy
 */

public class UsersJsonManager {
    private final Path filePath;
    private final Gson gson;
    private Map<String, JsonObject> usersById; // keep raw objects so we preserve subclass fields

    public UsersJsonManager(String filename) throws IOException {
        this.filePath = Paths.get(filename);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        load();
    }

    private void load() throws IOException {
        usersById = new LinkedHashMap<>();
        if (Files.exists(filePath)) {
            String content = new String(Files.readAllBytes(filePath));
            if (!content.trim().isEmpty()) {
                Type mapType = new TypeToken<Map<String, JsonObject>>(){}.getType();
                Map<String, JsonObject> loaded = gson.fromJson(content, mapType);
                if (loaded != null) usersById.putAll(loaded);
            }
        } else {  // create empty file
            save();
        }
    }

    public synchronized void save() throws IOException {
        String out = gson.toJson(usersById);
        Files.write(filePath, out.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public synchronized boolean emailExists(String email) {
        return usersById.values().stream().anyMatch(obj -> {
            JsonElement e = obj.get("email");
            return e != null && email.equalsIgnoreCase(e.getAsString());
        });
    }

    public synchronized boolean idExists(String id) {
        return usersById.containsKey(id);
    }

    public synchronized String generateUniqueUserId() {
        String id;
        do {
            id = "U" + (1000 + new Random().nextInt(9000));
        } while (idExists(id));
        return id;
    }

    public synchronized void addUser(JsonObject userObj) throws IOException {
        String id = userObj.get("userId").getAsString();
        if (idExists(id)) throw new IllegalArgumentException("Duplicate userId");
        usersById.put(id, userObj);
        save();
    }

    public synchronized Optional<JsonObject> findByEmail(String email) {
        return usersById.values().stream()
                .filter(obj -> obj.has("email") && email.equalsIgnoreCase(obj.get("email").getAsString()))
                .findFirst();
    }

    public synchronized void updateUser(JsonObject userObj) throws IOException {
        String id = userObj.get("userId").getAsString();
        if (!idExists(id)) throw new IllegalArgumentException("User id not found");
        usersById.put(id, userObj);
        save();
    }

    // get all users as Jsonobjects
    public synchronized List<JsonObject> getAllUsers() {
        return new ArrayList<>(usersById.values());
    }
}