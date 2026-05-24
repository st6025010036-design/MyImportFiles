package com.transaction;

import com.transaction.service.BankService;
import com.transaction.model.Customer;
import com.transaction.model.Transaction;
import com.transaction.model.MoneyRequest;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();
    
    public static void main(String[] args) {
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|         TRANSACTION SYSTEM - WEEK 5             |");
        System.out.println("|    Interfaces: Displayable, Transferable,       |");
        System.out.println("|    Requestable, Authenticatable, Searchable     |");
        System.out.println("+--------------------------------------------------+");
        
        createSampleData();
        
        while (true) {
            if (!bank.isLoggedIn()) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n+----------------------------------------+");
        System.out.println("|              MAIN MENU                  |");
        System.out.println("+----------------------------------------+");
        System.out.println("|  1. Open New Account                   |");
        System.out.println("|  2. Login                              |");
        System.out.println("|  3. Show All Customers (Displayable)   |");
        System.out.println("|  4. Show Statistics                    |");
        System.out.println("|  5. Test Searchable Interface          |");
        System.out.println("|  6. Exit                               |");
        System.out.println("+----------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: register(); break;
            case 2: login(); break;
            case 3: bank.showAllCustomers(); break;
            case 4: bank.showStatistics(); break;
            case 5: testSearchable(); break;
            case 6: System.out.println("\n[OK] Goodbye!"); System.exit(0); break;
            default: System.out.println("[ERROR] Invalid choice!");
        }
    }
    
    private static void showMainMenu() {
        String name = bank.getCurrentCustomer().getFullName();
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|     WELCOME, " + String.format("%-32s", name.toUpperCase()) + "|");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Send Money (Transferable)                   |");
        System.out.println("|  2. Request Money (Requestable)                 |");
        System.out.println("|  3. Check Balance                               |");
        System.out.println("|  4. Transaction History                         |");
        System.out.println("|  5. Show All Customers (Displayable)            |");
        System.out.println("|  6. Pending Requests (Requestable)              |");
        System.out.println("|  7. Approve Request (Requestable)               |");
        System.out.println("|  8. Change Password (Authenticatable)           |");
        System.out.println("|  9. Logout (Authenticatable)                    |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: sendMoney(); break;
            case 2: requestMoney(); break;
            case 3: bank.checkBalance(); break;
            case 4: bank.showTransactionHistory(); break;
            case 5: bank.showAllCustomers(); break;
            case 6: bank.showPendingRequests(); break;
            case 7: approveRequest(); break;
            case 8: changePassword(); break;
            case 9: bank.logout(); break;
            default: System.out.println("[ERROR] Invalid choice!");
        }
    }
    
    private static void register() {
        System.out.println("\n=== OPEN NEW ACCOUNT ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Phone (9-12 digits): ");
        String phone = scanner.nextLine();
        System.out.print("Initial Deposit: $");
        double deposit = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Password (min 4 chars): ");
        String password = scanner.nextLine();
        
        bank.registerCustomer(username, fullName, phone, deposit, password);
    }
    
    private static void login() {
        System.out.println("\n=== LOGIN ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        bank.login(username, password);
    }
    
    private static void sendMoney() {
        System.out.println("\n=== SEND MONEY ===\n");
        bank.showAllCustomers();
        System.out.print("\nSend to (username): ");
        String to = scanner.nextLine();
        System.out.print("Amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        
        bank.sendMoney(to, amount, desc);
    }
    
    private static void requestMoney() {
        System.out.println("\n=== REQUEST MONEY ===\n");
        bank.showAllCustomers();
        System.out.print("\nRequest from (username): ");
        String from = scanner.nextLine();
        System.out.print("Amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Reason: ");
        String reason = scanner.nextLine();
        
        bank.requestMoney(from, amount, reason);
    }
    
    private static void approveRequest() {
        bank.showPendingRequests();
        System.out.print("\nEnter Request ID to approve: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        bank.approveRequest(id);
    }
    
    private static void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===\n");
        System.out.print("Current password: ");
        String oldPwd = scanner.nextLine();
        System.out.print("New password (min 4 chars): ");
        String newPwd = scanner.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPwd = scanner.nextLine();
        
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("[ERROR] Passwords do not match!");
            return;
        }
        
        bank.changePassword(oldPwd, newPwd);
    }
    
    private static void testSearchable() {
        System.out.println("\n=== TESTING SEARCHABLE INTERFACE ===\n");
        
        System.out.print("Enter username to search: ");
        String username = scanner.nextLine();
        
        Customer found = bank.findCustomerByUsername(username);
        if (found != null) {
            found.displayInfo();
        } else {
            System.out.println("[ERROR] Customer not found!");
        }
        
        System.out.print("\nEnter min amount: $");
        double min = scanner.nextDouble();
        System.out.print("Enter max amount: $");
        double max = scanner.nextDouble();
        scanner.nextLine();
        
        var transactions = bank.findTransactionsByAmountRange(min, max);
        System.out.println("\nTransactions between $" + min + " and $" + max + ":");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    
    private static void createSampleData() {
        System.out.println("\n[INFO] Creating sample customers...");
        bank.registerCustomer("sokha", "Sokha Chea", "012345678", 1000, "1234");
        bank.registerCustomer("dara", "Dara Pich", "098765432", 500, "1234");
        bank.registerCustomer("sreymom", "Srey Mom", "011223344", 2000, "1234");
        bank.registerCustomer("vannak", "Vannak Khiev", "015566778", 750, "1234");
        System.out.println("[OK] Sample database ready!\n");
    }
}