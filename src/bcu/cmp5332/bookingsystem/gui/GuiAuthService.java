package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.util.List;

/**
 * GUI-side authentication service.
 * Uses the already-loaded UserDataManager for login operations.
 */
public class GuiAuthService {

    private final UserDataManager udm;

    /**
     * Creates a new GuiAuthService with the user data manager.
     *
     * @param udm the UserDataManager containing all system users
     */
    public GuiAuthService(UserDataManager udm) {
        this.udm = udm;
    }

    /**
     * Authenticates a user via GUI login.
     * Creates and returns a GuiSession if credentials are valid.
     *
     * @param email the user's email
     * @param password the user's password
     * @return a GuiSession for the authenticated user
     * @throws FlightBookingSystemException if credentials are invalid or validation fails
     */
    public GuiSession login(String email, String password) throws FlightBookingSystemException {
        if (email == null || email.isBlank()) {
            throw new FlightBookingSystemException("Email cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new FlightBookingSystemException("Password cannot be empty.");
        }

        List<User> users = udm.getUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                if (!u.getPassword().equals(password)) {
                    throw new FlightBookingSystemException("Invalid email or password.");
                }

                Role role = u.getRole();
                Integer customerId = null;
                if (role == Role.CUSTOMER) {
                    customerId = extractCustomerId(u);
                    if (customerId == null) {
                        throw new FlightBookingSystemException("Customer account is not linked properly.");
                    }
                }

                return new GuiSession(u, role, customerId);
            }
        }

        throw new FlightBookingSystemException("Invalid email or password.");
    }

    /**
     * Change this if your User class uses a different getter name.
     */
    private Integer extractCustomerId(User u) {
        // Expected to exist in your project
        return u.getCustomerId();
    }
}
