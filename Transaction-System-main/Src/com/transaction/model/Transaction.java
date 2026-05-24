package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Displayable {
    private static int nextId = 1001;
    
    private final int transactionId;
    private final Customer fromCustomer;
    private final Customer toCustomer;
    private final double amount;
    private final String description;
    private final String timestamp;
    private final TransactionType type;
    
    public Transaction(Customer fromCustomer, Customer toCustomer, double amount, 
                       String description, TransactionType type) {
        this.transactionId = nextId++;
        this.fromCustomer = fromCustomer;
        this.toCustomer = toCustomer;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Getters
    public int getTransactionId() { return transactionId; }
    public Customer getFromCustomer() { return fromCustomer; }
    public Customer getToCustomer() { return toCustomer; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    
    public static int getNextId() { return nextId; }
    
    // Implementing Displayable interface
    @Override
    public void displayInfo() {
        String direction = (type == TransactionType.SEND) ? "->" : "<-";
        System.out.printf("[%s] #%d | $%,.2f | %s %s %s | %s%n", 
            timestamp, transactionId, amount, 
            fromCustomer.getUsername(), direction, toCustomer.getUsername(), description);
    }
    
    @Override
    public String toString() {
        String direction = (type == TransactionType.SEND) ? "->" : "<-";
        return String.format("[%s] #%d | $%,.2f | %s %s %s | %s", 
            timestamp, transactionId, amount, 
            fromCustomer.getUsername(), direction, toCustomer.getUsername(), description);
    }
}