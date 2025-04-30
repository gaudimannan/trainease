package views;



import javax.swing.*;
import java.awt.*;

public class ViewBookingsPage extends JFrame {
    public ViewBookingsPage() {
        setTitle("View Bookings");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTable bookingsTable = new JTable(); // Use a JTable to display bookings
        // Populate the table with booking data from the database (Example: train_name, seat, user)
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
