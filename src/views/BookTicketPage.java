package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookTicketPage extends JFrame {
    public BookTicketPage() {
        setTitle("Book Ticket");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Select Train:"));
        JComboBox<String> trainComboBox = new JComboBox<>(new String[] {"Train 1", "Train 2"});
        add(trainComboBox);

        add(new JLabel("Select Seat:"));
        JComboBox<String> seatComboBox = new JComboBox<>(new String[] {"1", "2", "3", "4"});
        add(seatComboBox);

        JButton bookButton = new JButton("Book Ticket");
        add(new JLabel());
        add(bookButton);

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Booking logic (insert into database)
                JOptionPane.showMessageDialog(null, "Ticket Booked Successfully");
                dispose();
            }
        });

        setVisible(true);
    }
}
