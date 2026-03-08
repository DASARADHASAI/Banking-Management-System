package ui;

import models.Account;
import dao.AccountDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardForm extends JFrame {
    private String accountNumber;
    private JLabel lblWelcome;
    private JLabel lblBalance;

    public DashboardForm(String accountNumber) {
        this.accountNumber = accountNumber;

        setTitle("Banking Management System - Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        lblWelcome = new JLabel("Welcome, Loading...", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblBalance = new JLabel("Current Balance: ₹0.00", SwingConstants.CENTER);
        headerPanel.add(lblWelcome);
        headerPanel.add(lblBalance);
        add(headerPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnDeposit = new JButton("Deposit");
        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnTransfer = new JButton("Transfer");
        JButton btnHistory = new JButton("Transaction History");
        JButton btnViewDetails = new JButton("View Details");
        JButton btnLogout = new JButton("Logout");

        buttonsPanel.add(btnDeposit);
        buttonsPanel.add(btnWithdraw);
        buttonsPanel.add(btnTransfer);
        buttonsPanel.add(btnHistory);
        buttonsPanel.add(btnViewDetails);
        buttonsPanel.add(btnLogout);

        add(buttonsPanel, BorderLayout.CENTER);

        // Load account data
        refreshData();

        // Action Listeners
        btnDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DepositForm(accountNumber, DashboardForm.this).setVisible(true);
            }
        });

        btnWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WithdrawForm(accountNumber, DashboardForm.this).setVisible(true);
            }
        });

        btnTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransferForm(accountNumber, DashboardForm.this).setVisible(true);
            }
        });

        btnHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransactionHistoryForm(accountNumber).setVisible(true);
            }
        });

        btnViewDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewDetailsForm(accountNumber).setVisible(true);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });
    }

    public void refreshData() {
        AccountDAO dao = new AccountDAO();
        Account account = dao.getAccount(accountNumber);
        if (account != null) {
            lblWelcome.setText("Welcome, " + account.getCustomerName() + "!");
            lblBalance.setText("Current Balance: ₹" + String.format("%.2f", account.getBalance()));
        }
    }
}
