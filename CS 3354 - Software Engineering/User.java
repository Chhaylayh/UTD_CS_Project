// User.java
public class User {
    private String username;
    private String password;
    private int age;

    // Constructor
    public User(String username, String password, int age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    // Method to check if the user is an adult
    public boolean isAdult() {
        return age >= 18;
    }

    // Method to change the user's password
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Method to update the user's age
    public void updateAge(int newAge) {
        this.age = newAge;
    }

    // Method to check if the username format is valid
    public boolean isValidUsername() {
        // Simple check: only allowing alphanumeric characters and underscores
        return username.matches("^[a-zA-Z0-9_]+$");
    }
}
