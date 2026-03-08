package ui;

import dao.AccountDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WithdrawForm extends JFrame {
    private String accountNumber;
    private DashboardForm dashboard;

    public WithdrawForm(String accNum, DashboardForm dash) {
        this.accountNumber = accNum;
        this.dashboard = dash;

        setTitle("Withdraw Funds");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Amount (₹):"));
        JTextField txtAmount = new JTextField(10);
        inputPanel.add(txtAmount);

        JButton btnWithdraw = new JButton("Confirm Withdraw");
        JButton btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnWithdraw);
        buttonPanel.add(btnCancel);

        add(new JLabel("Enter withdrawal amount:", SwingConstants.CENTER));
        add(inputPanel);
        add(buttonPanel);

        // Action Listeners
        btnWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(WithdrawForm.this, "Amount must be greater than zero.");
                        return;
                    }

                    AccountDAO dao = new AccountDAO();
                    if (dao.withdraw(accountNumber, amount)) {
                        JOptionPane.showMessageDialog(WithdrawForm.this, "Withdrawal Successful!");
                        dashboard.refreshData(); // Update dashboard
                        dispose(); // Close form
                    } else {
                        JOptionPane.showMessageDialog(WithdrawForm.this, "Withdrawal Failed. Check your balance.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(WithdrawForm.this, "Invalid amount format.");
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
