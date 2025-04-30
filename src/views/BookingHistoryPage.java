// src/views/BookingHistoryPage.java
package views;

import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingHistoryPage extends JFrame {
    public BookingHistoryPage() {
        setTitle("Booking History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTable historyTable = new JTable(); // Create a table to display booking history
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch booking history from database and populate the table
        populateBookingHistory(historyTable);

        setVisible(true);
    }

    // Method to populate booking history
    private void populateBookingHistory(JTable table) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT train_name, booking_date FROM bookings WHERE user_id = 1";  // Use dynamic user_id
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Create a table model and set it to the JTable
            Object[][] data = new Object[10][3];  // Assume up to 10 bookings for now
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("train_name");

                data[i][2] = rs.getDate("booking_date");
                i++;
            }

            Object[] columnNames = {"Train Name", "Seat Number", "Booking Date"};
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
