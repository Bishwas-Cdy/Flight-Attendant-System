package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.LocalDate;

/**
 * Advances the system date. Admin only.
 */
public class AdvanceDate implements Command {

    private final LocalDate newDate;

    public AdvanceDate(LocalDate newDate) {
        this.newDate = newDate;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {

        if (newDate == null) {
            throw new IllegalArgumentException("New date cannot be null.");
        }

        LocalDate current = fbs.getSystemDate();

        if (!newDate.isAfter(current)) {
            throw new FlightBookingSystemException(
                    "New system date must be after current system date (" + current + ").");
        }

        fbs.setSystemDate(newDate);

        System.out.println("System date updated successfully.");
        System.out.println("New System Date: " + fbs.getSystemDate());
    }
}
