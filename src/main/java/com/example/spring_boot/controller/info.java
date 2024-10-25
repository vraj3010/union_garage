package com.example.spring_boot.controller;


import java.util.Map;
import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

import com.example.spring_boot.entity.Employee;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.EmployeeRepo;
// import com.example.spring_boot.services.UserDetailService;
import com.example.spring_boot.repository.UserDetailRepository;

@Controller
public class info {

    
    @GetMapping("/info/{id}")
    public String infopage(Model model,@PathVariable Long id){
        // System.out.println(id+"******");
        UserDetails m1=UserDetailRepository.getCustomerById(id);
        model.addAttribute("userdetails",m1);
        return "info";
    }
    @GetMapping("/edit/info/{id}")
    public String editinfo(Model model,@PathVariable Long id){
        UserDetails m1=UserDetailRepository.getCustomerById(id);
        model.addAttribute("userDetails",m1);
        return "edit_customer";
    }
    @PostMapping("/edit/info/{id}")
    public String edit(@ModelAttribute("userdetails") UserDetails m1,@PathVariable Long id) throws Exception{

          for (Field field : UserDetails.class.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            
            // Get field name and value
            String fieldName = field.getName();
            Object fieldValue = field.get(m1);
            
            System.out.println(fieldName);
            System.out.println(fieldValue);
            // Print each field name and value
            if(fieldName=="cust_id") continue;
            if(fieldName=="phoneNo")
            UserDetailRepository.upd_cust_detail(id, fieldName, new java.math.BigDecimal(fieldValue.toString()));
            else if(fieldName=="AadharNo"){
                UserDetailRepository.upd_cust_detail(id, "aadharNo", new java.math.BigDecimal(fieldValue.toString()));
            }
            else
            UserDetailRepository.upd_cust_detail(id, fieldName, fieldValue.toString());
         
        }
        return "redirect:/info/"+id;
    }
    @GetMapping("/delete/info/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        UserDetailRepository.deleteCustomerById(id); // Assuming deleteById method exists in UserDetailRepository
        return "redirect:/home"; // Redirect to a list of customers after deletion
    }
}
