package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Displays the help message with all available commands.
 * Shows different commands for admin and customer roles.
 */
public class Help implements Command {

    private Role role;

    /**
     * Creates a Help command for the specified user role.
     *
     * @param role the user's role (ADMIN or CUSTOMER)
     */
    public Help(Role role) {
        this.role = role;
    }

    /**
     * Executes the help command by printing the appropriate help message.
     *
     * @param flightBookingSystem the FlightBookingSystem (not used)
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        if (role == Role.ADMIN) {
            System.out.println(Command.ADMIN_HELP);
        } else {
            System.out.println(Command.CUSTOMER_HELP);
        }
    }
}
