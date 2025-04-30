package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassengerDashboard extends JFrame {
    public PassengerDashboard() {
        setTitle("Passenger Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Passenger Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons for different actions
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton bookTicketButton = new JButton("Book Tickets");
        JButton viewHistoryButton = new JButton("View Booking History");
        JButton cancelBookingButton = new JButton("Cancel Booking");

        panel.add(bookTicketButton);
        panel.add(viewHistoryButton);
        panel.add(cancelBookingButton);

        add(panel, BorderLayout.CENTER);

        // Button Actions
        bookTicketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BookTicketPage();
                dispose();
            }
        });

        viewHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BookingHistoryPage();
                dispose();
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CancelBookingPage();
                dispose();
            }
        });

        setVisible(true);
    }
}
