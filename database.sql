-- Banking Management System Database Script

CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    pin VARCHAR(4) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- e.g., 'DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT'
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);

-- Example dummy data for testing (optional)
-- INSERT INTO accounts (account_number, customer_name, pin, balance) VALUES ('1001', 'John Doe', '1234', 500.00);
-- INSERT INTO accounts (account_number, customer_name, pin, balance) VALUES ('1002', 'Jane Smith', '4321', 1000.00);
