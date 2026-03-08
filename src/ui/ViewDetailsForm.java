package ui;

import dao.AccountDAO;
import models.Account;

import javax.swing.*;
import java.awt.*;

public class ViewDetailsForm extends JFrame {

    public ViewDetailsForm(String accountNumber) {
        setTitle("Account Details");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        AccountDAO dao = new AccountDAO();
        Account account = dao.getAccount(accountNumber);

        if (account != null) {
            String details = "<html><body><h3>Account Information</h3>" +
                    "<b>Account Number:</b> " + account.getAccountNumber() + "<br>" +
                    "<b>Customer Name:</b> " + account.getCustomerName() + "<br>" +
                    "<b>Current Balance:</b> ₹" + String.format("%.2f", account.getBalance()) +
                    "</body></html>";

            JLabel lblDetails = new JLabel(details, SwingConstants.CENTER);
            add(lblDetails, BorderLayout.CENTER);
        } else {
            add(new JLabel("Error: Could not load account details.", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
