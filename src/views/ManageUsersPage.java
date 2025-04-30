package views;

import javax.swing.*;
import java.awt.*;

public class ManageUsersPage extends JFrame {
    public ManageUsersPage() {
        setTitle("Manage Users");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTable usersTable = new JTable(); // Use a JTable to display users
        // Populate the table with user data (Example: username, role)
        JScrollPane scrollPane = new JScrollPane(usersTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
