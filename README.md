# Flight Booking System

A comprehensive Java-based flight booking management system with both command-line and graphical user interfaces. Features user authentication, role-based access control, dynamic pricing, booking management, soft-delete functionality, and phone/email uniqueness validation.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Usage](#usage)
  - [CLI Commands](#cli-commands)
  - [GUI Interface](#gui-interface)
- [Authentication & Validation](#authentication--validation)
- [Business Logic](#business-logic)
- [Testing](#testing)
- [Documentation](#documentation)
- [Troubleshooting](#troubleshooting)

---

## Overview

The Flight Booking System is a university-level Java application demonstrating advanced object-oriented programming concepts:
- Dual interface architecture (CLI + GUI)
- Authentication with role-based access control
- User registration with email/phone uniqueness validation
- Admin account management (addadmin command)
- Persistent data management with file storage
- Dynamic pricing algorithms
- Comprehensive JUnit 5 test suite
- Professional Javadoc documentation

---

## Features

### Authentication & User Management
- [x] **Email-Based Login** — Unique email addresses for all users
- [x] **Phone Validation** — 10-digit phone numbers, globally unique
- [x] **Role-Based Access** — Separate admin and customer permissions
- [x] **Customer Registration** — Self-registration with full validation
- [x] **Admin Account Creation** — Admins can create other admin accounts via `addadmin`
- [x] **Account Deactivation** — Soft-delete with reactivation capability
- [x] **Active Account Enforcement** — Deactivated accounts cannot login

### Flight Management
- [x] **Add Flights** — Admin can add flights with capacity and pricing
- [x] **Soft Delete Flights** — Hide flights without permanent deletion
- [x] **Activation/Deactivation** — Toggle flight availability
- [x] **Capacity Enforcement** — Prevent overbooking beyond seat limit
- [x] **Past-Flight Restriction** — Cannot book departed flights
- [x] **Future Flight Filtering** — List only upcoming flights

### Customer Management
- [x] **Add Customers** — Admin can create customers with validation (email + phone uniqueness)
- [x] **Customer Profiles** — View name, phone, booking history
- [x] **Soft Delete Customers** — Hide without permanent deletion
- [x] **Deactivation/Reactivation** — Control customer access

### Booking Management
- [x] **Dynamic Pricing** — Price based on occupancy and days to departure
- [x] **Issue Bookings** — Book flights with calculated price
- [x] **Cancel Bookings** — Automatic cancellation fee (10%, min $5)
- [x] **Update Bookings** — Change flight with rebooking fee (5%, min $2)
- [x] **Booking History** — View all customer bookings with prices
- [x] **Price Snapshots** — Store booking price at time of purchase

### Data & Persistence
- [x] **File-Based Storage** — CSV format (flights.txt, customers.txt, bookings.txt, users.txt)
- [x] **Auto-Save** — Changes persisted after each command
- [x] **Atomic Persistence** — Both User and Customer records saved together
- [x] **System Date Management** — Admin-controlled date for testing

### User Interface
- [x] **Command-Line Interface** — Full command mode with help system
- [x] **Graphical Interface** — Professional Swing GUI with tabs
- [x] **Dual Authentication** — Login works in both CLI and GUI
- [x] **Responsive Forms** — Input validation with error messages
- [x] **Light Theme** — Consistent light color scheme across all windows

### Documentation & Testing
- [x] **Javadoc** — 119 HTML documentation files, all classes documented
- [x] **Unit Tests** — 50+ JUnit 5 tests across 6 test classes
- [x] **Code Coverage** — All major classes have test coverage
- [x] **README & Guides** — Complete documentation with examples

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 11+ |
| **GUI Framework** | Java Swing (AWT) |
| **Testing** | JUnit 5 |
| **Data Storage** | File I/O (CSV format) |
| **Build** | javac compiler |
| **Documentation** | Javadoc (119 files) |

---

## Project Structure

```
FlightBookingSystem_Dist/
├── src/bcu/cmp5332/bookingsystem/
│   ├── model/                 # Domain entities
│   │   ├── Flight.java
│   │   ├── Customer.java
│   │   ├── Booking.java
│   │   └── FlightBookingSystem.java
│   ├── auth/                  # Authentication & authorization
│   │   ├── User.java
│   │   ├── Role.java          (ADMIN, CUSTOMER enum)
│   │   ├── AuthService.java   (login, register, etc.)
│   │   └── UserDataManager.java
│   ├── commands/              # Command pattern - 19 commands
│   │   ├── Command.java       (interface)
│   │   ├── AddFlight.java
│   │   ├── AddCustomer.java
│   │   ├── AddAdmin.java      (NEW - placeholder)
│   │   ├── AddBooking.java
│   │   ├── CancelBooking.java
│   │   ├── UpdateBooking.java
│   │   ├── ListFlights.java
│   │   ├── ListCustomers.java
│   │   ├── ShowFlight.java
│   │   ├── ShowCustomer.java
│   │   ├── AdvanceDate.java
│   │   ├── DeactivateCustomer.java
│   │   ├── ReactivateCustomer.java
│   │   ├── DeactivateFlight.java
│   │   ├── ReactivateFlight.java
│   │   ├── Help.java
│   │   ├── LoadGUI.java
│   │   └── EditBooking.java
│   ├── data/                  # Data persistence
│   │   ├── DataManager.java   (interface)
│   │   ├── FlightDataManager.java
│   │   ├── CustomerDataManager.java
│   │   ├── BookingDataManager.java
│   │   ├── SystemDateManager.java
│   │   └── FlightBookingSystemData.java (factory)
│   ├── gui/                   # Swing GUI - 10 windows/panels
│   │   ├── LoginWindow.java
│   │   ├── GuiAuthMenu.java   (register + login)
│   │   ├── AdminWindow.java
│   │   ├── CustomerWindow.java
│   │   ├── FlightsPanel.java
│   │   ├── CustomersPanel.java
│   │   ├── BookingsPanel.java
│   │   ├── MyDetailsPanel.java
│   │   ├── AddFlightWindow.java
│   │   └── MainWindow.java
│   └── main/                  # Entry point & utilities
│       ├── Main.java
│       ├── CommandParser.java
│       └── FlightBookingSystemException.java
├── test/bcu/cmp5332/bookingsystem/
│   ├── auth/
│   │   ├── AuthServiceTest.java    (login, register, email uniqueness)
│   │   ├── UserTest.java           (user creation, roles)
│   │   └── RoleTest.java           (role enum)
│   ├── data/
│   │   └── SystemDateManagerTest.java (date persistence)
│   ├── commands/
│   │   ├── AddAdminTest.java       (command structure)
│   │   └── AddCustomerTest.java    (customer creation)
│   └── model/
│       ├── FlightTest.java
│       ├── CustomerTest.java
│       ├── BookingTest.java
│       └── FlightBookingSystemTest.java
├── resources/data/
│   ├── flights.txt
│   ├── customers.txt
│   ├── bookings.txt
│   ├── users.txt
│   └── systemdate.txt
├── bin/                       # Compiled classes
├── javadocs/                  # Generated Javadoc (119 files)
├── lib/                       # JUnit 5 libraries
└── README.md
```

---

## Setup & Installation

### Prerequisites
- Java 11 or higher (JDK)
- Terminal / Command prompt

### Compilation

```bash
cd FlightBookingSystem_Dist
javac -d bin src/bcu/cmp5332/bookingsystem/**/*.java
```

### Running the Application

```bash
# Command-Line Interface
java -cp bin bcu.cmp5332.bookingsystem.main.Main

# Then select option "3. Load GUI" to use graphical interface
```

---

## Usage

### CLI Commands

#### Authentication
```
Initial Menu:
  1. Login              (existing admin/customer)
  2. Register           (new customer self-registration)
  3. Load GUI           (open graphical interface)
  4. Exit

Registration Flow:
  - First name: required
  - Middle name: optional
  - Last name: required
  - Email: must be UNIQUE and valid format
  - Password: required
  - Phone: 10 digits ONLY, must be UNIQUE
```

#### Admin Commands
```
FLIGHT MANAGEMENT:
  addflight                               Add new flight
  listflights                             List active flights
  showflight [id]                         Display flight details
  deactivateflight [id]                   Hide flight
  reactivateflight [id]                   Restore flight

CUSTOMER MANAGEMENT:
  addcustomer                             Add new customer (interactive)
  listcustomers                           List active customers
  showcustomer [id]                       Display customer details
  deactivatecustomer [id]                 Disable customer
  reactivatecustomer [id]                 Restore customer

ADMIN MANAGEMENT:
  addadmin                                Create new admin account (interactive)

BOOKING MANAGEMENT:
  addbooking [cust_id] [flight_id]       Book customer on flight
  cancelbooking [cust_id] [flight_id]    Cancel booking (with fee)
  updatebooking [cust_id] [old_id] [new_id]  Change flight (with fee)

SYSTEM:
  advancedate [YYYY-MM-DD]               Set system date (for testing)
  help                                    Show all commands
  exit                                    Logout
```

#### Customer Commands
```
FLIGHTS:
  listflights                             View available flights
  showflight [id]                         View flight details

BOOKINGS:
  addbooking [your_id] [flight_id]       Book a flight
  cancelbooking [your_id] [flight_id]    Cancel booking
  updatebooking [your_id] [old_id] [new_id]  Change flight

PROFILE:
  showcustomer [your_id]                  View your details

SYSTEM:
  help                                    Show available commands
  exit                                    Logout
```

### GUI Interface

#### Login Window
- Email and password input
- "Login" button (existing users)
- "Register" button (new customers)
- "Load from File" auto-loads user data

#### Admin Window (Tabbed Interface)
**Flights Tab:**
- List all active flights
- Add new flight (dialog)
- Deactivate/Reactivate flights
- View flight details

**Customers Tab:**
- List all customers
- Add new customer (dialog with email + phone validation)
- View customer details
- Deactivate/Reactivate customers

**Bookings Tab:**
- View all bookings
- See booking details with calculated price
- Manage customer bookings

**System Menu:**
- Advance System Date (set date for testing)
- Add Admin (create admin accounts)

#### Customer Window (Tabbed Interface)
**Flights Tab:**
- Browse available flights
- View flight details

**My Bookings Tab:**
- View your active bookings
- Cancel bookings (with fee)
- Update flight selection

**My Details Tab:**
- View profile information
- See all your bookings

#### Registration Dialog (GUI)
```
Fields:
  - First Name (required)
  - Middle Name (optional)
  - Last Name (required)
  - Email (unique validation)
  - Password (required)
  - Phone (10-digit, unique validation)
```

---

## Authentication & Validation

### Email Validation
- **Format**: Standard email pattern (abc@domain.com)
- **Uniqueness**: Globally unique across system
- **Checked in**: Registration, login, admin creation, customer creation

### Phone Validation
- **Format**: Exactly 10 digits (0-9 only)
- **Uniqueness**: Globally unique per customer/user
- **Checked in**: Customer registration, admin customer creation, GUI registration

### User Roles
```
ADMIN
- Can create flights, customers, and admin accounts
- Can manage all bookings
- Can deactivate/reactivate users
- Can advance system date

CUSTOMER
- Can view available flights
- Can book, cancel, and update bookings
- Can view own profile
- Cannot access admin commands
```

### Input Validation
```
Dates:      YYYY-MM-DD format, validated via LocalDate.parse()
Phone:      10 digits only, checked via regex \\d{10}
Email:      Standard format, checked via regex pattern
Capacity:   Positive integer, >= 0
Price:      Positive decimal, >= 0
```

---

## Business Logic

### Dynamic Pricing Formula

**Final Price = Base Price × Seat Multiplier × Date Multiplier**

**Seat Occupancy Multiplier:**
- 0-50% occupied: 1.00 (no change)
- 50-80% occupied: 1.10 (+10%)
- 80%+ occupied: 1.20 (+20%)

**Days Until Departure:**
- 30+ days: 1.00 (no change)
- 8-30 days: 1.15 (+15%)
- ≤7 days: 1.30 (+30%)

**Example:**
- Base price: $100
- Flight 70% full: 1.10 multiplier
- 5 days to departure: 1.30 multiplier
- Final price: $100 × 1.10 × 1.30 = $143

### Fee Structure

| Operation | Calculation | Minimum |
|-----------|-------------|---------|
| Cancellation | 10% of booking price | $5 |
| Rebooking | 5% of booking price | $2 |

**Example:**
- Passenger booked flight for $150
- Cancels booking: Fee = max(10% × $150, $5) = $15
- Refund: $150 - $15 = $135

### Business Rules Enforced
- ✅ Cannot book deactivated flights
- ✅ Cannot book past-dated flights  
- ✅ Cannot book when flight at capacity
- ✅ Cannot book if customer deactivated
- ✅ Email must be unique (checked on registration)
- ✅ Phone must be 10 digits (checked on registration)
- ✅ Phone must be unique (checked on registration)
- ✅ Duplicate bookings automatically replaced

---

## Testing

### Test Coverage

**50+ JUnit 5 tests** across 6 test classes:

| Test Class | Purpose | Tests |
|-----------|---------|-------|
| **AuthServiceTest** | Login, registration, email/phone validation | 12 |
| **UserTest** | User creation, role assignment | 10 |
| **RoleTest** | Role enum functionality | 5 |
| **SystemDateManagerTest** | Date persistence (load/save) | 7 |
| **AddCustomerTest** | Customer creation via command | 7 |
| **AddAdminTest** | Admin creation command | 4 |
| **FlightTest** | Flight model operations | 8 |
| **CustomerTest** | Customer bookings | 9 |
| **BookingTest** | Booking creation & pricing | 6 |
| **FlightBookingSystemTest** | Repository operations | 9 |
| **BusinessRulesTest** | Rule enforcement | 9 |
| **PricingLogicTest** | Dynamic pricing | 9 |

### Sample Test Cases

```java
// Email uniqueness in registration
@Test
public void testRegisterCustomer_DuplicateEmail_ThrowsException() {
    authService.registerCustomer("John", "", "Doe", 
                                 "john@gmail.com", "pass123");
    
    assertThrows(FlightBookingSystemException.class, () -> {
        authService.registerCustomer("Jane", "", "Smith", 
                                     "john@gmail.com", "pass456");
    });
}

// Phone uniqueness in customer creation
@Test
public void testPhoneExists_WithDuplicatePhone() {
    Customer c = new Customer(1, "John", "5551234567");
    fbs.addCustomer(c);
    
    assertTrue(fbs.phoneExists("5551234567"));
}

// Dynamic pricing calculation
@Test
public void testDynamicPrice_HighOccupancy_ShortTimeframe() {
    // 85% occupancy (1.20) + 3 days (1.30) = $156
    double price = 100 * 1.20 * 1.30;
    assertEquals(156.0, price, 0.01);
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
- **119 HTML files** generated from source code
- **All 46 classes documented** with method signatures
- **Type-safe references** with @param, @return, @throws tags

```bash
# View documentation
open javadocs/index.html    # macOS
# or
start javadocs/index.html   # Windows
# or
xdg-open javadocs/index.html # Linux
```

### Documentation Files
- **index.html** — Main API documentation entry point
- **allclasses-index.html** — Complete class listing
- **allpackages-index.html** — Package overview
- **search.html** — API search functionality

### Additional Guides
- **README.md** — This file (usage & setup)
- **CODEBASE_GUIDE.md** — Architecture & design patterns

---

## Sample Data

### Sample Credentials

**Pre-configured Admin:**
```
Email:    admin@system.com
Password: admin123
```

**Demo Customer:**
```
Email:    bikalpa@gmail.com
Password: bikalpa123
Customer ID: 2
```

### users.txt Format
```
ID::FirstName::MiddleName::LastName::Email::Password::Role::CustomerID
1::System::Admin::User::admin@system.com::admin123::ADMIN::
2::Bishwas::::Chaudhary::bishwas@system.com::bishwas123::ADMIN::
3::Bikalpa::::Bhattarai::bikalpa@gmail.com::bikalpa123::CUSTOMER::2
```

### flights.txt Format
```
ID::FlightNumber::Origin::Destination::DepartureDate::Capacity::BasePrice::Active
1::BA100::London::Paris::2026-02-15::100::150.00::true
2::LH200::Berlin::Munich::2026-02-20::80::120.00::true
```

### customers.txt Format
```
ID::Name::Phone::Active
1::John Doe::5551234567::true
2::Bikalpa Bhattarai::9863047325::true
```

### bookings.txt Format
```
CustomerID::FlightID::BookingDate::BookingPrice
1::1::2026-02-01::195.00
2::2::2026-02-01::156.00
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Invalid email format" | Email must contain @ symbol (abc@domain.com) |
| "Email already exists" | Use different email (must be globally unique) |
| "Phone number already exists" | Phone must be unique; use different number |
| "Phone must be 10 digits" | Enter exactly 10 numeric digits (0-9) |
| "Cannot find class" error | Ensure `javac -d bin src/**/*.java` compiled all files |
| GUI doesn't load | Check Java version is 11+, run: `java -version` |
| Data not saving | Ensure `resources/data/` directory exists with .txt files |
| Login fails | Verify email and password in users.txt file |
| "Duplicate customer ID" | System auto-generates IDs; shouldn't occur in normal use |

---

## Key Classes & Methods

### Authentication
```java
// Register new customer
User user = authService.registerCustomer(first, middle, last, email, password);

// Register new admin
User admin = authService.registerAdmin(first, middle, last, email, password);

// Login
User user = authService.login(email, password);

// Check uniqueness
boolean emailExists = authService.emailExists(email);
boolean phoneExists = fbs.phoneExists(phone);
```

### Flight Management
```java
// Add flight
Flight flight = new Flight(id, number, origin, dest, date, capacity, price);
fbs.addFlight(flight);

// Get flight
Flight f = fbs.getFlightByID(flightId);

// List flights
List<Flight> flights = fbs.getFlights();
```

### Booking Operations
```java
// Add booking
Booking booking = new Booking(customer, flight, bookingDate, dynamicPrice);

// Cancel with fee
double fee = calculateCancellationFee(booking);
double refund = booking.getBookingPrice() - fee;

// Update booking
// Remove from old flight, add to new flight, calculate rebooking fee
```

---

## Architecture Patterns

### Design Patterns Implemented

1. **MVC Pattern**
   - Model: Flight, Customer, Booking, FlightBookingSystem
   - View: GUI windows and panels (Swing)
   - Controller: Commands and business logic

2. **Command Pattern**
   - Interface: `Command.java`
   - 19 command implementations
   - Parsed and executed polymorphically

3. **Factory Pattern**
   - `FlightBookingSystemData` creates DataManager instances

4. **Strategy Pattern**
   - Different `DataManager` implementations per entity type

5. **Repository Pattern**
   - `FlightBookingSystem` as central repository

---

## Project Statistics

- **Total Java Files**: 46 (source) + 12 (test)
- **Lines of Code**: 3,500+ (source only)
- **Classes & Interfaces**: 46
- **Commands**: 19
- **Test Cases**: 50+
- **Documentation**: 119 HTML pages + 2 Markdown guides
- **Database Files**: 4 (flights, customers, bookings, users, systemdate)

---

## Learning Outcomes Demonstrated

✅ Object-Oriented Programming (OOP) principles  
✅ Design patterns (Command, Factory, Strategy, MVC)  
✅ File I/O and data persistence  
✅ GUI development (Swing/AWT)  
✅ Authentication & authorization  
✅ Business logic & algorithms  
✅ Exception handling & validation  
✅ Unit testing (JUnit 5)  
✅ Code documentation (Javadoc)  
✅ Dual interface design (CLI + GUI)  

---

## Future Enhancement Ideas

- Database integration (MySQL/PostgreSQL instead of CSV files)
- Email notifications for bookings/cancellations
- Advanced reporting and analytics
- Payment gateway integration
- Mobile app companion
- Multi-language support
- Seat selection interface
- Loyalty/points program
- Real-time availability checking

---

## Support & Assistance

For issues, refer to:
1. **CODEBASE_GUIDE.md** — Architecture details and component breakdown
2. **javadocs/index.html** — Complete API reference
3. **Test files** — See usage examples in test classes
4. **Sample data** — Check resources/data/*.txt files for format

---

**Final Updated**: February 7, 2026  
**Status**: COMPLETE with Full Validation & Testing  
**Author**: Bishwas Chaudhary  


---

## Overview

The Flight Booking System is a university-level Java application designed to demonstrate advanced object-oriented programming concepts, including:
- Multi-tier architecture (Model-View-Controller pattern)
- Persistent data management
- User authentication and authorization
- Complex business logic implementation
- Professional GUI development with Swing
- Comprehensive testing with JUnit 5

The system allows administrators to manage flights, customers, and bookings, while customers can browse available flights and manage their bookings.

---

## Features

### Core Functionality
- [x] **User Authentication** — Email and password-based login with role-based access
- [x] **Flight Management** — Create, view, activate/deactivate flights
- [x] **Customer Management** — Register, view, and manage customer accounts
- [x] **Booking System** — Add, cancel, and update bookings with dynamic pricing
- [x] **Soft Delete** — Non-destructive deletion with account reactivation capability
- [x] **System Date Management** — Admin-controlled system date for testing

### Advanced Features
- [x] **Dynamic Pricing** — Based on seat occupancy and days until departure
- [x] **Cancellation Fees** — 10% minimum $5 on flight cancellations
- [x] **Rebooking Fees** — 5% minimum $2 on booking modifications
- [x] **Capacity Enforcement** — Flight seats cannot exceed configured capacity
- [x] **Past-Flight Restriction** — Prevents booking on departed flights
- [x] **Dual Interface** — Both CLI and GUI with shared authentication system
- [x] **Data Persistence** — File-based storage with CSV format

### Security
- [x] **Role-Based Access Control** — Separate admin and customer privileges
- [x] **Active Account Enforcement** — Deactivated accounts cannot log in
- [x] **Input Validation** — Email format, phone number, and date validation

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 11+ |
| **GUI Framework** | Java Swing (AWT) |
| **Date/Time** | Java LocalDate API |
| **Testing** | JUnit 5 |
| **Data Persistence** | File I/O (PrintWriter, Scanner) |
| **Build** | javac compiler |
| **Documentation** | Javadoc |

---

## Project Structure

```
FlightBookingSystem_Dist/
├── src/
│   └── bcu/cmp5332/bookingsystem/
│       ├── model/                      # Domain entities
│       │   ├── Flight.java
│       │   ├── Customer.java
│       │   ├── Booking.java
│       │   └── FlightBookingSystem.java
│       ├── commands/                   # Command pattern implementation
│       │   ├── Command.java (interface)
│       │   ├── AddFlight.java
│       │   ├── AddBooking.java
│       │   ├── CancelBooking.java
│       │   ├── UpdateBooking.java
│       │   ├── EditBooking.java
│       │   ├── ShowFlight.java
│       │   ├── ShowCustomer.java
│       │   ├── ListFlights.java
│       │   ├── ListCustomers.java
│       │   ├── AdvanceDate.java
│       │   ├── DeactivateCustomer.java
│       │   ├── ReactivateCustomer.java
│       │   ├── DeactivateFlight.java
│       │   ├── ReactivateFlight.java
│       │   ├── LoadGUI.java
│       │   └── Help.java
│       ├── data/                       # Data persistence layer
│       │   ├── DataManager.java (interface)
│       │   ├── FlightDataManager.java
│       │   ├── CustomerDataManager.java
│       │   ├── BookingDataManager.java
│       │   ├── FlightDataManager.java
│       │   └── FlightBookingSystemData.java (factory)
│       ├── auth/                       # Authentication & authorization
│       │   ├── User.java
│       │   ├── Role.java (enum)
│       │   ├── AuthService.java
│       │   └── UserDataManager.java
│       ├── gui/                        # Graphical user interface
│       │   ├── LoginWindow.java
│       │   ├── AdminWindow.java
│       │   ├── CustomerWindow.java
│       │   ├── GuiAuthMenu.java
│       │   ├── GuiAuthService.java
│       │   ├── GuiSession.java
│       │   ├── FlightsPanel.java
│       │   ├── CustomersPanel.java
│       │   ├── BookingsPanel.java
│       │   ├── MyDetailsPanel.java
│       │   ├── AddFlightWindow.java
│       │   └── MainWindow.java
│       └── main/                       # Application entry point & utilities
│           ├── Main.java
│           ├── CommandParser.java
│           ├── FlightBookingSystemException.java
│           └── EditBooking.java
├── test/
│   └── bcu/cmp5332/bookingsystem/
│       ├── model/                      # Model unit tests
│       │   ├── FlightTest.java
│       │   ├── CustomerTest.java
│       │   ├── BookingTest.java
│       │   └── FlightBookingSystemTest.java
│       └── logic/                      # Business logic tests
│           ├── BusinessRulesTest.java
│           └── PricingLogicTest.java
├── resources/data/
│   ├── flights.txt
│   ├── customers.txt
│   ├── bookings.txt
│   └── users.txt
├── bin/                                # Compiled class files
├── javadocs/                           # Generated Javadoc HTML
└── README.md                           # This file
```

---

## Setup & Installation

### Prerequisites
- **Java 11 or higher** (JDK)
- **Terminal/Command prompt**

### Compilation

```bash
cd FlightBookingSystem_Dist
javac -d bin src/bcu/cmp5332/bookingsystem/**/*.java
```

### Running the Application

#### Command-Line Interface
```bash
java -cp bin bcu.cmp5332.bookingsystem.main.Main
```

#### From GUI Menu
1. Run the CLI: `java -cp bin bcu.cmp5332.bookingsystem.main.Main`
2. Select option "3. Load GUI" from the auth menu
3. Or use the GUI option directly

---

## Usage

### Command-Line Interface (CLI)

#### Authentication
```
1. Login
   - Enter email: admin@system.com
   - Enter password: admin123
   
2. Register (Customer)
   - Enter first/middle/last name
   - Enter email (must be unique)
   - Enter password
   - Enter 10-digit phone number
```

#### Admin Commands
```
listflights                          List all active flights
listcustomers                        List all active customers
addflight                            Add new flight (interactive)
addcustomer                          Add new customer (interactive)
showflight [id]                      Display flight details
showcustomer [id]                    Display customer details
addbooking [cust_id] [flight_id]    Book a flight
cancelbooking [cust_id] [flight_id] Cancel booking
updatebooking [cust_id] [old_id] [new_id]  Change flight
advancedate [YYYY-MM-DD]            Set system date (admin)
deactivatecustomer [id]             Disable customer account
reactivatecustomer [id]             Restore customer account
deactivateflight [id]               Disable flight
reactivateflight [id]               Restore flight
loadgui                              Open GUI login window
help                                 Show all commands
exit                                 Quit application
```

#### Customer Commands
```
listflights                          View available flights
showflight [id]                      View flight details
showuser [your_id]                   View your profile
addbooking [your_id] [flight_id]    Book a flight
cancelbooking [your_id] [flight_id] Cancel booking
updatebooking [your_id] [old_id] [new_id]  Change flight
loadgui                              Open GUI login window
help                                 Show available commands
exit                                 Logout
```

### Graphical User Interface (GUI)

#### Login Screen
1. Enter email and password
2. Click "Login" to authenticate
3. Directed to Admin or Customer window based on role

#### Admin Window
- **Flights Tab**: Add flights, view all flights, deactivate/reactivate
- **Customers Tab**: View customers, manage accounts
- **Bookings Tab**: View all bookings, manage reservations

#### Customer Window
- **Flights Tab**: Browse available flights
- **My Bookings Tab**: View and manage your bookings
- **My Details Tab**: View your profile and active bookings

#### Sample Credentials
```
Admin:
- Email: admin@system.com
- Password: admin123

Demo Customer:
- Email: bikalpa@gmail.com
- Password: bikalpa123
```

---

## System Architecture

### Design Patterns Used

1. **Model-View-Controller (MVC)**
   - **Model**: Flight, Customer, Booking, FlightBookingSystem
   - **View**: GUI windows and panels (Swing)
   - **Controller**: Commands and business logic

2. **Command Pattern**
   - All user actions encapsulated as Command objects
   - Parsed and executed polymorphically
   - Enables undo/redo capability

3. **Factory Pattern**
   - FlightBookingSystemData acts as factory for DataManagers
   - Centralizes object creation

4. **Strategy Pattern**
   - Different DataManager implementations for different entities
   - Flexible data persistence strategy

5. **Singleton-like Pattern**
   - Single FlightBookingSystem instance per session
   - Single UserDataManager instance

### Data Flow

```
CLI/GUI Input
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

### Dynamic Pricing Algorithm

**Base Formula**: `final_price = base_price × seat_multiplier × date_multiplier`

**Seat-Based Multiplier** (occupancy rate):
- 0-50%: 1.00 (base price)
- 50-80%: 1.10 (+10%)
- 80%+: 1.20 (+20%)

**Date-Based Multiplier** (days until departure):
- 30+ days: 1.00 (base price)
- 8-30 days: 1.15 (+15%)
- ≤7 days: 1.30 (+30%)

### Fee Structure

| Operation | Fee | Minimum |
|-----------|-----|---------|
| Cancellation | 10% of booking price | $5 |
| Rebooking | 5% of booking price | $2 |

### Business Rules

- ✅ Flights cannot be booked if already departed
- ✅ Deactivated customers/flights cannot be used for bookings
- ✅ Duplicate bookings per customer per flight are prevented
- ✅ Flight capacity is enforced
- ✅ Email addresses must be unique
- ✅ Phone numbers must be 10 digits
- ✅ Inactive customers cannot login

---

## Testing

### Test Coverage

**50+ JUnit 5 tests** across 6 test classes:

| Test Class | Tests | Coverage |
|-----------|-------|----------|
| FlightTest | 8 | Flight model & validation |
| CustomerTest | 9 | Customer bookings & operations |
| BookingTest | 6 | Booking creation & pricing |
| FlightBookingSystemTest | 9 | System repository operations |
| BusinessRulesTest | 9 | Business rule enforcement |
| PricingLogicTest | 9 | Dynamic pricing calculation |
| **TOTAL** | **50+** | **Comprehensive** |

### Running Tests

```bash
# Compile with test files
javac -d bin -cp bin:lib/* src/**/*.java test/**/*.java

# Run tests with JUnit
java -cp bin:lib/junit-jupiter-api-5.x.x.jar:lib/junit-jupiter-engine-5.x.x.jar \
  org.junit.platform.console.ConsoleLauncher --scan-classpath
```

### Test Examples

```java
// Flight capacity enforcement
@Test
void testFlightCapacityEnforcement() {
    Flight f = new Flight(1, "BA123", "London", "Paris", 
                         LocalDate.of(2024,12,1), 2, 100);
    assertEquals(2, f.getCapacity());
    assertEquals(0, f.getPassengers().size());
}

// Dynamic pricing
@Test
void testDynamicPricingHighOccupancy() {
    // 85% occupancy + 5 days = 1.20 × 1.30 = 1.56 multiplier
    double price = calculateDynamicPrice(basePrice, occupancy, daysUntil);
    assertEquals(156.0, price, 0.01);
}
```

---

## Documentation

### Javadoc

Professional HTML documentation generated for all 46 Java classes:

```bash
# View in browser
open javadocs/index.html
```

**Includes:**
- Class-level documentation for all entities
- Method signatures with @param, @return, @throws
- Package-level summaries
- 83 HTML documentation pages
- Searchable API reference

### Code Guide

See `CODEBASE_GUIDE.md` for:
- Component breakdown
- Implementation status
- Design patterns used
- Class dependencies
- Getting started guide

---

## File Descriptions

### Model Package
| File | Purpose |
|------|---------|
| **Flight.java** | Represents a flight with capacity, pricing, and passenger tracking |
| **Customer.java** | Represents a customer with booking history |
| **Booking.java** | Represents a booking with immutable pricing snapshot |
| **FlightBookingSystem.java** | Main system repository for flights and customers |

### Commands Package
| File | Purpose |
|------|---------|
| **Command.java** | Interface for all executable commands |
| **AddBooking.java** | Books customer on flight with dynamic pricing |
| **CancelBooking.java** | Cancels booking with refund calculation |
| **UpdateBooking.java** | Changes booking to different flight |
| **ShowFlight/Customer.java** | Displays entity details |
| **ListFlights/Customers.java** | Lists all active entities |

### Data Package
| File | Purpose |
|------|---------|
| **DataManager.java** | Interface for data persistence |
| **FlightDataManager.java** | Loads/saves flights from CSV file |
| **CustomerDataManager.java** | Loads/saves customers from CSV file |
| **BookingDataManager.java** | Loads/saves bookings with pricing |
| **FlightBookingSystemData.java** | Factory and coordinator for all managers |

### Auth Package
| File | Purpose |
|------|---------|
| **User.java** | Represents system user (admin/customer) |
| **Role.java** | Enum for user roles (ADMIN, CUSTOMER) |
| **AuthService.java** | Handles login, registration, admin creation |
| **UserDataManager.java** | Loads/saves users from CSV file |

### GUI Package
| File | Purpose |
|------|---------|
| **LoginWindow.java** | Authentication interface |
| **AdminWindow.java** | Admin tabbed interface (flights, customers, bookings) |
| **CustomerWindow.java** | Customer tabbed interface (flights, bookings, profile) |
| **GuiAuthMenu.java** | GUI authentication menu with login/register options |
| **FlightsPanel.java** | Flight management panel (add, deactivate, reactivate) |
| **BookingsPanel.java** | Booking management (add, cancel, update) |

---

## Key Classes

### Flight.java
```java
public class Flight {
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;           // Total seats
    private double basePrice;       // Starting price
    private boolean active;         // Soft delete flag
    private Set<Customer> passengers;
}
```

### Customer.java
```java
public class Customer {
    private int id;
    private String name;
    private String phone;
    private boolean active;         // Soft delete flag
    private List<Booking> bookings;
}
```

### Booking.java
```java
public class Booking {
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double bookingPrice;    // Immutable snapshot
}
```

---

## Data Files

Located in `resources/data/`:

### flights.txt
```
ID::FlightNumber::Origin::Destination::DepartureDate::Capacity::BasePrice::Active
1::BA123::London::Paris::2024-11-20::100::150.00::true
2::LH456::Berlin::Munich::2024-11-25::80::120.00::true
```

### customers.txt
```
ID::Name::Phone::Active
1::John Doe::5551234567::true
2::Jane Smith::5559876543::true
```

### bookings.txt
```
CustomerID::FlightID::BookingDate::BookingPrice
1::1::2024-11-11::156.00
2::2::2024-11-11::142.50
```

### users.txt
```
ID::FirstName::MiddleName::LastName::Email::Password::Role::CustomerID
1::System::Admin::User::admin@system.com::admin123::ADMIN::
2::Bishwas::::Chaudhary::bishwas@system.com::bishwas123::ADMIN::
3::Bikalpa::::Bhattarai::bikalpa@gmail.com::bikalpa123::CUSTOMER::2
```

---

## Learning Outcomes

This project demonstrates:

[x] Object-Oriented Programming (OOP) principles  
[x] Design patterns (Command, Factory, Strategy, MVC)  
[x] Data persistence and file I/O  
[x] GUI development with Swing  
[x] User authentication and authorization  
[x] Business logic implementation  
[x] Exception handling and validation  
[x] Unit testing with JUnit  
[x] Code documentation with Javadoc  
[x] Dual interface development (CLI + GUI)  

---

## Sample Workflows

### Workflow 1: Admin Adding a Flight (CLI)
```
java -cp bin bcu.cmp5332.bookingsystem.main.Main
> Login as admin
> Select: addflight
> Enter flight number: AA789
> Enter origin: NYC
> Enter destination: LAX
> Enter departure date: 2024-12-15
> Enter capacity: 150
> Enter base price: 200.00
> Flight added successfully!
```

### Workflow 2: Customer Booking a Flight (GUI)
```
java -cp bin bcu.cmp5332.bookingsystem.main.Main
> Select: 3. Load GUI
> Click "Login" button
> Enter email: bikalpa@gmail.com
> Enter password: bikalpa123
> Click "Login"
> CustomerWindow opens
> Flights Tab: Browse flights
> Click "Book Flight" for desired flight
> Confirm booking with calculated dynamic price
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Cannot find class" error | Ensure `src/` directory is in classpath during compilation |
| GUI doesn't appear | Check Java Swing libraries are available (included in JDK) |
| Data files not found | Ensure `resources/data/` directory exists with required `.txt` files |
| Login fails | Verify credentials in `users.txt` |
| "Duplicate email" error | Email already exists; use unique email for registration |

---

## License

This project is created for educational purposes as part of university coursework.

---

## Author

**Bishwas Chaudhary**

---

## Acknowledgments

- Built using Java 11+ standard libraries
- GUI framework: Java Swing
- Testing framework: JUnit 5
- Documentation: Javadoc

---

## Support

For issues or questions:
1. Check CODEBASE_GUIDE.md for architecture details
2. Review javadocs/index.html for API documentation
3. Examine test cases for usage examples
4. Refer to sample data files for format specifications

---

**Last Updated**: February 6, 2026  
**Status**: COMPLETE and Documented
