package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Flight class.
 */
public class FlightTest {

    @Test
    public void testFlightCreationStoresFields() {
        Flight f = new Flight(1, "LH2560", "Birmingham", "Munich",
                LocalDate.of(2026, 2, 25), 100, 299.99);

        assertEquals(1, f.getId());
        assertEquals("LH2560", f.getFlightNumber());
        assertEquals("Birmingham", f.getOrigin());
        assertEquals("Munich", f.getDestination());
        assertEquals(LocalDate.of(2026, 2, 25), f.getDepartureDate());
        assertEquals(100, f.getCapacity());
        assertEquals(299.99, f.getBasePrice(), 0.0001);
    }

    @Test
    public void testFlightStartsActive() {
        Flight f = new Flight(2, "AA111", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);
        assertTrue(f.isActive());
    }

    @Test
    public void testDeactivateFlightSetsActiveFalse() {
        Flight f = new Flight(3, "AA222", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);
        f.deactivate();
        assertFalse(f.isActive());
    }

    @Test
    public void testReactivateFlightSetsActiveTrue() {
        Flight f = new Flight(4, "AA333", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);
        f.deactivate();
        f.reactivate();
        assertTrue(f.isActive());
    }

    @Test
    public void testAddPassengerWorks() throws FlightBookingSystemException {
        Flight f = new Flight(5, "AA444", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);

        Customer c = new Customer(1, "Test", "07000000001");

        f.addPassenger(c);

        assertEquals(1, f.getPassengers().size());
        assertTrue(f.getPassengers().contains(c));
    }

    @Test
    public void testAddSamePassengerTwiceRejected() throws FlightBookingSystemException {
        Flight f = new Flight(6, "AA555", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);

        Customer c = new Customer(2, "Test", "07000000002");
        f.addPassenger(c);

        assertThrows(FlightBookingSystemException.class, () -> f.addPassenger(c));
    }

    @Test
    public void testRemovePassengerWorks() throws FlightBookingSystemException {
        Flight f = new Flight(7, "AA666", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);

        Customer c = new Customer(3, "Test", "07000000003");
        f.addPassenger(c);

        f.removePassenger(c);

        assertEquals(0, f.getPassengers().size());
        assertFalse(f.getPassengers().contains(c));
    }

    @Test
    public void testRemoveNonExistingPassengerThrowsException() {
        Flight f = new Flight(8, "AA777", "KTM", "CTW",
                LocalDate.of(2026, 2, 10), 10, 100.0);

        Customer c = new Customer(4, "Test", "07000000004");

        assertThrows(FlightBookingSystemException.class, () -> f.removePassenger(c));
    }
}
