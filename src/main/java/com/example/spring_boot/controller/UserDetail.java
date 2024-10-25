package com.example.spring_boot.controller;

import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_boot.entity.User;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.UserDetailRepository;
import com.example.spring_boot.services.UserService;

import java.lang.reflect.Field;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import jakarta.validation.OverridesAttribute;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class UserDetail{

//     @Autowired
//     private JdbcTemplate j;
//     //  @Autowired
//     // private UserDetailService userDetailsService;
//     @Autowired
//     private UserService userService;
    @GetMapping("/userdetails")
    public String detail(@ModelAttribute("id") Long id,Model model) {
        UserDetails u=new UserDetails();
        System.out.println(id+"%%%");
        u.setCustId(id);
        model.addAttribute("userDetails",u);
        return "UserDetails";
    }
//     @Override
//     public void run(String... args) throws Exception{
        
//     }
    @PostMapping("/userdetails")  
    public String detail(@ModelAttribute("userDetails") UserDetails userdetails,Model model) throws Exception
    {
        Long id=userdetails.getCustId();
        UserDetailRepository.add_new_cust(id);
        for (Field field : UserDetails.class.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            
            // Get field name and value
            String fieldName = field.getName();
            Object fieldValue = field.get(userdetails);
            
            // System.out.println(fieldName);
            // System.out.println(fieldValue);
            // Print each field name and value
            if(fieldName=="phoneNo" || fieldName=="aadharNo")
            UserDetailRepository.upd_cust_detail(id, fieldName, new java.math.BigDecimal(fieldValue.toString()));
            else
            UserDetailRepository.upd_cust_detail(id, fieldName, fieldValue.toString());
            System.out.println("Field Name: " + fieldName + ", Field Value: " + fieldValue);
        }
        return "redirect:/login";
    }
    
}