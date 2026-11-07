package finalproject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPassField;
    private JTextField phoneNumberField;
    private JComboBox<String> roleBox;

    public RegisterFrame(ArrayList<User> userList, ArrayList<Cars> carsList) {
        setTitle("DGV Auto Deals - Register");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\showroombackground.png");

        JPanel background = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (backgroundImage.getIconWidth() > 0) {
                    g.drawImage(backgroundImage.getImage(), 0, 0,
                            getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(35, 42, 55));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350, 550));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        ImageIcon logoPhoto = new ImageIcon("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\dgvlogo.png");
        Image rawPhoto = logoPhoto.getImage();
        Image rescaledPhoto = rawPhoto.getScaledInstance(200, 133, Image.SCALE_SMOOTH);
        JLabel logoImage = new JLabel(new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        logoImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleText = new JLabel("Create your account");
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(290, 35));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(290, 35));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        confirmPassField = new JPasswordField();
        confirmPassField.setMaximumSize(new Dimension(290, 35));
        confirmPassField.setBorder(BorderFactory.createTitledBorder("Confirm Password"));

        phoneNumberField = new JTextField();
        phoneNumberField.setMaximumSize(new Dimension(290, 35));
        phoneNumberField.setBorder(BorderFactory.createTitledBorder("Contact/Phone No."));

        roleBox = new JComboBox<>(new String[]{"Customer", "Staff"});
        roleBox.setMaximumSize(new Dimension(290, 35));

        JButton registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(290, 35));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setMaximumSize(new Dimension(290, 35));

        panel.add(logoImage);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleText);
        panel.add(Box.createVerticalStrut(25));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(confirmPassField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(phoneNumberField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(roleBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cancelButton);

        background.add(panel);
        setContentPane(background);

        registerButton.addActionListener(e -> {
            String uname = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            String role = roleBox.getSelectedItem().toString();
            String phoneNo = phoneNumberField.getText().trim();

            if (uname.isEmpty() || pass.isEmpty() || phoneNo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all the required fields, including username, password and phone number.");
                return;
            }

            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match. Please re-enter.");
                passwordField.setText("");
                confirmPassField.setText("");
                return;
            }

            if (!phoneNo.startsWith("09") || phoneNo.length() != 11 || !phoneNo.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "Invalid phone number format.\nIt must start with '09' and contain exactly 11 digits (e.g., 09123456789).");
                return;
            }

            for (User user : userList) {
                if (uname.equals(user.getUsername())) {
                    JOptionPane.showMessageDialog(this,
                            "Username already exists.");
                    return;
                }
            }

            User newUser = new User(uname, pass, role, phoneNo);

            File file = new File("users.txt");

            try (PrintWriter writer = new PrintWriter(
                    new FileWriter(file, true))) {

                writer.println(
                        newUser.getUsername() + "|"
                        + newUser.getPassword() + "|"
                        + newUser.getRole() + "|"
                        + newUser.getPhoneNumber()
                );
                writer.flush();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to save the account.\n" + ex.getMessage(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            userList.add(newUser);

            System.out.println(
                    "Account saved to: " + file.getAbsolutePath());

            JOptionPane.showMessageDialog(this,
                    "Registration successful!\n"
                    + "Account saved successfully.");

            dispose();
            new WelcomeFrame(userList, carsList).setVisible(true);

            usernameField.setText("");
            passwordField.setText("");
            confirmPassField.setText("");
            phoneNumberField.setText("");
        });

        cancelButton.addActionListener(e -> {
            new WelcomeFrame(userList, carsList).setVisible(true);
            dispose();
        });
    }
}
