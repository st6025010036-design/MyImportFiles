package com.transaction.service;

import com.transaction.model.*;
import com.transaction.interfaces.*;
import java.util.*;

public class BankService implements Transferable, Requestable, Authenticatable, Searchable {
    
    private Map<String, Customer> customerDatabase = new HashMap<>();
    private Map<String, List<Transaction>> transactionHistory = new HashMap<>();
    private List<MoneyRequest> moneyRequests = new ArrayList<>();
    private Customer currentLoggedInCustomer = null;
    
    // ========== AUTHENTICATABLE INTERFACE METHODS ==========
    
    @Override
    public boolean login(String username, String password) {
        Customer customer = customerDatabase.get(username);
        if (customer == null) {
            System.out.println("[ERROR] Username not found!");
            return false;
        }
        if (customer.verifyPassword(password)) {
            currentLoggedInCustomer = customer;
            System.out.println("\n[OK] Welcome back, " + customer.getFullName() + "!");
            return true;
        } else {
            System.out.println("[ERROR] Incorrect password!");
            return false;
        }
    }
    
    @Override
    public void logout() {
        if (currentLoggedInCustomer != null) {
            System.out.println("\n[OK] Goodbye, " + currentLoggedInCustomer.getFullName() + "!");
            currentLoggedInCustomer = null;
        }
    }
    
