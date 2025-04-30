// src/database/BookingHistory.java
package database;

import java.sql.*;

public class BookingHistory {

    // Method to fetch booking history for a user
    public void fetchBookingHistory(String username) {
        int userId = getUserId(username);

        if (userId == -1) {
            System.out.println("User not found.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            // SQL query to fetch booking history for the user
            String query = "SELECT b.booking_id, t.train_name" +
                    "FROM bookings b " +
                    "JOIN trains t ON b.train_id = t.train_id " +
                    "WHERE b.user_id = ? " +
                    "ORDER BY b.booking_date DESC";  // Ordering by booking date

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Display the results in a readable format
            if (!rs.isBeforeFirst()) {  // If no results are found
                System.out.println("No booking history found for this user.");
            } else {
                System.out.println("Booking History for " + username + ":");
                while (rs.next()) {
                    int bookingId = rs.getInt("booking_id");
                    String trainName = rs.getString("train_name");

                    Timestamp bookingDate = rs.getTimestamp("booking_date");
                    System.out.println("Booking ID: " + bookingId);
                    System.out.println("Train: " + trainName);
                    System.out.println("Date: " + bookingDate);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get user ID based on the username
    private int getUserId(String username) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // User not found
    }
}
