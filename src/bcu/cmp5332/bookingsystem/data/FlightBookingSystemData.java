package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory and coordinator for all data persistence operations.
 * Manages loading and saving of flights, customers, bookings, and users.
 */
public class FlightBookingSystemData {

    private static final List<DataManager> dataManagers = new ArrayList<>();

    private static final UserDataManager userDataManager = new UserDataManager();
    private static final SystemDateManager systemDateManager = new SystemDateManager();

    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
    }

    /**
     * Loads all data from files into a new FlightBookingSystem.
     *
     * @return a FlightBookingSystem populated with data from files
     * @throws FlightBookingSystemException if data format is invalid
     * @throws IOException if file reading fails
     */
    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {

        FlightBookingSystem fbs = new FlightBookingSystem();

        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }

        // Load system date from file
        systemDateManager.loadData(fbs);

        // Users are not part of FlightBookingSystem object, so pass null
        userDataManager.loadData(null);

        return fbs;
    }

    /**
     * Saves all data from the FlightBookingSystem to files.
     *
     * @param fbs the FlightBookingSystem to save
     * @throws IOException if file writing fails
     */
    public static void store(FlightBookingSystem fbs) throws IOException {

        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }

        // Save system date to file
        systemDateManager.storeData(fbs);

        // Users are not part of FlightBookingSystem object, so pass null
        userDataManager.storeData(null);
    }

    /**
     * Returns the UserDataManager for user account operations.
     *
     * @return the UserDataManager instance
     */
    public static UserDataManager getUserDataManager() {
        return userDataManager;
    }
}
