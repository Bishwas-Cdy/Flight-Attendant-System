package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Flights list panel.
 * Admin: can Add/Deactivate/Reactivate.
 * Customer: view-only.
 */
public class FlightsPanel extends JPanel {

    private final FlightBookingSystem fbs;
    private final boolean adminMode;

    private final DefaultTableModel model;
    private final JTable table;

    private final JButton refreshBtn = new JButton("Refresh");
    private final JButton detailsBtn = new JButton("Show Details");
    private final JButton addBtn = new JButton("Add Flight");
    private final JButton deactivateBtn = new JButton("Deactivate");
    private final JButton reactivateBtn = new JButton("Reactivate");

    public FlightsPanel(FlightBookingSystem fbs, boolean adminMode) {
        this.fbs = fbs;
        this.adminMode = adminMode;

        model = new DefaultTableModel(new Object[]{
                "ID", "Flight No", "Origin", "Destination", "Date", "Seats", "Base Price", "Active"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);

        buildUi();
        loadFlights();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);
        top.add(detailsBtn);

        if (adminMode) {
            top.add(addBtn);
            top.add(deactivateBtn);
            top.add(reactivateBtn);
        }

        refreshBtn.addActionListener(e -> loadFlights());
        detailsBtn.addActionListener(e -> showDetails());

        if (adminMode) {
            addBtn.addActionListener(e -> {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                new AddFlightWindow(parent, fbs, this::loadFlights);
            });

            deactivateBtn.addActionListener(e -> setActive(false));
            reactivateBtn.addActionListener(e -> setActive(true));
        }

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadFlights() {
        model.setRowCount(0);

        List<Flight> flights;
        
        // Customers see only future flights (departure date > system date)
        // Admins see all flights
        if (adminMode) {
            flights = fbs.getFlights();
        } else {
            flights = fbs.getFutureFlights(fbs.getSystemDate());
        }
        
        for (Flight f : flights) {
            model.addRow(new Object[]{
                    f.getId(),
                    f.getFlightNumber(),
                    f.getOrigin(),
                    f.getDestination(),
                    f.getDepartureDate(),
                    f.getCapacity(),
                    String.format("%.2f", f.getBasePrice()),
                    f.isActive()
            });
        }
    }

    private Integer getSelectedFlightId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;

        Object v = model.getValueAt(row, 0);
        if (v instanceof Integer) return (Integer) v;
        return Integer.parseInt(v.toString());
    }

    private void showDetails() {
        Integer flightId = getSelectedFlightId();
        if (flightId == null) {
            JOptionPane.showMessageDialog(this, "Select a flight first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Flight f = fbs.getFlightByID(flightId);

            JTextArea area = new JTextArea(f.getDetailsLong());
            area.setEditable(false);
            area.setCaretPosition(0);

            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Flight Details",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setActive(boolean active) {
        Integer flightId = getSelectedFlightId();
        if (flightId == null) {
            JOptionPane.showMessageDialog(this, "Select a flight first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Flight f = fbs.getFlightByID(flightId);
            if (active) {
                f.reactivate();
            } else {
                f.deactivate();
            }
            loadFlights();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
