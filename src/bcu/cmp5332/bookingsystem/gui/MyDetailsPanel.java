package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Shows current logged-in customer's details.
 */
public class MyDetailsPanel extends JPanel {

    private final FlightBookingSystem fbs;
    private final int customerId;

    private final JTextArea area = new JTextArea();

    private final JButton refreshBtn = new JButton("Refresh");

    public MyDetailsPanel(FlightBookingSystem fbs, int customerId) {
        this.fbs = fbs;
        this.customerId = customerId;

        buildUi();
        refresh();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        area.setEditable(false);
        area.setCaretPosition(0);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);

        refreshBtn.addActionListener(e -> refresh());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    private void refresh() {
        try {
            Customer c = fbs.getCustomerByID(customerId);
            area.setText(c.getDetailsLong());
            area.setCaretPosition(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
