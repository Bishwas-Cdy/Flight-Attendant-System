package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class BookingDataManager implements DataManager {

    public final String RESOURCE = "./resources/data/bookings.txt";

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

                    // Rebuild relationships
                    customer.addBooking(booking);
                    flight.addPassenger(customer);

                } catch (Exception ex) {
                    throw new FlightBookingSystemException(
                            "Invalid booking data on line " + lineIdx + "\nError: " + ex.getMessage());
                }

                lineIdx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    out.print(customer.getId() + SEPARATOR);
                    out.print(booking.getFlight().getId() + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.getBookingPrice() + SEPARATOR);
                    out.println();
                }
            }
        }
    }
}
