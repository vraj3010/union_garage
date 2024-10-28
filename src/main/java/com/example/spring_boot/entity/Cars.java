package com.example.spring_boot.entity;

public class Cars {
    private Long cid;               // Car ID
    private String vin;            // Vehicle Identification Number
    private byte[] proof;          // Proof document (LONGBLOB)
    private Long customerId;        // Customer ID who owns the car
    private String carName;        // Car name
    private String model;          // Car model
    private int year;              // Manufacture year
    private String color;          // Color (nullable)
    private Integer mileage;       // Mileage (nullable)

    // Getters and Setters
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public byte[] getProof() {
        return proof;
    }

    public void setProof(byte[] proof) {
        this.proof = proof;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    @Override
    public String toString() {
        return "Cars [cid=" + cid + ", vin=" + vin + ", customerId=" + customerId 
                + ", carName=" + carName + ", model=" + model 
                + ", year=" + year + ", color=" + color + ", mileage=" + mileage + "]";
    }
}