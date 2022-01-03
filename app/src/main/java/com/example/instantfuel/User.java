package com.example.instantfuel;

public class User {
    private String userUID;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(String userUID, String name, String email, String password) {
        this.userUID = userUID;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
