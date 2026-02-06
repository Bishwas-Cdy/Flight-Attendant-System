package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.SwingUtilities;

/**
 * Loads the GUI version of the application.
 * Opens a login window for user authentication.
 */
public class LoadGUI implements Command {

    /**
     * Executes the load GUI command.
     * Launches the login window on the Swing event dispatch thread.
     *
     * @param flightBookingSystem the flight booking system
     * @throws FlightBookingSystemException if GUI launch fails
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        SwingUtilities.invokeLater(() -> new LoginWindow(flightBookingSystem));
    }
}
