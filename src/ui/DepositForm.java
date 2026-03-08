package ui;

import dao.AccountDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepositForm extends JFrame {
    private String accountNumber;
    private DashboardForm dashboard; // Reference to update dashboard balance

    public DepositForm(String accNum, DashboardForm dash) {
        this.accountNumber = accNum;
        this.dashboard = dash;

        setTitle("Deposit Funds");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Amount (₹):"));
        JTextField txtAmount = new JTextField(10);
        inputPanel.add(txtAmount);

        JButton btnDeposit = new JButton("Confirm Deposit");
        JButton btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnCancel);

        add(new JLabel("Enter deposit amount:", SwingConstants.CENTER));
        add(inputPanel);
        add(buttonPanel);

        // Action Listeners
        btnDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(DepositForm.this, "Amount must be greater than zero.");
                        return;
                    }

                    AccountDAO dao = new AccountDAO();
                    if (dao.deposit(accountNumber, amount)) {
                        JOptionPane.showMessageDialog(DepositForm.this, "Deposit Successful!");
                        dashboard.refreshData(); // Update dashboard
                        dispose(); // Close form
                    } else {
                        JOptionPane.showMessageDialog(DepositForm.this, "Deposit Failed.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DepositForm.this, "Invalid amount format.");
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
