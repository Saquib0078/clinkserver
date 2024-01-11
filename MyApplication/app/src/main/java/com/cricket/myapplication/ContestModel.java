package com.example.mytabdemoapp;

public class ContestModel {

    int poolprize,entry;
    private String key;

    public ContestModel(String key) {
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ContestModel(int poolprize, int entry) {
        this.poolprize = poolprize;
        this.entry = entry;
    }

    public ContestModel() {
    }

    public int getPoolprize() {
        return poolprize;
    }

    public void setPoolprize(int poolprize) {
        this.poolprize = poolprize;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }
}
