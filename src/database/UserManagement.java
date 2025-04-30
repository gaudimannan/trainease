// src/database/UserManagement.java
package database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserManagement {

    // Method to delete a user by user ID
    public boolean deleteUser(int userId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // If rows are affected, deletion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to view all users (for admin)
    public static class UserViewer {

        public void viewAllUsersGUI() {
            JFrame frame = new JFrame("All Users");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());

            String[] columnNames = {"User ID", "Username", "Role"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(tableModel);

            try (Connection conn = DatabaseConnection.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

                while (rs.next()) {
                    int id = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String role = rs.getString("role");

                    Object[] row = {id, username, role};
                    tableModel.addRow(row);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error retrieving users: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        }
    }
    public boolean registerUser(String username, String password, String role) {
        try (Connection conn = DatabaseConnection.connect()) {
            // Check if username already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // Username already exists
            }

            // Insert new user
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // For simplicity, storing plain text (Use hashing in production!)
            stmt.setString(3, role);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Authenticate user login
    public String authenticateUser(String username, String password) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Return role (Admin/Passenger)
            } else {
                return null; // Invalid credentials
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



}
