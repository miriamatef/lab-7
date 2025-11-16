/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab7;

/**
 *
 * @author carol
 */
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.json.*;


public class JsonDatabaseManager {
    public JSONArray readUsers() {
        JSONArray usersArray = new JSONArray();
        File file = new File("src/users.json"); 
        if (!file.exists()){ 
            return usersArray;
        }
        try (FileReader reader = new FileReader(file)) {
            if (file.length() == 0) {
                return usersArray;
            }
            usersArray = new JSONArray(new JSONTokener(reader));
        } catch (Exception e) {
            System.out.println("an error has occured from read coures");
 
        }
        return usersArray;
    }

    public JSONArray readCourses(){
        JSONArray coursesArray = new JSONArray();
        File file = new File("src/courses.json"); 
        if (!file.exists()){ 
            return coursesArray;
        }
        try (FileReader reader = new FileReader(file)) {
            if (file.length() == 0) {
                return coursesArray;
        }
          coursesArray = new JSONArray(new JSONTokener(reader));
        } catch (Exception e) {
            System.out.println("an error has occured from read coures");
        }
        return coursesArray;
    }
    
    public void writeUsers(JSONArray users){
        try (FileWriter file = new FileWriter("src/users.json")) {
            file.write(users.toString());           
        } catch (Exception e) {
            System.out.println("an error has occured from write users");
        }
    }
    
    public void writeCourses(JSONArray courses){
        try (FileWriter file = new FileWriter("src/courses.json")) {
            file.write(courses.toString());           
        } catch (Exception e) {       
            System.out.println("an error has occured from write courses");
        }
    }
    
    public String generateId(JSONArray array){
        Set<String> existingIds = new HashSet<>();
        for (Object obj : array) {
            JSONObject user = (JSONObject) obj;
            String id = (String) user.get("id");
            if (id != null) {
                existingIds.add(id);
            }
        }
        String newId;
        Random rand = new Random();
        do {
            newId = "U" +(1000 + rand.nextInt(9000));
        } while (existingIds.contains(newId));

        return newId;
    }

}
