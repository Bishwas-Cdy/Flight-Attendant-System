package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Adds a booking for a customer on a flight.
 */
public class AddBooking implements Command {

    private final int customerId;
    private final int flightId;

    public AddBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        Customer customer = fbs.getCustomerByID(customerId);
        Flight flight = fbs.getFlightByID(flightId);

        // Capacity enforcement
        int capacity = flight.getCapacity();
        int currentPassengers = flight.getPassengers().size();

        if (capacity > 0 && currentPassengers >= capacity) {
            throw new FlightBookingSystemException(
                    "Cannot add booking. Flight is full (" + capacity + " seats).");
        }

        // Store price at booking time (for now: base price)
        double priceAtBooking = flight.getBasePrice();

        Booking booking = new Booking(customer, flight, fbs.getSystemDate(), priceAtBooking);

        customer.addBooking(booking);
        flight.addPassenger(customer);

        System.out.println("Booking added successfully. Price: " + String.format("%.2f", priceAtBooking));
    }
}
