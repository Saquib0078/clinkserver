package com.example.mytabdemoapp;

public class DepositModel {

    String TransactionId,UpiId,Amount,key;
    String timestamp;

    public DepositModel(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public DepositModel(String transactionId, String upiId, String amount, String key) {
        TransactionId = transactionId;
        UpiId = upiId;
        Amount = amount;
        this.key = key;
    }

    public DepositModel() {
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getUpiId() {
        return UpiId;
    }

    public void setUpiId(String upiId) {
        UpiId = upiId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
