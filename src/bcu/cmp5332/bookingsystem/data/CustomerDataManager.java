package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {

        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int lineIdx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.trim().isEmpty()) {
                    lineIdx++;
                    continue;
                }

                String[] properties = line.split(SEPARATOR, -1);

                try {
                    int id = Integer.parseInt(properties[0]);
                    String name = properties[1];
                    String phone = properties[2];

                    Customer customer = new Customer(id, name, phone);
                    
                    // Handle active flag (backward compatible - default to true if missing)
                    boolean active = true;
                    if (properties.length > 3 && !properties[3].isEmpty()) {
                        active = Boolean.parseBoolean(properties[3]);
                    }
                    
                    if (!active) {
                        customer.deactivate();
                    }
                    
                    fbs.addCustomer(customer);

                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException(
                            "Unable to parse customer id " + properties[0] + " on line " + lineIdx + "\nError: " + ex);
                } catch (IllegalArgumentException ex) {
                    throw new FlightBookingSystemException(
                            "Invalid customer data on line " + lineIdx + "\nError: " + ex.getMessage());
                }

                lineIdx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getCustomers()) {
                out.print(customer.getId() + SEPARATOR);
                out.print(customer.getName() + SEPARATOR);
                out.print(customer.getPhone() + SEPARATOR);
                out.print(customer.isActive() + SEPARATOR);
                out.println();
            }
        }
    }
}
