package com.example.spring_boot.entity;

// import jakarta.persistence.*;


public class Role {
 
    private Long id;
    private String roleName;
    
    public Long getId() {
        return id;
    }
    public Role(){

    }
    public Role(Long id,String roleName){
        this.id=id;
        this.roleName=roleName;
        
    }
    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for roleName
    public String getRoleName() {
        return roleName;
    }

    // Setter for roleName
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
