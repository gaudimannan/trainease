// src/database/Booking.java
package database;

import java.sql.*;

public class Booking {

    // Method to book a ticket
    public boolean bookTicket(String username, String trainName, String selectedSeat) {
        // This assumes you have a method to fetch user ID based on the username
        int userId = getUserId(username);

        if (userId == -1) {
            System.out.println("User not found.");
            return false;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            // Query to get the train ID based on train name
            String trainQuery = "SELECT train_id FROM trains WHERE train_name = ?";
            PreparedStatement trainStmt = conn.prepareStatement(trainQuery);
            trainStmt.setString(1, trainName);
            ResultSet trainResult = trainStmt.executeQuery();

            if (trainResult.next()) {
                int trainId = trainResult.getInt("train_id");

                // Check if the seat is available before booking (Optional: Check available seats)
                String availabilityQuery = "SELECT available_seats FROM trains WHERE train_id = ?";
                PreparedStatement availabilityStmt = conn.prepareStatement(availabilityQuery);
                availabilityStmt.setInt(1, trainId);
                ResultSet availabilityResult = availabilityStmt.executeQuery();

                if (availabilityResult.next() && availabilityResult.getInt("available_seats") > 0) {
                    // Insert the booking into the bookings table
                    String bookingQuery = "INSERT INTO bookings (user_id, train_id, booking_date) VALUES (?, ?, ?, NOW())";
                    PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
                    bookingStmt.setInt(1, userId);
                    bookingStmt.setInt(2, trainId);
                    bookingStmt.setString(3, selectedSeat);

                    int rowsAffected = bookingStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Booking Successful!");
                        // Optionally, reduce available seats for the train
                        reduceAvailableSeats(conn, trainId);
                        return true;
                    } else {
                        System.out.println("Booking Failed.");
                        return false;
                    }
                } else {
                    System.out.println("Sorry, no available seats for this train.");
                    return false;
                }
            } else {
                System.out.println("Train not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get user ID from the username
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

    // Method to reduce available seats for the train (after successful booking)
    private void reduceAvailableSeats(Connection conn, int trainId) throws SQLException {
        String updateQuery = "UPDATE trains SET available_seats = available_seats - 1 WHERE train_id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1, trainId);
        updateStmt.executeUpdate();
    }
}
