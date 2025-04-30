package gui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import database.UserManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel panel;

    public static void main(String[] args) {
        try {
            FlatLaf.setup(new FlatMacLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                LoginScreen window = new LoginScreen();
                window.showAnimated(); // Animate appearance
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginScreen() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("🚆 Railway Login");
        frame.setSize(420, 320);
        frame.setUndecorated(true); // For smoother fade-in
        frame.setOpacity(0f);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("TrainMate (╹ڡ╹ ) ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.blue);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        usernameField.setVisible(false); // For slide-in

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setVisible(false); // For slide-in

        JButton btnLogin = new JButton(" Login");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setMaximumSize(new Dimension(200, 40));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setVisible(false); // For slide-in
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> loginUser());

        // Button hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(52, 152, 219));
            }
        });

        JButton btnSignup = new JButton(" Sign Up");
        btnSignup.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignup.setMaximumSize(new Dimension(200, 40));
        btnSignup.setFocusPainted(false);
        btnSignup.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSignup.setForeground(new Color(52, 152, 219));
        btnSignup.setBorderPainted(false);
        btnSignup.setContentAreaFilled(false);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignup.setVisible(false); // For slide-in
        btnSignup.addActionListener(e -> signupUser());

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnSignup);

        frame.getContentPane().add(panel);
    }

    // Show with fade-in and animated components
    public void showAnimated() {
        frame.setVisible(true);
        new Timer(10, new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1f) {
                    frame.setOpacity(1f);
                    ((Timer) e.getSource()).stop();
                    slideInComponents(); // Start next animation
                } else {
                    frame.setOpacity(opacity);
                }
            }
        }).start();
    }

    private void slideInComponents() {
        Component[] comps = panel.getComponents();
        new Timer(150, new ActionListener() {
            int index = 3; // Start at usernameField
            public void actionPerformed(ActionEvent e) {
                if (index < comps.length) {
                    comps[index].setVisible(true);
                    panel.revalidate();
                    panel.repaint();
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UserManagement userManagement = new UserManagement();
        String role = userManagement.authenticateUser(username, password);

        if (role == null) {
            JOptionPane.showMessageDialog(frame, "Invalid credentials!");
        } else if (role.equals("admin")) {
            JOptionPane.showMessageDialog(frame, "Welcome Admin!");
            frame.dispose();
            AdminDashboard.main(null);
        } else {
            JOptionPane.showMessageDialog(frame, "Welcome Passenger!");
            frame.dispose();
            PassengerDashboard.main(null);
        }
    }

    private void signupUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UserManagement userManagement = new UserManagement();
        boolean success = userManagement.registerUser(username, password, "passenger");

        if (success) {
            JOptionPane.showMessageDialog(frame, "Signup successful! Please log in.");
        } else {
            JOptionPane.showMessageDialog(frame, "Username already exists!");
        }
    }
}
