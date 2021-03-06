package com.example.instantfuel;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Order {
    private String userUID;
    private String FuelType;
    private int Quantity;
    private double TotalPrice;
    private Date date;
    private String adress;

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Order(String userUID, String fuelType, int quantity, double totalPrice, Date date, String adress) {
        this.userUID = userUID;
        FuelType = fuelType;
        Quantity = quantity;
        TotalPrice = totalPrice;
        this.date = date;
        this.adress = adress;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "userUID='" + userUID + '\'' +
                ", FuelType='" + FuelType + '\'' +
                ", Quantity=" + Quantity +
                ", TotalPrice=" + TotalPrice +
                ", date=" + date +
                ", adress='" + adress + '\'' +
                '}';
    }
}
