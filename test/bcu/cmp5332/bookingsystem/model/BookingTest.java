package bcu.cmp5332.bookingsystem.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Booking class.
 */
public class BookingTest {

    @Test
    public void testBookingStoresCustomerFlightAndDate() {
        Customer c = new Customer(1, "Test", "07000000001");
        Flight f = new Flight(1, "AB111", "KTM", "CTW", LocalDate.of(2026, 2, 10), 10, 5000.0);
        LocalDate d = LocalDate.of(2026, 1, 1);

        Booking b = new Booking(c, f, d, 5000.0);

        assertEquals(c, b.getCustomer());
        assertEquals(f, b.getFlight());
        assertEquals(d, b.getBookingDate());
    }

    @Test
    public void testBookingStoresBookingPrice() {
        Customer c = new Customer(2, "Test", "07000000002");
        Flight f = new Flight(2, "AB222", "KTM", "CTW", LocalDate.of(2026, 2, 10), 10, 5000.0);

        Booking b = new Booking(c, f, LocalDate.of(2026, 1, 1), 1234.56);

        assertEquals(1234.56, b.getBookingPrice(), 0.0001);
    }

    @Test
    public void testBookingPriceDoesNotChangeWhenFlightBasePriceChanges() {
        Customer c = new Customer(3, "Test", "07000000003");
        Flight f = new Flight(3, "AB333", "KTM", "CTW", LocalDate.of(2026, 2, 10), 10, 5000.0);

        Booking b = new Booking(c, f, LocalDate.of(2026, 1, 1), 5000.0);

        f.setBasePrice(9999.0);

        assertEquals(5000.0, b.getBookingPrice(), 0.0001);
    }
}
