package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Admin GUI window displaying tabbed interface for managing flights, customers, and bookings.
 */
public class AdminWindow extends JFrame {

    private final FlightBookingSystem fbs;
    private final GuiSession session;

    private final JTabbedPane tabs = new JTabbedPane();
    private JLabel dateLabel;

    private final FlightsPanel flightsPanel;
    private final CustomersPanel customersPanel;
    private final BookingsPanel bookingsPanel;

    /**
     * Creates a new admin window.
     *
     * @param fbs the FlightBookingSystem instance
     * @param session the authenticated admin session
     */
    public AdminWindow(FlightBookingSystem fbs, GuiSession session) {
        applyLightTheme();
        this.fbs = fbs;
        this.session = session;

        flightsPanel = new FlightsPanel(fbs, true);
        customersPanel = new CustomersPanel(fbs);
        bookingsPanel = new BookingsPanel(fbs, true, null);

        initialize();
    }

    /**
     * Initializes GUI components and layout.
     */
    private void initialize() {
        setTitle("Admin - Flight Booking System");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs.addTab("Flights", flightsPanel);
        tabs.addTab("Customers", customersPanel);
        tabs.addTab("Bookings", bookingsPanel);

        setJMenuBar(buildMenuBar());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);

        // Add status bar with system date
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        dateLabel = new JLabel("System Date: " + fbs.getSystemDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusBar.add(dateLabel);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem exit = new JMenuItem("Exit");

        save.addActionListener(e -> doSave());
        logout.addActionListener(e -> {
            dispose();
            new LoginWindow(fbs);
        });
        exit.addActionListener(e -> {
            doSave();
            System.exit(0);
        });

        file.add(save);
        file.add(logout);
        file.add(exit);

        JMenu system = new JMenu("System");
        JMenuItem advanceDate = new JMenuItem("Advance System Date");
        JMenuItem addAdmin = new JMenuItem("Add Admin");
        advanceDate.addActionListener(e -> handleAdvanceDate());
        addAdmin.addActionListener(e -> handleAddAdmin());
        system.add(advanceDate);
        system.add(addAdmin);

        bar.add(file);
        bar.add(system);
        return bar;
    }

    private void handleAdvanceDate() {
        String input = JOptionPane.showInputDialog(this,
                "Current System Date: " + fbs.getSystemDate() + "\n\nEnter new date (YYYY-MM-DD):",
                "Advance System Date",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        try {
            LocalDate newDate = LocalDate.parse(input.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            fbs.setSystemDate(newDate);
            FlightBookingSystemData.store(fbs);

            // Update the date label immediately
            dateLabel.setText("System Date: " + newDate);

            JOptionPane.showMessageDialog(this,
                    "System date set to: " + newDate,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh all panels to update based on new system date
            refreshAllPanels();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use YYYY-MM-DD (e.g., 2026-12-25)",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllPanels() {
        // Refresh the displayed data in all panels
        if (flightsPanel != null) flightsPanel.loadFlights();
        if (customersPanel != null) customersPanel.loadCustomers();
        if (bookingsPanel != null) bookingsPanel.loadBookings();
    }

    private void handleAddAdmin() {
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "First Name:", firstNameField,
                "Middle Name (optional):", middleNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Password:", passwordField
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Add Admin Account",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String firstName = firstNameField.getText().trim();
            String middleName = middleNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validation
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
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

            // Create admin account
            authService.registerAdmin(firstName, middleName, lastName, email, password);

            FlightBookingSystemData.store(fbs);

            JOptionPane.showMessageDialog(this,
                    "Admin account created successfully\n" +
                            "Email: " + email,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

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

    private void doSave() {
        try {
            FlightBookingSystemData.store(fbs);
            JOptionPane.showMessageDialog(this, "Saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
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
}
