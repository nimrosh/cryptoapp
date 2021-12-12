package com.example.cryptoapp;

public class Currency {
    private String id, name, status, type, address;
    private Object min_withdraw;
    private Object max_withdraw;

    public Currency(String id, String name, String status, String type, String address, Object min_withdraw, Object max_withdraw) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.address = address;
        this.min_withdraw = min_withdraw;
        this.max_withdraw = max_withdraw;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public Object getMin_withdraw() {
        return min_withdraw;
    }

    public Object getMax_withdraw() {
        return max_withdraw;
    }
}
