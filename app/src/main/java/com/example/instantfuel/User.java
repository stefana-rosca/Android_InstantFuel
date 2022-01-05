package com.example.instantfuel;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userUID;
    private String name;
    private String email;
    private String password;
    private String phone;

    public User() {
    }

    public User(String userUID, String name, String email, String password, String phone) {
        this.userUID = userUID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userUID", userUID);
        result.put("name", name);
        result.put("email", email);
        result.put("password", password);
        result.put("phone", phone);

        return result;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
