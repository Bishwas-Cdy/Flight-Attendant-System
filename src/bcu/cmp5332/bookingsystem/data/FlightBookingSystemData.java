package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingSystemData {

    private static final List<DataManager> dataManagers = new ArrayList<>();

    private static final UserDataManager userDataManager = new UserDataManager();

    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
    }

    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {

        FlightBookingSystem fbs = new FlightBookingSystem();

        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }

        // Users are not part of FlightBookingSystem object, so pass null
        userDataManager.loadData(null);

        return fbs;
    }

    public static void store(FlightBookingSystem fbs) throws IOException {

        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }

        // Users are not part of FlightBookingSystem object, so pass null
        userDataManager.storeData(null);
    }

    public static UserDataManager getUserDataManager() {
        return userDataManager;
    }
}
