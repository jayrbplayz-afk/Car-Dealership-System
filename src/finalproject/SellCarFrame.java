package finalproject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SellCarFrame extends JFrame {

    private JTextField makeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField colorField;
    private JTextField ownersField;
    private JTextField priceField;
    private JTextField imageField;

    private JComboBox<String> categoryBox;
    private JComboBox<String> fuelBox;
    private JComboBox<String> transmissionBox;
    private JComboBox<Integer> seatsBox;

    private JSlider conditionSlider;
    private JLabel conditionLabel;

    private String currentUser;
    private ArrayList<Cars> carsList;
    private Runnable refreshDashboard;

    public SellCarFrame(ArrayList<Cars> carsList, Runnable refreshDashboard, String currentUser) {
        
        this.currentUser = currentUser;
        this.carsList = carsList;
        this.refreshDashboard = refreshDashboard;

        setTitle("DGV Auto Deals - Sell Your Car");
        setSize(620, 730);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("SELL YOUR CAR");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(25, 20, 350, 35);
        panel.add(title);

        JLabel subtitle = new JLabel(
                "Enter your vehicle information below.");
        subtitle.setBounds(25, 55, 350, 25);
        panel.add(subtitle);

        makeField = new JTextField();
        modelField = new JTextField();
        yearField = new JTextField();
        colorField = new JTextField();
        ownersField = new JTextField("1");
        priceField = new JTextField();
        imageField = new JTextField("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\defaultcar.jpg");

        categoryBox = new JComboBox<>(new String[]{
            "Sedan", "SUV", "Coupe", "Hatchback",
            "Convertible", "Pickup", "Van"
        });

        fuelBox = new JComboBox<>(new String[]{
            "Gasoline", "Diesel", "Hybrid", "Electric"
        });

        transmissionBox = new JComboBox<>(new String[]{
            "Automatic", "Manual"
        });

        seatsBox = new JComboBox<>(
                new Integer[]{2, 4, 5, 6, 7, 8, 9, 10, 12, 15});

        addField(panel, "Car Make:", makeField, 95);
        addField(panel, "Car Model:", modelField, 130);
        addField(panel, "Year Model:", yearField, 165);
        addField(panel, "Color:", colorField, 200);
        addField(panel, "Body Category:", categoryBox, 235);
        addField(panel, "Fuel Type:", fuelBox, 305);
        addField(panel, "Transmission:", transmissionBox, 340);
        addField(panel, "Number of Seats:", seatsBox, 375);
        addField(panel, "Previous Owners:", ownersField, 410);

        conditionLabel = new JLabel("Condition: 1.0 - Poor");
        conditionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        conditionLabel.setBounds(25, 445, 210, 25);
        panel.add(conditionLabel);

        conditionSlider = new JSlider(10, 50, 10);
        conditionSlider.setMajorTickSpacing(10);
        conditionSlider.setMinorTickSpacing(5);
        conditionSlider.setPaintTicks(true);
        conditionSlider.setPaintLabels(true);
        conditionSlider.setSnapToTicks(true);
        conditionSlider.setBackground(Color.WHITE);
        conditionSlider.setBounds(240, 440, 320, 45);
        panel.add(conditionSlider);

        conditionSlider.addChangeListener(e -> {
            double condition = conditionSlider.getValue() / 10.0;

            if (condition <= 1.5) {
                conditionLabel.setText(
                        "Condition: " + condition + " - Poor");
            } else if (condition <= 2.5) {
                conditionLabel.setText(
                        "Condition: " + condition + " - Fair");
            } else if (condition <= 3.5) {
                conditionLabel.setText(
                        "Condition: " + condition + " - Good");
            } else if (condition <= 4.5) {
                conditionLabel.setText(
                        "Condition: " + condition + " - Very Good");
            } else {
                conditionLabel.setText(
                        "Condition: " + condition + " - Excellent");
            }
        });

        addField(panel, "Expected Price (PHP):", priceField, 495);
        addField(panel, "Image Path (optional):", imageField, 530);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(470, 560, 90, 25);
        panel.add(browseButton);

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                imageField.setText(
                        chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(25, 620, 260, 35);
        panel.add(cancelButton);

        JButton submitButton = new JButton("Submit Car");
        submitButton.setBounds(300, 620, 260, 35);
        submitButton.setBackground(new Color(65, 115, 220));
        submitButton.setForeground(Color.WHITE);
        panel.add(submitButton);

        cancelButton.addActionListener(e -> dispose());
        submitButton.addActionListener(e -> submitCar());

        add(panel);
    }

    private void addField(JPanel panel, String label,
                          JComponent field, int y) {

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Arial", Font.BOLD, 12));
        textLabel.setBounds(25, y, 200, 25);
        panel.add(textLabel);

        field.setBounds(240, y, 320, 25);
        panel.add(field);
    }

    private void submitCar() {

        String make = makeField.getText().trim();
        String model = modelField.getText().trim();
        String color = colorField.getText().trim();
        String imagePath = imageField.getText().trim();

        if (make.isEmpty() || model.isEmpty()
                || yearField.getText().trim().isEmpty()
                || color.isEmpty()
                || ownersField.getText().trim().isEmpty()
                || priceField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all required fields.");
            return;
        }

        try {
            int year = Integer.parseInt(
                    yearField.getText().trim());

            int owners = Integer.parseInt(
                    ownersField.getText().trim());

            double price = Double.parseDouble(
                    priceField.getText().trim());

            int currentYear = java.time.Year.now().getValue();

            if (year < 1886 || year > currentYear) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid model year.");
                return;
            }

            double condition =
                    conditionSlider.getValue() / 10.0;
            
            String seller = currentUser;

            UsedCars submittedCar = new UsedCars(
                    make,
                    model,
                    year,
                    (String) categoryBox.getSelectedItem(),
                    price,
                    (String) fuelBox.getSelectedItem(),
                    (Integer) seatsBox.getSelectedItem(),
                    (String) transmissionBox.getSelectedItem(),
                    color,
                    owners,
                    condition,
                    imagePath,
                    seller,
                    1
            );

            carsList.add(submittedCar);

            JOptionPane.showMessageDialog(
                    this,
                    "Your car has been submitted successfully!");

            refreshDashboard.run();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter valid numeric values "
                    + "for year, owners, and price.");
        }
    }
}
