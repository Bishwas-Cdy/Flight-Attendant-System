package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.gui.GuiAuthMenu;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Color;
import javax.swing.UIManager;

/**
 * Entry point for the Flight Booking System command-line interface.
 * Handles user authentication and command execution.
 */
public class Main {

    private static User currentUser = null;

    /**
     * Main entry point for the Flight Booking System.
     * Loads system data, authenticates users, and processes commands.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if file reading/writing fails
     * @throws FlightBookingSystemException if system operation fails
     */
    public static void main(String[] args) throws IOException, FlightBookingSystemException {

        // Set consistent Light Look and Feel for all GUI windows
        setGuiLookAndFeel();

        FlightBookingSystem fbs = FlightBookingSystemData.load();
        UserDataManager userDataManager = FlightBookingSystemData.getUserDataManager();
        AuthService authService = new AuthService(userDataManager.getUsers());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to Flight Booking System");

        while (true) {
            // ===== AUTH MENU (until login or exit) =====
            while (currentUser == null) {
                System.out.println();
                System.out.println("1. Login");
                System.out.println("2. Register (Customer)");
                System.out.println("3. Load GUI");
                System.out.println("4. Exit");
                System.out.print("Select option: ");

                String choice = br.readLine();

                if ("1".equals(choice)) {
                    System.out.print("Email: ");
                    String email = br.readLine().trim();

                    System.out.print("Password: ");
                    String password = br.readLine();

                    try {
                        currentUser = authService.login(email, password);
                        
                        // Check if customer is active
                        if (currentUser.getRole() == Role.CUSTOMER) {
                            Customer customer = fbs.getCustomerByID(currentUser.getCustomerId());
                            if (!customer.isActive()) {
                                System.out.println("Login failed: Your account has been deactivated. Please contact administrator.");
                                currentUser = null;
                                continue;
                            }
                        }
                        
                        System.out.println("Login successful. Welcome " + currentUser.getFullName()
                                + " [" + currentUser.getRole() + "]");
                        if (currentUser.getRole() == Role.CUSTOMER) {
                            System.out.println("Your Customer ID is: " + currentUser.getCustomerId());
                        }
                    } catch (FlightBookingSystemException ex) {
                        System.out.println(ex.getMessage());
                    }

                } else if ("2".equals(choice)) {
                    System.out.print("First name: ");
                    String first = br.readLine();

                    System.out.print("Middle name (press Enter if none): ");
                    String middle = br.readLine();

                    System.out.print("Last name: ");
                    String last = br.readLine();

                    String email;
                    while (true) {
                        System.out.print("Email: ");
                        email = br.readLine().trim();

                        if (!isValidEmail(email)) {
                            System.out.println("Invalid email format. Please enter a valid email.");
                            continue;
                        }

                        if (authService.emailExists(email)) {
                            System.out.println("Email already exists.");
                            continue;
                        }

                        break;
                    }

                    System.out.print("Password: ");
                    String password = br.readLine();

                    String phone;
                    while (true) {
                        System.out.print("Phone number (10 digits): ");
                        phone = br.readLine().trim();

                        if (!phone.matches("\\d{10}")) {
                            System.out.println("Invalid phone number. It must contain exactly 10 digits.");
                            continue;
                        }

                        if (fbs.phoneExists(phone)) {
                            System.out.println("Phone number already exists.");
                            continue;
                        }

                        break;
                    }

                    try {
                        // Create user
                        User user = authService.registerCustomer(first, middle, last, email, password);

                        // Create customer record and link to user
                        int newCustomerId = fbs.getCustomers().size() + 1;
                        Customer customer = new Customer(newCustomerId, user.getFullName(), phone);
                        fbs.addCustomer(customer);

                        user.setCustomerId(newCustomerId);

                        // Persist immediately
                        FlightBookingSystemData.store(fbs);

                        System.out.println("Registration successful. You can now login.");
                    } catch (FlightBookingSystemException ex) {
                        System.out.println(ex.getMessage());
                    }

                } else if ("3".equals(choice)) {
                    // Load GUI
                    javax.swing.SwingUtilities.invokeLater(() ->
                            new GuiAuthMenu(fbs)
                    );
                    return;

                } else if ("4".equals(choice)) {
                    FlightBookingSystemData.store(fbs);
                    System.exit(0);
                } else {
                    System.out.println("Invalid option.");
                }
            }

            // ===== POST-LOGIN MENU =====
            boolean backToAuth = false;
            while (!backToAuth) {

                System.out.println();
                if (currentUser.getRole() == Role.ADMIN) {
                    System.out.println("1. Enter Command Mode");
                    System.out.println("2. Create Admin Account");
                    System.out.println("3. Logout");
                    System.out.println("4. Exit");
                } else {
                    System.out.println("1. Enter Command Mode");
                    System.out.println("2. Logout");
                    System.out.println("3. Exit");
                }
                System.out.print("Select option: ");
                String opt = br.readLine();

                if ("1".equals(opt)) {
                    runCommandMode(br, fbs);
                    // returning here means user typed logout or we exited command mode
                    if (currentUser == null) {
                        backToAuth = true;
                    }

                } else if ("2".equals(opt) && currentUser.getRole() == Role.ADMIN) {
                    // Create Admin
                    System.out.print("First name: ");
                    String first = br.readLine();

                    System.out.print("Middle name (press Enter if none): ");
                    String middle = br.readLine();

                    System.out.print("Last name: ");
                    String last = br.readLine();

                    System.out.print("Email: ");
                    String email = br.readLine().trim();

                    System.out.print("Password: ");
                    String password = br.readLine();

                    try {
                        authService.createAdmin(currentUser, first, middle, last, email, password);
                        FlightBookingSystemData.store(fbs);
                        System.out.println("Admin account created successfully.");
                    } catch (FlightBookingSystemException ex) {
                        System.out.println(ex.getMessage());
                    }

                } else if (("2".equals(opt) && currentUser.getRole() == Role.CUSTOMER)
                        || ("3".equals(opt) && currentUser.getRole() == Role.ADMIN)) {
                    // Logout
                    currentUser = null;
                    backToAuth = true;

                } else if (("3".equals(opt) && currentUser.getRole() == Role.CUSTOMER)
                        || ("4".equals(opt) && currentUser.getRole() == Role.ADMIN)) {
                    // Exit
                    FlightBookingSystemData.store(fbs);
                    System.exit(0);

                } else {
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void runCommandMode(BufferedReader br, FlightBookingSystem fbs) throws IOException {

        System.out.println();
        System.out.println("System Date: " + fbs.getSystemDate());
        System.out.println("Enter 'help' to see available commands.");
        System.out.println("Type 'logout' to logout.");
        System.out.println("Type 'exit' to exit the program.");

        while (true) {
            System.out.print("> ");
            String line = br.readLine();

            if (line == null) {
                return;
            }

            String trimmed = line.trim();

            if (trimmed.equalsIgnoreCase("exit")) {
                try {
                    FlightBookingSystemData.store(fbs);
                } catch (IOException ignored) {
                }
                System.exit(0);
            }

            if (trimmed.equalsIgnoreCase("logout")) {
                currentUser = null;
                return;
            }

            try {
                // Role gates
                if (currentUser.getRole() == Role.CUSTOMER) {
                    String lower = trimmed.toLowerCase();

                    if (lower.startsWith("addflight") || lower.startsWith("addcustomer")
                            || lower.startsWith("listcustomers") || lower.startsWith("advancedate")) {
                        System.out.println("Only admin can use this command.");
                        continue;
                    }

                    if (lower.startsWith("showcustomer")) {
                        // Force showcustomer to own customerId
                        Integer cid = currentUser.getCustomerId();
                        if (cid == null) {
                            System.out.println("Your account is not linked to a customer record.");
                            continue;
                        }
                        trimmed = "showcustomer " + cid;
                    }

                    if (lower.startsWith("addbooking") || lower.startsWith("cancelbooking")
                            || lower.startsWith("updatebooking") || lower.startsWith("editbooking")) {

                        Integer cid = currentUser.getCustomerId();
                        if (cid == null) {
                            System.out.println("Your account is not linked to a customer record.");
                            continue;
                        }

                        // Validate that customer ID in command matches logged-in customer
                        if (!validateCustomerId(trimmed, cid)) {
                            System.out.println("Error: You can manage only your own bookings. Your customer ID is " + cid + ".");
                            continue;
                        }
                    }
                }

                // Special handling for addcustomer - requires both User and Customer creation (admin only)
                if (trimmed.toLowerCase().startsWith("addcustomer")) {
                    if (currentUser.getRole() != Role.ADMIN) {
                        System.out.println("Only admin can use this command.");
                    } else {
                        handleAddCustomerAdmin(br, fbs);
                        FlightBookingSystemData.store(fbs);
                    }
                    continue;
                }

                // Special handling for addadmin - requires admin User creation (admin only)
                if (trimmed.toLowerCase().startsWith("addadmin")) {
                    if (currentUser.getRole() != Role.ADMIN) {
                        System.out.println("Only admin can use this command.");
                    } else {
                        handleAddAdmin(br, fbs);
                        FlightBookingSystemData.store(fbs);
                    }
                    continue;
                }

                Command command = CommandParser.parse(trimmed, currentUser.getRole(), currentUser);
                command.execute(fbs);

                FlightBookingSystemData.store(fbs);

            } catch (FlightBookingSystemException ex) {
                System.out.println(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static String forceCustomerId(String commandLine, int customerId) {
        // command args:
        // addbooking <custId> <flightId>
        // cancelbooking <custId> <flightId>
        // updatebooking <custId> <oldFlightId> <newFlightId>
        // editbooking <custId> <newFlightId>
        String[] parts = commandLine.trim().split("\\s+");
        if (parts.length >= 2) {
            parts[1] = String.valueOf(customerId);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    private static boolean validateCustomerId(String commandLine, int loggedInCustomerId) {
        // Validates that the customer ID in the booking command matches the logged-in customer
        // command args:
        // addbooking <custId> <flightId>
        // cancelbooking <custId> <flightId>
        // updatebooking <custId> <oldFlightId> <newFlightId>
        // editbooking <custId> <newFlightId>
        try {
            String[] parts = commandLine.trim().split("\\s+");
            if (parts.length >= 2) {
                int providedCustomerId = Integer.parseInt(parts[1]);
                return providedCustomerId == loggedInCustomerId;
            }
            return false;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static void setGuiLookAndFeel() {
        try {
            // Use Metal Look and Feel for consistent light appearance
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
            
        } catch (Exception ex) {
            // Silently ignore - will use default Look and Feel
        }
    }

    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        if (email.contains(" ")) {
            return false;
        }

        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');

        // must contain exactly one '@'
        if (atIndex <= 0 || atIndex != lastAtIndex) {
            return false;
        }

        // must contain '.' after '@'
        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex <= atIndex + 1 || dotIndex == email.length() - 1) {
            return false;
        }

        return true;

    }

    private static void handleAddCustomerAdmin(BufferedReader br, FlightBookingSystem fbs) throws IOException {
        System.out.println();
        System.out.print("First name: ");
        String first = br.readLine();

        System.out.print("Middle name (press Enter if none): ");
        String middle = br.readLine();

        System.out.print("Last name: ");
        String last = br.readLine();

        // Email validation with uniqueness check
        String email;
        while (true) {
            System.out.print("Email: ");
            email = br.readLine().trim();

            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email.");
                continue;
            }

            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());
            
            if (authService.emailExists(email)) {
                System.out.println("Email already exists.");
                continue;
            }

            break;
        }

        System.out.print("Password: ");
        String password = br.readLine();

        // Phone validation
        String phone;
        while (true) {
            System.out.print("Phone number (10 digits): ");
            phone = br.readLine().trim();

            if (!phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number. It must contain exactly 10 digits.");
                continue;
            }

            if (fbs.phoneExists(phone)) {
                System.out.println("Phone number already exists.");
                continue;
            }

            break;
        }

        try {
            // Create user account
            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());
            User user = authService.registerCustomer(first, middle, last, email, password);

            // Create customer record and link to user
            int newCustomerId = fbs.getCustomers().size() + 1;
            Customer customer = new Customer(newCustomerId, user.getFullName(), phone);
            fbs.addCustomer(customer);

            user.setCustomerId(newCustomerId);

            FlightBookingSystemData.store(fbs);

            System.out.println("Customer added successfully with ID " + newCustomerId);
            System.out.println("Email: " + email);
            System.out.println("Phone: " + phone);
        } catch (FlightBookingSystemException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void handleAddAdmin(BufferedReader br, FlightBookingSystem fbs) throws IOException {
        System.out.println();
        System.out.print("First name: ");
        String first = br.readLine();

        System.out.print("Middle name (press Enter if none): ");
        String middle = br.readLine();

        System.out.print("Last name: ");
        String last = br.readLine();

        // Email validation with uniqueness check
        String email;
        while (true) {
            System.out.print("Email: ");
            email = br.readLine().trim();

            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email.");
                continue;
            }

            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());
            
            if (authService.emailExists(email)) {
                System.out.println("Email already exists.");
                continue;
            }

            break;
        }

        System.out.print("Password: ");
        String password = br.readLine();

        try {
            // Create admin user account
            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            AuthService authService = new AuthService(udm.getUsers());
            authService.registerAdmin(first, middle, last, email, password);

            System.out.println("Admin account created successfully");
            System.out.println("Email: " + email);
        } catch (FlightBookingSystemException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
