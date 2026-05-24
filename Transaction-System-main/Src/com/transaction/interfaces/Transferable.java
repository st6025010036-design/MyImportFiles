package com.transaction.interfaces;

import com.transaction.model.Customer;

public interface Transferable {
    boolean sendMoney(String toUsername, double amount, String description);
    boolean receiveMoney(Customer fromCustomer, double amount, String description);
}