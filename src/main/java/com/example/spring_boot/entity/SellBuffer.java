package com.example.spring_boot.entity;
public class SellBuffer {
    private int sellReqId;
    private long modelId;
    private long custId;
    private Long transactionId; // Nullable
    private Long emplId; // Nullable
    private java.sql.Timestamp datePurchased;
    private String status;

    // Getters and Setters
    public int getSellReqId() {
        return sellReqId;
    }

    public void setSellReqId(int sellReqId) {
        this.sellReqId = sellReqId;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getEmplId() {
        return emplId;
    }

    public void setEmplId(Long emplId) {
        this.emplId = emplId;
    }

    public java.sql.Timestamp getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(java.sql.Timestamp datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SellBuffer [sellReqId=" + sellReqId + ", modelId=" + modelId + ", custId=" + custId
                + ", transactionId=" + transactionId + ", emplId=" + emplId
                + ", datePurchased=" + datePurchased + ", status=" + status + "]";
    }
}
