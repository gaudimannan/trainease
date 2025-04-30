package views;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton manageTrainsButton = new JButton("Manage Trains");
        JButton viewBookingsButton = new JButton("View Bookings");
        JButton manageUsersButton = new JButton("Manage Users");

        panel.add(manageTrainsButton);
        panel.add(viewBookingsButton);
        panel.add(manageUsersButton);

        add(panel, BorderLayout.CENTER);

        manageTrainsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageTrainsPage();
                dispose();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewBookingsPage();
                dispose();
            }
        });

        manageUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageUsersPage();
                dispose();
            }
        });

        setVisible(true);
    }
}
