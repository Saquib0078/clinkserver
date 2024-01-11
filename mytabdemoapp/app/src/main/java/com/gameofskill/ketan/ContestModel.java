package com.example.mytabdemoapp;

public class ContestModel {

    int poolprize,entry,joinedUsers,maxUsers,contestLimit;
    private String key;

    public ContestModel(int poolprize, int entry, int joinedUsers, int maxUsers, int contestLimit, String key) {
        this.poolprize = poolprize;
        this.entry = entry;
        this.joinedUsers = joinedUsers;
        this.maxUsers = maxUsers;
        this.contestLimit = contestLimit;
        this.key = key;
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

    public int getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(int joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getContestLimit() {
        return contestLimit;
    }

    public void setContestLimit(int contestLimit) {
        this.contestLimit = contestLimit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
