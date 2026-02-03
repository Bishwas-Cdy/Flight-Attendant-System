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

        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int lineIdx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.trim().isEmpty()) {
                    lineIdx++;
                    continue;
                }

                String[] properties = line.split(SEPARATOR, -1);

                try {
                    int customerId = Integer.parseInt(properties[0]);
                    int flightId = Integer.parseInt(properties[1]);
                    LocalDate bookingDate = LocalDate.parse(properties[2]);

                    Customer customer = fbs.getCustomerByID(customerId);
                    Flight flight = fbs.getFlightByID(flightId);

                    Booking booking = new Booking(customer, flight, bookingDate);

                    customer.addBooking(booking);
                    flight.addPassenger(customer);

                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException(
                            "Unable to parse booking ids on line " + lineIdx + "\nError: " + ex);
                } catch (FlightBookingSystemException ex) {
                    throw new FlightBookingSystemException(
                            "Invalid booking data on line " + lineIdx + "\nError: " + ex.getMessage());
                } catch (IllegalArgumentException ex) {
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
                    out.println();
                }
            }
        }
    }
}
