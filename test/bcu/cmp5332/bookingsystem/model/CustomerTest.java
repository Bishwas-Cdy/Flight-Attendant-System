package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Customer class.
 */
public class CustomerTest {

    @Test
    public void testCustomerCreationStoresFields() {
        Customer c = new Customer(1, "John Doe", "07123456789");

        assertEquals(1, c.getId());
        assertEquals("John Doe", c.getName());
        assertEquals("07123456789", c.getPhone());
    }

    @Test
    public void testCustomerStartsActive() {
        Customer c = new Customer(2, "Jane Doe", "07000000000");
        assertTrue(c.isActive());
    }

    @Test
    public void testDeactivateSetsActiveFalse() {
        Customer c = new Customer(3, "A B", "07000000001");
        c.deactivate();
        assertFalse(c.isActive());
    }

    @Test
    public void testReactivateSetsActiveTrue() {
        Customer c = new Customer(4, "A B", "07000000002");
        c.deactivate();
        c.reactivate();
        assertTrue(c.isActive());
    }

    @Test
    public void testBookingsListStartsEmpty() {
        Customer c = new Customer(5, "Test", "07000000003");
        assertEquals(0, c.getBookings().size());
    }

    @Test
    public void testAddBookingIncreasesBookingsCount() throws FlightBookingSystemException {
        Customer c = new Customer(6, "Test", "07000000004");
        Flight f = new Flight(1, "AB123", "KTM", "PKR", LocalDate.of(2026, 2, 25), 10, 100.0);

        Booking b = new Booking(c, f, LocalDate.of(2026, 2, 1), 100.0);
        c.addBooking(b);

        assertEquals(1, c.getBookings().size());
    }

    @Test
    public void testDuplicateBookingSameFlightIsRejected() throws FlightBookingSystemException {
        Customer c = new Customer(7, "Test", "07000000005");
        Flight f = new Flight(2, "CD456", "KTM", "CTW", LocalDate.of(2026, 2, 10), 10, 50.0);

        c.addBooking(new Booking(c, f, LocalDate.of(2026, 2, 1), 50.0));

        assertThrows(FlightBookingSystemException.class, () -> {
            c.addBooking(new Booking(c, f, LocalDate.of(2026, 2, 2), 55.0));
        });
    }

    @Test
    public void testCancelBookingRemovesIt() throws FlightBookingSystemException {
        Customer c = new Customer(8, "Test", "07000000006");
        Flight f = new Flight(3, "EF789", "KTM", "BIR", LocalDate.of(2026, 3, 1), 10, 200.0);

        c.addBooking(new Booking(c, f, LocalDate.of(2026, 2, 1), 200.0));
        assertEquals(1, c.getBookings().size());

        c.cancelBookingForFlight(f);
        assertEquals(0, c.getBookings().size());
    }

    @Test
    public void testCancelNonExistingBookingThrowsException() {
        Customer c = new Customer(9, "Test", "07000000007");
        Flight f = new Flight(4, "GH101", "KTM", "MUC", LocalDate.of(2026, 3, 1), 10, 200.0);

        assertThrows(FlightBookingSystemException.class, () -> {
            c.cancelBookingForFlight(f);
        });
    }
}
