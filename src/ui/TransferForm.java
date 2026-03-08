package ui;

import dao.AccountDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferForm extends JFrame {
    private String accountNumber;
    private DashboardForm dashboard;

    public TransferForm(String accNum, DashboardForm dash) {
        this.accountNumber = accNum;
        this.dashboard = dash;

        setTitle("Transfer Funds");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JPanel targetPanel = new JPanel(new FlowLayout());
        targetPanel.add(new JLabel("Recipient Account:"));
        JTextField txtTargetAccount = new JTextField(15);
        targetPanel.add(txtTargetAccount);

        JPanel amountPanel = new JPanel(new FlowLayout());
        amountPanel.add(new JLabel("Amount (₹):"));
        JTextField txtAmount = new JTextField(10);
        amountPanel.add(txtAmount);

        JButton btnTransfer = new JButton("Confirm Transfer");
        JButton btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnTransfer);
        buttonPanel.add(btnCancel);

        add(new JLabel("Enter transfer details:", SwingConstants.CENTER));
        add(targetPanel);
        add(amountPanel);
        add(buttonPanel);

        // Action Listeners
        btnTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetAccount = txtTargetAccount.getText();
                if (targetAccount.isEmpty()) {
                    JOptionPane.showMessageDialog(TransferForm.this, "Please enter recipient account number.");
                    return;
                }

                if (targetAccount.equals(accountNumber)) {
                    JOptionPane.showMessageDialog(TransferForm.this, "Cannot transfer to your own account.");
                    return;
                }

                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(TransferForm.this, "Amount must be greater than zero.");
                        return;
                    }

                    AccountDAO dao = new AccountDAO();
                    if (dao.transfer(accountNumber, targetAccount, amount)) {
                        JOptionPane.showMessageDialog(TransferForm.this, "Transfer Successful!");
                        dashboard.refreshData(); // Update dashboard
                        dispose(); // Close form
                    } else {
                        JOptionPane.showMessageDialog(TransferForm.this,
                                "Transfer Failed. Check balance or recipient account number.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TransferForm.this, "Invalid amount format.");
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
