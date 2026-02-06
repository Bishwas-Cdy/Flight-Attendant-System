package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.LocalDate;

/**
 * Advances the system date to a new future date. Admin only.
 */
public class AdvanceDate implements Command {

    private final LocalDate newDate;

    /**
     * Creates an AdvanceDate command.
     *
     * @param newDate the new system date (must be after current date)
     */
    public AdvanceDate(LocalDate newDate) {
        this.newDate = newDate;
    }

    /**
     * Executes the advance date command.
     * Updates the system date to the specified future date.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if new date is not in the future
     */
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
