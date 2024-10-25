package com.example.spring_boot.entity;

public class catalogue {
    private Long model_id;
    private int mileage;
    private String engine_type;
    private int price;
    private String model_name;
    private int manufacture_year;
    private Long manufacturer_id;

    public catalogue(){

    }
    // Getters and Setters
    public Long getModelId() {
        return model_id;
    }

    public void setModelId(Long modelId) {
        this.model_id = modelId;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getEngineType() {
        return engine_type;
    }

    public void setEngineType(String engineType) {
        this.engine_type = engineType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getModelName() {
        return model_name;
    }

    public void setModelName(String modelName) {
        this.model_name = modelName;
    }

    public int getManufactureYear() {
        return manufacture_year;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufacture_year = manufactureYear;
    }

    public Long getManufacturerId() {
        return manufacturer_id;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturer_id = manufacturerId;
    }
}
