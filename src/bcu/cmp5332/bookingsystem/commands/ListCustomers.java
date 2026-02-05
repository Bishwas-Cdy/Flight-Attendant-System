package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to list all customers in the system.
 */
public class ListCustomers implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        int count = 0;
        for (Customer c : flightBookingSystem.getCustomers()) {
            if (c.isActive()) {
                System.out.println(c.getDetailsShort());
                count++;
            }
        }
        System.out.println(count + " customer(s)");
    }
}
