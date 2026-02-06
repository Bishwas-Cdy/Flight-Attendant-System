package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to add a new customer to the system.
 */
public class AddCustomer implements Command {

    private final String name;
    private final String phone;

    /**
     * Creates an AddCustomer command.
     *
     * @param name the customer's full name
     * @param phone the customer's phone number
     */
    public AddCustomer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Executes the add customer command.
     * Adds a new customer to the system with auto-generated ID.
     *
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if customer cannot be added
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem)
            throws FlightBookingSystemException {

        int newId = flightBookingSystem.getCustomers().size() + 1;

        Customer customer = new Customer(newId, name, phone);
        flightBookingSystem.addCustomer(customer);

        System.out.println("Customer added successfully with ID " + newId);
    }
}
