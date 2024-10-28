package com.example.spring_boot.entity;
import java.util.Date;

public class Insurance {
    private int policyNo;
    private Date startDate;
    private Date endDate;
    private int premiumAmt;
    private int coverageAmt;
    private String policyStatus;
    private int deductible;
    private int employeeId;
    private int transactionId;

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

    public int getPremiumAmt() {
        return premiumAmt;
    }

    public void setPremiumAmt(int premiumAmt) {
        this.premiumAmt = premiumAmt;
    }

    public int getCoverageAmt() {
        return coverageAmt;
    }

    public void setCoverageAmt(int coverageAmt) {
        this.coverageAmt = coverageAmt;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public int getDeductible() {
        return deductible;
    }

    public void setDeductible(int deductible) {
        this.deductible = deductible;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
