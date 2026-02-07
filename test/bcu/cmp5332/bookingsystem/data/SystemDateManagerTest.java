package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SystemDateManager class.
 * Tests loading and saving system date.
 */
public class SystemDateManagerTest {

    private SystemDateManager dateManager;
    private FlightBookingSystem fbs;

    @BeforeEach
    public void setup() {
        dateManager = new SystemDateManager();
        fbs = new FlightBookingSystem();
    }

    @Test
    public void testLoadData_SetsSystemDate() throws FlightBookingSystemException, IOException {
        dateManager.loadData(fbs);
        
        assertNotNull(fbs.getSystemDate());
    }

    @Test
    public void testLoadData_ReturnsValidDate() throws FlightBookingSystemException, IOException {
        dateManager.loadData(fbs);
        LocalDate loadedDate = fbs.getSystemDate();
        
        assertNotNull(loadedDate);
        assertTrue(loadedDate.getYear() >= 2024);
    }

    @Test
    public void testStoreData_Success() throws IOException, FlightBookingSystemException {
        LocalDate testDate = LocalDate.of(2026, 5, 15);
        fbs.setSystemDate(testDate);
        
        dateManager.storeData(fbs);
        
        FlightBookingSystem fbs2 = new FlightBookingSystem();
        dateManager.loadData(fbs2);
        
        assertEquals(testDate, fbs2.getSystemDate());
    }

    @Test
    public void testStoreData_FutureDate() throws IOException, FlightBookingSystemException {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        fbs.setSystemDate(futureDate);
        
        dateManager.storeData(fbs);
        
        FlightBookingSystem fbs2 = new FlightBookingSystem();
        dateManager.loadData(fbs2);
        
        assertEquals(futureDate, fbs2.getSystemDate());
    }

    @Test
    public void testStoreData_WithNull_DoesNotThrowException() throws IOException {
        assertDoesNotThrow(() -> {
            dateManager.storeData(null);
        });
    }

    @Test
    public void testSystemDate_IsNotNull() throws FlightBookingSystemException, IOException {
        dateManager.loadData(fbs);
        
        assertNotNull(fbs.getSystemDate());
    }

    @Test
    public void testLoadData_DateFormatIsValid() throws FlightBookingSystemException, IOException {
        dateManager.loadData(fbs);
        LocalDate date = fbs.getSystemDate();
        
        assertTrue(date.getMonthValue() >= 1 && date.getMonthValue() <= 12);
        assertTrue(date.getDayOfMonth() >= 1 && date.getDayOfMonth() <= 31);
    }
}
