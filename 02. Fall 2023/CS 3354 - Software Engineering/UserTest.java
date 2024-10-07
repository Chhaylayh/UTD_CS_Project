// UserTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    // Test case to check if the user is an adult
    @Test
    public void testIsAdult() {
        // Creating a user with age 25 (adult) and another with age 16 (minor)
        User adultUser = new User("Adult_User", "password123", 25);
        User minorUser = new User("Minor_User2", "password456", 16);

        // Asserting that the isAdult() method returns true for the adult user
        assertTrue(adultUser.isAdult(), "Adult user test failed");

        // Asserting that the isAdult() method returns false for the minor user
        assertFalse(minorUser.isAdult(), "Minor user test failed");
    }

    // Test case to check if the changePassword method works correctly
    @Test
    public void testChangePassword() {
        // Creating a user with the initial password "oldPassword"
        User user = new User("user123", "oldPassword", 30);

        // Asserting that the user's password is initially "oldPassword"
        assertEquals("oldPassword", user.getPassword(), "Initial password test failed");

        // Changing the user's password to "newPassword"
        user.changePassword("newPassword");

        // Asserting that the user's password has been changed to "newPassword"
        assertEquals("newPassword", user.getPassword(), "Password change test failed");
    }
// Test case to check if the user's age is updated correctly
    @Test
    public void testUpdateAge() {
        // Creating a user with the initial age of 25
        User user = new User("john_doe", "password123", 25);

        // Updating the user's age to 30
        user.updateAge(30);

        // Asserting that the user's age has been updated to 30
        assertEquals(30, user.getAge(), "Update age test failed");
    }


    // Test case to check if the username format is valid
    @Test
    public void testUsernameFormat() {
        // Creating a user with a valid username
        User validUser = new User("Valid_User", "password123", 25);

        // Creating a user with an invalid username (contains special characters)
        User invalidUser = new User("Invalid@User2", "pass456", 20);

        // Asserting that the valid username passes the format check
        assertTrue(validUser.isValidUsername(), "Valid username test failed");

        // Asserting that the invalid username fails the format check
        assertFalse(invalidUser.isValidUsername(), "Invalid username test failed");
    }
}
