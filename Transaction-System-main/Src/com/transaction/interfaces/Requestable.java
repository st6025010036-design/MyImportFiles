package com.transaction.interfaces;

public interface Requestable {
    void requestMoney(String fromUsername, double amount, String reason);
    boolean approveRequest(int requestId);
    void rejectRequest(int requestId);
    void showPendingRequests();
}