package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
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
    private final JButton addBtn = new JButton("Add Customer");
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
        top.add(addBtn);
        top.add(detailsBtn);
        top.add(deactivateBtn);
        top.add(reactivateBtn);

        refreshBtn.addActionListener(e -> loadCustomers());
        addBtn.addActionListener(e -> handleAddCustomer());
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

    private void handleAddCustomer() {
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();

        Object[] fields = {
                "First Name:", firstNameField,
                "Middle Name (optional):", middleNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Password:", passwordField,
                "Phone (10 digits):", phoneField
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Add Customer",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String firstName = firstNameField.getText().trim();
            String middleName = middleNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText().trim();

            // Validation
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid email format.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this,
                        "Phone number must contain exactly 10 digits.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if email already exists
            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());

            if (authService.emailExists(email)) {
                JOptionPane.showMessageDialog(this,
                        "Email already exists.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create user account
            User user = authService.registerCustomer(firstName, middleName, lastName, email, password);

            // Create customer record and link to user
            int newId = fbs.getCustomers().size() + 1;
            Customer customer = new Customer(newId, user.getFullName(), phone);
            fbs.addCustomer(customer);

            user.setCustomerId(newId);

            FlightBookingSystemData.store(fbs);

            JOptionPane.showMessageDialog(this,
                    "Customer added successfully with ID " + newId + "\n" +
                            "Email: " + email + "\n" +
                            "Phone: " + phone,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCustomers();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }
}
