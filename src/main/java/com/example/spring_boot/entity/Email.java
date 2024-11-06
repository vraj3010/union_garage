package com.example.spring_boot.entity;

public class Email {
    private Long id;
    private String email;

    // Constructor
    public Email() {}

    public Email(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
