package bcu.cmp5332.bookingsystem.main;

/**
 * Custom exception for the Flight Booking System.
 * Thrown when system operations fail due to invalid input, business rule violations,
 * or data errors.
 */
public class FlightBookingSystemException extends Exception {

    /**
     * Creates a new FlightBookingSystemException with the specified error message.
     *
     * @param message the error message describing what went wrong
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
}
