package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Short version of updatebooking.
 * Edits (updates) the most recent booking to a new flight.
 */
public class EditBooking implements Command {

    private final int customerId;
    private final int newFlightId;

    public EditBooking(int customerId, int newFlightId) {
        this.customerId = customerId;
        this.newFlightId = newFlightId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        Customer customer = fbs.getCustomerByID(customerId);

        if (customer.getBookings().isEmpty()) {
            throw new FlightBookingSystemException("Customer has no bookings to update.");
        }

        // Most recent booking = last in the list (simple assumption)
        Booking lastBooking = customer.getBookings().get(customer.getBookings().size() - 1);
        int oldFlightId = lastBooking.getFlight().getId();

        // Reuse the full UpdateBooking logic by creating the command and executing it
        UpdateBooking updater = new UpdateBooking(customerId, oldFlightId, newFlightId);
        updater.execute(fbs);
    }
}
