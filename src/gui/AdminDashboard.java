package gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import database.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;

public class AdminDashboard {

    private JFrame frame;
    private JTextField sourceField, destinationField, trainNameField, departureTimeField, arrivalTimeField, availableSeatsField, userIdField;
    private JPanel contentPanel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf()); // Light theme
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                AdminDashboard window = new AdminDashboard();
                window.showAnimated();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminDashboard() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("🚂 Admin Dashboard");
        frame.setUndecorated(true); // For opacity animation
        frame.setOpacity(0f);
        frame.setSize(1000, 900);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(new Color(219, 226, 239));

        // Title
        JLabel title = new JLabel("Admin Controls Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button + Field builder
        addTextField("Train Name:", trainNameField = new JTextField());
        addTextField("Departure Time (HH:mm:ss):", departureTimeField = new JTextField());
        addTextField("Arrival Time (HH:mm:ss):", arrivalTimeField = new JTextField());
        addTextField("Available Seats:", availableSeatsField = new JTextField());
        addTextField("Source:", sourceField = new JTextField());
        addTextField("Destination:", destinationField = new JTextField());

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(createStyledButton("➕ Add New Train", e -> addNewTrain()));
        contentPanel.add(createStyledButton("✏️ Update Train", e -> updateTrain()));
        contentPanel.add(createStyledButton("🗑 Delete Train", e -> deleteTrain()));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        contentPanel.add(createStyledButton("📋 View All Bookings", e -> viewAllBookings()));
        contentPanel.add(createStyledButton("👥 View All Users", e -> viewAllUsers()));

        addTextField("User ID to Delete:", userIdField = new JTextField());
        contentPanel.add(createStyledButton("🗑 Delete User", e -> deleteUser()));

        frame.setContentPane(contentPanel);
    }

    private void addTextField(String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lbl);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(field);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(63, 114, 175));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(250, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(63, 114, 175));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(63, 114, 175));
            }
        });

        return button;
    }

    // Animation on startup
    private void showAnimated() {
        frame.setVisible(true);
        new Timer(10, new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1f) {
                    frame.setOpacity(1f);
                    ((Timer) e.getSource()).stop();
                } else {
                    frame.setOpacity(opacity);
                }
            }
        }).start();
    }

    // Functional Methods
    private void viewAllBookings() {
        new BookingManagement().viewAllBookings();
    }

    private void viewAllUsers() {
        new UserManagement.UserViewer().viewAllUsersGUI();
    }

    private void deleteUser() {
        int userId = Integer.parseInt(userIdField.getText());
        boolean success = new UserManagement().deleteUser(userId);
        JOptionPane.showMessageDialog(frame, success ? "User deleted." : "Failed to delete user.");
    }

    private void addNewTrain() {
        try {
            String name = trainNameField.getText().trim();
            String source = sourceField.getText().trim();
            String dest = destinationField.getText().trim();
            Time dep = Time.valueOf(departureTimeField.getText().trim());
            Time arr = Time.valueOf(arrivalTimeField.getText().trim());
            int seats = Integer.parseInt(availableSeatsField.getText().trim());

            boolean success = new TrainManagement().addTrain(name, dep, arr, seats, source, dest);
            JOptionPane.showMessageDialog(frame, success ? "Train added." : "Failed to add train.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Check time format & seats.");
        }
    }

    private void updateTrain() {
        try {
            int trainId = Integer.parseInt(JOptionPane.showInputDialog("Enter Train ID to Update:"));
            String name = trainNameField.getText();
            String dep = departureTimeField.getText();
            String arr = arrivalTimeField.getText();
            int seats = Integer.parseInt(availableSeatsField.getText());

            boolean success = new TrainManagement().updateTrain(trainId, name, dep, arr, seats);
            JOptionPane.showMessageDialog(frame, success ? "Train updated." : "Failed to update train.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }

    private void deleteTrain() {
        try {
            int trainId = Integer.parseInt(JOptionPane.showInputDialog("Enter Train ID to Delete:"));
            boolean success = new TrainManagement().deleteTrain(trainId);
            JOptionPane.showMessageDialog(frame, success ? "Train deleted." : "Failed to delete train.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID.");
        }
    }
}
