package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.util.List;

public class Admin extends User implements Displayable {
    private int adminLevel;  // 1=Basic, 2=Supervisor, 3=Master
    
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
        System.out.println("\n[ADMIN] " + fullName + " is managing users...");
        System.out.println("  - View all accounts");
        System.out.println("  - Delete suspicious accounts");
        System.out.println("  - Generate reports");
    }
    
    public void viewAllTransactions(List<Transaction> allTransactions) {
        System.out.println("\n[ADMIN] All System Transactions:");
        for (Transaction t : allTransactions) {
            System.out.println(t);
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
        System.out.println("+-------------------------------------+");
        System.out.println("|         ADMIN INFORMATION          |");
        System.out.println("+-------------------------------------+");
        System.out.println("| Username: " + username);
        System.out.println("| Name: " + fullName);
        System.out.println("| Phone: " + phoneNumber);
        System.out.println("| Level: " + levelName + " (" + adminLevel + ")");
        System.out.println("+-------------------------------------+");
    }
}