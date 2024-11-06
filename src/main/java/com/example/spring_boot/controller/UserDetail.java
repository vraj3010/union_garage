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

import com.example.spring_boot.repository.CustomerEmailDAO;
import com.example.spring_boot.repository.UserDetailRepository;
import com.example.spring_boot.services.UserService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
// import jakarta.validation.OverridesAttribute;
import com.example.spring_boot.entity.*;

@Controller
public class UserDetail{

//     @Autowired
//     private JdbcTemplate j;
//     //  @Autowired
//     // private UserDetailService userDetailsService;
    @Autowired
    private UserService userService;
    @GetMapping("/userdetails")
    public String detail(Model model,@ModelAttribute("user") UserRegistrationDto userDto) {
        UserDetails u=new UserDetails();
        List<String> emailList = new ArrayList<>();
        System.out.println(userDto.getUsername());
        model.addAttribute("userDetails",u);
         model.addAttribute("emailList", emailList);
        return "UserDetails";
    }
//     @Override
//     public void run(String... args) throws Exception{
        
//     }
    @PostMapping("/userdetails")  
    public String detail( @RequestParam("emailList") List<String> e,@ModelAttribute("userDetails") UserDetails userdetails,Model model,@ModelAttribute("username") String username,@ModelAttribute("password") String password,@ModelAttribute("role") String role) throws Exception
    {

        UserRegistrationDto userDto=new UserRegistrationDto();
        userDto.setPassword(password);
        userDto.setRole(role);
        userDto.setUsername(username);
        User Newuser=userService.registerUser(userDto);
        Long id=Newuser.getId();
        UserDetailRepository.add_new_cust(id);
        userdetails.setCustId(id);
  
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
        }

        CustomerEmailDAO.addCustomerEmails(id, e);
        return "redirect:/login";
    }
    
}