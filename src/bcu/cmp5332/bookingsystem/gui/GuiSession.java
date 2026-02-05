package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;

/**
 * Simple session holder for GUI.
 */
public class GuiSession {

    private final User user;
    private final Role role;
    private final Integer customerId; // null for admin

    public GuiSession(User user, Role role, Integer customerId) {
        this.user = user;
        this.role = role;
        this.customerId = customerId;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isCustomer() {
        return role == Role.CUSTOMER;
    }
}
