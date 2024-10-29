package com.example.spring_boot.entity;
import java.util.Date;

public class Insurance {
    private int policyNo;            // Primary key, policy number
    private Date startDate;           // Start date of the insurance policy
    private Date endDate;             // End date of the insurance policy
    private int employeeId;           // ID of the employee associated with the policy
    private long transactionId;       // ID of the transaction associated with the policy
    private long carId;               // ID of the car associated with the policy
    private int planId;               // ID of the insurance plan associated with the policy

    // Getters and Setters
    public int getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(int policyNo) {
        this.policyNo = policyNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return "Insurance [policyNo=" + policyNo + ", startDate=" + startDate + ", endDate=" + endDate +
                ", employeeId=" + employeeId + ", transactionId=" + transactionId +
                ", carId=" + carId + ", planId=" + planId + "]";
    }
}
