package finalproject;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryManagerFrame extends JFrame {
    private ArrayList<Cars> carsList;
    private Runnable onUpdate;
    private JTable table;
    private DefaultTableModel tableModel;
    private String[][] carColorsList = {
        {"Red", "Blue", "White", "Black", "Grey", "Silver", "Orange", "Green", "Brown"},
        {"Navy Blue", "Wine Red", "Silver", "Grey"},
        {"Yellow","Bright Red","Cyan","Lime Green","Bright Blue","Purple","Pink"},
        {"White","Black","Grey"}
        };

    public InventoryManagerFrame(ArrayList<Cars> carsList, Runnable onUpdate) {
        this.carsList = carsList;
        this.onUpdate = onUpdate;

        setTitle("DGV Auto Deals - Inventory Manager (CRUD)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        JLabel titleLabel = new JLabel("Inventory Manager (CRUD System)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 27));
        titleLabel.setForeground(new Color(30, 35, 45));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table Setup (Read)
        String[] columns = {"ID", "Condition", "Make", "Model", "Year", "Category", "Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action Buttons Panel (CRUD Operations)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addCarButton = new JButton("Create (New Car)");
        styleButton(addCarButton, new Color(65, 115, 220), Color.WHITE);
        addCarButton.addActionListener(e -> openCarDialog(null, false));

        JButton addUsedCarButton = new JButton("Create (Used Car)");
        styleButton(addUsedCarButton, new Color(225, 110, 40), Color.WHITE);
        addUsedCarButton.addActionListener(e -> openCarDialog(null, true));

        JButton editButton = new JButton("Update (Edit Car)");
        styleButton(editButton, new Color(40, 150, 90), Color.WHITE);
        editButton.addActionListener(e -> editCar());

        JButton toggleSoldButton = new JButton("Toggle Status");
        styleButton(toggleSoldButton, new Color(90, 100, 115), Color.WHITE);
        toggleSoldButton.addActionListener(e -> toggleSoldStatus());

        JButton deleteButton = new JButton("Delete Car");
        styleButton(deleteButton, new Color(200, 50, 50), Color.WHITE);
        deleteButton.addActionListener(e -> deleteCar());

        JButton closeButton = new JButton("Close");
        styleButton(closeButton, new Color(210, 215, 225), new Color(40, 45, 55));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(addCarButton);
        buttonPanel.add(addUsedCarButton);
        buttonPanel.add(editButton);
        buttonPanel.add(toggleSoldButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Cars car : carsList) {
            double price = (car instanceof NewCars) ? ((NewCars) car).getTotalPrice() : ((UsedCars) car).getTotalPrice();
            String status = car.isSoldOut() ? "Sold Out" : "Available";

            Object[] row = {
                car.getVehId(),
                car.getMake(),
                car.getModel(),
                car.getYearModel(),
                car.getVehCategory(),
                String.format("₱%,.0f", price),
                status
            };
            tableModel.addRow(row);
        }
    }

    private void openCarDialog(Cars carToEdit, boolean paramIsUsed) {
        boolean isEditMode = (carToEdit != null);
        final boolean isUsed = isEditMode ? (carToEdit instanceof UsedCars) : paramIsUsed;

        JDialog dialog = new JDialog(this, isEditMode ? (isUsed ? "Edit Used Car" : "Edit Brand New Car") : (isUsed ? "Add Used Car" : "Add Brand New Car"), true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField makeField = new JTextField(isEditMode ? carToEdit.getMake() : "");
        JTextField modelField = new JTextField(isEditMode ? carToEdit.getModel() : "");
        JTextField yearField = new JTextField(isEditMode ? String.valueOf(carToEdit.getYearModel()) : "");
        JTextField categoryField = new JTextField(isEditMode ? carToEdit.getVehCategory() : "");
        JTextField fuelField = new JTextField(isEditMode ? carToEdit.getFuelType() : "");
        JTextField seatsField = new JTextField(isEditMode ? String.valueOf(carToEdit.getSeats()) : "");
        JTextField transmissionField = new JTextField(isEditMode ? carToEdit.getTransmission() : "");
        JTextField basePriceField = new JTextField(isEditMode ? String.valueOf(carToEdit.getBasePrice()) : "");
        JTextField imageField = new JTextField(isEditMode ? carToEdit.getImageLink() : "C:\\Users\\user\\Documents\\NetBeansProjects\\DeGuzmanJoelVardeleonVonAllenOOP\\assetimages\\defaultcar.jpg");
        JTextField sellerField = new JTextField(isEditMode ? carToEdit.getSeller() : "");

        formPanel.add(new JLabel("Make:")); formPanel.add(makeField);
        formPanel.add(new JLabel("Model:")); formPanel.add(modelField);
        formPanel.add(new JLabel("Year Model:")); formPanel.add(yearField);
        formPanel.add(new JLabel("Body Category:")); formPanel.add(categoryField);
        formPanel.add(new JLabel("Fuel Type:")); formPanel.add(fuelField);
        formPanel.add(new JLabel("Seats:")); formPanel.add(seatsField);
        formPanel.add(new JLabel("Transmission:")); formPanel.add(transmissionField);
        formPanel.add(new JLabel("Base Price (₱):")); formPanel.add(basePriceField);
        formPanel.add(new JLabel("Image Link:")); formPanel.add(imageField);
        formPanel.add(new JLabel("Seller:")); formPanel.add(sellerField);

        JTextField extra1Field = new JTextField();
        JTextField extra2Field = new JTextField();
        JTextField extra3Field = new JTextField();
        JTextField extra4Field = new JTextField();

        if (isUsed) {
            formPanel.add(new JLabel("Prev Owners:")); formPanel.add(extra1Field);
            formPanel.add(new JLabel("Condition (1-5):")); formPanel.add(extra2Field);
            formPanel.add(new JLabel("Color: ")); formPanel.add(extra3Field);
            if (isEditMode) {
                UsedCars uc = (UsedCars) carToEdit;
                extra1Field.setText(String.valueOf(uc.getPrevOwnerCount()));
                extra2Field.setText(String.valueOf(uc.getConditionScale()));
                extra3Field.setText(uc.getUsedCarColor());
            }
        } else {
            formPanel.add(new JLabel("Warranty (Years):")); formPanel.add(extra1Field);
            formPanel.add(new JLabel("Dealer Markup (₱):")); formPanel.add(extra2Field);
            formPanel.add(new JLabel("Color Group (1-4): ")); formPanel.add(extra3Field);
            formPanel.add(new JLabel("Stock Quantity: ")); formPanel.add((extra3Field));
            if (isEditMode) {
                NewCars nc = (NewCars) carToEdit;
                extra1Field.setText(String.valueOf(nc.getWarranty()));
                extra2Field.setText(String.valueOf(nc.getDealerMarkup()));
                extra3Field.setText("");
                extra4Field.setText(String.valueOf(nc.getStockQty()));
            }
        }

        JButton saveButton = new JButton(isEditMode ? "Update Car" : "Save Car");
        styleButton(saveButton, new Color(65, 115, 220), Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                String make = makeField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String category = categoryField.getText().trim();
                String fuel = fuelField.getText().trim();
                int seats = Integer.parseInt(seatsField.getText().trim());
                String transmission = transmissionField.getText().trim();
                double basePrice = Double.parseDouble(basePriceField.getText().trim());
                String image = imageField.getText().trim();
                String seller = sellerField.getText().trim();

                Cars updatedCar;
                if (isUsed) {
                    int prevOwners = Integer.parseInt(extra1Field.getText().trim());
                    double conditionScale = Double.parseDouble(extra2Field.getText().trim());
                    String color = extra3Field.getText().trim();
                    updatedCar = new UsedCars(make, model, year, category, basePrice, fuel, seats, transmission, color, prevOwners, conditionScale, image, seller, 1);
                } else {
                    int warranty = Integer.parseInt(extra1Field.getText().trim());
                    double markup = Double.parseDouble(extra2Field.getText().trim());
                    int index = Integer.parseInt(extra3Field.getText().trim());
                    int stock = Integer.parseInt(extra4Field.getText().trim());
                    updatedCar = new NewCars(make, model, year, category, basePrice, fuel, seats, transmission, carColorsList[index], markup, warranty, image, seller, stock);
                }

                if (isEditMode && carToEdit.isSoldOut()) {
                    updatedCar.setSoldOut(true);
                }

                if (isEditMode) {
                    int selectedIndex = table.getSelectedRow();
                    carsList.set(selectedIndex, updatedCar);
                    JOptionPane.showMessageDialog(this, "Car updated successfully!");
                } else {
                    carsList.add(updatedCar);
                    JOptionPane.showMessageDialog(this, "Car added successfully!");
                }

                refreshTable();
                if (onUpdate != null) onUpdate.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for numeric fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editCar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car from the table to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cars car = carsList.get(selectedRow);
        openCarDialog(car, car instanceof UsedCars);
    }

    private void toggleSoldStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car from the table.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cars car = carsList.get(selectedRow);
        car.setSoldOut(!car.isSoldOut());
        refreshTable();
        if (onUpdate != null) onUpdate.run();
    }

    private void deleteCar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car from the table.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            carsList.remove(selectedRow);
            refreshTable();
            if (onUpdate != null) onUpdate.run();
        }
    }
}