package com.transaction.interfaces;

import com.transaction.model.Customer;
import com.transaction.model.Transaction;
import java.util.List;

public interface Searchable {
    Customer findCustomerByUsername(String username);
    List<Transaction> findTransactionsByCustomer(String username);
    List<Transaction> findTransactionsByDateRange(String startDate, String endDate);
    List<Transaction> findTransactionsByAmountRange(double minAmount, double maxAmount);
}