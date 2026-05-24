package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MoneyRequest implements Displayable {
    
    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, PAID
    }
    
    private static int nextRequestId = 5001;
    
    private final int requestId;
    private final Customer requester;
    private final Customer target;
    private final double amount;
    private final String reason;
    private final String timestamp;
    private RequestStatus status;
    
    public MoneyRequest(Customer requester, Customer target, double amount, String reason) {
        this.requestId = nextRequestId++;
        this.requester = requester;
        this.target = target;
        this.amount = amount;
        this.reason = reason;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = RequestStatus.PENDING;
    }
    
    // Getters
    public int getRequestId() { return requestId; }
    public Customer getRequester() { return requester; }
    public Customer getTarget() { return target; }
    public double getAmount() { return amount; }
    public String getReason() { return reason; }
    public String getTimestamp() { return timestamp; }
    public RequestStatus getStatus() { return status; }
    
    public static int getNextRequestId() { return nextRequestId; }
    
    public boolean approve() {
        if (status == RequestStatus.PENDING) {
            if (target.withdraw(amount)) {
                requester.deposit(amount);
                status = RequestStatus.APPROVED;
                return true;
            } else {
                status = RequestStatus.REJECTED;
                System.out.println("[ERROR] Request #" + requestId + " rejected: Insufficient balance!");
                return false;
            }
        }
        return false;
    }
    
    public void reject() {
        if (status == RequestStatus.PENDING) {
            status = RequestStatus.REJECTED;
            System.out.println("[ERROR] Request #" + requestId + " rejected.");
        }
    }
    
    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }
    
    // Implementing Displayable interface
    @Override
    public void displayInfo() {
        System.out.println("+---------------------------------------------+");
        System.out.println("|           MONEY REQUEST #" + requestId + "                       |");
        System.out.println("+---------------------------------------------+");
        System.out.println("| Requester:  " + requester.getUsername());
        System.out.println("| Target:     " + target.getUsername());
        System.out.printf("| Amount:     $%,.2f%n", amount);
        System.out.println("| Reason:     " + reason);
        System.out.println("| Status:     " + status);
        System.out.println("| Time:       " + timestamp);
        System.out.println("+---------------------------------------------+");
    }
}