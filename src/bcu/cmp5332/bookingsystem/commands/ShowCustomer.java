package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to show detailed information about a customer.
 */
public class ShowCustomer implements Command {

    private final int customerId;

    /**
     * Creates a ShowCustomer command.
     *
     * @param customerId the ID of the customer to display
     */
    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Executes the show customer command.
     * Displays detailed information about the customer and their bookings.
     *
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if customer is not found
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem)
            throws FlightBookingSystemException {

        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        System.out.println(customer.getDetailsLong());
    }
}
