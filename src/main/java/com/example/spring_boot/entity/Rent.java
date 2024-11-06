package com.example.spring_boot.entity;

import java.util.Date;

public class Rent {
    private Long rent_id;
    private Date start_date;
    private Date end_date;
    private Long model_id;
    private Long renter_id;
    private Long employee_id;
    private Long transaction_id;
    private String rental_status;  // "Pending", "Active", "Completed", "Cancelled"

    // Getters and setters
    public Long getRent_id() {
        return rent_id;
    }

    public void setRent_id(Long rent_id) {
        this.rent_id = rent_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Long getModel_id() {
        return model_id;
    }

    public void setModel_id(Long model_id) {
        this.model_id = model_id;
    }

    public Long getRenter_id() {
        return renter_id;
    }

    public void setRenter_id(Long renter_id) {
        this.renter_id = renter_id;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getRental_status() {
        return rental_status;
    }

    public void setRental_status(String rental_status) {
        this.rental_status = rental_status;
    }
}
