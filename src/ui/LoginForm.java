package ui;

import dao.AccountDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField txtAccountNumber;
    private JPasswordField txtPin;
    private JButton btnLogin;
    private JButton btnCreateAccount;

    public LoginForm() {
        setTitle("Banking Management System - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(4, 2, 10, 10)); // Grid layout for simplicity

        // Components
        JLabel lblAccountNumber = new JLabel("Account Number:", SwingConstants.CENTER);
        txtAccountNumber = new JTextField();

        JLabel lblPin = new JLabel("PIN:", SwingConstants.CENTER);
        txtPin = new JPasswordField();

        btnLogin = new JButton("Login");
        btnCreateAccount = new JButton("Create New Account");

        // Add to frame
        add(lblAccountNumber);
        add(txtAccountNumber);
        add(lblPin);
        add(txtPin);
        add(new JLabel("")); // Empty label for spacing
        add(btnLogin);
        add(new JLabel(""));
        add(btnCreateAccount);

        // Action Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        btnCreateAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccountForm().setVisible(true);
                dispose(); // Close current window
            }
        });
    }

    private void login() {
        String accountNumber = txtAccountNumber.getText();
        String pin = new String(txtPin.getPassword());

        if (accountNumber.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both account number and PIN.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        AccountDAO dao = new AccountDAO();
        if (dao.login(accountNumber, pin)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new DashboardForm(accountNumber).setVisible(true);
            this.dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Account Number or PIN.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
