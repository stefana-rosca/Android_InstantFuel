package com.example.instantfuel;

public class Fuel {
    public String name;
    public int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fuel(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
