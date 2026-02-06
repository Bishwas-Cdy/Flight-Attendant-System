package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;

/**
 * Holds session information for authenticated GUI users.
 * Stores the current user, their role, and customer ID (if applicable).
 */
public class GuiSession {

    private final User user;
    private final Role role;
    private final Integer customerId; // null for admin

    /**
     * Creates a new GUI session for an authenticated user.
     *
     * @param user the authenticated user
     * @param role the user's role (ADMIN or CUSTOMER)
     * @param customerId the customer ID (null if user is admin)
     */
    public GuiSession(User user, Role role, Integer customerId) {
        this.user = user;
        this.role = role;
        this.customerId = customerId;
    }

    /**
     * Returns the authenticated user.
     *
     * @return the User object
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the user's role.
     *
     * @return the Role (ADMIN or CUSTOMER)
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the customer ID if the user is a customer.
     *
     * @return the customer ID, or null if user is an admin
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Checks if the user is an admin.
     *
     * @return true if user role is ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * Checks if the user is a customer.
     *
     * @return true if user role is CUSTOMER, false otherwise
     */
    public boolean isCustomer() {
        return role == Role.CUSTOMER;
    }
}
