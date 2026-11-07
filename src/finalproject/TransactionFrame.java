package finalproject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TransactionFrame extends JFrame {

    private Cars car;
    private String currentId;
    private String currentUser;
    private JTextField amountInput;
    private double amount;
    private JComboBox<String> paymentType;

    public TransactionFrame(String currentId, String currentUser, Cars car) {
        this.currentId = currentId;
        this.currentUser = currentUser;
        this.car = car;

        setTitle("DGV Auto Deals - Payment Confirmation");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Select Payment Type");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel midPanel = new JPanel(new GridLayout(2, 1, 3, 3));
        midPanel.setOpaque(false);

        JLabel details = new JLabel(
                "<html>"
                + "<b>Vehicle:</b> " + car.getYearModel() + " "
                + car.getMake() + " " + car.getModel()
                + "<br><br>"
                + "<b>Total Price:</b> ₱"
                + String.format("%,.0f", getCarPrice())
                + "<br><br>"
                + "</html>"
        );

        details.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel amountPanel = new JPanel(new GridLayout(2, 1, 3, 5));
        amountPanel.setOpaque(false);

        paymentType = new JComboBox<>(
                new String[]{"Full Payment", "Down Payment"}
        );

        paymentType.setBorder(
                BorderFactory.createTitledBorder("Payment Type:"));

        amountInput = new JTextField();
        amountInput.setBorder(
                BorderFactory.createTitledBorder("Amount to Pay (PHP):"));

        updatePaymentAmount();

        paymentType.addActionListener(e -> updatePaymentAmount());

        amountPanel.add(paymentType);
        amountPanel.add(amountInput);

        JButton cancelButton = new JButton("No, Cancel");
        JButton confirmButton = new JButton("Yes, Continue");

        confirmButton.setBackground(new Color(65, 115, 220));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setOpaque(false);
        buttons.add(cancelButton);
        buttons.add(confirmButton);

        cancelButton.addActionListener(e -> dispose());
        confirmButton.addActionListener(e -> confirmPurchase());

        midPanel.add(details);
        midPanel.add(amountPanel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(midPanel, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        add(panel);
    }

    private void updatePaymentAmount() {
        double price = getCarPrice();

        if ("Down Payment".equals(paymentType.getSelectedItem())) {
            if (car instanceof NewCars) {
                amount = price * 0.20;
            } else if (car instanceof UsedCars) {
                amount = price * 0.15;
            } else {
                amount = 0;
            }
        } else {
            amount = price;
        }

        amountInput.setText(String.format("%,.2f", amount));
    }

    private void confirmPurchase() {

        if (car.isSoldOut()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This vehicle has already been sold.",
                    "Purchase Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            dispose();
            return;
        }

        double price = getCarPrice();
        String selectedPayment = (String) paymentType.getSelectedItem();

        double amountPaid;

        try {
            amountPaid = Double.parseDouble(
                    amountInput.getText().trim().replace(",", "")
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid payment amount.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (amountPaid <= 0 || amountPaid > price) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid payment amount.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double balance = price - amountPaid;
        boolean isFullPayment = balance <= 0.01;

        String finance = isFullPayment
                ? "Full Purchase"
                : "Down Payment";

        String status = isFullPayment
                ? "PAID & SETTLED"
                : "DOWN PAYMENT";

        String message = isFullPayment
                ? "Are you sure you want to purchase this car?"
                : "Confirm down payment?\n\n"
                + "\nAmount to Pay: ₱"
                + String.format("%,.2f", amountPaid);

        int choice = JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirm Payment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (car.isSoldOut()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This vehicle is no longer available.",
                    "Purchase Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            dispose();
            return;
        }

        TransactionReceipt receipt = new TransactionReceipt(
                currentId,
                car,
                currentUser,
                finance,
                amountPaid
        );
        
        if (!saveTransaction(receipt, amount, amountPaid, status)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save the transaction.\n"
                    + "Check the NetBeans Output window for the error.",
                    "Transaction Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (isFullPayment) {
            car.decreaseStock();
        }

        JOptionPane.showMessageDialog(
                this,
                isFullPayment
                        ? "Purchase successful!"
                        : "Down payment successful!\n"
                        + "Amount Paid: ₱"
                        + String.format("%,.2f", amountPaid),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        new ReceiptFrame(receipt).setVisible(true);
        dispose();
    }
    
    private boolean saveTransaction(
            TransactionReceipt receipt,
            double amount,
            double amountPaid,
            String status) {

        String vehicle = car.getYearModel() + " " + car.getMake() + " " + car.getModel() + " (" + getCarColor() + ")";

        File file = new File("transactions.txt");

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(file, true))) {

            writer.println(
                    clean(receipt.getTransactionId()) + "|"
                    + clean(receipt.getTimestamp()) + "|"
                    + clean(currentUser) + "|"
                    + clean(currentId) + "|"
                    + clean(vehicle) + "|"
                    + clean(receipt.getPaymentMethod()) + "|"
                    + amount + "|"
                    + amountPaid + "|"
                    + status
            );

            System.out.println(
                    "Transaction saved to: " + file.getAbsolutePath());

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("|", "/")
                .replace("\n", " ")
                .replace("\r", " ");
    }

    private double getCarPrice() {

        if (car instanceof NewCars) {
            return ((NewCars) car).getTotalPrice();
        }

        if (car instanceof UsedCars) {
            return ((UsedCars) car).getTotalPrice();
        }

        return 0.0;
    }
    
    private String getCarColor() {

        if (car instanceof NewCars) {
            return ((NewCars) car).getNewCarColor();
        }

        if (car instanceof UsedCars) {
            return ((UsedCars) car).getUsedCarColor();
        }

        return "";
    }
}