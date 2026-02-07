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
    private BookingStatus status;
    private double feeLast;
    private String feeType;

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
        this.status = BookingStatus.ACTIVE;
        this.feeLast = 0.0;
        this.feeType = null;
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

    /**
     * Returns the current status of the booking.
     *
     * @return booking status (ACTIVE or CANCELED)
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status the booking status
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Returns the fee applied in the last transaction (rebooking or cancellation).
     *
     * @return the fee amount
     */
    public double getFeeLast() {
        return feeLast;
    }

    /**
     * Sets the fee applied in the last transaction.
     *
     * @param feeLast the fee amount
     */
    public void setFeeLast(double feeLast) {
        this.feeLast = feeLast;
    }

    /**
     * Returns the type of fee applied (e.g., "REBOOK", "CANCEL").
     *
     * @return the fee type
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * Sets the type of fee applied.
     *
     * @param feeType the fee type
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
