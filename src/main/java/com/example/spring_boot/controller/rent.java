package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.RenterRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class rent {
    
    @GetMapping("/rent")
    public String getMethodName(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        boolean check=RenterRepository.isPresentById(id);
        

        if(!check){
            Renter r=new Renter();
            model.addAttribute("rental", r);
        return "RenterDetails";}

        return "rentcars";
    }

    @PostMapping("/rentdetails")
    public String submitRenterDetails(@ModelAttribute("rental") Renter rental,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        rental.setRenter_id(id);
        RenterRepository.save(rental);
    // Save the rental details or perform any necessary logic
    // rentalService.save(rental);

    return "redirect:/rentcars";  // Redirect to a success page after submission
}
    
}
