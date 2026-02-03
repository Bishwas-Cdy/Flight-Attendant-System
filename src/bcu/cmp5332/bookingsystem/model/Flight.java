package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a flight in the Flight Booking System.
 * Each flight holds a unique set of passengers (customers).
 */
public class Flight {

    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;

    private final Set<Customer> passengers;

    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        passengers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Returns a copy of passengers as a list.
     *
     * @return passengers list
     */
    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf);
    }

    /**
     * Returns detailed flight information including passenger list.
     *
     * @return long details string
     */
    public String getDetailsLong() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("Flight #").append(id).append("\n");
        sb.append("Flight No: ").append(flightNumber).append("\n");
        sb.append("Origin: ").append(origin).append("\n");
        sb.append("Destination: ").append(destination).append("\n");
        sb.append("Departure Date: ").append(departureDate.format(dtf)).append("\n");
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
