package com.example.instantfuel;

public class NewTeamMember {
    private String email;
    private String name;
    private String phoneNr;
    private String city;

    public NewTeamMember(String email, String name, String phoneNr, String city) {
        this.email = email;
        this.name = name;
        this.phoneNr = phoneNr;
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
