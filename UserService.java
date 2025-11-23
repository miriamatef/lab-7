/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package lab7.isa;

import org.json.*;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private JSONObject studentData;
    public Student(JSONObject studentData) {
        this.studentData = studentData;
        // Ensure required arrays exist
        if (!studentData.has("enrolledCourses")) {
            studentData.put("enrolledCourses", new JSONArray());
        }
        if (!studentData.has("progress")) {
            studentData.put("progress", new JSONArray());
        }
    }
    
    public Student(String userId, String username, String email, String passwordHash) {
        this.studentData = new JSONObject();
        studentData.put("id", userId);
        studentData.put("role", "student");
        studentData.put("username", username);
        studentData.put("email", email);
        studentData.put("password", passwordHash);
        studentData.put("enrolledCourses", new JSONArray());
        studentData.put("progress", new JSONArray());
    }

    public Student() {
        this.studentData = new JSONObject();
        studentData.put("role", "student");
        studentData.put("enrolledCourses", new JSONArray());
        studentData.put("progress", new JSONArray());
    }
    
    public JSONObject toJSON() {
        return studentData;
    }
    
    public String getUserId() {
        return studentData.optString("id", "");
    }
    
    public String getUsername() {
        return studentData.optString("username", "");
    }
    
    public String getEmail() {
        return studentData.optString("email", "");
    }
    
    public String getPasswordHash() {
        return studentData.optString("password", "");
    }
    
    public String getRole() {
        return studentData.optString("role", "student");
    }
    
    public List<Integer> getEnrolledCourses() {
        List<Integer> courses = new ArrayList<>();
        JSONArray coursesArray = studentData.optJSONArray("enrolledCourses");
        if (coursesArray != null) {
            for (Object obj : coursesArray) {
                if (obj instanceof Integer) {
                    courses.add((Integer) obj);
                } else if (obj instanceof String) {
                    try {
                        courses.add(Integer.parseInt((String) obj));
                    } catch (NumberFormatException e) {
                      
                    }
                }
            }
        }
        return courses;
    }
    public List<Progress> getProgress() {
        List<Progress> progressList = new ArrayList<>();
        JSONArray progressArray = studentData.optJSONArray("progress");
        if (progressArray != null) {
            for (Object obj : progressArray) {
                if (obj instanceof JSONObject) {
                    progressList.add(new Progress((JSONObject) obj));
                }
            }
        }
        return progressList;
    }
    
    public void setUsername(String username) {
        studentData.put("username", username);
    }
    
    public void setEmail(String email) {
        studentData.put("email", email);
    }
    
    public void setPasswordHash(String passwordHash) {
        studentData.put("password", passwordHash);
    }
   
    public void setEnrolledCourses(List<Integer> enrolledCourses) {
        JSONArray coursesArray = new JSONArray();
        for (Integer courseId : enrolledCourses) {
            coursesArray.put(courseId);
        }
        studentData.put("enrolledCourses", coursesArray);
    }
    
  
    public void setProgress(List<Progress> progress) {
        JSONArray progressArray = new JSONArray();
        for (Progress p : progress) {
            progressArray.put(p.toJSON());
        }
        studentData.put("progress", progressArray);
    }
    

    public void enrollCourse(int courseId) {
        JSONArray coursesArray = studentData.optJSONArray("enrolledCourses");
        if (coursesArray == null) {
            coursesArray = new JSONArray();
            studentData.put("enrolledCourses", coursesArray);
        }
        

        for (Object obj : coursesArray) {
            if (obj instanceof Integer && (Integer) obj == courseId) {
                return;
            }
        }
        
    
        coursesArray.put(courseId);
        

        JSONArray progressArray = studentData.optJSONArray("progress");
        if (progressArray == null) {
            progressArray = new JSONArray();
            studentData.put("progress", progressArray);
        }
        

        boolean progressExists = false;
        for (Object obj : progressArray) {
            if (obj instanceof JSONObject) {
                JSONObject prog = (JSONObject) obj;
                if (prog.optInt("courseId", -1) == courseId) {
                    progressExists = true;
                    break;
                }
            }
        }
        
        if (!progressExists) {
            Progress newProgress = new Progress(courseId);
            progressArray.put(newProgress.toJSON());
        }
    }
    
    public Progress getProgressForCourse(int courseId) {
        JSONArray progressArray = studentData.optJSONArray("progress");
        if (progressArray == null) return null;
        
        for (Object obj : progressArray) {
            if (obj instanceof JSONObject) {
                JSONObject prog = (JSONObject) obj;
                if (prog.optInt("courseId", -1) == courseId) {
                    return new Progress(prog);
                }
            }
        }
        return null;
    }
    
    public void unenrollCourse(int courseId) {
        JSONArray coursesArray = studentData.optJSONArray("enrolledCourses");
        if (coursesArray != null) {
            JSONArray newCourses = new JSONArray();
            for (Object obj : coursesArray) {
                if (!(obj instanceof Integer && (Integer) obj == courseId)) {
                    newCourses.put(obj);
                }
            }
            studentData.put("enrolledCourses", newCourses);
        }
        
      
        JSONArray progressArray = studentData.optJSONArray("progress");
        if (progressArray != null) {
            JSONArray newProgress = new JSONArray();
            for (Object obj : progressArray) {
                if (obj instanceof JSONObject) {
                    JSONObject prog = (JSONObject) obj;
                    if (prog.optInt("courseId", -1) != courseId) {
                        newProgress.put(prog);
                    }
                }
            }
            studentData.put("progress", newProgress);
        }
    }
    
    
    public boolean isEnrolledIn(int courseId) {
        List<Integer> enrolled = getEnrolledCourses();
        return enrolled.contains(courseId);
    }
    
    @Override
    public String toString() {
        return getUsername() + " (Student)";
    }
}

