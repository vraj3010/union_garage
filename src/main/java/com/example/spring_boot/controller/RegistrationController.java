package com.example.spring_boot.controller;
// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Controller
public class RegistrationController {

    
    private final UserService userService;
    
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() 
            && !(authentication.getPrincipal() instanceof String)) { // checking if user is not "anonymousUser"
        // Redirect to profile page if already logged in
        return "redirect:/info";
    }
        model.addAttribute("user", new UserRegistrationDto());
        List<Role> k=new ArrayList<>();
        Role r1=new Role(1L,"USER");
        Role r2=new Role(2L,"RENTER");
        k.add(r1);
        k.add(r2);
        model.addAttribute("roles", k);
        return "registration"; // Name of the Thymeleaf template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto,RedirectAttributes redirectAttributes) throws Exception{
        User Newuser=userService.registerUser(userDto);
        Long id=Newuser.getId();
        System.out.println(id+"***");
        redirectAttributes.addFlashAttribute("id", id);
        return "redirect:/userdetails"; // Redirect to login after registration
    }
}
