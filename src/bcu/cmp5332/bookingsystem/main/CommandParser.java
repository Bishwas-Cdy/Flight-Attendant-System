package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.commands.Help;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CommandParser {

    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        line = line.trim();

        if (line.isEmpty()) {
            throw new FlightBookingSystemException("Invalid command.");
        }

        try {
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Flight Number: ");
                String flightNumber = reader.readLine();

                System.out.print("Origin: ");
                String origin = reader.readLine();

                System.out.print("Destination: ");
                String destination = reader.readLine();

                LocalDate departureDate = parseDateWithAttempts(reader);

                return new AddFlight(flightNumber, origin, destination, departureDate);
            }

            if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Customer Name: ");
                String name = reader.readLine();

                System.out.print("Phone Number: ");
                String phone = reader.readLine();

                return new AddCustomer(name, phone);
            }

            if (cmd.equals("loadgui")) {
                return new LoadGUI();
            }

            if (parts.length == 1) {
                if (cmd.equals("listflights")) {
                    return new ListFlights();
                } else if (cmd.equals("listcustomers")) {
                    return new ListCustomers();
                } else if (cmd.equals("help")) {
                    return new Help();
                }
            }

            if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);

                if (cmd.equals("showflight")) {
                    return new ShowFlight(id);
                } else if (cmd.equals("showcustomer")) {
                    return new ShowCustomer(id);
                }
            }

            if (parts.length == 3) {
                int id1 = Integer.parseInt(parts[1]);
                int id2 = Integer.parseInt(parts[2]);

                if (cmd.equals("addbooking")) {
                    return new AddBooking(id1, id2);
                } else if (cmd.equals("cancelbooking")) {
                    return new CancelBooking(id1, id2);
                } else if (cmd.equals("editbooking") || cmd.equals("updatebooking")) {
                    // Two-id version from brief: update the customer's most recent booking
                    return new EditBooking(id1, id2);
                }
            }

            if (parts.length == 4) {
                int customerId = Integer.parseInt(parts[1]);
                int oldFlightId = Integer.parseInt(parts[2]);
                int newFlightId = Integer.parseInt(parts[3]);

                if (cmd.equals("updatebooking") || cmd.equals("editbooking")) {
                    // More explicit version: update from old flight to new flight
                    return new UpdateBooking(customerId, oldFlightId, newFlightId);
                }
            }

        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid number format in command.");
        }

        throw new FlightBookingSystemException("Invalid command.");
    }

    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts)
            throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher that 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }

        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }

    private static LocalDate parseDateWithAttempts(BufferedReader br)
            throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
