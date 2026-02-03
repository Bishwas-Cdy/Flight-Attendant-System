# Flight Booking System - Codebase Guide

## [Overview] Project Overview

This is a **Flight Booking Management System** - a skeleton Java application designed for managing flight bookings, customers, and flights. The system follows an **MVC (Model-View-Controller)** architectural pattern with separate layers for data handling, business logic, and user interface.

**Key Purpose**: Allow users to manage flights, customers, and their bookings through both a command-line interface (CLI) and a graphical user interface (GUI).

**Status**: This is skeleton code with many TODO implementations, meaning core business logic needs to be completed.

---

## [Structure] Project Structure

```
FlightBookingSystem_Dist/
├── src/                          # Source code directory
│   └── bcu/cmp5332/bookingsystem/
│       ├── commands/             # Command pattern implementations (CLI commands)
│       ├── data/                 # Data persistence layer (file I/O)
│       ├── gui/                  # Graphical User Interface components
│       ├── main/                 # Entry point and core utilities
│       └── model/                # Business logic models (Flight, Customer, Booking)
├── resources/data/               # Data storage files
│   ├── flights.txt              # Persisted flight data
│   ├── customers.txt            # Persisted customer data
│   └── bookings.txt             # Persisted booking data
└── bin/                          # Compiled bytecode (auto-generated)
```

---

## [Components] Component Breakdown

### 1. **Model Layer** (`model/` package)
The core business entities representing the domain:

#### **FlightBookingSystem.java**
- **Purpose**: Main system orchestrator that manages all flights and customers
- **Key Attributes**:
  - `systemDate`: Fixed date (2024-11-11) for the system
  - `customers`: Map of all customers (ID → Customer)
  - `flights`: Map of all flights (ID → Flight)
- **Key Methods**:
  - `addFlight()`: Add a new flight with duplicate checking
  - `addCustomer()`: Add a new customer (TODO)
  - `getFlightByID()`: Retrieve flight by ID
  - `getCustomerByID()`: Retrieve customer by ID (TODO)
- **Status**: Partially implemented

#### **Flight.java**
- **Purpose**: Represents a single flight
- **Attributes**:
  - `id`: Unique identifier
  - `flightNumber`: Flight code (e.g., "BA123")
  - `origin`: Departure city
  - `destination`: Arrival city
  - `departureDate`: LocalDate of departure
  - `passengers`: Set of customers booked on this flight
- **Methods**:
  - `getDetailsShort()`: Returns formatted flight info (e.g., "Flight #1 - BA123 - London to Paris on 11/11/2024")
  - `getDetailsLong()`: Full details (TODO)
  - `addPassenger()`: Add customer to flight (TODO)
- **Status**: Mostly complete, setters/getters implemented

#### **Customer.java**
- **Purpose**: Represents a customer who can book flights
- **Attributes**:
  - `id`: Unique customer ID
  - `name`: Customer name
  - `phone`: Contact number
  - `bookings`: List of all bookings made by this customer
- **Methods**:
  - Constructor (TODO)
  - Getters/Setters (TODO)
  - `addBooking()`: Register a booking (TODO)
- **Status**: Skeleton only - needs implementation

#### **Booking.java**
- **Purpose**: Represents a booking relationship between customer and flight
- **Attributes**:
  - `customer`: Reference to booking customer
  - `flight`: Reference to booked flight
  - `bookingDate`: LocalDate when booking was made
- **Methods**:
  - Constructor (TODO)
  - Getters/Setters (TODO)
- **Status**: Skeleton only - needs implementation

---

### 2. **Command Layer** (`commands/` package)
Implements the **Command Pattern** for CLI operations:

#### **Command.java** (Interface)
- **Purpose**: Contract for all executable commands
- **Key Method**: `execute(FlightBookingSystem)` - executes the command
- **HELP_MESSAGE**: Lists all available commands
  - `listflights`, `listcustomers`, `addflight`, `addcustomer`
  - `showflight`, `showcustomer`, `addbooking`, `cancelbooking`, `editbooking`
  - `loadgui`, `help`, `exit`

#### **ListFlights.java**
- **Purpose**: Display all available flights
- **Implementation**: [COMPLETE] Complete - iterates through all flights and prints short details
- **Usage**: `listflights`

#### **AddFlight.java**
- **Purpose**: Create and add a new flight to the system
- **Implementation**: [COMPLETE] Complete
- **Logic**: 
  - Auto-generates next flight ID
  - Creates Flight object
  - Adds to system with validation
- **Usage**: `addflight` (prompts for flight details)

#### **AddCustomer.java**
- **Purpose**: Create and add a new customer
- **Parameters**: name, phone
- **Implementation**: [TODO] TODO - needs to create Customer and add to system

