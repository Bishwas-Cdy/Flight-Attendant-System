package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Deactivates a flight (soft delete). Admin only.
 */
public class DeactivateFlight implements Command {

    private final int flightId;
    private User currentUser;

    /**
     * Creates a DeactivateFlight command.
     *
     * @param flightId the ID of the flight to deactivate
     * @param currentUser the admin user executing the command
     */
    public DeactivateFlight(int flightId, User currentUser) {
        this.flightId = flightId;
        this.currentUser = currentUser;
    }

    /**
     * Executes the deactivate flight command.
     * Prevents new bookings on this flight and hides it from flight lists.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if not admin or flight not found
     */
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
