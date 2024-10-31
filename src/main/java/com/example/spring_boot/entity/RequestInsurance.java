package com.example.spring_boot.entity;
public class RequestInsurance {
    private int reqId;       // Primary key of the request
    private int planId;      // Plan ID referenced from insurance_plan
    private long carId;      // Car ID referenced from cars
    private Long custId;     // Customer ID associated with the car

    // Getters and Setters
    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "RequestInsurance [reqId=" + reqId + ", planId=" + planId + ", carId=" + carId + ", custId=" + custId + "]";
    }
}
