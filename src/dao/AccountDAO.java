package dao;

import database.DatabaseConnection;
import models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Transaction;

public class AccountDAO {

    /**
     * Creates a new account in the database.
     * 
     * @return true if successful, false otherwise.
     */
    public boolean createAccount(Account account) {
        String query = "INSERT INTO accounts (account_number, customer_name, pin, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getCustomerName());
            pstmt.setString(3, account.getPin());
            pstmt.setDouble(4, account.getBalance());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates login credentials.
     * 
     * @return true if credentials are correct.
     */
    public boolean login(String accountNumber, String pin) {
        String query = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pin);

            ResultSet rs = pstmt.executeQuery();
            // If there's at least one result, login is successful
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves an account from the database.
     */
    public Account getAccount(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getString("account_number"),
                        rs.getString("customer_name"),
                        rs.getString("pin"),
                        rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deposits money into an account.
     */
    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0)
            return false;

        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                insertTransaction(accountNumber, "DEPOSIT", amount, "Deposited to account");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Withdraws money from an account.
     */
    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0)
            return false;

        Account acc = getAccount(accountNumber);
        if (acc == null || acc.getBalance() < amount) {
            return false; // Insufficient balance or account not found
        }

        String query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                insertTransaction(accountNumber, "WITHDRAWAL", amount, "Withdrawn from account");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Transfers money between two accounts.
     */
    public boolean transfer(String fromAccount, String toAccount, double amount) {
        if (amount <= 0)
            return false;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check balance and withdraw from sender
            Account sender = getAccount(fromAccount);
            if (sender == null || sender.getBalance() < amount) {
                conn.rollback();
                return false;
            }

            String withdrawQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawQuery)) {
                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setString(2, fromAccount);
                withdrawStmt.executeUpdate();
            }

            // 2. Deposit to receiver
            String depositQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement depositStmt = conn.prepareStatement(depositQuery)) {
                depositStmt.setDouble(1, amount);
                depositStmt.setString(2, toAccount);
                int result = depositStmt.executeUpdate();

                if (result == 0) {
                    // Receiver account might not exist
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); // Commit transaction

            // Record transactions after commit
            insertTransaction(fromAccount, "TRANSFER_OUT", amount, "Transfer to " + toAccount);
            insertTransaction(toAccount, "TRANSFER_IN", amount, "Transfer from " + fromAccount);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset to default
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Helper method to insert a transaction record.
     */
    private void insertTransaction(String accountNumber, String type, double amount, String description) {
        String query = "INSERT INTO transactions (account_number, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accountNumber);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the transaction history for an account.
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("account_number"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_date"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
