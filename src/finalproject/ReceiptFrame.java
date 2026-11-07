package finalproject;

import javax.swing.*;
import java.awt.*;

public class ReceiptFrame extends JFrame {

    public ReceiptFrame(TransactionReceipt receipt) {
        setTitle("DGV Auto Deals - Official Receipt Ticket");
        setSize(480, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel titleText = new JLabel("DGV AUTO DEALS");
        titleText.setFont(new Font("Arial", Font.BOLD, 18));
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleText.setForeground(new Color(25, 35, 55));

        JLabel subtitleText = new JLabel("OFFICIAL TRANSACTION TICKET");
        subtitleText.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleText.setForeground(new Color(110, 115, 125));

        header.add(titleText);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleText);

        JPanel body = new JPanel(new GridLayout(8, 2, 8, 8));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));

        body.add(new JLabel("Transaction ID:"));
        body.add(new JLabel(receipt.getTransactionId()));
        body.add(new JLabel("Customer:"));
        body.add(new JLabel(receipt.getAccountName()));
        body.add(new JLabel("ID / Reference:"));
        body.add(new JLabel(receipt.getCustomerId()));
        body.add(new JLabel("Vehicle Model:"));
        body.add(new JLabel(receipt.getPurchasedCar().toString()));
        body.add(new JLabel("Payment Type:"));
        body.add(new JLabel(receipt.getPaymentMethod()));
        body.add(new JLabel("Timestamp:"));
        body.add(new JLabel(receipt.getTimestamp()));
        body.add(new JLabel("Status:"));
        
        JLabel status = new JLabel("PAID & SETTLED");
        status.setForeground(new Color(40, 165, 80));
        status.setFont(new Font("Arial", Font.BOLD, 12));
        body.add(status);

        body.add(new JLabel("Total Paid:"));
        JLabel amount = new JLabel("₱" + String.format("%,.0f", receipt.getPaidAmount()));
        amount.setFont(new Font("Arial", Font.BOLD, 15));
        amount.setForeground(new Color(35, 115, 75));
        body.add(amount);

        JButton closeBtn = new JButton("Close Receipt");
        closeBtn.setBackground(new Color(65, 115, 220));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 13));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(body, BorderLayout.CENTER);
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
