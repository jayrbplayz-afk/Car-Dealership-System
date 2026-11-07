package finalproject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginFrame extends JFrame {

    int attemptCount = 0;
    final static int MAX_ATTEMPTS = 3;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(ArrayList<User> userList, ArrayList<Cars> carsList) {
        setTitle("DGV Auto Deals - Login");
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
        panel.setPreferredSize(new Dimension(350, 420));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        ImageIcon logoPhoto = new ImageIcon("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\dgvlogo.png");
        Image rawPhoto = logoPhoto.getImage();
        Image rescaledPhoto = rawPhoto.getScaledInstance(200, 133, Image.SCALE_SMOOTH);
        JLabel logoImage = new JLabel(new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        logoImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleText = new JLabel("Log-in to your account");
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(290, 35));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(290, 35));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(290, 35));

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
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cancelButton);

        background.add(panel);
        setContentPane(background);

        loginButton.addActionListener(e -> {
            String uname = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            User foundUser = null;
            for (User user : userList) {
                if (user.getUsername().equals(uname) && user.getPassword().equals(pass)) {
                    foundUser = user;
                    break;
                }
            }

            if (foundUser != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + foundUser.getUsername() + " (" + foundUser.getRole() + ")");
                new DashboardFrame(userList, carsList, foundUser.getUsername(), foundUser.getRole(), foundUser.getUserId()).setVisible(true);
                dispose();

            } else {
                attemptCount++;
                int remainingAttempts = MAX_ATTEMPTS - attemptCount;
                if (remainingAttempts > 0) {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password.");
                } else {
                    JOptionPane.showMessageDialog(this, "Too many attempts, the program will now close.", "Locked Out", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        });
        cancelButton.addActionListener(e -> {
            new WelcomeFrame(userList, carsList).setVisible(true);
            dispose();
        });
    }
}
