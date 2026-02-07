package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Placeholder command for adding a new admin account.
 * Special handling in Main.java - this command is not directly executed.
 */
public class AddAdmin implements Command {

    /**
     * Creates an AddAdmin command.
     * Note: This command is handled specially in Main.java, not executed directly.
     */
    public AddAdmin() {
    }

    /**
     * This method should not be called directly.
     * AddAdmin is handled by Main.java's handleAddAdmin() method.
     *
     * @param flightBookingSystem the FlightBookingSystem
     * @throws FlightBookingSystemException always, as this should be handled in Main
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        throw new FlightBookingSystemException("AddAdmin should be handled by Main, not executed directly");
    }
}
