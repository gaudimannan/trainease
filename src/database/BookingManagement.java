// src/database/BookingManagement.java
package database;

import java.sql.*;

public class BookingManagement {

    // Method to view all bookings
    public void viewAllBookings() {
        try (Connection conn = DatabaseConnection.connect()) {
            // Query to join bookings, users, and trains to display all bookings
            String query = "SELECT b.booking_id, u.username, t.train_name, b.booking_date " +
                    "FROM bookings b " +
                    "JOIN users u ON b.user_id = u.user_id " +
                    "JOIN trains t ON b.train_id = t.train_id";



            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Display the results in a readable format
            if (!rs.isBeforeFirst()) {  // If no results are found
                System.out.println("No bookings found.");
            } else {
                System.out.println("All Bookings:");
                while (rs.next()) {
                    int bookingId = rs.getInt("booking_id");
                    String username = rs.getString("username");
                    String trainName = rs.getString("train_name");

                    Timestamp bookingDate = rs.getTimestamp("booking_date");
                    System.out.println("Booking ID: " + bookingId);
                    System.out.println("User: " + username);
                    System.out.println("Train: " + trainName);

                    System.out.println("Booking Date: " + bookingDate);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean bookTicket(int userId, int trainId) {
        try (Connection conn = DatabaseConnection.connect()) {
            // Reduce seat count
            String updateSeatsQuery = "UPDATE trains SET available_seats = available_seats - 1 WHERE train_id = ? AND available_seats > 0";
            PreparedStatement updateStmt = conn.prepareStatement(updateSeatsQuery);
            updateStmt.setInt(1, trainId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                return false; // No available seats
            }

            // Insert booking record
            String insertQuery = "INSERT INTO bookings (user_id, train_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, userId);
            stmt.setInt(2, trainId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelTicket(int bookingId) {
        try (Connection conn = DatabaseConnection.connect()) {
            // Get train ID before deleting booking
            String getTrainQuery = "SELECT train_id FROM bookings WHERE booking_id = ?";
            PreparedStatement getTrainStmt = conn.prepareStatement(getTrainQuery);
            getTrainStmt.setInt(1, bookingId);
            ResultSet rs = getTrainStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }
            int trainId = rs.getInt("train_id");

            // Delete booking
            String deleteQuery = "DELETE FROM bookings WHERE booking_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, bookingId);
            int rowsDeleted = deleteStmt.executeUpdate();

            if (rowsDeleted > 0) {
                // Increase seat count
                String updateSeatsQuery = "UPDATE trains SET available_seats = available_seats + 1 WHERE train_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSeatsQuery);
                updateStmt.setInt(1, trainId);
                updateStmt.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewUserBookings(int userId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM bookings WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Booking ID | Train ID");
            while (rs.next()) {
                System.out.println(rs.getInt("booking_id") + " | " + rs.getInt("train_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
