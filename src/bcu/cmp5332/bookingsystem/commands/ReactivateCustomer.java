package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Reactivates a customer account. Admin only.
 */
public class ReactivateCustomer implements Command {

    private final int customerId;
    private User currentUser;

    public ReactivateCustomer(int customerId, User currentUser) {
        this.customerId = customerId;
        this.currentUser = currentUser;
    }

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
