package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AddCustomer command class.
 * Tests that AddCustomer can be created and executed.
 */
public class AddCustomerTest {

    private AddCustomer addCustomerCommand;
    private FlightBookingSystem fbs;

    @BeforeEach
    public void setup() {
        addCustomerCommand = new AddCustomer("John Doe", "5551234567");
        fbs = new FlightBookingSystem();
    }

    @Test
    public void testAddCustomerCommand_Exists() {
        assertNotNull(addCustomerCommand);
    }

    @Test
    public void testAddCustomerCommand_IsCommandType() {
        assertTrue(addCustomerCommand instanceof Command);
    }

    @Test
    public void testAddCustomerCommand_CanBeCreated() {
        AddCustomer cmd = new AddCustomer("Jane Smith", "5559876543");
        assertNotNull(cmd);
    }

    @Test
    public void testAddCustomerCommand_Execute_Success() throws FlightBookingSystemException {
        assertEquals(0, fbs.getCustomers().size());
        
        addCustomerCommand.execute(fbs);
        
        assertEquals(1, fbs.getCustomers().size());
    }

    @Test
    public void testAddCustomerCommand_NameStored() throws FlightBookingSystemException {
        addCustomerCommand.execute(fbs);
        
        String customerName = fbs.getCustomers().get(0).getName();
        assertEquals("John Doe", customerName);
    }

    @Test
    public void testAddCustomerCommand_PhoneStored() throws FlightBookingSystemException {
        addCustomerCommand.execute(fbs);
        
        String customerPhone = fbs.getCustomers().get(0).getPhone();
        assertEquals("5551234567", customerPhone);
    }

    @Test
    public void testAddCustomerCommand_DifferentNames() throws FlightBookingSystemException {
        AddCustomer cmd1 = new AddCustomer("John", "1111111111");
        AddCustomer cmd2 = new AddCustomer("Jane", "2222222222");
        
        cmd1.execute(fbs);
        cmd2.execute(fbs);
        
        assertEquals(2, fbs.getCustomers().size());
        assertEquals("John", fbs.getCustomers().get(0).getName());
        assertEquals("Jane", fbs.getCustomers().get(1).getName());
    }
}

