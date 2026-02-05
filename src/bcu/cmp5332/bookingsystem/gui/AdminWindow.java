package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Admin GUI window.
 */
public class AdminWindow extends JFrame {

    private final FlightBookingSystem fbs;
    private final GuiSession session;

    private final JTabbedPane tabs = new JTabbedPane();

    private final FlightsPanel flightsPanel;
    private final CustomersPanel customersPanel;
    private final BookingsPanel bookingsPanel;

    public AdminWindow(FlightBookingSystem fbs, GuiSession session) {
        this.fbs = fbs;
        this.session = session;

        flightsPanel = new FlightsPanel(fbs, true);
        customersPanel = new CustomersPanel(fbs);
        bookingsPanel = new BookingsPanel(fbs, true, null);

        initialize();
    }

    private void initialize() {
        setTitle("Admin - Flight Booking System");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs.addTab("Flights", flightsPanel);
        tabs.addTab("Customers", customersPanel);
        tabs.addTab("Bookings", bookingsPanel);

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
