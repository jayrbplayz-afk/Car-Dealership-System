package finalproject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame(ArrayList<User> userList, ArrayList<Cars> carsList) {
        setTitle("DGV Auto Deals - Welcome");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900,600));
        
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
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350,300));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        ImageIcon logoPhoto = new ImageIcon("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\dgvlogo.png");
        Image rawPhoto = logoPhoto.getImage();
        Image rescaledPhoto = rawPhoto.getScaledInstance(200, 133, Image.SCALE_SMOOTH);
        JLabel logoImage = new JLabel(new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        logoImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JLabel subtitleText = new JLabel("Welcome to DGV Auto Deals");
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(290, 35));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton registerButton = new JButton("Register New User");
        registerButton.setMaximumSize(new Dimension(290, 35));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(logoImage);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleText);
        panel.add(Box.createVerticalStrut(25));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(12));
        panel.add(registerButton);

        
        background.add(panel);
        setContentPane(background);

        loginButton.addActionListener(e -> {
            new LoginFrame(userList, carsList).setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            new RegisterFrame(userList, carsList).setVisible(true);
            dispose();
        });
    }
}