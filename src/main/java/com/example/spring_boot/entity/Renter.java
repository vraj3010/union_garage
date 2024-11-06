package com.example.spring_boot.entity;

import java.util.Date;

public class Renter {
    private Long renter_id;
    private String driver_history;
    private byte[] license_proof; 
    private String form_path;
    private Date last_rental_date;
    private Date signup_date;
    private String referral_source;
    private String status;  // "Pending", "Approved", "Rejected"


    // Default constructor
    public Renter() {}

    // Parameterized constructor
    public Renter(Long renter_id, String driver_history, byte[] license_proof, String form_path,
                  Date last_rental_date, Date signup_date, String referral_source, String status) {
        this.renter_id = renter_id;
        this.driver_history = driver_history;
        this.license_proof = license_proof;
        this.form_path = form_path;
        this.last_rental_date = last_rental_date;
        this.signup_date = signup_date;
        this.referral_source = referral_source;
        this.status = status;
    }

    // Getters and setters
    public Long getRenter_id() {
        return renter_id;
    }

    public void setRenter_id(Long renter_id) {
        this.renter_id = renter_id;
    }

    public String getDriver_history() {
        return driver_history;
    }

    public void setDriver_history(String driver_history) {
        this.driver_history = driver_history;
    }

    public byte[] getLicense_proof() {
        return license_proof;
    }

    public void setLicense_proof(byte[] license_proof) {
        this.license_proof = license_proof;
    }

    public String getForm_path() {
        return form_path;
    }

    public void setForm_path(String form_path) {
        this.form_path = form_path;
    }

    public Date getLast_rental_date() {
        return last_rental_date;
    }

    public void setLast_rental_date(Date last_rental_date) {
        this.last_rental_date = last_rental_date;
    }

    public Date getSignup_date() {
        return signup_date;
    }

    public void setSignup_date(Date signup_date) {
        this.signup_date = signup_date;
    }

    public String getReferral_source() {
        return referral_source;
    }

    public void setReferral_source(String referral_source) {
        this.referral_source = referral_source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
