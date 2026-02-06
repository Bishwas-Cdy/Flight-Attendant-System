package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * Displays all active flights in the system.
 * Shows flight details and counts the total number of flights.
 */
public class ListFlights implements Command {

    /**
     * Executes the list flights command.
     * Prints all active flights and the total count.
     *
     * @param flightBookingSystem the FlightBookingSystem to list flights from
     * @throws FlightBookingSystemException if execution fails
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> flights = flightBookingSystem.getFlights();
        int count = 0;
        for (Flight flight : flights) {
            if (flight.isActive()) {
                System.out.println(flight.getDetailsShort());
                count++;
            }
        }
        System.out.println(count + " flight(s)");
    }
}
