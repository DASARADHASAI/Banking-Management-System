package models;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private String accountNumber;
    private String transactionType;
    private double amount;
    private Timestamp transactionDate;
    private String description;

    public Transaction(int transactionId, String accountNumber, String transactionType, double amount,
            Timestamp transactionDate, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    // Constructor without ID and Date (for creating new transactions)
    public Transaction(String accountNumber, String transactionType, double amount, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }
}
