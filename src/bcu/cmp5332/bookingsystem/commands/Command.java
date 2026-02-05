package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public interface Command {

    public static final String ADMIN_HELP = "Commands:\n"
            + "\tlistflights                               print all flights\n"
            + "\tlistcustomers                             print all customers\n"
            + "\taddflight                                 add a new flight (includes capacity and base price)\n"
            + "\taddcustomer                               add a new customer\n"
            + "\tshowflight [flight id]                    show flight details\n"
            + "\tshowcustomer [customer id]                show customer details\n"
            + "\taddbooking [customer id] [flight id]      add a new booking\n"
            + "\tcancelbooking [customer id] [flight id]   cancel a booking\n"
            + "\tupdatebooking [customer id] [old flight id] [new flight id]\tupdate a booking\n"
            + "\tadvancedate [YYYY-MM-DD]                  advance the system date (admin only)\n"
            + "\tdeactivatecustomer [customer id]          deactivate a customer account\n"
            + "\treactivatecustomer [customer id]          reactivate a customer account\n"
            + "\tdeactivateflight [flight id]              deactivate a flight\n"
            + "\treactivateflight [flight id]              reactivate a flight\n"
            + "\tloadgui                                   loads the GUI version of the app\n"
            + "\thelp                                      prints this help message\n"
            + "\texit                                      exits the program";

    public static final String CUSTOMER_HELP = "Commands:\n"
            + "\tlistflights                               print all flights\n"
            + "\tshowflight [flight id]                    show flight details\n"
            + "\tshowcustomer [your id]                    show your customer details\n"
            + "\taddbooking [your id] [flight id]          add a new booking\n"
            + "\tcancelbooking [your id] [flight id]       cancel a booking\n"
            + "\tupdatebooking [your id] [old flight id] [new flight id]\tupdate a booking\n"
            + "\tloadgui                                   loads the GUI version of the app\n"
            + "\thelp                                      prints this help message\n"
            + "\texit                                      exits the program";

    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;

}
