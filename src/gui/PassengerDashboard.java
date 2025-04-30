package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import database.*;

public class PassengerDashboard {

    private JFrame frame;
    private JTextField trainIdField, bookingIdField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new PassengerDashboard().showDashboard());
    }

    public void showDashboard() {
        frame.setVisible(true);
        fadeInFrame();
    }

    public PassengerDashboard() {
        frame = new JFrame("Passenger Dashboard");
        frame.setUndecorated(true);
        frame.setOpacity(0f);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("🚆 Passenger Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(20));

        card.add(createInputField("Train ID to Book:", trainIdField = new JTextField(20)));
        card.add(Box.createVerticalStrut(15));
        card.add(createInputField("Booking ID to Cancel:", bookingIdField = new JTextField(20)));
        card.add(Box.createVerticalStrut(30));

        card.add(createActionButton("📋 View Available Trains", e -> viewAvailableTrains()));
        card.add(Box.createVerticalStrut(10));
        card.add(createActionButton("🎟 Book Ticket", e -> bookTicket()));
        card.add(Box.createVerticalStrut(10));
        card.add(createActionButton("❌ Cancel Ticket", e -> cancelTicket()));
        card.add(Box.createVerticalStrut(10));
        card.add(createActionButton("📖 View My Bookings", e -> viewMyBookings()));

        mainPanel.add(card);
        frame.setContentPane(mainPanel);
    }

    private JPanel createInputField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(200, 30));
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JButton createActionButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(300, 40));
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
        });

        return button;
    }

    private void fadeInFrame() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            float opacity = 0f;
            public void run() {
                opacity += 0.05f;
                if (opacity >= 1f) {
                    frame.setOpacity(1f);
                    cancel();
                } else {
                    frame.setOpacity(opacity);
                }
            }
        }, 0, 20);
    }

    private void viewAvailableTrains() {
        TrainManagement.TrainViewer trainViewer = new TrainManagement.TrainViewer();
        trainViewer.setVisible(true);
    }

    private void bookTicket() {
        try {
            int trainId = Integer.parseInt(trainIdField.getText());
            int userId = 1;
            BookingManagement bookingManagement = new BookingManagement();
            boolean success = bookingManagement.bookTicket(userId, trainId);
            JOptionPane.showMessageDialog(frame, success ? "✅ Ticket booked!" : "❌ Booking failed.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "⚠️ Invalid Train ID.");
        }
    }

    private void cancelTicket() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            BookingManagement bookingManagement = new BookingManagement();
            boolean success = bookingManagement.cancelTicket(bookingId);
            JOptionPane.showMessageDialog(frame, success ? "✅ Ticket canceled!" : "❌ Cancellation failed.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "⚠️ Invalid Booking ID.");
        }
    }

    private void viewMyBookings() {
        int userId = 1;
        BookingManagement bookingManagement = new BookingManagement();
        bookingManagement.viewUserBookings(userId);
    }
}
