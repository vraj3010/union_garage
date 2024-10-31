package com.example.spring_boot.entity;
import java.util.Date;

public class Insurance {
    private int policyNo;
    private Date startDate;
    private Date dueDate;
    private Long employeeId;
    private Long carId;
    private int planId;

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
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
        return "Insurance [policyNo=" + policyNo + ", startDate=" + startDate + ", dueDate=" + dueDate +
                ", employeeId=" + employeeId + ", carId=" + carId + ", planId=" + planId + "]";
    }
}