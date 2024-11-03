package com.example.spring_boot.entity;
import java.util.Date;

public class Trxn {
    private Long transId;
    private String transDesc;
    private String method;
    private Date transDate;
    private int total;

    
    // Getters and Setters
    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int tot) {
        this.total = tot;
    }
}
