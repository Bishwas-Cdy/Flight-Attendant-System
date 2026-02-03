package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to list all customers in the system.
 */
public class ListCustomers implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        for (Customer c : flightBookingSystem.getCustomers()) {
            System.out.println(c.getDetailsShort());
        }
        System.out.println(flightBookingSystem.getCustomers().size() + " customer(s)");
    }
}
