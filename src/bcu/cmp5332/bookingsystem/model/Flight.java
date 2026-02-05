package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a flight in the flight booking system.
 */
public class Flight {

    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;

    // New fields
    private int capacity;
    private double basePrice;

    private Set<Customer> passengers = new HashSet<>();

    /**
     * Constructor with capacity and base price.
     *
     * @param id flight id
     * @param flightNumber flight number
     * @param origin origin
     * @param destination destination
     * @param departureDate departure date
     * @param capacity total seats available
     * @param basePrice base price for booking
     */
    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate, int capacity, double basePrice) {

        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("Flight number cannot be empty.");
        }
        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin cannot be empty.");
        }
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination cannot be empty.");
        }
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null.");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative.");
        }

        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    /**
     * Backwards-compatible constructor (if older code still calls it).
     */
    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate) {
        this(id, flightNumber, origin, destination, departureDate, 0, 0.0);
    }

    public int getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("Flight number cannot be empty.");
        }
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin cannot be empty.");
        }
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination cannot be empty.");
        }
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null.");
        }
        this.departureDate = departureDate;
    }

    /**
     * Returns the capacity (total seats) of the flight.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity (total seats) of the flight.
     *
     * @param capacity number of seats (must be 0 or more)
     */
    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }
        this.capacity = capacity;
    }

    /**
     * Returns the base price of the flight.
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price of the flight.
     *
     * @param basePrice base price (must be 0 or more)
     */
    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative.");
        }
        this.basePrice = basePrice;
    }

    /**
     * Returns current passengers as a list copy.
     */
    public ArrayList<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf)
                + " | Seats: " + capacity
                + " | Base Price: " + String.format("%.2f", basePrice);
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append("Flight #").append(id).append("\n");
        sb.append("Flight No: ").append(flightNumber).append("\n");
        sb.append("Origin: ").append(origin).append("\n");
        sb.append("Destination: ").append(destination).append("\n");
        sb.append("Departure Date: ").append(departureDate.format(dtf)).append("\n");
        sb.append("Capacity: ").append(capacity).append("\n");
        sb.append("Base Price: ").append(String.format("%.2f", basePrice)).append("\n");
        sb.append("---------------------------\n");
        sb.append("Passengers:\n");

        if (passengers.isEmpty()) {
            sb.append("No passengers\n");
            sb.append("0 passenger(s)\n");
            return sb.toString();
        }

        for (Customer c : passengers) {
            sb.append("* ").append(c.getDetailsShort()).append("\n");
        }

        sb.append(passengers.size()).append(" passenger(s)\n");
        return sb.toString();
    }

    /**
     * Adds a passenger to this flight.
     *
     * @param passenger the customer to add
     * @throws FlightBookingSystemException if passenger already exists on this flight
     */
    public void addPassenger(Customer passenger) throws FlightBookingSystemException {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null.");
        }
        if (passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is already booked on this flight.");
        }
        passengers.add(passenger);
    }

    /**
     * Removes a passenger from this flight.
     *
     * @param passenger the customer to remove
     * @throws FlightBookingSystemException if passenger is not on this flight
     */
    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null.");
        }
        if (!passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Passenger is not booked on this flight.");
        }
        passengers.remove(passenger);
    }
}
