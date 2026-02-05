package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.data.DataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDataManager implements DataManager {

    private static final String RESOURCE = "./resources/data/users.txt";
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {

        users.clear();

        File file = new File(RESOURCE);
        if (!file.exists()) {
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            int lineIdx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.trim().isEmpty()) {
                    lineIdx++;
                    continue;
                }

                String[] parts = line.split(SEPARATOR, -1);

                try {
                    int id = Integer.parseInt(parts[0]);
                    String first = parts[1];
                    String middle = parts[2];
                    String last = parts[3];
                    String email = parts[4];
                    String password = parts[5];
                    Role role = Role.valueOf(parts[6]);

                    Integer customerId = null;
                    if (parts.length > 7 && parts[7] != null && !parts[7].trim().isEmpty()) {
                        customerId = Integer.parseInt(parts[7].trim());
                    }

                    users.add(new User(id, first, middle, last, email, password, role, customerId));

                } catch (Exception ex) {
                    throw new FlightBookingSystemException(
                            "Invalid users.txt data on line " + lineIdx + "\nError: " + ex.getMessage());
                }

                lineIdx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (User u : users) {
                out.print(u.getId() + SEPARATOR);
                out.print(u.getFirstName() + SEPARATOR);
                out.print(u.getMiddleName() + SEPARATOR);
                out.print(u.getLastName() + SEPARATOR);
                out.print(u.getEmail() + SEPARATOR);
                out.print(u.getPassword() + SEPARATOR);
                out.print(u.getRole() + SEPARATOR);

                if (u.getCustomerId() == null) {
                    out.print("" + SEPARATOR);
                } else {
                    out.print(u.getCustomerId() + SEPARATOR);
                }

                out.println();
            }
        }
    }
}
