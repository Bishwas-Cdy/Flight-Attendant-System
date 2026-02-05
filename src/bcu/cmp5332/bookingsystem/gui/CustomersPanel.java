package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin customers panel.
 */
public class CustomersPanel extends JPanel {

    private final FlightBookingSystem fbs;

    private final DefaultTableModel model;
    private final JTable table;

    private final JButton refreshBtn = new JButton("Refresh");
    private final JButton detailsBtn = new JButton("Show Details");
    private final JButton deactivateBtn = new JButton("Deactivate");
    private final JButton reactivateBtn = new JButton("Reactivate");

    public CustomersPanel(FlightBookingSystem fbs) {
        this.fbs = fbs;

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Phone", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);

        buildUi();
        loadCustomers();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);
        top.add(detailsBtn);
        top.add(deactivateBtn);
        top.add(reactivateBtn);

        refreshBtn.addActionListener(e -> loadCustomers());
        detailsBtn.addActionListener(e -> showDetails());
        deactivateBtn.addActionListener(e -> setActive(false));
        reactivateBtn.addActionListener(e -> setActive(true));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadCustomers() {
        model.setRowCount(0);
        List<Customer> customers = fbs.getCustomers();
        for (Customer c : customers) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getPhone(),
                    c.isActive()
            });
        }
    }

    private Integer getSelectedCustomerId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object v = model.getValueAt(row, 0);
        if (v instanceof Integer) return (Integer) v;
        return Integer.parseInt(v.toString());
    }

    private void showDetails() {
        Integer id = getSelectedCustomerId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Customer c = fbs.getCustomerByID(id);
            JTextArea area = new JTextArea(c.getDetailsLong());
            area.setEditable(false);
            area.setCaretPosition(0);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Customer Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setActive(boolean active) {
        Integer id = getSelectedCustomerId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a customer first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Customer c = fbs.getCustomerByID(id);
            if (active) {
                c.reactivate();
            } else {
                c.deactivate();
            }
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
