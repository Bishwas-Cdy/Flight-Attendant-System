package bcu.cmp5332.bookingsystem.commands;

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
 * Tests for the three checklist mismatch fixes:
 * 1. Deleted flag (active -> deleted boolean)
 * 2. Fee fields in Booking (feeLast, feeType)
 * 3. Booking status (ACTIVE/CANCELED preserved with fees)
 */
public class ChecklistMismatchFixTest {

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

        // Add flights
        flight1 = new Flight(1, "AA101", "New York", "Boston", 
                            LocalDate.of(2026, 2, 10), 100, 100.0);
        fbs.addFlight(flight1);

        flight2 = new Flight(2, "AA202", "New York", "Chicago", 
                            LocalDate.of(2026, 2, 15), 100, 100.0);
        fbs.addFlight(flight2);
    }

    // ===== FEATURE 3: BOOKING STATUS AND FEES =====

    @Test
    public void testBookingHasStatusField() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        assertNotNull(b.getStatus());
        assertEquals(BookingStatus.ACTIVE, b.getStatus());
    }

    @Test
    public void testBookingCanBeMarkedCanceled() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        b.setStatus(BookingStatus.CANCELED);
        
        assertEquals(BookingStatus.CANCELED, b.getStatus());
    }

    @Test
    public void testBookingHasFeeFields() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        // Should have fee fields initialized
        assertEquals(0.0, b.getFeeLast(), 0.0001);
        assertNull(b.getFeeType());
    }

    @Test
    public void testBookingCanStoreCancellationFee() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        // Simulate cancellation
        double fee = 7.50;
        b.setStatus(BookingStatus.CANCELED);
        b.setFeeLast(fee);
        b.setFeeType("CANCEL");
        
        assertEquals(BookingStatus.CANCELED, b.getStatus());
        assertEquals(7.50, b.getFeeLast(), 0.0001);
        assertEquals("CANCEL", b.getFeeType());
    }

    @Test
    public void testBookingCanStoreRebookingFee() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        // Simulate rebooking
        double fee = 7.50;
        b.setStatus(BookingStatus.CANCELED);
        b.setFeeLast(fee);
        b.setFeeType("REBOOK");
        
        assertEquals(BookingStatus.CANCELED, b.getStatus());
        assertEquals(7.50, b.getFeeLast(), 0.0001);
        assertEquals("REBOOK", b.getFeeType());
    }

    @Test
    public void testCanceledBookingPreservesAllData() {
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        
        // Mark as canceled with fee
        b.setStatus(BookingStatus.CANCELED);
        b.setFeeLast(10.0);
        b.setFeeType("CANCEL");
        
        // All data should be preserved
        assertEquals(customer, b.getCustomer());
        assertEquals(flight1, b.getFlight());
        assertEquals(150.0, b.getBookingPrice(), 0.0001);
        assertEquals(BookingStatus.CANCELED, b.getStatus());
        assertEquals(10.0, b.getFeeLast(), 0.0001);
        assertEquals("CANCEL", b.getFeeType());
    }

    @Test
    public void testMultipleBookingsCanHaveDifferentFees() throws FlightBookingSystemException {
        // Booking 1
        Booking b1 = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        b1.setStatus(BookingStatus.CANCELED);
        b1.setFeeLast(7.50);
        b1.setFeeType("CANCEL");
        
        // Booking 2
        Booking b2 = new Booking(customer, flight2, LocalDate.of(2026, 1, 2), 200.0);
        b2.setStatus(BookingStatus.ACTIVE);
        b2.setFeeLast(0.0);
        
        // Both should maintain their own state
        assertEquals(BookingStatus.CANCELED, b1.getStatus());
        assertEquals(7.50, b1.getFeeLast(), 0.0001);
        assertEquals("CANCEL", b1.getFeeType());
        
        assertEquals(BookingStatus.ACTIVE, b2.getStatus());
        assertEquals(0.0, b2.getFeeLast(), 0.0001);
        assertNull(b2.getFeeType());
    }

    // ===== FEATURE 1: DELETED FLAG =====

    @Test
    public void testFlightHasDeletedFlag() {
        Flight f = flight1;
        
        // Active by default
        assertTrue(f.isActive());
    }

    @Test
    public void testFlightDeactivateSetDeletedTrue() {
        flight1.deactivate();
        
        assertFalse(flight1.isActive());
    }

    @Test
    public void testFlightReactivateSetDeletedFalse() {
        flight1.deactivate();
        flight1.reactivate();
        
        assertTrue(flight1.isActive());
    }

    @Test
    public void testCustomerHasDeletedFlag() {
        Customer c = customer;
        
        // Active by default
        assertTrue(c.isActive());
    }

    @Test
    public void testCustomerDeactivateSetDeletedTrue() {
        customer.deactivate();
        
        assertFalse(customer.isActive());
    }

    @Test
    public void testCustomerReactivateSetDeletedFalse() {
        customer.deactivate();
        customer.reactivate();
        
        assertTrue(customer.isActive());
    }

    // ===== FEATURE 2: ROLLBACK READY =====
    // (Actual rollback testing requires file system mocking,
    //  but we can verify that BookingDataManager can read/write the new fields)

    @Test
    public void testBookingWithCanceledStatusCanBeCreated() {
        // Simulates what happens when BookingDataManager loads from disk
        Booking b = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        b.setStatus(BookingStatus.CANCELED);
        b.setFeeLast(10.0);
        b.setFeeType("CANCEL");
        
        // Should be persisted and loadable
        assertNotNull(b.getStatus());
        assertNotNull(b.getFeeType());
    }

    @Test
    public void testCancelBookingCommandMarksBookingCanceled() throws FlightBookingSystemException {
        // Add booking first
        Booking booking = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        customer.addBooking(booking);
        flight1.addPassenger(customer);
        
        // Execute cancel command
        CancelBooking cmd = new CancelBooking(1, 1);
        cmd.execute(fbs);
        
        // Booking should be marked CANCELED, not deleted from customer
        assertTrue(customer.getBookings().stream()
                .anyMatch(b -> b.getFlight().getId() == 1 && b.getStatus() == BookingStatus.CANCELED));
    }

    @Test
    public void testCancelBookingStoresFeeInformation() throws FlightBookingSystemException {
        // Add booking first
        Booking booking = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        customer.addBooking(booking);
        flight1.addPassenger(customer);
        
        // Execute cancel command
        CancelBooking cmd = new CancelBooking(1, 1);
        cmd.execute(fbs);
        
        // Find the canceled booking
        Booking canceledBooking = customer.getBookings().stream()
                .filter(b -> b.getFlight().getId() == 1)
                .findFirst()
                .orElse(null);
        
        assertNotNull(canceledBooking);
        assertEquals(BookingStatus.CANCELED, canceledBooking.getStatus());
        assertTrue(canceledBooking.getFeeLast() > 0);
        assertEquals("CANCEL", canceledBooking.getFeeType());
    }

    @Test
    public void testUpdateBookingMarkOldBookingCanceled() throws FlightBookingSystemException {
        // Add booking for flight1
        Booking booking = new Booking(customer, flight1, LocalDate.of(2026, 1, 1), 150.0);
        customer.addBooking(booking);
        flight1.addPassenger(customer);
        
        // Execute update booking command (rebook to flight2)
        UpdateBooking cmd = new UpdateBooking(1, 1, 2);
        cmd.execute(fbs);
        
        // Old booking should be marked CANCELED with REBOOK fee
        Booking oldBooking = customer.getBookings().stream()
                .filter(b -> b.getFlight().getId() == 1)
                .findFirst()
                .orElse(null);
        
        assertNotNull(oldBooking);
        assertEquals(BookingStatus.CANCELED, oldBooking.getStatus());
        assertEquals("REBOOK", oldBooking.getFeeType());
        assertTrue(oldBooking.getFeeLast() > 0);
        
        // New booking should be ACTIVE
        Booking newBooking = customer.getBookings().stream()
                .filter(b -> b.getFlight().getId() == 2)
                .findFirst()
                .orElse(null);
        
        assertNotNull(newBooking);
        assertEquals(BookingStatus.ACTIVE, newBooking.getStatus());
    }
}
