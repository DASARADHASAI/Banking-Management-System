package ui;

import dao.AccountDAO;
import models.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

public class CreateAccountForm extends JFrame {
    private JTextField txtName;
    private JPasswordField txtPin;
    private JPasswordField txtConfirmPin;
    private JTextField txtInitialDeposit;
    private JButton btnCreate;
    private JButton btnCancel;

    public CreateAccountForm() {
        setTitle("Create New Account");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Components
        add(new JLabel("Full Name:", SwingConstants.CENTER));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("4-Digit PIN:", SwingConstants.CENTER));
        txtPin = new JPasswordField();
        add(txtPin);

        add(new JLabel("Confirm PIN:", SwingConstants.CENTER));
        txtConfirmPin = new JPasswordField();
        add(txtConfirmPin);

        add(new JLabel("Initial Deposit (₹):", SwingConstants.CENTER));
        txtInitialDeposit = new JTextField();
        add(txtInitialDeposit);

        btnCreate = new JButton("Create");
        btnCancel = new JButton("Cancel");

        add(btnCreate);
        add(btnCancel);

        // Action Listeners
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });
    }

    private void createAccount() {
        String name = txtName.getText();
        String pin = new String(txtPin.getPassword());
        String confirmPin = new String(txtConfirmPin.getPassword());
        String depositStr = txtInitialDeposit.getText();

        if (name.isEmpty() || pin.isEmpty() || depositStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (pin.length() != 4 || !pin.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits.");
            return;
        }

        if (!pin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this, "PINs do not match.");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(depositStr);
            if (initialDeposit < 0) {
                JOptionPane.showMessageDialog(this, "Deposit cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid deposit amount.");
            return;
        }

        // Generate a random 6-digit account number that is unique
        AccountDAO dao = new AccountDAO();
        String accountNumber;
        do {
            accountNumber = String.format("%06d", new Random().nextInt(999999));
        } while (dao.getAccount(accountNumber) != null); // Ensure uniqueness

        Account newAccount = new Account(accountNumber, name, pin, initialDeposit);

        if (dao.createAccount(newAccount)) {
            // Enhanced success message to make it clearer for testing
            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\n\n" +
                            "IMPORTANT: Your Account Number is [ " + accountNumber + " ]\n" +
                            "Please save this number for login.",
                    "Account Created", JOptionPane.INFORMATION_MESSAGE);
            new LoginForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.");
        }
    }
}
