package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Add flight window (works with AdminWindow + FlightsPanel).
 * Uses Command pattern to add flight.
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private final JFrame parent;
    private final FlightBookingSystem fbs;
    private final Runnable onSuccessRefresh;

    private final JTextField flightNoText = new JTextField();
    private final JTextField originText = new JTextField();
    private final JTextField destinationText = new JTextField();
    private final JTextField depDateText = new JTextField();
    private final JTextField capacityText = new JTextField();
    private final JTextField basePriceText = new JTextField();

    private final JButton addBtn = new JButton("Add");
    private final JButton cancelBtn = new JButton("Cancel");

    public AddFlightWindow(JFrame parent, FlightBookingSystem fbs, Runnable onSuccessRefresh) {
        this.parent = parent;
        this.fbs = fbs;
        this.onSuccessRefresh = onSuccessRefresh;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Add a New Flight");
        setSize(360, 300);

        JPanel topPanel = new JPanel(new GridLayout(6, 2));
        topPanel.add(new JLabel("Flight No: "));
        topPanel.add(flightNoText);

        topPanel.add(new JLabel("Origin: "));
        topPanel.add(originText);

        topPanel.add(new JLabel("Destination: "));
        topPanel.add(destinationText);

        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD): "));
        topPanel.add(depDateText);

        topPanel.add(new JLabel("Capacity (seats): "));
        topPanel.add(capacityText);

        topPanel.add(new JLabel("Base Price: "));
        topPanel.add(basePriceText);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.add(new JLabel(" "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            setVisible(false);
            dispose();
        }
    }

    private void addFlight() {
        try {
            String flightNumber = flightNoText.getText().trim();
            String origin = originText.getText().trim();
            String destination = destinationText.getText().trim();

            if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
                throw new FlightBookingSystemException("Flight number, origin and destination cannot be empty.");
            }

            LocalDate departureDate;
            try {
                departureDate = LocalDate.parse(depDateText.getText().trim());
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format.");
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityText.getText().trim());
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Capacity must be a valid integer.");
            }
            if (capacity < 0) {
                throw new FlightBookingSystemException("Capacity must be non-negative.");
            }

            double basePrice;
            try {
                basePrice = Double.parseDouble(basePriceText.getText().trim());
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Base price must be a valid number.");
            }
            if (basePrice < 0) {
                throw new FlightBookingSystemException("Base price must be non-negative.");
            }

            Command addFlightCmd = new AddFlight(flightNumber, origin, destination, departureDate, capacity, basePrice);
            addFlightCmd.execute(fbs);

            JOptionPane.showMessageDialog(this, "Flight added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (onSuccessRefresh != null) {
                onSuccessRefresh.run();
            }

            setVisible(false);
            dispose();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
