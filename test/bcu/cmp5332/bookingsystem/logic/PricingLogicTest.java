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
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for pricing logic and fees using the real command implementations.
 */
public class PricingLogicTest {

    @Test
    public void testBasePriceUsedWhenNoDynamicRuleTriggers() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        // far future so daysToDeparture > 30
        Flight f = new Flight(1, "P1", "KTM", "CTW", fbs.getSystemDate().plusDays(90), 10, 100.0);

        fbs.addCustomer(c);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);

        Booking b = c.getBookings().get(0);
        assertEquals(100.0, b.getBookingPrice(), 0.01);
    }

    @Test
    public void testSeatBasedDynamicPricing10Percent() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer buyer = new Customer(1, "Buyer", "07000000001");
        Flight f = new Flight(1, "P2", "KTM", "CTW", fbs.getSystemDate().plusDays(90), 10, 100.0);

        // Make occupancy = 0.5 (5/10) BEFORE booking
        for (int i = 100; i < 105; i++) {
            Customer dummy = new Customer(i, "D" + i, "07000000000");
            f.addPassenger(dummy);
        }

        fbs.addCustomer(buyer);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);

        Booking b = buyer.getBookings().get(0);
        // 10% increase, no date increase (90 days)
        assertEquals(110.0, b.getBookingPrice(), 0.05);
    }

    @Test
    public void testDateBasedDynamicPricing15Percent() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        // within 30 days, > 7 days
        Flight f = new Flight(1, "P3", "KTM", "CTW", fbs.getSystemDate().plusDays(20), 10, 100.0);

        fbs.addCustomer(c);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);

        Booking b = c.getBookings().get(0);
        // 15% increase
        assertEquals(115.0, b.getBookingPrice(), 0.05);
    }

    @Test
    public void testCombinedSeatAndDatePricing() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        // within 7 days => +30%
        Flight f = new Flight(1, "P4", "KTM", "CTW", fbs.getSystemDate().plusDays(5), 10, 100.0);

        // occupancy >= 0.8 => +20% (8/10)
        for (int i = 200; i < 208; i++) {
            Customer dummy = new Customer(i, "D" + i, "07000000000");
            f.addPassenger(dummy);
        }

        fbs.addCustomer(c);
        fbs.addFlight(f);

        new AddBooking(1, 1).execute(fbs);

        Booking b = c.getBookings().get(0);

        // Seat +20% then Date +30% => 100 * 1.20 * 1.30 = 156
        assertEquals(156.0, b.getBookingPrice(), 0.2);
    }

    @Test
    public void testCancellationFeeAndRefundOutput() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");
        Flight f = new Flight(1, "P5", "KTM", "CTW", fbs.getSystemDate().plusDays(90), 10, 20.0);

        fbs.addCustomer(c);
        fbs.addFlight(f);

        // Make booking with base 20.0 (no dynamic triggers)
        new AddBooking(1, 1).execute(fbs);

        // Capture printed output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));

        try {
            new CancelBooking(1, 1).execute(fbs);
        } finally {
            System.setOut(old);
        }

        String printed = out.toString();

        // For price 20.0: 10% = 2.0 but min fee is 5.0 => refund = 15.0
        assertTrue(printed.contains("Cancellation fee: 5.00"));
        assertTrue(printed.contains("Refund amount: 15.00"));
    }

    @Test
    public void testRebookingFeeAndNewBookingPriceStored() throws FlightBookingSystemException {
        FlightBookingSystem fbs = new FlightBookingSystem();

        Customer c = new Customer(1, "C", "07000000001");

        // Old flight far => no date trigger
        Flight oldF = new Flight(1, "OLD", "KTM", "CTW", fbs.getSystemDate().plusDays(90), 10, 100.0);

        // New flight within 20 days => +15% date trigger
        Flight newF = new Flight(2, "NEW", "KTM", "PKR", fbs.getSystemDate().plusDays(20), 10, 200.0);

        fbs.addCustomer(c);
        fbs.addFlight(oldF);
        fbs.addFlight(newF);

        // Initial booking on old flight
        new AddBooking(1, 1).execute(fbs);

        Booking booking = c.getBookings().get(0);
        double oldPrice = booking.getBookingPrice(); // should be 100.0

        // Rebooking fee = 5% of oldPrice, min 2.0 => 5.0
        // New dynamic price: 200 * 1.15 = 230.0 (no seat trigger)
        // New stored price = 230.0 + 5.0 = 235.0
        new UpdateBooking(1, 1, 2).execute(fbs);

        assertEquals(oldPrice * 0.05, 5.0, 0.0001);
        
        // Old booking should be marked CANCELED and still on flight 1
        assertEquals(1, booking.getFlight().getId());
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
        assertEquals(5.0, booking.getFeeLast(), 0.0001);
        assertEquals("REBOOK", booking.getFeeType());
        
        // New booking should be created for flight 2
        assertEquals(2, c.getBookings().size());
        Booking newBooking = c.getBookings().get(1);
        assertEquals(2, newBooking.getFlight().getId());
        assertEquals(235.0, newBooking.getBookingPrice(), 0.5);
        assertEquals(BookingStatus.ACTIVE, newBooking.getStatus());
    }
}
