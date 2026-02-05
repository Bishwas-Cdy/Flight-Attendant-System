package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.AdvanceDate;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.DeactivateCustomer;
import bcu.cmp5332.bookingsystem.commands.DeactivateFlight;
import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.commands.Help;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ReactivateCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands and creates corresponding Command objects.
 */
public class CommandParser {

    /**
     * Parses a command line and returns the appropriate Command object.
     *
     * @param line the command line input
     * @param role the user's role
     * @param user the current user
     * @return a Command object
     * @throws IOException if input reading fails
     * @throws FlightBookingSystemException if command is invalid
     */
    public static Command parse(String line, Role role, User user) throws IOException, FlightBookingSystemException {
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

                int capacity = readIntWithRetry(reader, "Capacity (seats): ", 0);
                double basePrice = readDoubleWithRetry(reader, "Base Price ($): ", 0);

                return new AddFlight(flightNumber, origin, destination, departureDate, capacity, basePrice);
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

            if (cmd.equals("advancedate")) {
                if (parts.length != 2) {
                    throw new FlightBookingSystemException("Usage: advancedate YYYY-MM-DD");
                }
                LocalDate newDate = LocalDate.parse(parts[1]);
                return new AdvanceDate(newDate);
            }

            if (parts.length == 1) {
                if (cmd.equals("listflights")) {
                    return new ListFlights();
                } else if (cmd.equals("listcustomers")) {
                    return new ListCustomers();
                } else if (cmd.equals("help")) {
                    return new Help(role);
                }
            }

            if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);

                if (cmd.equals("showflight")) {
                    return new ShowFlight(id);
                } else if (cmd.equals("showcustomer")) {
                    return new ShowCustomer(id);
                } else if (cmd.equals("deactivatecustomer")) {
                    return new DeactivateCustomer(id, user);
                } else if (cmd.equals("reactivatecustomer")) {
                    return new ReactivateCustomer(id, user);
                } else if (cmd.equals("deactivateflight")) {
                    return new DeactivateFlight(id, user);
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
                    return new EditBooking(id1, id2);
                }
            }

            if (parts.length == 4) {
                int customerId = Integer.parseInt(parts[1]);
                int oldFlightId = Integer.parseInt(parts[2]);
                int newFlightId = Integer.parseInt(parts[3]);

                if (cmd.equals("updatebooking") || cmd.equals("editbooking")) {
                    return new UpdateBooking(customerId, oldFlightId, newFlightId);
                }
            }

        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid number format in command.");
        }

        throw new FlightBookingSystemException("Invalid command.");
    }

    /**
     * Reads an integer from user input with retry logic.
     * Keeps prompting until a valid integer >= minValue is entered.
     *
     * @param br BufferedReader for input
     * @param prompt the prompt to display to user
     * @param minValue the minimum acceptable value (inclusive)
     * @return the validated integer input
     * @throws IOException if input reading fails
     */
    public static int readIntWithRetry(BufferedReader br, String prompt, int minValue) throws IOException {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(br.readLine());
                if (value < minValue) {
                    System.out.println("Value must be at least " + minValue + ". Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a double from user input with retry logic.
     * Keeps prompting until a valid double >= minValue is entered.
     *
     * @param br BufferedReader for input
     * @param prompt the prompt to display to user
     * @param minValue the minimum acceptable value (inclusive)
     * @return the validated double input
     * @throws IOException if input reading fails
     */
    public static double readDoubleWithRetry(BufferedReader br, String prompt, double minValue) throws IOException {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(br.readLine());
                if (value < minValue) {
                    System.out.println("Value must be at least " + minValue + ". Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Parses departure date from user input with retry logic.
     *
     * @param br BufferedReader for input
     * @param attempts number of retry attempts allowed
     * @return the parsed LocalDate
     * @throws IOException if input reading fails
     * @throws FlightBookingSystemException if date parsing fails after all attempts
     */
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

    /**
     * Parses departure date with default 3 retry attempts.
     *
     * @param br BufferedReader for input
     * @return the parsed LocalDate
     * @throws IOException if input reading fails
     * @throws FlightBookingSystemException if date parsing fails
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br)
            throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}

