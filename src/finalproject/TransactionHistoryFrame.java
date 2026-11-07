package finalproject;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TransactionHistoryFrame extends JFrame {

    private DefaultTableModel tableModel;
    private String currentUser;
    private String currentRole;
    private JTable table;
    private JLabel fileLabel;

    public TransactionHistoryFrame(String currentUser, String currentRole) {

        this.currentUser = currentUser;
        this.currentRole = currentRole;

        setTitle("DGV - Transaction History");
        setSize(1400, 750);
        setMinimumSize(new Dimension(1100, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 20, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(30, 40, 55));

        fileLabel = new JLabel();
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        fileLabel.setForeground(new Color(100, 110, 125));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(fileLabel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 15));
        refreshButton.setPreferredSize(new Dimension(120, 45));
        refreshButton.setBackground(new Color(65, 115, 220));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> loadTransactions());

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
            "Transaction ID",
            "Date and Time",
            "Customer",
            "ID Ref",
            "Vehicle",
            "Purchase Type",
            "Cost",
            "Amount Paid",
            "Payment Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(42);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(225, 230, 238));
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 48));
        header.setBackground(new Color(35, 48, 70));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

                if (isSelected) {
                    component.setBackground(new Color(220, 232, 255));
                    component.setForeground(new Color(30, 40, 55));
                } else {
                    component.setBackground(
                            row % 2 == 0 ? Color.WHITE
                                    : new Color(245, 247, 250));
                    component.setForeground(new Color(40, 45, 55));
                }

                return component;
            }
        });

        int[] columnWidths = {
            150, 190, 150, 110, 340, 150, 150, 150, 160
        };

        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 25, 25, 25),
                BorderFactory.createLineBorder(
                        new Color(220, 225, 235))));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadTransactions();
    }

    private void loadTransactions() {

        tableModel.setRowCount(0);

        File file = new File("transactions.txt");
        fileLabel.setText("File: " + file.getAbsolutePath());

        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {

                String[] data = scanner.nextLine().split("\\|", -1);

                if (data.length != 7 && data.length != 9) {
                    continue;
                }

                boolean isStaff = currentRole != null
                        && (currentRole.equalsIgnoreCase("Admin")
                        || currentRole.equalsIgnoreCase("Staff"));

                boolean isOwnTransaction = isStaff
                        || (currentUser != null
                        && data[2].equals(currentUser));

                if (!isOwnTransaction) {
                    continue;
                }

                try {
                    String[] row = new String[10];

                    row[0] = data[0];
                    row[1] = data[1];
                    row[2] = data[2];
                    row[3] = data[3];
                    row[4] = data[4];
                    row[5] = data[5];

                    if (data.length == 10) {
                        double amount = Double.parseDouble(data[6]);
                        double amountPaid = Double.parseDouble(data[7]);
                        double balance = Double.parseDouble(data[8]);

                        row[6] = formatMoney(amount);
                        row[7] = formatMoney(amountPaid);
                        row[8] = data[8];

                    } else {
                        double amount = Double.parseDouble(data[6]);
                        double amountPaid = Double.parseDouble(data[7]);

                        row[6] = formatMoney(amount);
                        row[7] = formatMoney(amountPaid);
                        row[8] = "PAID & SETTLED";
                    }

                    tableModel.addRow(row);

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Skipped invalid transaction record.");
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load transaction history.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String formatMoney(double amount) {
        return "₱" + String.format("%,.2f", amount);
    }
}