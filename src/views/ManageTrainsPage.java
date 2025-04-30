package views;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageTrainsPage extends JFrame {
    public ManageTrainsPage() {
        setTitle("Manage Trains");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Train Name:"));
        JTextField trainNameField = new JTextField();
        add(trainNameField);

        add(new JLabel("Source:"));
        JTextField sourceField = new JTextField();
        add(sourceField);

        add(new JLabel("Destination:"));
        JTextField destinationField = new JTextField();
        add(destinationField);

        add(new JLabel("Departure Time:"));
        JTextField departureTimeField = new JTextField();
        add(departureTimeField);

        add(new JLabel("Total Seats:"));
        JTextField totalSeatsField = new JTextField();
        add(totalSeatsField);

        JButton addTrainButton = new JButton("Add Train");
        add(new JLabel()); // Empty label for spacing
        add(addTrainButton);

        addTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add train logic (Insert into database)
                JOptionPane.showMessageDialog(null, "Train Added Successfully");
                dispose(); // Close current page after adding the train
            }
        });

        setVisible(true);
    }
}
