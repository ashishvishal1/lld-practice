package com.example.model;

public class Item implements Product {
    private long cost;
    private String name;

    public Item(long cost, String name) {
        this.cost = cost;
        this.name = name;
    }


    public void setCost(long cost) {
        this.cost = cost;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public long getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }


    @Override
    public long calculateSum() {
        return this.cost;
    }
}
