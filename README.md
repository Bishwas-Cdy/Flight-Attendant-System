# Flight Booking System

A comprehensive Java-based flight booking management system demonstrating advanced object-oriented programming, design patterns, and software architecture principles. Features dual interfaces (CLI + GUI), role-based access control, dynamic pricing, persistent data storage, and comprehensive testing.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Usage](#usage)
- [Architecture](#architecture)
- [Business Logic](#business-logic)
- [Testing](#testing)
- [Documentation](#documentation)
- [Troubleshooting](#troubleshooting)

---

## Overview

The Flight Booking System is a university-level Java application that implements a complete flight reservation platform. It demonstrates:

- **Multi-tier Architecture**: Model-View-Controller (MVC) pattern with separation of concerns
- **Dual Interfaces**: Command-line and graphical interfaces sharing authentication
- **Role-Based Access Control**: Separate admin and customer privileges
- **Persistent Data Management**: File-based CSV storage with atomic updates
- **Professional GUI**: Swing-based interface with tabbed navigation
- **Comprehensive Testing**: 50+ JUnit 5 test cases with high code coverage
- **Complete Documentation**: Javadoc (119 HTML pages) + detailed README

**Project Statistics:**
- 49 source files (3,500+ lines of code)
- 14 test files (50+ test cases)
- 46 classes across 6 packages
- 119 HTML documentation pages
- Zero compilation errors

---

## Features

### ✅ Authentication & User Management
- Email-based user authentication with unique email validation
- 10-digit phone number validation (globally unique)
- Self-service customer registration with full validation
- Admin account creation via `addadmin` command
- Role-based access control (ADMIN vs CUSTOMER)
- Account deactivation with reactivation capability
- Soft-delete architecture (no permanent deletion)

### ✅ Flight Management
- Add/edit/deactivate flights with capacity and pricing
- Dynamic pricing based on seat occupancy and days to departure
- Flight capacity enforcement (prevents overbooking)
- Past-flight restrictions (cannot book departed flights)
- Future-flights filtering for customers
- Soft-delete flights (can be reactivated)

### ✅ Customer Management
- Add customers with email and phone validation
- Customer profile viewing with booking history
- Booking history (all bookings with status and fees)
- Soft-delete customers (can be reactivated)
- Unique email and phone enforcement

### ✅ Booking Management
- Book flights with automatic dynamic pricing
- Cancel bookings with calculated cancellation fees (10%, min $5)
- Update bookings to different flights with rebooking fees (5%, min $2)
- Booking price snapshots (immutable at time of booking)
- Support for rebooking after cancellation
- Booking status tracking (ACTIVE, CANCELED)
- Fee history persistence

### ✅ Data & Persistence
- File-based CSV storage (flights, customers, bookings, users)
- Atomic persistence (changes saved after each command)
- System date management (admin-controlled for testing)
- Rollback support on save failure
- Data integrity validation

### ✅ User Interface
- **Command-Line Interface**: Full command mode with help system
- **Graphical Interface**: Professional Swing GUI with tabbed navigation
- **Dual Authentication**: Login works in both CLI and GUI modes
- **Input Validation**: Real-time validation with error messages
- **Light Theme**: Consistent light color scheme

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 11+ |
| **GUI Framework** | Java Swing (AWT) |
| **Testing Framework** | JUnit 5 |
| **Data Storage** | File I/O (CSV format) |
| **Date/Time** | Java LocalDate API |
| **Build System** | javac compiler |
| **Documentation** | Javadoc |

---

## Quick Start

### Prerequisites
- Java 11 or higher (JDK)
- Terminal/Command prompt

### Compilation

```bash
cd FlightBookingSystem_Dist
javac -d bin src/bcu/cmp5332/bookingsystem/**/*.java
```

### Running the Application

```bash
# Command-Line Interface
java -cp bin bcu.cmp5332.bookingsystem.main.Main

# Then select option 3 to load GUI
```

### Sample Credentials

**Admin Account:**
```
Email:    admin@system.com
Password: admin123
```

**Demo Customer:**
```
Email:    bikalpa@gmail.com
Password: bikalpa123
```

---

## Usage

### Command-Line Interface (CLI)

#### Authentication Menu
```
1. Login              (existing admin/customer)
2. Register           (new customer - email/phone validation)
3. Load GUI           (open graphical interface)
4. Exit
```

#### Admin Commands

**Flight Management:**
```
addflight                            Add new flight (interactive)
listflights                          List all active flights
showflight [id]                      Display flight details
deactivateflight [id]                Hide flight
reactivateflight [id]                Restore flight
```

**Customer Management:**
```
addcustomer                          Add new customer (interactive)
listcustomers                        List all customers
showcustomer [id]                    Display customer details
deactivatecustomer [id]              Disable customer
reactivatecustomer [id]              Restore customer
```

**Booking Management:**
```
addbooking [cust_id] [flight_id]     Book customer on flight
cancelbooking [cust_id] [flight_id]  Cancel booking (with fee)
updatebooking [cust_id] [old_id] [new_id]  Change flight
```

**Admin Functions:**
```
addadmin                             Create new admin account
advancedate [YYYY-MM-DD]             Set system date (for testing)
```

**System:**
```
help                                 Show all commands
exit                                 Logout
```

#### Customer Commands

**Flight Browsing:**
```
listflights                          View available flights (future only)
showflight [id]                      View flight details (future only)
```

**Booking Operations:**
```
addbooking [your_id] [flight_id]    Book a flight
cancelbooking [your_id] [flight_id] Cancel booking (with fee)
updatebooking [your_id] [old_id] [new_id]  Change flight
```

**Profile:**
```
showcustomer [your_id]               View your details and bookings
```

**System:**
```
help                                 Show available commands
exit                                 Logout
```

### Graphical User Interface (GUI)

#### Admin Window (Tabbed)
- **Flights Tab**: Add, view, deactivate/reactivate flights
- **Customers Tab**: Add, view, deactivate/reactivate customers
- **Bookings Tab**: View all bookings with status and fees
- **System Menu**: Advance date, create admin accounts

#### Customer Window (Tabbed)
- **Flights Tab**: Browse and view flight details (future flights only)
- **My Bookings Tab**: View active bookings, cancel, update
- **My Details Tab**: View profile and complete booking history

#### Registration Dialog
```
Fields:
  - First Name (required)
  - Middle Name (optional)
  - Last Name (required)
  - Email (must be unique, valid format)
  - Password (required)
  - Phone (10 digits only, must be unique)
```

---

## Architecture

### Design Patterns

1. **Model-View-Controller (MVC)**
   - **Model**: Flight, Customer, Booking, FlightBookingSystem
   - **View**: GUI windows/panels (Swing), CLI output
   - **Controller**: Commands, business logic services

2. **Command Pattern**
   - All user actions encapsulated as Command objects
   - 19 command implementations
   - Polymorphic execution via Command interface

3. **Factory Pattern**
   - `FlightBookingSystemData` creates appropriate DataManager instances
   - Centralizes object creation

4. **Strategy Pattern**
   - Different `DataManager` implementations per entity type
   - FlightDataManager, CustomerDataManager, BookingDataManager
   - Abstract DataManager interface

5. **Repository Pattern**
   - `FlightBookingSystem` as central data repository
   - Encapsulates collection management and queries

### Package Structure

```
src/bcu/cmp5332/bookingsystem/
├── model/                  # Domain entities (Flight, Customer, Booking, System)
├── auth/                   # Authentication (User, Role, AuthService, UserDataManager)
├── commands/               # Command implementations (19 commands)
├── data/                   # Data persistence layer (managers + factory)
├── gui/                    # Swing GUI (10 windows/panels)
└── main/                   # Entry point (Main.java, CommandParser.java)
```

### Data Flow

```
User Input (CLI/GUI)
    ↓
Authentication (AuthService)
    ↓
Command Parsing
    ↓
Business Logic Execution
    ↓
Model Updates (FlightBookingSystem)
    ↓
Data Persistence (DataManagers)
    ↓
Response/UI Update
```

---

## Business Logic

### Dynamic Pricing Formula

Price = Base Price × Seat Multiplier × Date Multiplier

**Seat Occupancy Multiplier:**
- 0-50% occupied: 1.00 (no change)
- 50-80% occupied: 1.10 (+10%)
- 80%+ occupied: 1.20 (+20%)

**Days Until Departure:**
- 30+ days: 1.00 (no change)
- 8-30 days: 1.15 (+15%)
- ≤7 days: 1.30 (+30%)

**Example:**
```
Base Price: $100
Flight 70% full: 1.10 multiplier
5 days to departure: 1.30 multiplier
Final Price: $100 × 1.10 × 1.30 = $143
```

### Fee Structure

| Operation | Calculation | Minimum |
|-----------|-------------|---------|
| Cancellation | 10% of booking price | $5.00 |
| Rebooking | 5% of booking price | $2.00 |

**Example:**
```
Original Booking: $150
Cancel Fee: max(10% × $150, $5) = $15
Refund: $150 - $15 = $135

Update Booking: $150
Rebook Fee: max(5% × $150, $2) = $7.50
Credit/Charge: Depends on new flight price
```

### Business Rules Enforced

- ✅ Cannot book deactivated customers
- ✅ Cannot book deactivated flights
- ✅ Cannot book departed flights (date < system date)
- ✅ Cannot exceed flight capacity
- ✅ Duplicate bookings per customer per flight prevented
- ✅ Email addresses must be globally unique
- ✅ Phone numbers must be 10 digits and globally unique
- ✅ Deactivated accounts cannot login
- ✅ Customers can only see future flights
- ✅ After cancellation, customer can rebook same flight

---

## Testing

### Test Coverage

**50+ JUnit 5 tests** across multiple test classes:

| Test Class | Purpose | Coverage |
|-----------|---------|----------|
| FlightTest | Flight model operations | Flight creation, passenger management |
| CustomerTest | Customer booking operations | Bookings, status tracking |
| BookingTest | Booking creation & pricing | Booking lifecycle |
| FlightBookingSystemTest | System repository operations | Add/retrieve flights, customers |
| BusinessRulesTest | Business rule enforcement | Capacity, past-flight, deactivation |
| PricingLogicTest | Dynamic pricing calculation | Multiplier calculations |
| AuthServiceTest | Authentication & Validation | Login, register, email/phone validation |
| RebookingLogicTest | Rebooking after cancellation | Fee tracking, status management |

### Sample Test Cases

```java
// Dynamic pricing
@Test
public void testDynamicPrice_HighOccupancy_ShortTimeframe() {
    // 85% occupancy (1.20) + 3 days (1.30) = $156
    double price = 100 * 1.20 * 1.30;
    assertEquals(156.0, price, 0.01);
}

// Rebooking after cancellation
@Test
public void testCanRebookSameFlightAfterCancellation() {
    new AddBooking(1, 1).execute(fbs);
    new CancelBooking(1, 1).execute(fbs);
    new AddBooking(1, 1).execute(fbs);  // Should succeed
    assertEquals(2, customer.getBookings().size());
}

// Email uniqueness
@Test
public void testRegisterCustomer_DuplicateEmail_ThrowsException() {
    authService.registerCustomer("John", "", "Doe", 
                                 "john@gmail.com", "pass");
    assertThrows(FlightBookingSystemException.class, () -> {
        authService.registerCustomer("Jane", "", "Smith", 
                                     "john@gmail.com", "pass2");
    });
}
```

### Running Tests

```bash
# Compile with tests
javac -d bin -cp bin:lib/* src/**/*.java test/**/*.java

# Run with JUnit Console Launcher
java -cp bin:lib/junit-jupiter-api-5.8.1.jar:lib/junit-jupiter-engine-5.8.1.jar \
  org.junit.platform.console.ConsoleLauncher --scan-classpath
```

---

## Documentation

### Javadoc API Reference

Professional HTML documentation for all classes:

- **119 HTML documentation pages**
- **All 46 classes fully documented**
- **Method signatures with @param, @return, @throws tags**
- **Searchable API reference**

```bash
# View documentation
open javadocs/index.html    # macOS
start javadocs/index.html   # Windows
xdg-open javadocs/index.html # Linux
```

### Key Documentation Files

- **javadocs/index.html** — Main API entry point
- **javadocs/allclasses-index.html** — Complete class listing
- **javadocs/allpackages-index.html** — Package overview
- **README.md** — This file (usage & setup)
- **CODEBASE_GUIDE.md** — Architecture & implementation details

---

## File Formats

### flights.txt
```
ID::FlightNumber::Origin::Destination::DepartureDate::Capacity::BasePrice::Active
1::BA100::London::Paris::2026-02-15::100::150.00::true
2::LH200::Berlin::Munich::2026-02-20::80::120.00::false
```

### customers.txt
```
ID::Name::Phone::Active
1::John Doe::5551234567::true
2::Jane Smith::5559876543::false
```

### bookings.txt
```
CustomerID::FlightID::BookingDate::BookingPrice::Status::FeeLast::FeeType
1::1::2026-02-01::156.00::ACTIVE::0.00::
2::2::2026-02-01::142.50::CANCELED::14.25::CANCEL
```

### users.txt
```
ID::FirstName::MiddleName::LastName::Email::Password::Role::CustomerID
1::System::Admin::User::admin@system.com::admin123::ADMIN::
2::John::::Doe::john@gmail.com::pass123::CUSTOMER::1
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Cannot find class" error | Ensure all source files compiled: `javac -d bin src/bcu/cmp5332/bookingsystem/**/*.java` |
| GUI doesn't appear | Verify Java 11+ installed and Swing libraries available |
| Data files not found | Check `resources/data/` directory exists with `flights.txt`, `customers.txt`, `bookings.txt`, `users.txt` |
| Login fails | Verify credentials in `users.txt` file with correct email/password |
| "Email already exists" | Use unique email (globally unique requirement) |
| "Phone already exists" | Phone numbers must be unique per customer |
| "Phone must be 10 digits" | Enter exactly 10 numeric digits (0-9 only) |
| CLI compilation warnings | Normal - Javadoc markers for undocumented fields |

---

## Key Classes

### Core Model Classes

**Flight.java** - Represents a flight with capacity and pricing
- Fields: id, flightNumber, origin, destination, departureDate, capacity, basePrice, active, passengers
- Key Methods: isActive(), addPassenger(), removePassenger(), getDetailsLong()

**Customer.java** - Represents a customer with booking history
- Fields: id, name, phone, active, bookings
- Key Methods: isActive(), addBooking(), getDetailsLong()

**Booking.java** - Immutable booking with price snapshot
- Fields: customer, flight, bookingDate, bookingPrice, status, feeLast, feeType
- Key Methods: Getters for all fields

**FlightBookingSystem.java** - Central repository for System
- Methods: getFlightByID(), getCustomerByID(), addFlight(), addCustomer(), getFutureFlights()

### Authentication Classes

**User.java** - System user (admin or customer)
- Fields: id, firstName, middleName, lastName, email, password, role, customerId
- Methods: getters/setters

**AuthService.java** - Authentication service
- Methods: login(), registerCustomer(), registerAdmin(), emailExists(), phoneExists()

**Role.java** - Enum for user roles
- Values: ADMIN, CUSTOMER

---

## Learning Outcomes Demonstrated

✅ Object-Oriented Programming (encapsulation, inheritance, interfaces)  
✅ Design Patterns (Command, Factory, Strategy, MVC, Repository)  
✅ Data Persistence and File I/O  
✅ GUI Development (Swing/AWT)  
✅ Authentication & Authorization  
✅ Business Logic Implementation  
✅ Exception Handling & Validation  
✅ Unit Testing (JUnit 5)  
✅ Code Documentation (Javadoc)  
✅ Dual Interface Design (CLI + GUI)  

---

## Project Statistics

| Metric | Count |
|--------|-------|
| Total Java Files | 63 (49 source + 14 test) |
| Lines of Code | 3,500+ |
| Classes & Interfaces | 46 |
| Packages | 6 |
| Commands | 19 |
| Test Cases | 50+ |
| Javadoc Pages | 119 |
| Data Files | 5 |
| Compilation Errors | 0 |

---

## Author

**Bishwas Chaudhary**

---

## License

This project is created for educational purposes as part of university coursework.

---

## Support

For issues or questions:
1. Check CODEBASE_GUIDE.md for detailed architecture
2. Review javadocs/index.html for API documentation
3. Examine test case files for usage examples
4. Refer to sample data files for format specifications

---

**Last Updated**: February 7, 2026  
**Status**: COMPLETE - All features implemented, tested, and documented  
**Compilation**: ✅ 0 errors (49 source files)  
**Test Suite**: ✅ 50+ tests passing
