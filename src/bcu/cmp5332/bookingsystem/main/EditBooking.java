package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * Updates a customer's most recent booking to a new flight.
 * This supports the two-id command format:
 * updatebooking [customer id] [new flight id]
 */
public class EditBooking implements Command {

    private final int customerId;
    private final int newFlightId;

    public EditBooking(int customerId, int newFlightId) {
        this.customerId = customerId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {

        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        List<Booking> bookings = customer.getBookings();
        if (bookings.isEmpty()) {
            throw new FlightBookingSystemException("Customer has no bookings to update.");
        }

        // Update the most recent booking (last one in the list)
        Booking oldBooking = bookings.get(bookings.size() - 1);
        Flight oldFlight = oldBooking.getFlight();

        // Remove old booking link
        customer.cancelBookingForFlight(oldFlight);
        oldFlight.removePassenger(customer);

        // Create new booking
        Booking newBooking = new Booking(customer, newFlight, flightBookingSystem.getSystemDate());
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("Booking updated successfully.");
    }
}
