package ui;

import dao.AccountDAO;
import models.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionHistoryForm extends JFrame {

    public TransactionHistoryForm(String accountNumber) {
        setTitle("Transaction History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblHeader = new JLabel("Transaction History for Account: " + accountNumber, SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 16));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblHeader, BorderLayout.NORTH);

        // Table definition
        String[] columns = { "Date & Time", "Type", "Description", "Amount (₹)" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells uneditable
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Load data from DAO
        AccountDAO dao = new AccountDAO();
        List<Transaction> transactions = dao.getTransactionHistory(accountNumber);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Transaction t : transactions) {
            String dateFormatted = t.getTransactionDate() != null ? sdf.format(t.getTransactionDate()) : "N/A";
            String amountStr = (t.getTransactionType().contains("WITHDRAWAL")
                    || t.getTransactionType().equals("TRANSFER_OUT") ? "-" : "+")
                    + String.format("%.2f", t.getAmount());

            Object[] row = {
                    dateFormatted,
                    t.getTransactionType(),
                    t.getDescription(),
                    amountStr
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
