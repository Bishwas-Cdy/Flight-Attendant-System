package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.temporal.ChronoUnit;

/**
 * Updates an existing booking from one flight to another with dynamic pricing.
 * Rebooking is not allowed from or to past flights. Applies a rebooking fee.
 * Shows refund/credit if old flight price was higher than new flight price plus fee.
 */
public class UpdateBooking implements Command {

    private final int customerId;
    private final int oldFlightId;
    private final int newFlightId;

    /**
     * Creates an UpdateBooking command.
     *
     * @param customerId the customer's ID
     * @param oldFlightId the ID of the current flight booking
     * @param newFlightId the ID of the new flight to rebook to
     */
    public UpdateBooking(int customerId, int oldFlightId, int newFlightId) {
        this.customerId = customerId;
        this.oldFlightId = oldFlightId;
        this.newFlightId = newFlightId;
    }

    /**
     * Executes the update booking command.
     * Validates that old flight has not departed, cancels old booking, applies rebooking fee,
     * and creates new booking with dynamic pricing. Shows refund/credit if customer overpaid.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if update cannot be completed
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        Customer customer = fbs.getCustomerByID(customerId);
        Flight oldFlight = fbs.getFlightByID(oldFlightId);
        Flight newFlight = fbs.getFlightByID(newFlightId);

        // Soft delete check
        if (!customer.isActive()) {
            throw new FlightBookingSystemException("Customer account is inactive.");
        }

        if (oldFlightId == newFlightId) {
            throw new FlightBookingSystemException("Old flight and new flight cannot be the same.");
        }

        // Block rebooking from past flights
        if (oldFlight.getDepartureDate().isBefore(fbs.getSystemDate())) {
            throw new FlightBookingSystemException("Cannot rebook. Old flight has already departed.");
        }

        // Past-flight restriction for the NEW flight
        if (newFlight.getDepartureDate().isBefore(fbs.getSystemDate())) {
            throw new FlightBookingSystemException("Cannot rebook. New flight has already departed.");
        }

        // Soft delete check for new flight
        if (!newFlight.isActive()) {
            throw new FlightBookingSystemException("New flight is inactive.");
        }

        Booking booking = findBooking(customer, oldFlightId);
        if (booking == null) {
            throw new FlightBookingSystemException("Booking not found for this customer and old flight.");
        }

        // Prevent duplicates: customer cannot already have ACTIVE booking for new flight
        // (allows rebooking after cancellation)
        Booking existingNewFlightBooking = findBooking(customer, newFlightId);
        if (existingNewFlightBooking != null && existingNewFlightBooking.getStatus() == BookingStatus.ACTIVE) {
            throw new FlightBookingSystemException("Customer already has an active booking for the new flight.");
        }

        // Capacity enforcement on new flight
        int cap = newFlight.getCapacity();
        int currentPassengers = newFlight.getPassengers().size();
        if (cap > 0 && currentPassengers >= cap) {
            throw new FlightBookingSystemException("Cannot rebook. New flight is full (" + cap + " seats).");
        }

        // Rebooking fee based on old booking price
        double oldPrice = booking.getBookingPrice();
        double rebookFee = oldPrice * 0.05; // 5%
        if (rebookFee < 2.0) {
            rebookFee = 2.0;
        }

        // Calculate dynamic price for the new flight
        double newDynamicPrice = calculateDynamicPrice(fbs, newFlight);

        // Mark old booking as CANCELED and store rebook fee
        booking.setStatus(BookingStatus.CANCELED);
        booking.setFeeLast(rebookFee);
        booking.setFeeType("REBOOK");

        // Create new booking
        Booking newBooking = new Booking(customer, newFlight, fbs.getSystemDate(), newDynamicPrice + rebookFee);
        newBooking.setStatus(BookingStatus.ACTIVE);
        newBooking.setFeeLast(0.0);

        // Add new booking to customer and passenger to new flight
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);

        // Remove passenger from old flight
        oldFlight.removePassenger(customer);

        System.out.println("Booking updated successfully.");
        System.out.println("Old booking price: " + String.format("%.2f", oldPrice));
        System.out.println("Rebooking fee (5% of old price, minimum $2): " + String.format("%.2f", rebookFee));
        System.out.println("New flight base price: " + String.format("%.2f", newDynamicPrice));
        System.out.println("New booking price: " + String.format("%.2f", newBooking.getBookingPrice()));

        // Calculate and show refund/credit if applicable
        double refundAfterFee = oldPrice - rebookFee;
        double amountToPay = newBooking.getBookingPrice() - refundAfterFee;

        if (amountToPay < 0) {
            System.out.println("Credit to your account: " + String.format("%.2f", -amountToPay));
        } else if (amountToPay > 0) {
            System.out.println("Amount to pay: " + String.format("%.2f", amountToPay));
        } else {
            System.out.println("No additional payment required.");
        }
    }

    private Booking findBooking(Customer customer, int flightId) {
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == flightId && b.getStatus() == BookingStatus.ACTIVE) {
                return b;
            }
        }
        return null;
    }

    private double calculateDynamicPrice(FlightBookingSystem fbs, Flight flight) throws FlightBookingSystemException {

        // Past-flight restriction (extra safety)
        if (flight.getDepartureDate().isBefore(fbs.getSystemDate())) {
            throw new FlightBookingSystemException("Cannot calculate price. Flight has already departed.");
        }

        double price = flight.getBasePrice();

        // Seat-based pricing
        int capacity = flight.getCapacity();
        int currentPassengers = flight.getPassengers().size();

        if (capacity > 0) {
            double occupancyRate = (double) currentPassengers / capacity;

            if (occupancyRate >= 0.8) {
                price = price * 1.20;
            } else if (occupancyRate >= 0.5) {
                price = price * 1.10;
            }
        }

        // Date-based pricing
        long daysToDeparture = ChronoUnit.DAYS.between(fbs.getSystemDate(), flight.getDepartureDate());

        if (daysToDeparture <= 7) {
            price = price * 1.30;
        } else if (daysToDeparture <= 30) {
            price = price * 1.15;
        }

        return price;
    }
}
