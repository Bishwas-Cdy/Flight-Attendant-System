package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AddAdmin command class.
 * Tests that AddAdmin throws proper exception when executed directly.
 */
public class AddAdminTest {

    private AddAdmin addAdminCommand;
    private FlightBookingSystem fbs;

    @BeforeEach
    public void setup() {
        addAdminCommand = new AddAdmin();
        fbs = new FlightBookingSystem();
    }

    @Test
    public void testAddAdminCommand_Exists() {
        assertNotNull(addAdminCommand);
    }

    @Test
    public void testAddAdminCommand_IsCommandType() {
        assertTrue(addAdminCommand instanceof Command);
    }

    @Test
    public void testAddAdminCommand_Execute_ThrowsException() {
        assertThrows(FlightBookingSystemException.class, () -> {
            addAdminCommand.execute(fbs);
        });
    }

    @Test
    public void testAddAdminCommand_ErrorMessage() {
        FlightBookingSystemException ex = assertThrows(FlightBookingSystemException.class, () -> {
            addAdminCommand.execute(fbs);
        });
        
        assertTrue(ex.getMessage().contains("Main"));
    }
}
