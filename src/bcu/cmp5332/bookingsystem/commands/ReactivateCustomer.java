package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Reactivates a previously deactivated customer account. Admin only.
 */
public class ReactivateCustomer implements Command {

    private final int customerId;
    private User currentUser;

    /**
     * Creates a ReactivateCustomer command.
     *
     * @param customerId the ID of the customer to reactivate
     * @param currentUser the admin user executing the command
     */
    public ReactivateCustomer(int customerId, User currentUser) {
        this.customerId = customerId;
        this.currentUser = currentUser;
    }

    /**
     * Executes the reactivate customer command.
     * Allows the previously deactivated customer to log in and use the system again.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if not admin or customer not found
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new FlightBookingSystemException("Only admin can reactivate customers.");
        }

        Customer c = fbs.getCustomerByID(customerId);
        c.reactivate();

        System.out.println("Customer reactivated successfully.");
    }
}
