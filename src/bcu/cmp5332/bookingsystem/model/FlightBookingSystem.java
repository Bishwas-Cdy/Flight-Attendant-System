package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the whole Flight Booking System.
 * Stores all flights and customers in memory.
 */
public class FlightBookingSystem {

    private LocalDate systemDate = LocalDate.parse("2024-11-11");

    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();

    /**
     * Returns the current system date.
     *
     * @return the system date
     */
    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Sets the system date (admin command will validate rules).
     *
     * @param systemDate new system date
     */
    public void setSystemDate(LocalDate systemDate) {
        if (systemDate == null) {
            throw new IllegalArgumentException("System date cannot be null.");
        }
        this.systemDate = systemDate;
    }

    /**
     * Returns an unmodifiable list of all flights in the system.
     *
     * @return list of flights
     */
    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns a flight by id.
     *
     * @param id flight id
     * @return matching flight
     * @throws FlightBookingSystemException if not found
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    /**
     * Returns an unmodifiable list of all customers.
     *
     * @return customers list
     */
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns a customer by id.
     *
     * @param id customer id
     * @return matching customer
     * @throws FlightBookingSystemException if not found
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("There is no customer with that ID.");
        }
        return customers.get(id);
    }

    /**
     * Adds a new flight to the system.
     *
     * @param flight the flight to add
     * @throws IllegalArgumentException if flight is null or duplicate flight id
     * @throws FlightBookingSystemException if flight number and departure date already exist
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null.");
        }

        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }

        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber())
                    && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException(
                        "There is a flight with same number and departure date in the system");
            }
        }

        flights.put(flight.getId(), flight);
    }

    /**
     * Adds a new customer to the system.
     *
     * @param customer the customer to add
     * @throws IllegalArgumentException if customer is null or duplicate customer id
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer ID.");
        }

        customers.put(customer.getId(), customer);
    }

    /**
     * Checks if a phone number already exists in the system.
     *
     * @param phone the phone number to check
     * @return true if phone exists, false otherwise
     */
    public boolean phoneExists(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        for (Customer customer : customers.values()) {
            if (customer.getPhone().equals(phone.trim())) {
                return true;
            }
        }
        return false;
    }
}
