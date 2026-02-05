package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a customer for a flight.
 * Stores the booking price at the time the booking was created.
 */
public class Booking {

    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double bookingPrice;

    /**
     * Creates a booking and stores the flight base price as booking price.
     *
     * @param customer the customer
     * @param flight the flight
     * @param bookingDate the date of booking
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this(customer, flight, bookingDate, flight == null ? 0.0 : flight.getBasePrice());
    }

    /**
     * Creates a booking with an explicit stored price (used when loading from file).
     *
     * @param customer the customer
     * @param flight the flight
     * @param bookingDate the date of booking
     * @param bookingPrice the stored price for this booking
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate, double bookingPrice) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Returns the stored booking price.
     */
    public double getBookingPrice() {
        return bookingPrice;
    }

    /**
     * Updates the stored booking price.
     * (Used for updatebooking later when you change to a different flight.)
     */
    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }
}
