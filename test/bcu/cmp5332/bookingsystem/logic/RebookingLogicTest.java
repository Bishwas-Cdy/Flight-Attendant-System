package bcu.cmp5332.bookingsystem.logic;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for rebooking logic after cancellations (fixes for critical errors).
 * Verifies that customers can rebook after canceling, and operations target ACTIVE bookings only.
 */
public class RebookingLogicTest {

    private FlightBookingSystem fbs;
    private Customer customer;
    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    public void setup() throws FlightBookingSystemException {
        fbs = new FlightBookingSystem();

        // Add customer
        customer = new Customer(1, "John Doe", "1234567890");
        fbs.addCustomer(customer);

        // Add flights for future dates
        flight1 = new Flight(1, "AA101", "New York", "Boston",
                            fbs.getSystemDate().plusDays(30), 100, 100.0);
        fbs.addFlight(flight1);

        flight2 = new Flight(2, "AA202", "New York", "Chicago",
                            fbs.getSystemDate().plusDays(40), 100, 100.0);
        fbs.addFlight(flight2);
    }

    // ===== BUG FIX: Cancel/UpdateBooking must target ACTIVE bookings only =====

    @Test
    public void testCancelBookingTargetsActiveBookingWhenMultipleExist() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);
        assertEquals(1, customer.getBookings().size());

        // Cancel it
        new CancelBooking(1, 1).execute(fbs);
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());

        // Book flight 1 again (creates 2nd booking for same flight)
        new AddBooking(1, 1).execute(fbs);
        assertEquals(2, customer.getBookings().size());
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());
        assertEquals(BookingStatus.ACTIVE, customer.getBookings().get(1).getStatus());

        // Cancel again - should cancel the ACTIVE one (index 1), not the CANCELED one (index 0)
        new CancelBooking(1, 1).execute(fbs);
        
        // Verify the ACTIVE booking (index 1) is now CANCELED
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(1).getStatus());
        
        // Both should have fee info
        assertTrue(customer.getBookings().get(0).getFeeLast() > 0);
        assertTrue(customer.getBookings().get(1).getFeeLast() > 0);
    }

    @Test
    public void testUpdateBookingTargetsActiveBookingWhenMultipleExist() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);

        // Cancel it
        new CancelBooking(1, 1).execute(fbs);

        // Book flight 1 again
        new AddBooking(1, 1).execute(fbs);
        assertEquals(2, customer.getBookings().size());

        // Update the ACTIVE booking to flight 2
        new UpdateBooking(1, 1, 2).execute(fbs);

        // Should now have 3 bookings:
        // [0] flight 1 - CANCELED (from cancel)
        // [1] flight 1 - CANCELED (from update, marked REBOOK)
        // [2] flight 2 - ACTIVE (new booking from update)
        assertEquals(3, customer.getBookings().size());
        
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());
        assertEquals("CANCEL", customer.getBookings().get(0).getFeeType());
        
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(1).getStatus());
        assertEquals("REBOOK", customer.getBookings().get(1).getFeeType());
        
        assertEquals(BookingStatus.ACTIVE, customer.getBookings().get(2).getStatus());
        assertEquals(2, customer.getBookings().get(2).getFlight().getId());
    }

    // ===== FIX #1: ALLOW REBOOKING AFTER CANCELLATION =====

    @Test
    public void testCanRebookSameFlightAfterCancellation() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);
        assertEquals(1, customer.getBookings().size());
        assertEquals(BookingStatus.ACTIVE, customer.getBookings().get(0).getStatus());

        // Cancel booking
        new CancelBooking(1, 1).execute(fbs);
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());

        // Should be able to rebook the SAME flight now
        new AddBooking(1, 1).execute(fbs);

        // Should have 2 bookings: 1 CANCELED + 1 ACTIVE
        assertEquals(2, customer.getBookings().size());
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());
        assertEquals(BookingStatus.ACTIVE, customer.getBookings().get(1).getStatus());
        assertEquals(1, customer.getBookings().get(1).getFlight().getId());
    }

    @Test
    public void testCancelledBookingDoesNotBlockNewBooking() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);

        // Cancel it
        new CancelBooking(1, 1).execute(fbs);
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());

        // Try to book the same flight again - should NOT throw exception
        assertDoesNotThrow(() -> {
            new AddBooking(1, 1).execute(fbs);
        });
    }

    // ===== FIX #2: ENUM COMPARISON IN CUSTOMER DETAILS =====

    @Test
    public void testGetDetailsLongShowsCanceledStatus() throws FlightBookingSystemException {
        // Book and cancel
        new AddBooking(1, 1).execute(fbs);
        new CancelBooking(1, 1).execute(fbs);

        // Get details
        String details = customer.getDetailsLong();

        // Should show CANCELED status
        assertTrue(details.contains("CANCELED"), "Details should show CANCELED status");
        assertTrue(details.contains("CANCEL Fee"), "Details should show CANCEL fee type");
    }

    @Test
    public void testGetDetailsLongShowsFeeInfo() throws FlightBookingSystemException {
        // Book and cancel
        new AddBooking(1, 1).execute(fbs);
        new CancelBooking(1, 1).execute(fbs);

        // Get details
        String details = customer.getDetailsLong();

        // Should show fee information
        assertTrue(details.contains("Fee:"), "Details should show fee amount");
    }

    // ===== FIX #3: UPDATE BOOKING AFTER CANCELLATION =====

    @Test
    public void testCanRebookToDifferentFlightAfterCancel() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);
        assertEquals(1, customer.getBookings().size());

        // Cancel flight 1 booking
        new CancelBooking(1, 1).execute(fbs);
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());

        // After cancel, use AddBooking to rebook (not UpdateBooking)
        new AddBooking(1, 2).execute(fbs);

        // Should now have 2 bookings: old CANCELED for flight1 + new ACTIVE for flight2
        assertEquals(2, customer.getBookings().size());
        assertEquals(BookingStatus.CANCELED, customer.getBookings().get(0).getStatus());
        assertEquals(BookingStatus.ACTIVE, customer.getBookings().get(1).getStatus());
        assertEquals(2, customer.getBookings().get(1).getFlight().getId());
    }

    @Test
    public void testUpdateBookingMarksOldBookingCanceled() throws FlightBookingSystemException {
        // Book flight 1
        new AddBooking(1, 1).execute(fbs);

        // Rebook to flight 2
        new UpdateBooking(1, 1, 2).execute(fbs);

        // Old booking should be CANCELED
        Booking oldBooking = customer.getBookings().stream()
                .filter(b -> b.getFlight().getId() == 1)
                .findFirst()
                .orElse(null);

        assertNotNull(oldBooking);
        assertEquals(BookingStatus.CANCELED, oldBooking.getStatus());
        assertEquals("REBOOK", oldBooking.getFeeType());
    }

    @Test
    public void testCannotRebookIfActiveBookingExistsForNewFlight() throws FlightBookingSystemException {
        // Book flight 1 and flight 2
        new AddBooking(1, 1).execute(fbs);
        new AddBooking(1, 2).execute(fbs);

        // Try to updatebook flight 1 to flight 2 - should fail (flight2 already booked ACTIVE)
        FlightBookingSystemException ex = assertThrows(FlightBookingSystemException.class, () -> {
            new UpdateBooking(1, 1, 2).execute(fbs);
        });

        assertTrue(ex.getMessage().contains("active booking"));
    }

    @Test
    public void testCanRebookToFlightIfOnlyPreviousCanceledBookingExists() throws FlightBookingSystemException {
        // Book flight 1 and flight 2, then cancel flight 2
        new AddBooking(1, 1).execute(fbs);
        new AddBooking(1, 2).execute(fbs);
        new CancelBooking(1, 2).execute(fbs);

        // Now update booking from flight 1 to flight 2 - should work even though old booking exists
        // because the old booking is CANCELED
        assertDoesNotThrow(() -> {
            new UpdateBooking(1, 1, 2).execute(fbs);
        });

        // Flight 2 should now have an ACTIVE booking
        Booking newBooking = customer.getBookings().stream()
                .filter(b -> b.getFlight().getId() == 2 && b.getStatus() == BookingStatus.ACTIVE)
                .findFirst()
                .orElse(null);

        assertNotNull(newBooking);
    }

    // ===== FULL WORKFLOW TEST =====

    @Test
    public void testCompleteRebookingWorkflow() throws FlightBookingSystemException {
        // 1. Initial booking on flight 1
        new AddBooking(1, 1).execute(fbs);
        assertEquals(1, customer.getBookings().size());
        Booking booking1 = customer.getBookings().get(0);
        assertEquals(BookingStatus.ACTIVE, booking1.getStatus());
        assertEquals(1, booking1.getFlight().getId());

        // 2. Update booking to flight 2 (rebook ACTIVE booking)
        new UpdateBooking(1, 1, 2).execute(fbs);

        // Should have 2 bookings: old CANCELED with REBOOK fee + new ACTIVE on flight 2
        assertEquals(2, customer.getBookings().size());
        
        Booking oldBooking = customer.getBookings().get(0);
        assertEquals(BookingStatus.CANCELED, oldBooking.getStatus());
        assertEquals("REBOOK", oldBooking.getFeeType());
        assertEquals(1, oldBooking.getFlight().getId());

        Booking newBooking = customer.getBookings().get(1);
        assertEquals(BookingStatus.ACTIVE, newBooking.getStatus());
        assertEquals(2, newBooking.getFlight().getId());

        // 3. Cancel the new booking
        new CancelBooking(1, 2).execute(fbs);
        assertEquals(BookingStatus.CANCELED, newBooking.getStatus());
        assertEquals("CANCEL", newBooking.getFeeType());

        // 4. Verify details show CANCELED status
        String details = customer.getDetailsLong();
        assertTrue(details.contains("CANCELED"));
        assertTrue(details.contains("Fee:"));
    }
}
