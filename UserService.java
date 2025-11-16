package Lab6;
import com.google.gson.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserService {
    private final UsersJsonManager jsonManager;
    private final Gson gson;
    private User currentUser = null;

    public UserService(UsersJsonManager jsonManager) {
        this.jsonManager = jsonManager;
        this.gson = new GsonBuilder().create();
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Student signupStudent(String username, String email, String password) throws IOException {
        validateEmailFormat(email);
        if (jsonManager.emailExists(email)) throw new IllegalArgumentException("Email already registered");
        String id = jsonManager.generateUniqueUserId();
        String hash = sha256Hex(password);
        Student s = new Student(id, username, email, hash);

        JsonObject obj = (JsonObject) gson.toJsonTree(s);
        jsonManager.addUser(obj);
        return s;
    }

    public synchronized Instructor signupInstructor(String username, String email, String password) throws IOException {
        validateEmailFormat(email);
        if (jsonManager.emailExists(email)) throw new IllegalArgumentException("Email already registered");
        String id = jsonManager.generateUniqueUserId();
        String hash = sha256Hex(password);
        Instructor ins = new Instructor(id, username, email, hash);

        JsonObject obj = (JsonObject) gson.toJsonTree(ins);
        jsonManager.addUser(obj);
        return ins;
    }

    public synchronized User login(String email, String password) {
        Optional<JsonObject> optional = jsonManager.findByEmail(email);
        if (!optional.isPresent()) throw new IllegalArgumentException("Email not found");
        JsonObject obj = optional.get();
        String storedHash = obj.get("passwordHash").getAsString();
        String role = obj.get("role").getAsString();

        String hash = sha256Hex(password);
        if (!hash.equals(storedHash)) throw new IllegalArgumentException("Incorrect password");

        // construct proper subclass
        if ("student".equalsIgnoreCase(role)) {
            Student s = gson.fromJson(obj, Student.class);
            currentUser = s;
            return s;
        } else {
            Instructor ins = gson.fromJson(obj, Instructor.class);
            currentUser = ins;
            return ins;
        }
    }

    public synchronized void logout() {
        currentUser = null;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    private void validateEmailFormat(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new IllegalArgumentException("Invalid email format");
    }
}