/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7;

/**
 *
 * @author carol
 */
import org.json.JSONArray;
import org.json.JSONObject;
import java.security.MessageDigest;

public class UserService {
    private JsonDatabaseManager db;

    public UserService(JsonDatabaseManager db) {
        this.db = db;
    }

    
    public boolean signup(String role, String username, String email, String password) {
        JSONArray users=db.readUsers();
        if(!checkVaildEmail(email))
            return false;
        for(Object obj : users){
            JSONObject user = (JSONObject) obj;
            if(user.getString("email").equals(email)){
                System.out.println("you already have an account, go to login");
                return false;
            }
        }
        
        String hashedPassword = hashPassword(password);
        JSONObject newUser = new JSONObject();
        newUser.put("id", db.generateId(users));
        newUser.put("role",role);
        newUser.put("email", email);
        newUser.put("password", hashedPassword);
        users.put(newUser);
        db.writeUsers(users);
        System.out.println("Sign up Successful");
        return true;       
    }


    public JSONObject login(String email, String password) {
        JSONArray users = db.readUsers();
        String hashed = hashPassword(password);
        for(Object obj : users){
            JSONObject user = (JSONObject) obj;
            if(user.getString("email").equals(email) && user.getString("password").equals(hashed)){
                    System.out.println("Login Successful");
                    System.out.println("Welcome " + user.getString("role"));
                    return user;
            } 
        }
        System.out.println("Incorrct username or password");
        return null;
    }

    public JSONObject getUserByEmail(String email) {
        if(!checkVaildEmail(email))
            return null;
        JSONArray users = db.readUsers();
        for(Object obj :users){
            JSONObject user = (JSONObject) obj;
            if(user.getString("email").equals(email))
                return user;
        }
        System.out.println("email not found");
        return null;
    }

    boolean checkVaildEmail(String email){
        if (email == null || email.isEmpty()) {
            System.out.println("email not vaild");
             return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("email not vaild");
            return false;
        }
        return true;
    }
    
    public  void logout (String email, String password) {
        JSONArray users = db.readUsers();
        String hashed = hashPassword(password);
        for(Object obj : users){
            JSONObject user = (JSONObject) obj;
            if(user.getString("email").equals(email) && user.getString("password").equals(hashed)){
                    System.out.println("Logout Successful");     
            } 
        }
        System.out.println("logout unsuccessful. Enter correct email and password");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

