package com.transaction;

import com.transaction.model.*;
import com.transaction.service.BankService;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();
    private static List<User> allUsers = new ArrayList<>();  // Polymorphism!
    
    public static void main(String[] args) {
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|         TRANSACTION SYSTEM - WEEK 6             |");
        System.out.println("|         INHERITANCE: User → Customer, Admin     |");
        System.out.println("+--------------------------------------------------+");
        
        testInheritance();
        
        // Rest of your main menu code...
    }
    
    private static void testInheritance() {
        System.out.println("\n=== TESTING INHERITANCE ===\n");
        
        // 1. Create Customer and Admin objects
        Customer sokha = new Customer("sokha", "Sokha Chea", "012345678", 1000, "1234");
        Customer dara = new Customer("dara", "Dara Pich", "098765432", 500, "1234");
        Admin admin = new Admin("admin1", "System Admin", "099999999", "admin123", 3);
        
        allUsers.add(sokha);
        allUsers.add(dara);
        allUsers.add(admin);
        
        // 2. Test shared methods from User superclass
        System.out.println("1. Testing shared methods from User superclass:");
        System.out.println("   Sokha verifyPassword('1234'): " + sokha.verifyPassword("1234"));
        System.out.println("   Admin verifyPassword('admin123'): " + admin.verifyPassword("admin123"));
        
        // 3. Test subclass-specific methods
        System.out.println("\n2. Testing Customer-specific methods:");
        sokha.deposit(200);
        System.out.println("   Sokha deposit $200. New balance: $" + sokha.getBalance());
        sokha.withdraw(50);
        System.out.println("   Sokha withdraw $50. New balance: $" + sokha.getBalance());
        
        System.out.println("\n3. Testing Admin-specific methods:");
        admin.manageUsers();
        
        // 4. Test polymorphism - treating all as User
        System.out.println("\n4. Testing Polymorphism (all as User type):");
        for (User u : allUsers) {
            u.displayInfo();  // Different output for Customer vs Admin!
        }
        
        // 5. Test constructor chaining with super()
        System.out.println("\n5. Constructor chaining verified - all objects created successfully!");
    }
}