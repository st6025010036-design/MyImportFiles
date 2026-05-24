package com.transaction.interfaces;

import com.transaction.model.Customer;

public interface Authenticatable {
    boolean login(String username, String password);
    void logout();
    boolean verifyPassword(String password);
    void changePassword(String oldPassword, String newPassword);
    Customer getCurrentCustomer();
    boolean isLoggedIn();
}