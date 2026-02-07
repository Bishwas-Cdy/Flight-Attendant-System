package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.util.List;

/**
 * Provides authentication and user management services.
 * Handles user login, customer registration, and admin account creation.
 */
public class AuthService {

    private final List<User> users;

    /**
     * Creates a new AuthService with the given list of users.
     *
     * @param users the list of system users
     */
    public AuthService(List<User> users) {
        this.users = users;
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param email the user's email
     * @param password the user's password
     * @return the authenticated User
     * @throws FlightBookingSystemException if email or password is invalid
     */
    public User login(String email, String password) throws FlightBookingSystemException {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        throw new FlightBookingSystemException("Invalid email or password.");
    }

    /**
     * Registers a new customer account.
     *
     * @param first customer's first name
     * @param middle customer's middle name
     * @param last customer's last name
     * @param email customer's email (must be unique)
     * @param password customer's password
     * @return the newly created Customer User
     * @throws FlightBookingSystemException if email already exists or validation fails
     */
    public User registerCustomer(String first, String middle, String last,
                                 String email, String password) throws FlightBookingSystemException {

        validateUniqueEmail(email);

        int newId = users.size() + 1;
        User user = new User(newId, first, middle, last, email, password, Role.CUSTOMER, null);
        users.add(user);
        return user;
    }

    /**
     * Creates a new admin account. Only existing admins can call this.
     *
     * @param currentUser the current authenticated user (must be admin)
     * @param first admin's first name
     * @param middle admin's middle name
     * @param last admin's last name
     * @param email admin's email (must be unique)
     * @param password admin's password
     * @return the newly created Admin User
     * @throws FlightBookingSystemException if currentUser is not admin or email exists
     */
    public User createAdmin(User currentUser, String first, String middle, String last,
                            String email, String password) throws FlightBookingSystemException {

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new FlightBookingSystemException("Only admin can create another admin.");
        }

        validateUniqueEmail(email);

        int newId = users.size() + 1;
        User admin = new User(newId, first, middle, last, email, password, Role.ADMIN, null);
        users.add(admin);
        return admin;
    }

    /**
     * Checks if an email is already registered in the system.
     *
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private void validateUniqueEmail(String email) throws FlightBookingSystemException {
        if (emailExists(email)) {
            throw new FlightBookingSystemException("Email already exists.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email cannot be empty.");
        }
    }
}
