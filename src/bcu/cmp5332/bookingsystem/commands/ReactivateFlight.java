package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Reactivates a previously deactivated flight. Admin only.
 */
public class ReactivateFlight implements Command {

    private final int flightId;
    private User currentUser;

    /**
     * Creates a ReactivateFlight command.
     *
     * @param flightId the ID of the flight to reactivate
     * @param currentUser the admin user executing the command
     */
    public ReactivateFlight(int flightId, User currentUser) {
        this.flightId = flightId;
        this.currentUser = currentUser;
    }

    /**
     * Executes the reactivate flight command.
     * Allows bookings on this flight again and includes it in flight lists.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if not admin or flight not found
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new FlightBookingSystemException("Only admin can reactivate flights.");
        }

        Flight f = fbs.getFlightByID(flightId);
        f.reactivate();

        System.out.println("Flight reactivated successfully.");
    }
}
