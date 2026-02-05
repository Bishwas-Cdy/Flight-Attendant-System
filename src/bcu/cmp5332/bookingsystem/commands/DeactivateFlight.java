package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Deactivates a flight. Admin only.
 */
public class DeactivateFlight implements Command {

    private final int flightId;
    private User currentUser;

    public DeactivateFlight(int flightId, User currentUser) {
        this.flightId = flightId;
        this.currentUser = currentUser;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new FlightBookingSystemException("Only admin can deactivate flights.");
        }

        Flight f = fbs.getFlightByID(flightId);
        f.deactivate();

        System.out.println("Flight deactivated successfully.");
    }
}
