package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages persistence of Flight data to/from file storage.
 * Handles both legacy format (5 fields) and new format (7 fields with capacity and basePrice).
 */
public class FlightDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/flights.txt";
    
    /**
     * Loads flight data from file. Supports backward compatibility with old format.
     * Format: id::flightNumber::origin::destination::departureDate::capacity::basePrice::active::
     * 
     * @param fbs the flight booking system to populate
     * @throws IOException if file read fails
     * @throws FlightBookingSystemException if data parsing fails
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    
                    // Handle capacity (backward compatible - default to 0 if missing)
                    int capacity = 0;
                    if (properties.length > 5 && !properties[5].isEmpty()) {
                        try {
                            capacity = Integer.parseInt(properties[5]);
                        } catch (NumberFormatException e) {
                            capacity = 0;
                        }
                    }
                    
                    // Handle basePrice (backward compatible - default to 0.0 if missing)
                    double basePrice = 0.0;
                    if (properties.length > 6 && !properties[6].isEmpty()) {
                        try {
                            basePrice = Double.parseDouble(properties[6]);
                        } catch (NumberFormatException e) {
                            basePrice = 0.0;
                        }
                    }
                    
                    Flight flight = new Flight(id, flightNumber, origin, destination, 
                                               departureDate, capacity, basePrice);
                    
                    // Handle active flag (backward compatible - default to true if missing)
                    boolean active = true;
                    if (properties.length > 7 && !properties[7].isEmpty()) {
                        active = Boolean.parseBoolean(properties[7]);
                    }
                    
                    if (!active) {
                        flight.deactivate();
                    }
                    
                    fbs.addFlight(flight);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse flight id " + properties[0] 
                        + " on line " + line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores flight data to file in new format including capacity, basePrice, and active flag.
     * Format: id::flightNumber::origin::destination::departureDate::capacity::basePrice::active::
     * 
     * @param fbs the flight booking system containing flights to store
     * @throws IOException if file write fails
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Flight flight : fbs.getFlights()) {
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.getBasePrice() + SEPARATOR);
                out.print(flight.isActive() + SEPARATOR);
                out.println();
            }
        }
    }
}