#### **Help.java**
- **Purpose**: Display all available commands
- **Implementation**: [COMPLETE] Complete - prints HELP_MESSAGE

#### **LoadGUI.java**
- **Purpose**: Launch the graphical user interface
- **Implementation**: [COMPLETE] Complete - creates MainWindow instance
- **Usage**: `loadgui`

---

### 3. **Data Layer** (`data/` package)
Handles persistent storage and file I/O:

#### **DataManager.java** (Interface)
- **Purpose**: Contract for data persistence implementations
- **Constant**: `SEPARATOR = "::"` - delimiter for CSV-like format
- **Methods**:
  - `loadData()`: Load data from file into FlightBookingSystem
  - `storeData()`: Save FlightBookingSystem data to file

#### **FlightBookingSystemData.java**
- **Purpose**: Factory/coordinator for all data managers
- **How it works**:
  - Static initialization loads all active DataManagers
  - `load()`: Creates empty FlightBookingSystem and calls all managers' loadData()
  - `store()`: Calls all managers' storeData() to persist data
- **Currently Active**: Only FlightDataManager (customers/bookings commented out)

#### **FlightDataManager.java**
- **Purpose**: Load/save flights from `./resources/data/flights.txt`
- **Implementation**: [COMPLETE] Complete
- **Format**: `ID::FlightNumber::Origin::Destination::DepartureDate`
- **Example Line**: `1::BA123::London::Paris::2024-11-11`

#### **CustomerDataManager.java**
- **Purpose**: Load/save customers from `./resources/data/customers.txt`
- **Implementation**: [TODO] TODO
- **Expected Format**: `ID::Name::Phone` (similar to flights)

#### **BookingDataManager.java**
- **Purpose**: Load/save bookings from `./resources/data/bookings.txt`
- **Implementation**: [TODO] TODO
- **Expected Format**: `CustomerID::FlightID::BookingDate`

---

### 4. **Main/Utility Layer** (`main/` package)

#### **Main.java**
- **Purpose**: Application entry point
- **Execution Flow**:
  1. Loads all data via `FlightBookingSystemData.load()`
  2. Displays welcome message
  3. Enters infinite loop reading user commands
  4. Parses and executes commands
  5. Saves data before exit
- **Implementation**: [COMPLETE] Complete

#### **CommandParser.java**
- **Purpose**: Parse user input and return appropriate Command objects
- **Supported Commands** (partially implemented):
  - [COMPLETE] `addflight` - Prompts for details, creates AddFlight command
  - [COMPLETE] `listflights` - Returns ListFlights command
  - [COMPLETE] `help` - Returns Help command
  - [COMPLETE] `loadgui` - Returns LoadGUI command
  - [TODO] `addcustomer` - TODO
  - [TODO] `listcustomers` - TODO
  - [TODO] `showflight [id]` - TODO
  - [TODO] `showcustomer [id]` - TODO
  - [TODO] `addbooking [cust_id] [flight_id]` - TODO
  - [TODO] `editbooking [booking_id] [flight_id]` - TODO
  - [TODO] `cancelbooking [cust_id] [flight_id]` - TODO
- **Date Parsing**: Helper method `parseDateWithAttempts()` validates date format (YYYY-MM-DD) with retry logic

#### **FlightBookingSystemException.java**
- **Purpose**: Custom exception for system-specific errors
- **Usage**: Thrown for invalid commands, duplicate bookings, invalid IDs, etc.
- **Implementation**: [COMPLETE] Complete

---

### 5. **GUI Layer** (`gui/` package)
Swing-based graphical interface:

#### **MainWindow.java**
- **Purpose**: Main application window with menu-driven interface
- **GUI Components**:
  - MenuBar with menus: Admin, Flights, Bookings, Customers
  - Menu Items:
    - **Admin**: Exit
    - **Flights**: View, Add, Delete
    - **Bookings**: Issue, Update, Cancel
    - **Customers**: View, Add, Delete
- **Implementation Status**: Partial - UI setup complete, action listeners need implementation
- **Extends**: JFrame with ActionListener

#### **AddFlightWindow.java**
- **Purpose**: Dialog window to add a new flight
- **GUI Components**:
  - Text fields for: Flight Number, Origin, Destination, Departure Date
  - Buttons: Add, Cancel
- **Implementation**: Mostly complete - validates date format and executes AddFlight command
- **Parent**: MainWindow instance

---

## [Flow] How the System Works

