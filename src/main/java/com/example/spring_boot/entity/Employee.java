package com.example.spring_boot.entity;
import java.math.BigDecimal;
public class Employee {
    private Long empId;
    private Integer salary;
    private BigDecimal phoneNo;
    private Long dept_id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private BigDecimal aadharNo;
    public Employee(){

    }
    // Getters and Setters
    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public BigDecimal getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(BigDecimal phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getDept() {
        return dept_id;
    }

    public void setDept(Long dept_id) {
        this.dept_id = dept_id;
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
}
