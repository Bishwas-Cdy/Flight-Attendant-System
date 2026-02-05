package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.UserDataManager;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;

/**
 * GUI login screen. Opens AdminWindow or CustomerWindow.
 */
public class LoginWindow extends JFrame {

    private final FlightBookingSystem fbs;

    private final JTextField emailText = new JTextField();
    private final JPasswordField passwordText = new JPasswordField();

    private final JButton loginBtn = new JButton("Login");
    private final JButton exitBtn = new JButton("Exit");

    public LoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Login - Flight Booking System");
        setSize(380, 180);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Email:"));
        form.add(emailText);
        form.add(new JLabel("Password:"));
        form.add(passwordText);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(loginBtn);
        buttons.add(exitBtn);

        loginBtn.addActionListener(e -> doLogin());
        exitBtn.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void doLogin() {
        String email = emailText.getText();
        String pass = new String(passwordText.getPassword());

        try {
            UserDataManager udm = FlightBookingSystemData.getUserDataManager();
            GuiAuthService auth = new GuiAuthService(udm);

            GuiSession session = auth.login(email, pass);

            JOptionPane.showMessageDialog(this,
                    "Login successful.\nWelcome " + session.getUser().getFullName() + " [" + session.getRole() + "]",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if (session.isAdmin()) {
                new AdminWindow(fbs, session);
            } else {
                new CustomerWindow(fbs, session);
            }

            dispose();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