### Application Startup Flow
```
1. Main.java runs
   ↓
2. FlightBookingSystemData.load() 
   ├→ Creates empty FlightBookingSystem
   ├→ Calls FlightDataManager.loadData() (reads flights.txt)
   └→ Would call CustomerDataManager & BookingDataManager (if enabled)
   ↓
3. Shows prompt and enters command loop
   ↓
4. User types command → CommandParser.parse() 
   ↓
5. Command.execute() runs 
   ↓
6. User exits → FlightBookingSystemData.store() saves data
```

### Sample Workflow: Adding a Flight (CLI)
```
User Input: "addflight"
    ↓
CommandParser prompts for:
  - Flight Number
  - Origin
  - Destination
  - Departure Date (YYYY-MM-DD format, 3 attempts)
    ↓
Creates AddFlight command
    ↓
AddFlight.execute():
  - Generates next ID
  - Creates Flight object
  - Validates no duplicate
  - Adds to FlightBookingSystem
    ↓
Prints: "Flight #1 added."
```

### Data Format in Files
**flights.txt**: `ID::FlightNumber::Origin::Destination::DepartureDate`
```
1::BA123::London::Paris::2024-11-11
2::AF456::Paris::Berlin::2024-11-15
```

**customers.txt** (when implemented): `ID::Name::Phone`
```
1::John Doe::07777123456
2::Jane Smith::07777654321
```

**bookings.txt** (when implemented): `CustomerID::FlightID::BookingDate`
```
1::1::2024-11-01
2::2::2024-11-02
```

---

## [Status] Implementation Status Summary

### [COMPLETE] Complete Components
- Flight model & setters/getters
- FlightBookingSystem core structure
- Main entry point and command loop
- FlightDataManager (flights persistence)
- Command interface and help system
- AddFlight command
- ListFlights command
- Help command
- LoadGUI command
- CommandParser (partial)
- MainWindow GUI (UI structure)
- AddFlightWindow GUI

### [TODO] TODO Components (Need Implementation)
- **Models**:
  - Customer: constructor, getters/setters, addBooking()
  - Booking: constructor, getters/setters
  - Flight.getDetailsLong(), Flight.addPassenger()
  - FlightBookingSystem.getCustomerByID(), addCustomer()

- **Commands**:
  - AddCustomer.execute()
  - ListCustomers
  - ShowFlight
  - ShowCustomer
  - AddBooking
  - CancelBooking
  - EditBooking

- **Data Managers**:
  - CustomerDataManager.loadData() & storeData()
  - BookingDataManager.loadData() & storeData()

- **CommandParser**:
  - Parsing for addcustomer, listcustomers, showflight, showcustomer, etc.

- **GUI**:
  - Action listeners for menu items in MainWindow
  - Customer and booking windows

---

## [Patterns] Key Design Patterns Used

1. **Command Pattern**: Each CLI action is a Command object executed polymorphically
2. **MVC Pattern**: Separation of Model (entities), View (GUI), Controller (commands)
3. **Factory Pattern**: FlightBookingSystemData acts as factory for DataManagers
4. **Singleton-like Pattern**: Single FlightBookingSystem instance per session
5. **Strategy Pattern**: Different DataManagers implement different storage strategies

---

## [Start] Getting Started

### To Run the Application:
```bash
cd FlightBookingSystem_Dist
javac -d bin src/bcu/cmp5332/bookingsystem/**/*.java
java -cp bin bcu.cmp5332.bookingsystem.main.Main
```

### Available CLI Commands:
```
listflights          # View all flights
addflight            # Add new flight (interactive)
help                 # Show all commands
loadgui              # Launch GUI version
exit                 # Exit application
```

---

## [Notes] Notes for Development

1. **Data Files**: Must exist in `./resources/data/` with proper format
2. **System Date**: Fixed to 2024-11-11 for consistency
3. **ID Generation**: Auto-incremented from max existing ID
4. **Error Handling**: Custom FlightBookingSystemException thrown for all errors
5. **Date Format**: Must be YYYY-MM-DD (e.g., 2024-11-11)
6. **File Separator**: Uses `::` as delimiter in data files
7. **GUI Framework**: Uses Java Swing (AWT components)

---

## [Dependencies] Class Dependencies

```
Main → FlightBookingSystemData → FlightBookingSystem
                              ↓
                        FlightDataManager, 
                        CustomerDataManager,
                        BookingDataManager

CommandParser → Command → (AddFlight, ListFlights, Help, etc.)
                             ↓
                        FlightBookingSystem → (Flight, Customer, Booking)

MainWindow (GUI) → FlightBookingSystem
                → AddFlightWindow
                → AddFlightCommand
```

---

**Created**: 2024-11-11 | **Type**: Skeleton Code | **Status**: Partial Implementation
