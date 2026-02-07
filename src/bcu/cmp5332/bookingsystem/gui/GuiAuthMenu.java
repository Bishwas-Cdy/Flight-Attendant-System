package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

/**
 * GUI Authentication Menu.
 * Mirrors CLI auth: Login / Register (Customer) / Exit.
 * Uses same auth service and data as CLI.
 */
public class GuiAuthMenu extends JFrame {

    private final FlightBookingSystem fbs;

    public GuiAuthMenu(FlightBookingSystem fbs) {
        this.fbs = fbs;
        applyLightTheme();
        initialize();
    }

    private static void applyLightTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            
            // Force light colors explicitly to prevent dark mode
            UIManager.put("Panel.background", new Color(238, 238, 238));
            UIManager.put("Button.background", new Color(218, 218, 218));
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Label.foreground", Color.BLACK);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("TextArea.foreground", Color.BLACK);
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.foreground", Color.BLACK);
            UIManager.put("Table.gridColor", Color.LIGHT_GRAY);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", Color.BLACK);
            UIManager.put("OptionPane.background", new Color(238, 238, 238));
            UIManager.put("OptionPane.messageForeground", Color.BLACK);
        } catch (Exception ex) {
            // Silently ignore
        }
    }

    private void initialize() {
        setTitle("Flight Booking System");
        setSize(380, 240);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome to Flight Booking System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register (Customer)");
        JButton exitBtn = new JButton("Exit");

        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.add(exitBtn);

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> handleRegister());
        exitBtn.addActionListener(e -> System.exit(0));

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null) return;

        String password = JOptionPane.showInputDialog(this, "Password:");
        if (password == null) return;

        try {
            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());

            User user = authService.login(email, password);

            // Check if customer is active
            if (user.getRole() == Role.CUSTOMER) {
                Customer customer = fbs.getCustomerByID(user.getCustomerId());
                if (!customer.isActive()) {
                    JOptionPane.showMessageDialog(this,
                            "Your account has been deactivated. Please contact administrator.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Login successful.\nWelcome " + user.getFullName() + " [" + user.getRole() + "]",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // Create session and open appropriate window
            GuiSession session = new GuiSession(user, user.getRole(),
                    user.getRole() == Role.CUSTOMER ? user.getCustomerId() : null);

            if (user.getRole() == Role.ADMIN) {
                new AdminWindow(fbs, session);
            } else {
                new CustomerWindow(fbs, session);
            }

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        JTextField firstField = new JTextField();
        JTextField middleField = new JTextField();
        JTextField lastField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();

        Object[] fields = {
                "First Name:", firstField,
                "Middle Name (optional):", middleField,
                "Last Name:", lastField,
                "Email:", emailField,
                "Password:", passwordField,
                "Phone (10 digits):", phoneField
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Register Customer",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String first = firstField.getText().trim();
            String middle = middleField.getText().trim();
            String last = lastField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();

            // Validation
            if (first.isEmpty() || last.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
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

            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());

            // Check if email already exists
            if (authService.emailExists(email)) {
                JOptionPane.showMessageDialog(this,
                        "Email already exists.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if phone already exists
            if (fbs.phoneExists(phone)) {
                JOptionPane.showMessageDialog(this,
                        "Phone number already exists.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create user
            User user = authService.registerCustomer(first, middle, last, email, password);

            // Create customer record and link to user
            int newCustomerId = fbs.getCustomers().size() + 1;
            Customer customer = new Customer(newCustomerId, user.getFullName(), phone);
            fbs.addCustomer(customer);

            user.setCustomerId(newCustomerId);

            // Persist
            FlightBookingSystemData.store(fbs);

            JOptionPane.showMessageDialog(this,
                    "Registration successful. You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        if (email.contains(" ")) {
            return false;
        }

        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');

        if (atIndex <= 0 || atIndex != lastAtIndex) {
            return false;
        }

        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex <= atIndex + 1 || dotIndex == email.length() - 1) {
            return false;
        }

        return true;
    }
}
