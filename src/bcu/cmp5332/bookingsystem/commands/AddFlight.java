package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command to add a new flight with capacity and base price.
 */
public class AddFlight implements Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int capacity;
    private final double basePrice;

    /**
     * Constructor for AddFlight command with capacity and pricing.
     *
     * @param flightNumber unique flight identifier
     * @param origin departure city
     * @param destination arrival city
     * @param departureDate date of departure
     * @param capacity total number of seats
     * @param basePrice base ticket price
     */
    public AddFlight(String flightNumber, String origin, String destination,
                     LocalDate departureDate, int capacity, double basePrice) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    /**
     * Executes the add flight command.
     * 
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if flight cannot be added
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Find next available ID
        int maxId = 0;
        for (Flight f : flightBookingSystem.getFlights()) {
            if (f.getId() > maxId) {
                maxId = f.getId();
            }
        }
        
        // Create and add new flight
        Flight flight = new Flight(++maxId, flightNumber, origin, destination, 
                                   departureDate, capacity, basePrice);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added successfully.");
        System.out.println("- Flight Number: " + flightNumber);
        System.out.println("- Capacity: " + capacity + " seats");
        System.out.println("- Base Price: $" + String.format("%.2f", basePrice));
    }
}
