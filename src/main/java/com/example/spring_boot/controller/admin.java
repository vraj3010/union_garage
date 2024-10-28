package com.example.spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.services.RoleService;
import com.example.spring_boot.services.UserService;

import java.util.*;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.EmployeeRepo;
@Controller
public class admin{
    @Autowired
    private final UserService userService;
   
    public admin(UserService userService){
        this.userService=userService;
    }
 @GetMapping("/addemp")
    public String add(Model model) {
       
        model.addAttribute("user", new UserRegistrationDto());
        List<Role> k=new ArrayList<>();
        Role r1=new Role(1L,"EMPLOYEE");
        k.add(r1);
        model.addAttribute("roles", k);
        return "registeremployee"; // Name of the Thymeleaf template
    }
    
    @PostMapping("/addemp")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto) throws Exception{
        // System.out.println(userDto.getPassword()+"^^^##$$%#$%");
        User Newuser=userService.registerUser(userDto);
        Long id=Newuser.getId();
        System.out.println(id);
        EmployeeRepo.add_new_emp(id);
        return "redirect:/admin"; // Redirect to login after registration
    }
    
}