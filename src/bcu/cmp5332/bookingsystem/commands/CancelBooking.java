package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Cancels a booking and applies a cancellation fee.
 * Fee is 10% of booking price with a minimum of $5.
 */
public class CancelBooking implements Command {

    private final int customerId;
    private final int flightId;

    /**
     * Creates a CancelBooking command.
     *
     * @param customerId the customer's ID
     * @param flightId the flight's ID
     */
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    /**
     * Executes the cancel booking command.
     * Cancels the booking and calculates refund amount after fee deduction.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if booking not found or cancellation fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        Customer customer = fbs.getCustomerByID(customerId);
        Flight flight = fbs.getFlightByID(flightId);

        Booking booking = findBooking(customer, flightId);
        if (booking == null) {
            throw new FlightBookingSystemException("Booking not found for this customer and flight.");
        }

        double bookingPrice = booking.getBookingPrice();
        double fee = bookingPrice * 0.10; // 10%
        if (fee < 5.0) {
            fee = 5.0;
        }

        double refund = bookingPrice - fee;
        if (refund < 0) {
            refund = 0;
        }

        // Remove booking from customer and passenger from flight
        customer.cancelBookingForFlight(flight);
        flight.removePassenger(customer);

        System.out.println("Booking cancelled successfully.");
        System.out.println("Cancellation fee: " + String.format("%.2f", fee));
        System.out.println("Refund amount: " + String.format("%.2f", refund));
    }

    private Booking findBooking(Customer customer, int flightId) {
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == flightId) {
                return b;
            }
        }
        return null;
    }
}
