package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer (passenger) in the Flight Booking System.
 * A customer can make one or more bookings.
 */
public class Customer {

    private int id;
    private String name;
    private String phone;

    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Creates a new customer.
     *
     * @param id the unique customer id
     * @param name the customer name
     * @param phone the phone number
     */
    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Returns a one-line summary of the customer.
     *
     * @return short details string
     */
    public String getDetailsShort() {
        return "Id: " + id + " - " + name + " - " + phone;
    }

    /**
     * Returns detailed customer information including booking list.
     *
     * @return long details string
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer #").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("--------------------------\n");
        sb.append("Bookings:\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (bookings.isEmpty()) {
            sb.append("No bookings\n");
            sb.append("0 booking(s)\n");
            return sb.toString();
        }

        for (Booking b : bookings) {
            Flight f = b.getFlight();
            sb.append("* Booking date: ")
                    .append(b.getBookingDate().format(dtf))
                    .append(" | Price: ")
                    .append(String.format("%.2f", b.getBookingPrice()))
                    .append(" | Flight #").append(f.getId())
                    .append(" - ").append(f.getFlightNumber())
                    .append(" - ").append(f.getOrigin()).append(" to ").append(f.getDestination())
                    .append(" on ").append(f.getDepartureDate().format(dtf))
                    .append("\n");
        }

        sb.append(bookings.size()).append(" booking(s)\n");
        return sb.toString();
    }

    /**
     * Adds a booking to the customer.
     * A customer must not have two bookings for the same flight.
     *
     * @param booking the booking to add
     * @throws FlightBookingSystemException if booking for the same flight already exists
     */
    public void addBooking(Booking booking) throws FlightBookingSystemException {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }

        Flight newFlight = booking.getFlight();
        for (Booking existing : bookings) {
            if (existing.getFlight().getId() == newFlight.getId()) {
                throw new FlightBookingSystemException(
                        "Customer already has a booking for that flight.");
            }
        }

        bookings.add(booking);
    }

    /**
     * Cancels (removes) the booking for the given flight from this customer.
     *
     * @param flight the flight to cancel booking for
     * @throws FlightBookingSystemException if no booking exists for the given flight
     */
    public void cancelBookingForFlight(Flight flight)
            throws FlightBookingSystemException {

        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null.");
        }

        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getFlight().getId() == flight.getId()) {
                toRemove = b;
                break;
            }
        }

        if (toRemove == null) {
            throw new FlightBookingSystemException(
                    "Customer does not have a booking for that flight.");
        }

        bookings.remove(toRemove);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the customer's bookings list.
     * Commands should prefer addBooking and cancelBookingForFlight.
     *
     * @return list of bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }
}
