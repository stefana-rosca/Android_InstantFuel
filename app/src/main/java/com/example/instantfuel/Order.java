package com.example.instantfuel;

import java.util.Date;

public class Order {
    private String FuelType;
    private int Quantity;
    private double TotalPrice;
    private Date date;

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

    public Order(String fuelType, int quantity, double totalPrice, Date date) {
        FuelType = fuelType;
        Quantity = quantity;
        TotalPrice = totalPrice;
        this.date = date;
    }
}
