package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Customer GUI window with limited features. Restricts access to own bookings and flights.
 */
public class CustomerWindow extends JFrame {

    private final FlightBookingSystem fbs;
    private final GuiSession session;

    private final JTabbedPane tabs = new JTabbedPane();

    private final FlightsPanel flightsPanel;
    private final BookingsPanel bookingsPanel;
    private final MyDetailsPanel myDetailsPanel;

    /**
     * Creates a new customer window.
     *
     * @param fbs the FlightBookingSystem instance
     * @param session the authenticated customer session
     */
    public CustomerWindow(FlightBookingSystem fbs, GuiSession session) {
        this.fbs = fbs;
        this.session = session;

        flightsPanel = new FlightsPanel(fbs, false);
        bookingsPanel = new BookingsPanel(fbs, false, session.getCustomerId());
        myDetailsPanel = new MyDetailsPanel(fbs, session.getCustomerId());

        initialize();
    }

    /**
     * Initializes GUI components and layout.
     */
    private void initialize() {
        setTitle("Customer - Flight Booking System");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs.addTab("Flights", flightsPanel);
        tabs.addTab("My Bookings", bookingsPanel);
        tabs.addTab("My Details", myDetailsPanel);

        setJMenuBar(buildMenuBar());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem exit = new JMenuItem("Exit");

        save.addActionListener(e -> doSave());
        logout.addActionListener(e -> {
            dispose();
            new LoginWindow(fbs);
        });
        exit.addActionListener(e -> {
            doSave();
            System.exit(0);
        });

        file.add(save);
        file.add(logout);
        file.add(exit);

        bar.add(file);
        return bar;
    }

    private void doSave() {
        try {
            FlightBookingSystemData.store(fbs);
            JOptionPane.showMessageDialog(this, "Saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
