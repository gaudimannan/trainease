
package views;

import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancelBookingPage extends JFrame {
    public CancelBookingPage() {
        setTitle("Cancel Booking");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create table to display booking options
        JTable cancelTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(cancelTable);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch booking history from database and populate the table
        populateCancelBookings(cancelTable);

        JButton cancelButton = new JButton("Cancel Booking");
        add(cancelButton, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> {
            int selectedRow = cancelTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) cancelTable.getValueAt(selectedRow, 0);  // Get the booking ID from the selected row
                if (cancelBooking(bookingId)) {
                    JOptionPane.showMessageDialog(null, "Booking Cancelled Successfully");
                    dispose(); // Close the page
                } else {
                    JOptionPane.showMessageDialog(null, "Cancellation Failed");
                }
            }
        });

        setVisible(true);
    }

    // Method to populate the cancel booking table
    private void populateCancelBookings(JTable table) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT booking_id, train_name FROM bookings WHERE user_id = 1";  // Use dynamic user_id
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Create a table model and set it to the JTable
            Object[][] data = new Object[10][3];  // Assume up to 10 bookings for now
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("booking_id");  // Booking ID
                data[i][1] = rs.getString("train_name");

                i++;
            }

            Object[] columnNames = {"Booking ID", "Train Name", "Seat Number"};
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to cancel a booking
    private boolean cancelBooking(int bookingId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "DELETE FROM bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Return true if cancellation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false in case of error
        }
    }
}
