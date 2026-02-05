package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class Help implements Command {

    private Role role;

    public Help(Role role) {
        this.role = role;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        if (role == Role.ADMIN) {
            System.out.println(Command.ADMIN_HELP);
        } else {
            System.out.println(Command.CUSTOMER_HELP);
        }
    }
}
