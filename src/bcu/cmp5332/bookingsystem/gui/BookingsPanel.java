package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Bookings panel for admin or customer.
 * If adminMode is false, customerIdLocked must be non-null.
 */
public class BookingsPanel extends JPanel {

    private final FlightBookingSystem fbs;
    private final boolean adminMode;
    private final Integer customerIdLocked;

    private final DefaultTableModel model;
    private final JTable table;

    private final JButton refreshBtn = new JButton("Refresh");
    private final JButton addBtn = new JButton("Add Booking");
    private final JButton cancelBtn = new JButton("Cancel Booking");
    private final JButton updateBtn = new JButton("Update Booking");

    public BookingsPanel(FlightBookingSystem fbs, boolean adminMode, Integer customerIdLocked) {
        this.fbs = fbs;
        this.adminMode = adminMode;
        this.customerIdLocked = customerIdLocked;

        model = new DefaultTableModel(new Object[]{"Customer ID", "Flight ID", "Booking Date", "Stored Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);

        buildUi();
        loadBookings();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);
        top.add(addBtn);
        top.add(cancelBtn);
        top.add(updateBtn);

        refreshBtn.addActionListener(e -> loadBookings());
        addBtn.addActionListener(e -> addBooking());
        cancelBtn.addActionListener(e -> cancelBooking());
        updateBtn.addActionListener(e -> updateBooking());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadBookings() {
        model.setRowCount(0);

        if (adminMode) {
            for (Customer c : fbs.getCustomers()) {
                for (Booking b : c.getBookings()) {
                    model.addRow(new Object[]{
                            c.getId(),
                            b.getFlight().getId(),
                            b.getBookingDate(),
                            String.format("%.2f", b.getBookingPrice())
                    });
                }
            }
        } else {
            try {
                Customer c = fbs.getCustomerByID(customerIdLocked);
                for (Booking b : c.getBookings()) {
                    model.addRow(new Object[]{
                            c.getId(),
                            b.getFlight().getId(),
                            b.getBookingDate(),
                            String.format("%.2f", b.getBookingPrice())
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Integer askCustomerIdIfNeeded() throws FlightBookingSystemException {
        if (!adminMode) {
            return customerIdLocked;
        }

        String s = JOptionPane.showInputDialog(this, "Customer ID:");
        if (s == null) throw new FlightBookingSystemException("Action cancelled.");
        s = s.trim();
        if (s.isEmpty()) throw new FlightBookingSystemException("Customer ID required.");

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Customer ID must be a number.");
        }
    }

    private int askFlightId(String prompt) throws FlightBookingSystemException {
        String s = JOptionPane.showInputDialog(this, prompt);
        if (s == null) throw new FlightBookingSystemException("Action cancelled.");
        s = s.trim();
        if (s.isEmpty()) throw new FlightBookingSystemException("Flight ID required.");

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Flight ID must be a number.");
        }
    }

    private void addBooking() {
        try {
            Integer customerId = askCustomerIdIfNeeded();
            int flightId = askFlightId("Flight ID:");

            new AddBooking(customerId, flightId).execute(fbs);
            loadBookings();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking() {
        try {
            Integer customerId = askCustomerIdIfNeeded();
            int flightId = askFlightId("Flight ID to cancel:");

            new CancelBooking(customerId, flightId).execute(fbs);
            loadBookings();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBooking() {
        try {
            Integer customerId = askCustomerIdIfNeeded();
            int oldFlightId = askFlightId("Old Flight ID:");
            int newFlightId = askFlightId("New Flight ID:");

            new UpdateBooking(customerId, oldFlightId, newFlightId).execute(fbs);
            loadBookings();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