    @Override
    public boolean verifyPassword(String password) {
        if (currentLoggedInCustomer == null) return false;
        return currentLoggedInCustomer.verifyPassword(password);
    }
    
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (currentLoggedInCustomer != null) {
            currentLoggedInCustomer.changePassword(oldPassword, newPassword);
        } else {
            System.out.println("[ERROR] Please login first!");
        }
    }
    
    @Override
    public Customer getCurrentCustomer() {
        return currentLoggedInCustomer;
    }
    
    @Override
    public boolean isLoggedIn() {
        return currentLoggedInCustomer != null;
    }
    
    // ========== TRANSFERABLE INTERFACE METHODS ==========
    
    @Override
    public boolean sendMoney(String toUsername, double amount, String description) {
        if (currentLoggedInCustomer == null) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        if (currentLoggedInCustomer.getUsername().equals(toUsername)) {
            System.out.println("[ERROR] Cannot send money to yourself!");
            return false;
        }
        
        Customer recipient = customerDatabase.get(toUsername);
        if (recipient == null) {
            System.out.println("[ERROR] Recipient '" + toUsername + "' not found!");
            return false;
        }
        if (amount <= 0) {
            System.out.println("[ERROR] Amount must be greater than $0!");
            return false;
        }
        
        boolean success = currentLoggedInCustomer.withdraw(amount);
        if (!success) {
            System.out.printf("[ERROR] Insufficient balance! Your balance: $%,.2f%n", 
                currentLoggedInCustomer.getBalance());
            return false;
        }
        
        recipient.deposit(amount);
        
        Transaction senderTx = new Transaction(currentLoggedInCustomer, recipient, 
            amount, description, TransactionType.SEND);
        Transaction recipientTx = new Transaction(currentLoggedInCustomer, recipient, 
            amount, description, TransactionType.RECEIVE);
        
        transactionHistory.get(currentLoggedInCustomer.getUsername()).add(senderTx);
        transactionHistory.get(recipient.getUsername()).add(recipientTx);
        
        System.out.println("\n+-----------------------------------------+");
        System.out.println("|           TRANSACTION RECEIPT           |");
        System.out.println("+-----------------------------------------+");
        System.out.printf("| Amount:      $%,.2f%n", amount);
        System.out.println("| From:        " + currentLoggedInCustomer.getFullName());
        System.out.println("| To:          " + recipient.getFullName());
        System.out.println("| Description: " + description);
        System.out.println("| Status:      [OK] COMPLETED");
        System.out.printf("| New Balance: $%,.2f%n", currentLoggedInCustomer.getBalance());
        System.out.println("+-----------------------------------------+");
        return true;
    }
    
    @Override
    public boolean receiveMoney(Customer fromCustomer, double amount, String description) {
        if (currentLoggedInCustomer == null) {
            System.out.println("[ERROR] Please login first!");
            return false;
        }
        currentLoggedInCustomer.deposit(amount);
        Transaction tx = new Transaction(fromCustomer, currentLoggedInCustomer, 
            amount, description, TransactionType.RECEIVE);
        transactionHistory.get(currentLoggedInCustomer.getUsername()).add(tx);
        return true;
    }
    
    // ========== REQUESTABLE INTERFACE METHODS ==========
    
    @Override
    public void requestMoney(String fromUsername, double amount, String reason) {
        if (currentLoggedInCustomer == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        if (currentLoggedInCustomer.getUsername().equals(fromUsername)) {
            System.out.println("[ERROR] Cannot request money from yourself!");
            return;
        }
        
        Customer target = customerDatabase.get(fromUsername);
        if (target == null) {
            System.out.println("[ERROR] User '" + fromUsername + "' not found!");
            return;
        }
        if (amount <= 0) {
            System.out.println("[ERROR] Amount must be greater than $0!");
            return;
        }
        
        MoneyRequest request = new MoneyRequest(currentLoggedInCustomer, target, amount, reason);
        moneyRequests.add(request);
        
        System.out.println("\n+-----------------------------------------+");
        System.out.println("|           MONEY REQUEST SENT            |");
        System.out.println("+-----------------------------------------+");
        System.out.printf("| Requested:   $%,.2f%n", amount);
        System.out.println("| From:        " + target.getFullName());
        System.out.println("| To:          " + currentLoggedInCustomer.getFullName());
        System.out.println("| Reason:      " + reason);
        System.out.println("| Request ID:  " + request.getRequestId());
        System.out.println("| Status:      PENDING");
        System.out.println("+-----------------------------------------+");
    }
    
    @Override
    public boolean approveRequest(int requestId) {
        for (MoneyRequest req : moneyRequests) {
            if (req.getRequestId() == requestId) {
                if (req.getTarget().equals(currentLoggedInCustomer)) {
                    boolean approved = req.approve();
                    if (approved) {
                        Transaction tx = new Transaction(req.getTarget(), req.getRequester(),
                            req.getAmount(), "REQUEST APPROVED: " + req.getReason(), TransactionType.SEND);
                        transactionHistory.get(req.getTarget().getUsername()).add(tx);
                        transactionHistory.get(req.getRequester().getUsername()).add(
                            new Transaction(req.getTarget(), req.getRequester(), req.getAmount(),
                            "REQUEST FULFILLED: " + req.getReason(), TransactionType.RECEIVE));
                        System.out.println("[OK] Request #" + requestId + " approved!");
                        return true;
                    }
                } else {
                    System.out.println("[ERROR] You cannot approve this request!");
                    return false;
                }
                return false;
            }
        }
        System.out.println("[ERROR] Request #" + requestId + " not found!");
        return false;
    }
    
    @Override
    public void rejectRequest(int requestId) {
        for (MoneyRequest req : moneyRequests) {
            if (req.getRequestId() == requestId) {
                if (req.getTarget().equals(currentLoggedInCustomer)) {
                    req.reject();
                } else {
                    System.out.println("[ERROR] You cannot reject this request!");
                }
                return;
            }
        }
        System.out.println("[ERROR] Request #" + requestId + " not found!");
    }
    
    @Override
    public void showPendingRequests() {
        boolean found = false;
        System.out.println("\n=== PENDING MONEY REQUESTS ===");
        for (MoneyRequest req : moneyRequests) {
            if (req.getStatus() == MoneyRequest.RequestStatus.PENDING) {
                req.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No pending requests.");
        }
    }
    
    // ========== SEARCHABLE INTERFACE METHODS ==========
    
    @Override
    public Customer findCustomerByUsername(String username) {
        return customerDatabase.get(username);
    }
    
    @Override
    public List<Transaction> findTransactionsByCustomer(String username) {
        return transactionHistory.getOrDefault(username, new ArrayList<>());
    }
    
    @Override
    public List<Transaction> findTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> result = new ArrayList<>();
        for (List<Transaction> list : transactionHistory.values()) {
            for (Transaction t : list) {
                String txDate = t.getTimestamp().split(" ")[0];
                if (txDate.compareTo(startDate) >= 0 && txDate.compareTo(endDate) <= 0) {
                    result.add(t);
                }
            }
        }
        return result;
    }
    
    @Override
    public List<Transaction> findTransactionsByAmountRange(double minAmount, double maxAmount) {
        List<Transaction> result = new ArrayList<>();
        for (List<Transaction> list : transactionHistory.values()) {
            for (Transaction t : list) {
                if (t.getAmount() >= minAmount && t.getAmount() <= maxAmount) {
                    result.add(t);
                }
            }
        }
        return result;
    }
    
    // ========== REGISTRATION AND VIEW METHODS ==========
    
    public boolean registerCustomer(String username, String fullName, String phoneNumber, 
                                     double initialDeposit, String password) {
        if (customerDatabase.containsKey(username)) {
            System.out.println("[ERROR] Username already exists!");
            return false;
        }
        if (!phoneNumber.matches("\\d{9,12}")) {
            System.out.println("[ERROR] Invalid phone number!");
            return false;
        }
        if (initialDeposit < 0) {
            System.out.println("[ERROR] Initial deposit cannot be negative!");
            return false;
        }
        if (password == null || password.length() < 4) {
            System.out.println("[ERROR] Password must be at least 4 characters!");
            return false;
        }
        
        Customer newCustomer = new Customer(username, fullName, phoneNumber, initialDeposit, password);
        customerDatabase.put(username, newCustomer);
        transactionHistory.put(username, new ArrayList<>());
        
        System.out.println("\n[OK] ACCOUNT OPENED SUCCESSFULLY!");
        System.out.println("   Account Number: " + newCustomer.getAccountNumber());
        System.out.println("   Username: " + username);
        System.out.printf("   Initial Balance: $%,.2f%n", initialDeposit);
        return true;
    }
    
    public void checkBalance() {
        if (currentLoggedInCustomer == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        System.out.println("\n+-----------------------------------------+");
        System.out.println("|              CURRENT BALANCE             |");
        System.out.println("+-----------------------------------------+");
        System.out.println("| Account: " + currentLoggedInCustomer.getAccountNumber());
        System.out.printf("| Balance: $%,.2f%n", currentLoggedInCustomer.getBalance());
        System.out.println("+-----------------------------------------+");
    }
    
    public void showTransactionHistory() {
        if (currentLoggedInCustomer == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        List<Transaction> myHistory = transactionHistory.get(currentLoggedInCustomer.getUsername());
        if (myHistory == null || myHistory.isEmpty()) {
            System.out.println("\n[EMPTY] No transactions yet.");
            return;
        }
        
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|              YOUR TRANSACTION HISTORY            |");
        System.out.println("+-------------------------------------------------+");
        for (Transaction t : myHistory) {
            System.out.println(t);
        }
    }
    
    public void showAllCustomers() {
        if (customerDatabase.isEmpty()) {
            System.out.println("[EMPTY] No customers registered.");
            return;
        }
        System.out.println("\n=== ALL BANK CUSTOMERS (" + customerDatabase.size() + " customers) ===");
        for (Customer c : customerDatabase.values()) {
            c.displayInfo();
        }
        System.out.println("\n[STATIC] Next Account Number: " + Customer.getNextAccountNum());
    }
    
    public void showStatistics() {
        System.out.println("\n=== BANK STATISTICS ===");
        System.out.println("Total Customers: " + customerDatabase.size());
        System.out.println("[STATIC] Next Account Number: " + Customer.getNextAccountNum());
        System.out.println("[STATIC] Next Transaction ID: " + Transaction.getNextId());
        System.out.println("[STATIC] Next Request ID: " + MoneyRequest.getNextRequestId());
        System.out.println("Money Requests: " + moneyRequests.size());
    }
}