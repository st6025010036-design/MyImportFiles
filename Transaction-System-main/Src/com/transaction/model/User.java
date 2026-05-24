package com.transaction.model;

public abstract class User {
    protected String username;
    protected String fullName;
    protected String phoneNumber;
    protected String password;
    
    public User(String username, String fullName, String phoneNumber, String password) {
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public void changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            if (newPassword != null && newPassword.length() >= 4) {
                this.password = newPassword;
                System.out.println("[OK] Password changed successfully!");
            } else {
                System.out.println("[ERROR] Password must be at least 4 characters!");
            }
        } else {
            System.out.println("[ERROR] Current password is incorrect!");
        }
    }
    
    public abstract void displayInfo();
}