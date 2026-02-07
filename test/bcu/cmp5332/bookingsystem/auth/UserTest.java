package bcu.cmp5332.bookingsystem.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for User class.
 * Tests user creation, role assignment, and customer linkage.
 */
public class UserTest {

    private User adminUser;
    private User customerUser;

    @BeforeEach
    public void setup() {
        adminUser = new User(1, "Admin", "", "User", "admin@gmail.com", "password123", 
                            Role.ADMIN, null);
        customerUser = new User(2, "John", "", "Doe", "john@gmail.com", "password456", 
                               Role.CUSTOMER, 5);
    }

    @Test
    public void testUserCreation_Admin() {
        assertEquals(1, adminUser.getId());
        assertEquals("admin@gmail.com", adminUser.getEmail());
        assertEquals(Role.ADMIN, adminUser.getRole());
    }

    @Test
    public void testUserCreation_Customer() {
        assertEquals(2, customerUser.getId());
        assertEquals("john@gmail.com", customerUser.getEmail());
        assertEquals(Role.CUSTOMER, customerUser.getRole());
    }

    @Test
    public void testAdminUser_HasNoCustomerId() {
        assertNull(adminUser.getCustomerId());
    }

    @Test
    public void testCustomerUser_HasCustomerId() {
        assertEquals(5, customerUser.getCustomerId());
        assertNotNull(customerUser.getCustomerId());
    }

    @Test
    public void testSetCustomerId() {
        User user = new User(3, "Test", "", "User", "test@gmail.com", "pass123", 
                            Role.CUSTOMER, null);
        assertNull(user.getCustomerId());
        
        user.setCustomerId(10);
        assertEquals(10, user.getCustomerId());
    }

    @Test
    public void testUserFullName_Admin() {
        String fullName = adminUser.getFullName();
        assertTrue(fullName.contains("Admin"));
        assertTrue(fullName.contains("User"));
    }

    @Test
    public void testUserFullName_Customer() {
        String fullName = customerUser.getFullName();
        assertTrue(fullName.contains("John"));
        assertTrue(fullName.contains("Doe"));
    }

    @Test
    public void testUserPassword_NotEmpty() {
        assertNotNull(adminUser.getPassword());
        assertNotNull(customerUser.getPassword());
        assertFalse(adminUser.getPassword().isEmpty());
    }

    @Test
    public void testUserEmail_NotEmpty() {
        assertNotNull(adminUser.getEmail());
        assertNotNull(customerUser.getEmail());
        assertFalse(adminUser.getEmail().isEmpty());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Admin", adminUser.getFirstName());
        assertEquals("John", customerUser.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("User", adminUser.getLastName());
        assertEquals("Doe", customerUser.getLastName());
    }
}
