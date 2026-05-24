package com.transaction.model;

import com.transaction.interfaces.Displayable;

public class Customer extends User implements Displayable {
    private static int nextAccountNum = 1001;
    
    private final String accountNumber;
    private double balance;
    
    public Customer(String username, String fullName, String phoneNumber, 
                    double initialBalance, String password) {
        super(username, fullName, phoneNumber, password);
        this.accountNumber = "ACC" + nextAccountNum++;
        this.balance = initialBalance;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public static int getNextAccountNum() {
        return nextAccountNum;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("+-------------------------------------+");
        System.out.println("|         CUSTOMER INFORMATION        |");
        System.out.println("+-------------------------------------+");
        System.out.println("| Account: " + accountNumber);
        System.out.println("| Username: " + username);
        System.out.println("| Name: " + fullName);
        System.out.println("| Phone: " + phoneNumber);
        System.out.printf("| Balance: $%,.2f%n", balance);
        System.out.println("+-------------------------------------+");
    }
}