package bcu.cmp5332.bookingsystem.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Role-based access control.
 * Tests that admin and customer roles are properly defined.
 */
public class RoleTest {

    @Test
    public void testAdminRoleExists() {
        assertNotNull(Role.ADMIN);
        assertEquals("ADMIN", Role.ADMIN.toString());
    }

    @Test
    public void testCustomerRoleExists() {
        assertNotNull(Role.CUSTOMER);
        assertEquals("CUSTOMER", Role.CUSTOMER.toString());
    }

    @Test
    public void testAdminAndCustomerAreDifferent() {
        assertNotEquals(Role.ADMIN, Role.CUSTOMER);
    }

    @Test
    public void testRoleComparison_Admin() {
        Role role = Role.ADMIN;
        
        assertTrue(role == Role.ADMIN);
        assertFalse(role == Role.CUSTOMER);
    }

    @Test
    public void testRoleComparison_Customer() {
        Role role = Role.CUSTOMER;
        
        assertTrue(role == Role.CUSTOMER);
        assertFalse(role == Role.ADMIN);
    }
}
