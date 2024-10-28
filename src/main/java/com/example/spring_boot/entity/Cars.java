package com.example.spring_boot.entity;

public class Cars {
    private int cid; // Car ID
    private String vin; // Vehicle Identification Number
    private byte[] proof; // Proof document (LONGBLOB)
    private int customerId; // Customer ID who owns the car

    // Getters and Setters
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Cars [cid=" + cid + ", vin=" + vin + ", customerId=" + customerId + "]";
    }
}
