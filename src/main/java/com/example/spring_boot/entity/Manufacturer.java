package com.example.spring_boot.entity;

public class Manufacturer {
    private Long manuId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String location;

    public Manufacturer() {
        // Default constructor
    }

    // Getters and Setters
    public Long getManuId() {
        return manuId;
    }

    public void setManuId(Long manuId) {
        this.manuId = manuId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
