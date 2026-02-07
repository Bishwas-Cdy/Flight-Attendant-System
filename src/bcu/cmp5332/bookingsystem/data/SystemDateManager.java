package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Manages persistence of the system date to and from file.
 */
public class SystemDateManager implements DataManager {

    private static final String FILEPATH = "resources/data/systemdate.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws FlightBookingSystemException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            String line = br.readLine();

            if (line != null && !line.trim().isEmpty()) {
                try {
                    LocalDate systemDate = LocalDate.parse(line.trim());
                    fbs.setSystemDate(systemDate);
                } catch (Exception ex) {
                    throw new FlightBookingSystemException("Invalid system date format in file: " + line);
                }
            }
            // If file is empty, use default date (2024-11-11)
        } catch (IOException ex) {
            // File doesn't exist yet - use default date
            if (fbs != null) {
                fbs.setSystemDate(LocalDate.parse("2024-11-11"));
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        if (fbs == null) return;

        try (PrintWriter pw = new PrintWriter("resources/data/systemdate.txt")) {
            pw.println(fbs.getSystemDate().toString());
        }
    }
}
