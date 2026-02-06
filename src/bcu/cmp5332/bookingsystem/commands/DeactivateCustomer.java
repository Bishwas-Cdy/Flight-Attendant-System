package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Deactivates a customer account (soft delete). Admin only.
 */
public class DeactivateCustomer implements Command {

    private final int customerId;
    private User currentUser;

    /**
     * Creates a DeactivateCustomer command.
     *
     * @param customerId the ID of the customer to deactivate
     * @param currentUser the admin user executing the command
     */
    public DeactivateCustomer(int customerId, User currentUser) {
        this.customerId = customerId;
        this.currentUser = currentUser;
    }

    /**
     * Executes the deactivate customer command.
     * Prevents the customer from logging in and using the system.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if not admin or customer not found
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new FlightBookingSystemException("Only admin can deactivate customers.");
        }

        Customer c = fbs.getCustomerByID(customerId);
        c.deactivate();

        System.out.println("Customer deactivated successfully.");
    }
}
