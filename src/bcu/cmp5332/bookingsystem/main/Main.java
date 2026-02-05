package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.auth.AuthService;
import bcu.cmp5332.bookingsystem.auth.Role;
import bcu.cmp5332.bookingsystem.auth.User;
import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static User currentUser = null;

    public static void main(String[] args) throws IOException, FlightBookingSystemException {

        FlightBookingSystem fbs = FlightBookingSystemData.load();
        UserDataManager userDataManager = FlightBookingSystemData.getUserDataManager();
        AuthService authService = new AuthService(userDataManager.getUsers());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=== Flight Booking System ===");

        while (true) {
            // ===== AUTH MENU (until login or exit) =====
            while (currentUser == null) {
                System.out.println();
                System.out.println("1. Login");
                System.out.println("2. Register (Customer)");
                System.out.println("3. Exit");
                System.out.print("Select option: ");

                String choice = br.readLine();

                if ("1".equals(choice)) {
                    System.out.print("Email: ");
                    String email = br.readLine();

                    System.out.print("Password: ");
                    String password = br.readLine();

                    try {
                        currentUser = authService.login(email, password);
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
                        email = br.readLine();

                        if (isValidEmail(email)) {
                            break;
                        } else {
                            System.out.println("Invalid email format. Please enter a valid email.");
                        }
                    }

                    System.out.print("Password: ");
                    String password = br.readLine();

                    String phone;
                    while (true) {
                        System.out.print("Phone number (10 digits): ");
                        phone = br.readLine();

                        if (phone.matches("\\d{10}")) {
                            break;
                        } else {
                            System.out.println("Invalid phone number. It must contain exactly 10 digits.");
                        }
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
                    String email = br.readLine();

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
                            || lower.startsWith("listcustomers")) {
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

                        // Replace first numeric argument with logged in customer's id
                        trimmed = forceCustomerId(trimmed, cid);
                    }
                }

                Command command = CommandParser.parse(trimmed, currentUser.getRole());
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
}
