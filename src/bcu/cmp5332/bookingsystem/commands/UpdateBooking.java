package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to update a booking to a different flight.
 */
public class UpdateBooking implements Command {

    private final int customerId;
    private final int oldFlightId;
    private final int newFlightId;

    public UpdateBooking(int customerId, int oldFlightId, int newFlightId) {
        this.customerId = customerId;
        this.oldFlightId = oldFlightId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem)
            throws FlightBookingSystemException {

        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight oldFlight = flightBookingSystem.getFlightByID(oldFlightId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);

        customer.cancelBookingForFlight(oldFlight);
        oldFlight.removePassenger(customer);

        Booking newBooking = new Booking(customer, newFlight,
                flightBookingSystem.getSystemDate());

        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        System.out.println("Booking updated successfully.");
    }
}
