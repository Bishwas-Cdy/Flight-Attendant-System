package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.temporal.ChronoUnit;

/**
 * Adds a booking for a customer on a flight with dynamic pricing.
 * Booking is not allowed for past flights.
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

        // Past-flight restriction
        if (flight.getDepartureDate().isBefore(fbs.getSystemDate())) {
            throw new FlightBookingSystemException("Cannot add booking. Flight has already departed.");
        }

        // Capacity enforcement
        int capacity = flight.getCapacity();
        int currentPassengers = flight.getPassengers().size();

        if (capacity > 0 && currentPassengers >= capacity) {
            throw new FlightBookingSystemException(
                    "Cannot add booking. Flight is full (" + capacity + " seats).");
        }

        // --- Dynamic pricing ---
        double price = flight.getBasePrice();

        // Seat-based pricing
        if (capacity > 0) {
            double occupancyRate = (double) currentPassengers / capacity;

            if (occupancyRate >= 0.8) {
                price = price * 1.20; // +20%
            } else if (occupancyRate >= 0.5) {
                price = price * 1.10; // +10%
            }
        }

        // Date-based pricing
        long daysToDeparture = ChronoUnit.DAYS.between(
                fbs.getSystemDate(), flight.getDepartureDate());

        if (daysToDeparture <= 7) {
            price = price * 1.30; // +30%
        } else if (daysToDeparture <= 30) {
            price = price * 1.15; // +15%
        }

        // Create booking with calculated price
        Booking booking = new Booking(customer, flight, fbs.getSystemDate(), price);

        customer.addBooking(booking);
        flight.addPassenger(customer);

        System.out.println("Booking added successfully.");
        System.out.println("Final price: " + String.format("%.2f", price));
    }
}
