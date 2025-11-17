public synchronized boolean signup(String role, String username, String email, String password) {
    if (username == null || username.isBlank() ||
        email == null || email.isBlank() ||
        password == null || password.isBlank() ||
        role == null || role.isBlank()) return false;

    if (username.matches("\\d+")) return false;

    JSONArray users = db.readUsers();

    for (Object o : users) {
        JSONObject u = (JSONObject) o;
        if (u.getString("username").equalsIgnoreCase(username) ||
            u.getString("email").equalsIgnoreCase(email)) return false;
    }

    JSONObject newUser = new JSONObject();
    newUser.put("id", db.generateUserId());
    newUser.put("role", role);
    newUser.put("username", username);
    newUser.put("email", email);
    newUser.put("password", password);
    newUser.put("loggedIn", false);

    users.put(newUser);
    db.writeUsers(users); // persist immediately
    return true;
}
