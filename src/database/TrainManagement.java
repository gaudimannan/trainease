// src/database/TrainManagement.java
package database;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TrainManagement {

    // Method to add a new train schedule
    public boolean addTrain(String trainName, Time departureTime, Time arrivalTime, int totalSeats, String source, String destination) {
        String sql = "INSERT INTO trains (train_name, departure_time, arrival_time, available_seats, source, destination) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, trainName);
            pst.setTime(2, departureTime);
            pst.setTime(3, arrivalTime);
            pst.setInt(4, totalSeats);
            pst.setString(5, source);
            pst.setString(6, destination);

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to update an existing train schedule
    public boolean updateTrain(int trainId, String trainName, String departureTime, String arrivalTime, int availableSeats) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "UPDATE trains SET train_name = ?, departure_time = ?, arrival_time = ?, available_seats = ? WHERE train_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, trainName);
            stmt.setString(2, departureTime);
            stmt.setString(3, arrivalTime);
            stmt.setInt(4, availableSeats);
            stmt.setInt(5, trainId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // If rows are affected, the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a train schedule by train ID
    public boolean deleteTrain(int trainId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "DELETE FROM trains WHERE train_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, trainId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static class TrainViewer extends JFrame {

        private JTextArea textArea;  // For displaying train details

        public TrainViewer() {
            // Set up JFrame
            setTitle("Available Trains");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create JTextArea for displaying train data
            textArea = new JTextArea();
            textArea.setEditable(false);  // Make it non-editable
            JScrollPane scrollPane = new JScrollPane(textArea);

            // Add scrollPane to the JFrame
            add(scrollPane, BorderLayout.CENTER);

            // Call method to display trains in the JTextArea
            viewAvailableTrains();

        }

        public void viewAvailableTrains() {
            try (Connection conn = DatabaseConnection.connect()) {
                String query = "SELECT * FROM trains WHERE available_seats > 0";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // Clear existing text before updating
                textArea.setText("");  // Clears any previous data

                // Append header
                textArea.append("Train ID | Name | Departure | Arrival | Seats\n");

                // Loop through the result set and append data to the JTextArea
                while (rs.next()) {
                    int trainId = rs.getInt("train_id");
                    String trainName = rs.getString("train_name");
                    String departureTime = rs.getString("departure_time");
                    String arrivalTime = rs.getString("arrival_time");
                    int availableSeats = rs.getInt("available_seats");

                    // Append the row data in a formatted manner
                    textArea.append(String.format("%d | %s | %s | %s | %d\n", trainId, trainName, departureTime, arrivalTime, availableSeats));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
