package com.example.spring_boot.entity;
public class InsurancePlan {
    private int planId;
    private String planName;
    private int coverageAmt;
    private int deductible;
    private int premiumAmt;
    private String frequency;

    // Getters and Setters
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getCoverageAmt() {
        return coverageAmt;
    }

    public void setCoverageAmt(int coverageAmt) {
        this.coverageAmt = coverageAmt;
    }

    public int getDeductible() {
        return deductible;
    }

    public void setDeductible(int deductible) {
        this.deductible = deductible;
    }

    public int getPremiumAmt() {
        return premiumAmt;
    }

    public void setPremiumAmt(int premiumAmt) {
        this.premiumAmt = premiumAmt;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "InsurancePlan [planId=" + planId + ", planName=" + planName + ", coverageAmt=" + coverageAmt
                + ", deductible=" + deductible + ", premiumAmt=" + premiumAmt + ", frequency=" + frequency + "]";
    }
}
