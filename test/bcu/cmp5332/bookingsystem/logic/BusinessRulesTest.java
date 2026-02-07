package bcu.cmp5332.bookingsystem.logic;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for core business rules using real commands.
 */
public class BusinessRulesTest {

    @Test
    public void testCapacityEnforcementBlocksBookingWhenFull() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c1 = new Customer(1, "C1", "07000000001");
        Customer c2 = new Customer(2, "C2", "07000000002");
        Customer c3 = new Customer(3, "C3", "07000000003");

        // capacity 2
        Flight f = new Flight(1, "AB001", "KTM", "CTW",
                fbs.getSystemDate().plusDays(60), 2, 100.0);

        fbs.addCustomer(c1);
        fbs.addCustomer(c2);
        fbs.addCustomer(c3);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);
        new AddBooking(2, 1).execute(fbs);

        // third should fail
        assertThrows(FlightBookingSystemException.class, () -> new AddBooking(3, 1).execute(fbs));
    }

    @Test
    public void testInactiveCustomerCannotBook() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        c.deactivate();

        Flight f = new Flight(1, "AB002", "KTM", "CTW",
                fbs.getSystemDate().plusDays(60), 10, 100.0);

        fbs.addCustomer(c);
        fbs.addFlight(f);

        assertThrows(FlightBookingSystemException.class, () -> new AddBooking(1, 1).execute(fbs));
    }

    @Test
    public void testInactiveFlightCannotBeBooked() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        Flight f = new Flight(1, "AB003", "KTM", "CTW",
                fbs.getSystemDate().plusDays(60), 10, 100.0);
        f.deactivate();

        fbs.addCustomer(c);
        fbs.addFlight(f);

        assertThrows(FlightBookingSystemException.class, () -> new AddBooking(1, 1).execute(fbs));
    }

    @Test
    public void testPastFlightBookingIsBlocked() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        Flight past = new Flight(1, "AB004", "KTM", "CTW",
                fbs.getSystemDate().minusDays(1), 10, 100.0);

        fbs.addCustomer(c);
        fbs.addFlight(past);

        assertThrows(FlightBookingSystemException.class, () -> new AddBooking(1, 1).execute(fbs));
    }

    @Test
    public void testUpdateBookingMovesPassengerToNewFlight() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");

        Flight oldF = new Flight(1, "OLD1", "KTM", "CTW",
                fbs.getSystemDate().plusDays(50), 10, 100.0);

        Flight newF = new Flight(2, "NEW1", "KTM", "PKR",
                fbs.getSystemDate().plusDays(50), 10, 200.0);

        fbs.addCustomer(c);
        fbs.addFlight(oldF);
        fbs.addFlight(newF);

        // book old flight
        new AddBooking(1, 1).execute(fbs);
        assertTrue(oldF.getPassengers().contains(c));
        assertFalse(newF.getPassengers().contains(c));

        // update booking to new flight
        new UpdateBooking(1, 1, 2).execute(fbs);

        assertFalse(oldF.getPassengers().contains(c));
        assertTrue(newF.getPassengers().contains(c));
    }

    @Test
    public void testCancelBookingRemovesPassengerFromFlight() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        Flight f = new Flight(1, "AB005", "KTM", "CTW",
                fbs.getSystemDate().plusDays(60), 10, 100.0);

        fbs.addCustomer(c);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);
        assertTrue(f.getPassengers().contains(c));
        assertEquals(1, c.getBookings().size());

        new CancelBooking(1, 1).execute(fbs);

        // Passenger should be removed from flight
        assertFalse(f.getPassengers().contains(c));
        
        // Booking should be marked CANCELED, not deleted
        assertEquals(1, c.getBookings().size());
        assertEquals(BookingStatus.CANCELED, c.getBookings().get(0).getStatus());
    }

    @Test
    public void testUpdateBookingFailsIfOldBookingNotFound() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");

        Flight oldF = new Flight(1, "OLD2", "KTM", "CTW",
                fbs.getSystemDate().plusDays(50), 10, 100.0);

        Flight newF = new Flight(2, "NEW2", "KTM", "PKR",
                fbs.getSystemDate().plusDays(50), 10, 200.0);

        fbs.addCustomer(c);
        fbs.addFlight(oldF);
        fbs.addFlight(newF);

        // no booking exists yet, update should fail
        assertThrows(FlightBookingSystemException.class, () -> new UpdateBooking(1, 1, 2).execute(fbs));
    }
}
