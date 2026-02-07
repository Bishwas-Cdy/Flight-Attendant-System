package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages persistence of booking records to and from a file.
 * Loads and saves bookings from ./resources/data/bookings.txt.
 */
public class BookingDataManager implements DataManager {

    public final String RESOURCE = "./resources/data/bookings.txt";

    /**
     * Loads bookings from the bookings.txt file and adds them to the system.
     * Format: customerId::flightId::bookingDate::bookingPrice::status::feeLast::feeType::
     *
     * @param fbs the FlightBookingSystem to add loaded bookings to
     * @throws IOException if file reading fails
     * @throws FlightBookingSystemException if booking data format is invalid
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {

        File file = new File(RESOURCE);
        if (!file.exists()) {
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            int lineIdx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.trim().isEmpty()) {
                    lineIdx++;
                    continue;
                }

                String[] parts = line.split(SEPARATOR, -1);

                try {
                    int customerId = Integer.parseInt(parts[0]);
                    int flightId = Integer.parseInt(parts[1]);
                    LocalDate bookingDate = LocalDate.parse(parts[2]);

                    Customer customer = fbs.getCustomerByID(customerId);
                    Flight flight = fbs.getFlightByID(flightId);

                    double bookingPrice;

                    // New format has price in parts[3]
                    if (parts.length > 3 && parts[3] != null && !parts[3].trim().isEmpty()) {
                        bookingPrice = Double.parseDouble(parts[3].trim());
                    } else {
                        // Old format: no price stored, so use flight base price
                        bookingPrice = flight.getBasePrice();
                    }

                    Booking booking = new Booking(customer, flight, bookingDate, bookingPrice);

                    // Load status (backward compatible - default to ACTIVE)
                    if (parts.length > 4 && parts[4] != null && !parts[4].trim().isEmpty()) {
                        try {
                            booking.setStatus(BookingStatus.valueOf(parts[4].trim()));
                        } catch (IllegalArgumentException e) {
                            booking.setStatus(BookingStatus.ACTIVE);
                        }
                    }

                    // Load feeLast (backward compatible - default to 0.0)
                    if (parts.length > 5 && parts[5] != null && !parts[5].trim().isEmpty()) {
                        try {
                            booking.setFeeLast(Double.parseDouble(parts[5].trim()));
                        } catch (NumberFormatException e) {
                            booking.setFeeLast(0.0);
                        }
                    }

                    // Load feeType (backward compatible - default to null)
                    if (parts.length > 6 && parts[6] != null && !parts[6].trim().isEmpty()) {
                        booking.setFeeType(parts[6].trim());
                    }

                    // Rebuild relationships (even for canceled bookings, we keep them in the system)
                    customer.addBooking(booking);
                    if (booking.getStatus() == BookingStatus.ACTIVE) {
                        flight.addPassenger(customer);
                    }

                } catch (Exception ex) {
                    throw new FlightBookingSystemException(
                            "Invalid booking data on line " + lineIdx + "\nError: " + ex.getMessage());
                }

                lineIdx++;
            }
        }
    }

    /**
     * Saves all bookings from the system to the bookings.txt file.
     * Format: customerId::flightId::bookingDate::bookingPrice::status::feeLast::feeType::
     *
     * @param fbs the FlightBookingSystem containing bookings to save
     * @throws IOException if file writing fails
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    out.print(customer.getId() + SEPARATOR);
                    out.print(booking.getFlight().getId() + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.getBookingPrice() + SEPARATOR);
                    out.print(booking.getStatus() + SEPARATOR);
                    out.print(booking.getFeeLast() + SEPARATOR);
                    out.print(booking.getFeeType() != null ? booking.getFeeType() : "" + SEPARATOR);
                    out.println();
                }
            }
        }
    }
}
