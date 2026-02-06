package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to show detailed information about a flight.
 */
public class ShowFlight implements Command {

    private final int flightId;

    /**
     * Creates a ShowFlight command.
     *
     * @param flightId the ID of the flight to display
     */
    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes the show flight command.
     * Displays detailed information about the flight.
     *
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if flight is not found
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem)
            throws FlightBookingSystemException {

        Flight flight = flightBookingSystem.getFlightByID(flightId);
        System.out.println(flight.getDetailsLong());
    }
}
