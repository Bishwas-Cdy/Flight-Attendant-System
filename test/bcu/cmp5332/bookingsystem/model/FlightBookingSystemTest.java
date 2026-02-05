package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FlightBookingSystem class.
 */
public class FlightBookingSystemTest {

    @Test
    public void testAddCustomerAndGetCustomerWorks() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Customer c = new Customer(1, "John", "07000000001");

        fbs.addCustomer(c);

        Customer fetched = fbs.getCustomerByID(1);
        assertEquals("John", fetched.getName());
    }

    @Test
    public void testGetMissingCustomerThrowsException() {
        FlightBookingSystem fbs = new FlightBookingSystem();

        assertThrows(FlightBookingSystemException.class, () -> fbs.getCustomerByID(999));
    }

    @Test
    public void testAddFlightAndGetFlightWorks() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Flight f = new Flight(1, "LH2560", "BIR", "MUC", LocalDate.of(2026, 2, 10), 10, 100.0);

        fbs.addFlight(f);

        Flight fetched = fbs.getFlightByID(1);
        assertEquals("LH2560", fetched.getFlightNumber());
    }

    @Test
    public void testGetMissingFlightThrowsException() {
        FlightBookingSystem fbs = new FlightBookingSystem();

        assertThrows(FlightBookingSystemException.class, () -> fbs.getFlightByID(999));
    }
}
