package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.util.List;

public class AuthService {

    private final List<User> users;

    public AuthService(List<User> users) {
        this.users = users;
    }

    public User login(String email, String password) throws FlightBookingSystemException {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        throw new FlightBookingSystemException("Invalid email or password.");
    }

    public User registerCustomer(String first, String middle, String last,
                                 String email, String password) throws FlightBookingSystemException {

        validateUniqueEmail(email);

        int newId = users.size() + 1;
        User user = new User(newId, first, middle, last, email, password, Role.CUSTOMER, null);
        users.add(user);
        return user;
    }

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

    private void validateUniqueEmail(String email) throws FlightBookingSystemException {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new FlightBookingSystemException("Email already exists.");
            }
        }
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email cannot be empty.");
        }
    }
}
