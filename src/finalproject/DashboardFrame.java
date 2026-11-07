package finalproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class DashboardFrame extends JFrame {

    private JPanel carsPanel;
    private JTextField searchField;

    private String currentRole;
    private String currentUser;
    private String currentId;

    private JComboBox<String> sortComboBox;
    private JLabel breadcrumb = new JLabel();

    ArrayList<Cars> carsList;
    ArrayList<User> userList;

    public DashboardFrame(ArrayList<User> userList, ArrayList<Cars> carsList, String currentUser, String currentRole, String currentId) {
        this.userList = userList;
        this.carsList = carsList;
        this.currentUser = currentUser;
        this.currentRole = currentRole;
        this.currentId = currentId;

        setTitle("DGV Auto Deals - Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);

        displayCars("");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 35, 55));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        ImageIcon logoPhoto = new ImageIcon("C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\dgvlogoalt.png");
        Image rawPhoto = logoPhoto.getImage();
        Image rescaledPhoto = rawPhoto.getScaledInstance(125, 48, Image.SCALE_SMOOTH);
        JLabel logoImage = new JLabel(new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        logoImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(currentRole);
        roleLabel.setForeground(Color.LIGHT_GRAY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logoImage);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(roleLabel);
        sidebar.add(Box.createVerticalStrut(35));

        sidebar.add(createNavButton("Inventory", true));
        sidebar.add(Box.createVerticalStrut(10));

        if (currentRole.equals("Customer")) {
            sidebar.add(createNavButton("Transaction History", false));
        } else if (currentRole.equals("Admin") || currentRole.equals("Staff")) {
            sidebar.add(createNavButton("Users List", false));
        }

        sidebar.add(Box.createVerticalStrut(10));

        if (currentRole.equals("Customer")) {
            sidebar.add(createNavButton("Sell a Car", false));
        } else if (currentRole.equals("Admin") || currentRole.equals("Staff")) {
            sidebar.add(createNavButton("Inventory Manager", false));
        }

        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createNavButton("Settings", false));

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = createNavButton("Logout", false);
        logoutButton.addActionListener(e -> {
            new WelcomeFrame(userList, carsList).setVisible(true);
            dispose();
        });

        sidebar.add(logoutButton);
        return sidebar;
    }

    private JButton createNavButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(190, 42));
        button.setPreferredSize(new Dimension(190, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        if (selected) {
            button.setBackground(new Color(65, 115, 220));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(40, 52, 75));
            button.setForeground(Color.WHITE);
        }

        button.addActionListener(e -> {
            if (text.equals("Transaction History")) {
                new TransactionHistoryFrame(currentUser, currentRole).setVisible(true);
            } else if (text.equals("Sell a Car")) {
                new SellCarFrame(
                        carsList,
                        () -> displayCars(searchField.getText()),
                        currentUser
                ).setVisible(true);
            } else if (text.equals("Inventory Manager")) {
                new InventoryManagerFrame(
                        carsList,
                        () -> displayCars(searchField.getText())
                ).setVisible(true);
            } else if (!text.equals("Inventory") && !text.equals("Logout")) {
                JOptionPane.showMessageDialog(
                        this,
                        text + " page will be added later."
                );
            }
        });

        return button;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setOpaque(false);

        JPanel titleBreadcrumbPanel = new JPanel();
        titleBreadcrumbPanel.setLayout(
                new BoxLayout(titleBreadcrumbPanel, BoxLayout.Y_AXIS));
        titleBreadcrumbPanel.setOpaque(false);

        breadcrumb.setText("Dashboard  /  Inventory  /  ");
        breadcrumb.setForeground(new Color(110, 115, 125));
        breadcrumb.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel title = new JLabel("Car Dealership Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(new Color(30, 35, 45));

        titleBreadcrumbPanel.add(title);
        titleBreadcrumbPanel.add(Box.createVerticalStrut(4));
        titleBreadcrumbPanel.add(breadcrumb);

        String[] sortOptions = {
            "Default (None)",
            "Alphabetical (A-Z)",
            "Year: Newest to Oldest",
            "Year: Oldest to Newest",
            "Price: Low to High",
            "Price: High to Low"
        };

        sortComboBox = new JComboBox<>(sortOptions);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        searchField.setToolTipText("Search for a car");

        sortComboBox.addActionListener(e -> displayCars(searchField.getText()));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(80, 35));
        searchButton.addActionListener(e -> displayCars(searchField.getText()));
        searchField.addActionListener(e -> displayCars(searchField.getText()));

        JPanel topRightControls = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRightControls.setOpaque(false);
        topRightControls.add(new JLabel("Sort by:"));
        topRightControls.add(sortComboBox);
        topRightControls.add(searchField);
        topRightControls.add(searchButton);

        header.add(titleBreadcrumbPanel, BorderLayout.WEST);
        header.add(topRightControls, BorderLayout.EAST);

        carsPanel = new JPanel(new GridLayout(0, 3, 18, 18));
        carsPanel.setBackground(new Color(245, 247, 250));
        carsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(carsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void displayCars(String search) {
        carsPanel.removeAll();
        carsPanel.setLayout(new GridLayout(0, 3, 18, 18));

        ArrayList<Cars> filteredList = new ArrayList<>(carsList);
        String query = search == null ? "" : search.toLowerCase().trim();

        if (!query.isEmpty()) {
            ArrayList<Cars> searchList = new ArrayList<>(carsList);

            CarSearchSorter.quickSort(searchList, 0, searchList.size() - 1,
                    (c1, c2) -> {
                        String name1 = (c1.getMake() + " " + c1.getModel())
                                .toLowerCase();
                        String name2 = (c2.getMake() + " " + c2.getModel())
                                .toLowerCase();
                        return name1.compareTo(name2);
                    });

            filteredList = CarSearchSorter.linearSearch(searchList, query);
        }

        Comparator<Cars> comparator
                = getComparator((String) sortComboBox.getSelectedItem());

        if (comparator != null && !filteredList.isEmpty()) {
            CarSearchSorter.quickSort(filteredList, 0,
                    filteredList.size() - 1, comparator);
        }

        if (filteredList.isEmpty()) {
            carsPanel.setLayout(new BorderLayout());
            JLabel noResults = new JLabel("No cars found.",
                    SwingConstants.CENTER);
            noResults.setFont(new Font("Arial", Font.BOLD, 18));
            carsPanel.add(noResults, BorderLayout.CENTER);
        } else {
            for (Cars car : filteredList) {
                carsPanel.add(createCarCard(car));
            }
        }

        carsPanel.revalidate();
        carsPanel.repaint();
    }

    private double getCarPrice(Cars car) {
        if (car instanceof NewCars) {
            return ((NewCars) car).getTotalPrice();
        } else if (car instanceof UsedCars) {
            return ((UsedCars) car).getTotalPrice();
        }
        return 0.0;
    }

    private int parseYear(Object yearObj) {
        if (yearObj instanceof Integer) {
            return (Integer) yearObj;
        }

        if (yearObj != null) {
            try {
                return Integer.parseInt(yearObj.toString().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    private JPanel createCarCard(Cars car) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(200, 280));
        imagePanel.setBackground(new Color(235, 240, 248));

        ImageIcon carPhoto = new ImageIcon(car.getImageLink());
        Image rawPhoto = carPhoto.getImage();
        Image rescaledPhoto = rawPhoto.getScaledInstance(
                500, 280, Image.SCALE_SMOOTH);

        JLabel carImage = new JLabel(
                new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        imagePanel.add(carImage, BorderLayout.CENTER);

        if (car.isSoldOut()) {
            JLabel soldOutLabel = new JLabel("SOLD OUT");
            soldOutLabel.setOpaque(true);
            soldOutLabel.setBackground(Color.RED);
            soldOutLabel.setForeground(Color.WHITE);
            soldOutLabel.setFont(new Font("Arial", Font.BOLD, 13));
            soldOutLabel.setBorder(
                    BorderFactory.createEmptyBorder(7, 10, 7, 10));

            JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            badgePanel.setOpaque(false);
            badgePanel.add(soldOutLabel);
            imagePanel.add(badgePanel, BorderLayout.NORTH);
        }

        JLabel condition = new JLabel();
        condition.setFont(new Font("Arial", Font.BOLD, 11));

        JLabel name = new JLabel(
                car.getYearModel() + " " + car.getMake() + " " + car.getModel());
        name.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel category = new JLabel(car.getVehCategory());
        category.setForeground(new Color(80, 90, 105));

        JLabel price = new JLabel();
        price.setFont(new Font("Arial", Font.BOLD, 17));
        price.setForeground(new Color(35, 115, 75));

        if (car instanceof NewCars) {
            NewCars newCar = (NewCars) car;
            condition.setText("BRAND NEW");
            condition.setForeground(new Color(40, 165, 80));
            price.setText("₱" + String.format("%,.0f", newCar.getBasePrice()));
        } else if (car instanceof UsedCars) {
            UsedCars usedCar = (UsedCars) car;
            condition.setText("USED");
            condition.setForeground(new Color(225, 110, 40));
            price.setText("₱" + String.format("%,.0f", usedCar.getBasePrice()));
        }

        JButton detailsButton = new JButton("View Details");
        detailsButton.setBackground(new Color(65, 115, 220));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);

        detailsButton.addActionListener(e -> {
            if (currentRole.equals("Customer")) {
                showCarDetailsPage(car);
            } else if (currentRole.equals("Staff") || currentRole.equals("Admin")) {

                if (car instanceof NewCars) {
                    NewCars newCar = (NewCars) car;
                    JOptionPane.showMessageDialog(
                            this,
                            "Car ID: " + newCar.getVehId()
                            + "\nMake: " + newCar.getMake()
                            + "\nModel: " + newCar.getModel()
                            + "\nYear: " + newCar.getYearModel()
                            + "\nBody Type: " + newCar.getVehCategory()
                            + "\nFuel Type: " + newCar.getFuelType()
                            + "\nNo. of Seats: " + newCar.getSeats()
                            + "\nTransmission Type: " + newCar.getTransmission()
                            + "\nBase Price: " + String.format("₱%,.0f", newCar.getBasePrice())
                            + "\nWarranty: " + newCar.getWarranty() + " year(s)"
                            + "\nMarkups: " + String.format("₱%,.0f", newCar.getDealerMarkup())
                            + "\nTotal Price: " + String.format("₱%,.0f", newCar.getTotalPrice())
                            + "\nIn Stock: " + String.valueOf(newCar.getStockQty()),
                            "Car Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else if (car instanceof UsedCars) {
                    UsedCars usedCar = (UsedCars) car;
                    JOptionPane.showMessageDialog(
                            this,
                            "Car ID: " + usedCar.getVehId()
                            + "\nMake: " + usedCar.getMake()
                            + "\nModel: " + usedCar.getModel()
                            + "\nYear: " + usedCar.getYearModel()
                            + "\nBody Type: " + usedCar.getVehCategory()
                            + "\nColor: " + usedCar.getUsedCarColor()
                            + "\nFuel Type: " + usedCar.getFuelType()
                            + "\nNo. of Seats: " + usedCar.getSeats()
                            + "\nTransmission Type: " + usedCar.getTransmission()
                            + "\nBase Price: " + String.format("₱%,.0f", usedCar.getBasePrice())
                            + "\nNo. of Previous Owners: " + usedCar.getPrevOwnerCount() + " year(s)"
                            + "\nCondition Level: " + String.format("%,.1f", usedCar.getConditionScale()) + "/5.0"
                            + "\nTotal Price: " + String.format("₱%,.0f", usedCar.getTotalPrice()),
                            "Car Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(condition);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(name);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(category);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(price);
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(detailsButton);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void showCarDetailsPage(Cars car) {
        carsPanel.removeAll();
        carsPanel.setLayout(new BorderLayout(0, 15));
        carsPanel.setBackground(new Color(245, 247, 250));

        JPanel titleArea = new JPanel(new BorderLayout());
        titleArea.setOpaque(false);

        breadcrumb.setText("Dashboard  /  Inventory  /  "
                + car.getMake() + " " + car.getModel());

        JPanel topTextArea = new JPanel(new BorderLayout());
        topTextArea.setOpaque(false);
        topTextArea.setBorder(
                BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JLabel titleLabel = new JLabel(car.getMake() + " " + car.getModel());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 35, 45));

        double totalPrice = getCarPrice(car);
        JLabel priceLabel = new JLabel("₱" + String.format("%,.0f", totalPrice));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setForeground(new Color(35, 35, 45));

        topTextArea.add(titleLabel, BorderLayout.WEST);
        topTextArea.add(priceLabel, BorderLayout.EAST);
        titleArea.add(topTextArea, BorderLayout.CENTER);

        JPanel mainContentCard = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContentCard.setBackground(Color.WHITE);
        mainContentCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel imageWrapper = new JPanel(new BorderLayout());
        imageWrapper.setOpaque(false);

        ImageIcon rawPhoto = new ImageIcon(car.getImageLink());
        Image rescaledPhoto = rawPhoto.getImage().getScaledInstance(
                785, 440, Image.SCALE_SMOOTH);
        JLabel bigImageLabel = new JLabel(
                new ImageIcon(rescaledPhoto), SwingConstants.CENTER);
        imageWrapper.add(bigImageLabel, BorderLayout.CENTER);

        JPanel rightDetails = new JPanel();
        rightDetails.setLayout(new BoxLayout(rightDetails, BoxLayout.Y_AXIS));
        rightDetails.setOpaque(false);

        JPanel detailSpecsheetArea = new JPanel(new GridLayout(5, 3, 10, 10));
        detailSpecsheetArea.setOpaque(false);
        detailSpecsheetArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        detailSpecsheetArea.add(createSpecItem("Make:", car.getMake()));
        detailSpecsheetArea.add(createSpecItem("Seats:", String.valueOf(car.getSeats())));
        detailSpecsheetArea.add(createSpecItem("Model:", car.getModel()));
        detailSpecsheetArea.add(createSpecItem("Fuel Type:", car.getFuelType()));
        detailSpecsheetArea.add(createSpecItem("Body Type:", car.getVehCategory()));
        detailSpecsheetArea.add(createSpecItem("Year:", String.valueOf(car.getYearModel())));
        detailSpecsheetArea.add(createSpecItem("Transmission:", car.getTransmission()));
        detailSpecsheetArea.add(createSpecItem("Seller:", car.getSeller()));
        detailSpecsheetArea.add(createSpecItem("Base Price:", String.format("₱%,.0f", car.getBasePrice())));

        rightDetails.add(detailSpecsheetArea);
        rightDetails.add(Box.createVerticalStrut(10));

        JPanel secondarySpecsArea = new JPanel(new GridLayout(1, 3, 5, 5));
        secondarySpecsArea.setBackground(new Color(250, 250, 250));
        secondarySpecsArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        secondarySpecsArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel pricingPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        pricingPanel.setOpaque(false);
        pricingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel totalPricePanel = new JPanel(new GridLayout(3, 1, 0, 0));
        totalPricePanel.setOpaque(false);
        totalPricePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel totalPriceTitle = new JLabel("Total Price:");
        totalPriceTitle.setFont(new Font("Arial", Font.PLAIN, 24));
        totalPriceTitle.setForeground(new Color(130, 135, 145));

        JLabel totalPriceValue = new JLabel();
        totalPriceValue.setFont(new Font("Arial", Font.BOLD, 30));
        totalPriceValue.setForeground(new Color(40, 45, 55));

        totalPricePanel.add(totalPriceTitle);
        totalPricePanel.add(totalPriceValue);

        JPanel downPaymentPanel = new JPanel(new GridLayout(3, 1, 0, 0));
        downPaymentPanel.setOpaque(false);
        downPaymentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel downPaymentTitle = new JLabel("Down Payment:");
        downPaymentTitle.setFont(new Font("Arial", Font.PLAIN, 24));
        downPaymentTitle.setForeground(new Color(130, 135, 145));

        JLabel downPaymentValue = new JLabel();
        downPaymentValue.setFont(new Font("Arial", Font.BOLD, 30));
        downPaymentValue.setForeground(new Color(40, 45, 55));

        downPaymentPanel.add(downPaymentTitle);
        downPaymentPanel.add(downPaymentValue);

        if (car instanceof NewCars) {
            NewCars newCar = (NewCars) car;
            totalPriceValue.setText(String.format("₱%,.0f", newCar.getTotalPrice()));
            downPaymentValue.setText(String.format("₱%,.0f", newCar.getDownPayment()) + "/mo");

            if (newCar.getWarranty() != 0) {
                secondarySpecsArea.add(createSpecItem(
                        "Warranty:", String.valueOf(newCar.getWarranty() + " year(s)")));
            } else {
                secondarySpecsArea.add(createSpecItem("Warranty:", "None"));
            }

            secondarySpecsArea.add(createSpecItem(
                    "Markup:", String.format("₱%,.0f", newCar.getDealerMarkup())));

            rightDetails.add(secondarySpecsArea);

            JPanel colorPickerPanel = new JPanel(new GridLayout(2, 1, 0, 0));
            colorPickerPanel.setOpaque(false);
            colorPickerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            JLabel colorTitleLabel = new JLabel("Select Exterior Color:");
            colorTitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            colorTitleLabel.setForeground(new Color(130, 135, 145));

            JComboBox<String> colorComboBox = new JComboBox<>(newCar.getCarColorPicker());
            colorComboBox.setSelectedItem(newCar.getNewCarColor());
            colorComboBox.setFont(new Font("Arial", Font.BOLD, 16));
            colorComboBox.addActionListener(e -> {
                String chosenColor = (String) colorComboBox.getSelectedItem();
                newCar.setNewCarColor(chosenColor);
            });

            colorPickerPanel.add(colorTitleLabel);
            colorPickerPanel.add(colorComboBox);
            secondarySpecsArea.add(colorPickerPanel);

        } else if (car instanceof UsedCars) {
            UsedCars usedCar = (UsedCars) car;
            totalPriceValue.setText(String.format("₱%,.0f", usedCar.getTotalPrice()));
            downPaymentValue.setText(String.format("₱%,.0f", usedCar.getDownPayment()) + "/mo");

            secondarySpecsArea.add(createSpecItem(
                    "Previous Owner(s):", String.valueOf(usedCar.getPrevOwnerCount())));

            secondarySpecsArea.add(createSpecItem(
                    "Condition:", String.format("%.1f/5.0", usedCar.getConditionScale())));

            secondarySpecsArea.add(createSpecItem(
                    "Exterior Color:", usedCar.getUsedCarColor()));

            rightDetails.add(secondarySpecsArea);
        }

        pricingPanel.add(totalPricePanel);
        pricingPanel.add(downPaymentPanel);

        rightDetails.add(pricingPanel);

        JPanel actionArea = new JPanel(new GridLayout(1, 2, 10, 0));
        actionArea.setOpaque(false);
        actionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton paymentButton = new JButton("Proceed to Payment");
        paymentButton.setBackground(new Color(65, 115, 220));
        paymentButton.setForeground(Color.WHITE);
        paymentButton.setFont(new Font("Arial", Font.BOLD, 13));
        paymentButton.setFocusPainted(false);

        if (car.isSoldOut()) {
            paymentButton.setEnabled(false);
            paymentButton.setText("SOLD OUT");
        } else {
            paymentButton.addActionListener(ev -> {
                if (car.isSoldOut()) {
                    JOptionPane.showMessageDialog(this,
                            "This vehicle has already been sold.");
                    return;
                }

                new TransactionFrame(currentId, currentUser, car).setVisible(true);
            });
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(210, 215, 225));
        backButton.setForeground(new Color(40, 45, 55));
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setFocusPainted(false);
        backButton.addActionListener(ev -> {
            breadcrumb.setText("Dashboard  /  Inventory  /  ");
            displayCars(searchField.getText());
        });

        actionArea.add(paymentButton);
        actionArea.add(backButton);
        rightDetails.add(actionArea);

        mainContentCard.add(imageWrapper);
        mainContentCard.add(rightDetails);

        JPanel containerWrapper = new JPanel(new BorderLayout(0, 15));
        containerWrapper.setOpaque(false);
        containerWrapper.add(mainContentCard, BorderLayout.CENTER);

        carsPanel.add(titleArea, BorderLayout.NORTH);
        carsPanel.add(containerWrapper, BorderLayout.CENTER);
        carsPanel.add(Box.createVerticalStrut(180), BorderLayout.SOUTH);

        carsPanel.revalidate();
        carsPanel.repaint();
    }

    private JPanel createSpecItem(String label, String value) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 0));
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTitle.setForeground(new Color(130, 135, 145));

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Arial", Font.BOLD, 24));
        lblVal.setForeground(new Color(40, 45, 55));

        panel.add(lblTitle);
        panel.add(lblVal);

        return panel;
    }

    private Comparator<Cars> getComparator(String sortOption) {
        if (sortOption == null) {
            return null;
        }

        switch (sortOption) {
            case "Alphabetical (A-Z)":
                return (c1, c2) -> {
                    String name1 = c1.getMake() + " " + c1.getModel();
                    String name2 = c2.getMake() + " " + c2.getModel();
                    return name1.compareToIgnoreCase(name2);
                };

            case "Year: Newest to Oldest":
                return (c1, c2) -> {
                    int yearComparison = Integer.compare(
                            parseYear(c2.getYearModel()),
                            parseYear(c1.getYearModel()));

                    if (yearComparison != 0) {
                        return yearComparison;
                    }

                    String name1 = c1.getMake() + " " + c1.getModel();
                    String name2 = c2.getMake() + " " + c2.getModel();
                    return name1.compareToIgnoreCase(name2);
                };

            case "Year: Oldest to Newest":
                return (c1, c2) -> {
                    int yearComparison = Integer.compare(
                            parseYear(c1.getYearModel()),
                            parseYear(c2.getYearModel()));

                    if (yearComparison != 0) {
                        return yearComparison;
                    }

                    String name1 = c1.getMake() + " " + c1.getModel();
                    String name2 = c2.getMake() + " " + c2.getModel();
                    return name1.compareToIgnoreCase(name2);
                };

            case "Price: Low to High":
                return (c1, c2) -> Double.compare(
                        getCarPrice(c1), getCarPrice(c2));

            case "Price: High to Low":
                return (c1, c2) -> Double.compare(
                        getCarPrice(c2), getCarPrice(c1));

            default:
                return null;
        }
    }
}
