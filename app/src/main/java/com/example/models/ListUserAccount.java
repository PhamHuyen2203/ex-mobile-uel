package com.example.models;

import java.util.ArrayList;

public class ListUserAccount {
    public static ArrayList<UserAccount> getUserAccount() {
        ArrayList<UserAccount> database=new ArrayList<>();
        database.add(new UserAccount("admin", "123", "Administrator", "Pham Thuy Huyen", true));
        database.add(new UserAccount("user1", "123", "Employee", "Nguyen Van A", true));
        database.add(new UserAccount("user2", "123", "Reporter", "Tran Van B", true));
        return database;
    }

    public static UserAccount login(String username, String password) {
        //step 1: query database
        ArrayList<UserAccount> database = getUserAccount();
        //step 2: check username, password
        for (UserAccount account : database) {
            if (account.getUserName().equalsIgnoreCase(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }
}
