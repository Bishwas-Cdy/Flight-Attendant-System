package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Interface for persistent data management.
 * Implementations handle loading and saving data objects to files.
 */
public interface DataManager {
    
    /** Delimiter used for separating fields in data files. */
    public static final String SEPARATOR = "::";
    
    /**
     * Loads data from a file and populates the FlightBookingSystem.
     *
     * @param fbs the FlightBookingSystem to populate
     * @throws IOException if file reading fails
     * @throws FlightBookingSystemException if data format is invalid
     */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException;
    
    /**
     * Saves data from the FlightBookingSystem to a file.
     *
     * @param fbs the FlightBookingSystem to save
     * @throws IOException if file writing fails
     */
    public void storeData(FlightBookingSystem fbs) throws IOException;
    
}
