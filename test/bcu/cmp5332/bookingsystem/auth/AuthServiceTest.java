package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthService class.
 * Tests user registration, email validation, and authentication.
 */
public class AuthServiceTest {

    private AuthService authService;
    private List<User> users;

    @BeforeEach
    public void setup() {
        users = new ArrayList<>();
        authService = new AuthService(users);
    }

    @Test
    public void testRegisterCustomer_Success() throws FlightBookingSystemException {
        User user = authService.registerCustomer("John", "David", "Doe", 
                                                  "john@gmail.com", "password123");
        
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@gmail.com", user.getEmail());
        assertEquals(Role.CUSTOMER, user.getRole());
    }

    @Test
    public void testRegisterCustomer_AddsToList() throws FlightBookingSystemException {
        assertEquals(0, users.size());
        
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertEquals(1, users.size());
    }

    @Test
    public void testRegisterCustomer_DuplicateEmail_ThrowsException() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertThrows(FlightBookingSystemException.class, () -> {
            authService.registerCustomer("Jane", "", "Smith", "john@gmail.com", "password456");
        });
    }

    @Test
    public void testRegisterAdmin_Success() throws FlightBookingSystemException {
        User admin = authService.registerAdmin("Admin", "", "User", 
                                               "admin@gmail.com", "admin123");
        
        assertEquals("Admin", admin.getFirstName());
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    public void testEmailExists_WithExistingEmail() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertTrue(authService.emailExists("john@gmail.com"));
    }

    @Test
    public void testEmailExists_WithNonExistingEmail() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertFalse(authService.emailExists("notexist@gmail.com"));
    }

    @Test
    public void testEmailExists_CaseInsensitive() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertTrue(authService.emailExists("JOHN@GMAIL.COM"));
        assertTrue(authService.emailExists("John@Gmail.Com"));
    }

    @Test
    public void testLogin_CorrectCredentials() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        User user = authService.login("john@gmail.com", "password123");
        
        assertEquals("john@gmail.com", user.getEmail());
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testLogin_WrongPassword_ThrowsException() throws FlightBookingSystemException {
        authService.registerCustomer("John", "", "Doe", "john@gmail.com", "password123");
        
        assertThrows(FlightBookingSystemException.class, () -> {
            authService.login("john@gmail.com", "wrongpassword");
        });
    }

    @Test
    public void testLogin_NonExistentEmail_ThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> {
            authService.login("notexist@gmail.com", "password123");
        });
    }

    @Test
    public void testRegisterCustomer_FullNameSet() throws FlightBookingSystemException {
        User user = authService.registerCustomer("John", "David", "Doe", 
                                                  "john@gmail.com", "password123");
        
        String fullName = user.getFullName();
        assertTrue(fullName.contains("John"));
        assertTrue(fullName.contains("David"));
        assertTrue(fullName.contains("Doe"));
    }
}
