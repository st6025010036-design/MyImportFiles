package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.util.List;

public class Admin extends User implements Displayable {
    private int adminLevel;
    
    public Admin(String username, String fullName, String phoneNumber, 
                 String password, int adminLevel) {
        super(username, fullName, phoneNumber, password);
        this.adminLevel = adminLevel;
    }
    
    public int getAdminLevel() { return adminLevel; }
    
    public void setAdminLevel(int adminLevel) {
        if (adminLevel >= 1 && adminLevel <= 3) {
            this.adminLevel = adminLevel;
        }
    }
    
    public void manageUsers() {
        System.out.println("[ADMIN] " + getFullName() + " is managing users...");
        System.out.println("  - View all accounts");
        System.out.println("  - Delete suspicious accounts");
        System.out.println("  - Generate reports");
    }
    
    // ========== BASIC VIEW ALL TRANSACTIONS (KEEP EXISTING) ==========
    public void viewAllTransactions(List<Transaction> allTransactions) {
        System.out.println("\n=== ALL SYSTEM TRANSACTIONS ===");
        if (allTransactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        for (Transaction t : allTransactions) {
            System.out.println(t);
        }
        System.out.println("Total: " + allTransactions.size() + " transactions");
    }
    
    // ========== OVERLOADED VIEW ALL TRANSACTIONS METHOD 1: Filter by type ==========
    public void viewAllTransactions(List<Transaction> allTransactions, TransactionType type) {
        System.out.println("\n=== " + type + " TRANSACTIONS ===");
        int count = 0;
        for (Transaction t : allTransactions) {
            if (t.getType() == type) {
                System.out.println(t);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No " + type + " transactions found.");
        } else {
            System.out.println("Total: " + count + " transactions");
        }
    }
    
    // ========== OVERLOADED VIEW ALL TRANSACTIONS METHOD 2: Filter by date ==========
    public void viewAllTransactions(List<Transaction> allTransactions, String date) {
        System.out.println("\n=== TRANSACTIONS ON " + date + " ===");
        int count = 0;
        for (Transaction t : allTransactions) {
            if (t.getTimestamp().startsWith(date)) {
                System.out.println(t);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No transactions found on " + date);
        } else {
            System.out.println("Total: " + count + " transactions");
        }
    }
    
    @Override
    public void displayInfo() {
        String levelName = "";
        switch (adminLevel) {
            case 1: levelName = "Basic Admin"; break;
            case 2: levelName = "Supervisor"; break;
            case 3: levelName = "Master Admin"; break;
        }
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                    ADMIN INFORMATION                    │");
        System.out.println("├─────────────────────────────────────────────────────────┤");
        System.out.println("│ Username:    " + getUsername());
        System.out.println("│ Name:        " + getFullName());
        System.out.println("│ Phone:       " + getPhoneNumber());
        System.out.println("│ Level:       " + levelName + " (" + adminLevel + ")");
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
}