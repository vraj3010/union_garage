package com.example.spring_boot.entity;
import java.math.BigDecimal;

public class UserDetails {
    private long custId;      
    private BigDecimal phoneNo;
    private String firstName; 
    private String middleName; 
    private String lastName; 
    private String houseNo;   
    private String street;    
    private String city;      
    private String state;      
    private BigDecimal aadharNo; 
    private String license; 
    public UserDetails(){

    }
    public UserDetails( 
        Long custId,     
    BigDecimal phoneNo,
     String firstName,
     String middleName, 
    String lastName, 
    String houseNo,  
    String street,   
    String city,      
     String state,      
     BigDecimal aadharNo, 
     String license 
 ){
    this.custId=custId;     
    this.phoneNo=phoneNo;
     this.firstName=firstName;
     this.middleName=middleName; 
     this.lastName=lastName;
    this.houseNo=houseNo; 
     this.street= street;  
    this.city=city;    
    this.state= state;      
     this.aadharNo=aadharNo; 
     this.license= license;
 }
    
    // Getters and Setters
    public long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public BigDecimal getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(BigDecimal phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(BigDecimal aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

}