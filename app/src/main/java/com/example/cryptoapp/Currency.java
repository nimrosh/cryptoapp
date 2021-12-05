package com.example.cryptoapp;

public class Currency {
    private String id, name, status, type, address;
    private Double min_withdraw;
    private Integer max_withdraw;

    public Currency(String id, String name, String status, String type, String address, Double min_withdraw, Integer max_withdraw) {
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

    public Double getMin_withdraw() {
        return min_withdraw;
    }

    public Integer getMax_withdraw() {
        return max_withdraw;
    }
}
