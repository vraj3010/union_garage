package com.example.spring_boot.entity;

public class Inventory {
    private long inventoryId;
    private String location;
    private int sellQuantity;
    private int rentQuantity;
    private long modelId;

    // Getters and setters
    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public int getRentQuantity() {
        return rentQuantity;
    }

    public void setRentQuantity(int rentQuantity) {
        this.rentQuantity = rentQuantity;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", location='" + location + '\'' +
                ", sellQuantity=" + sellQuantity +
                ", rentQuantity=" + rentQuantity +
                ", modelId=" + modelId +
                '}';
    }
}