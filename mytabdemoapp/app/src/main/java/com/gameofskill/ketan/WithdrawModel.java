package com.example.mytabdemoapp;

public class WithdrawModel {
    String upiId,key;
    int Amount;

    public WithdrawModel(String upiId, String key, int amount) {
        this.upiId = upiId;
        this.key = key;
        Amount = amount;
    }

    public WithdrawModel() {
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
