package com.example.spring_boot.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.entity.Renter;
import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.RentCarDAO;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.spring_boot.entity.*;


@Controller
public class verifyrenters {
    @GetMapping("/verifyrenters")
    public String getMethodName(Model model) {
        List<Renter> r=EmployeeRepo.getUnapprovedRenters();
        model.addAttribute("r", r);
        return "verify_renters";
    }

    @PostMapping("/verifyrenters")
    public String postMethodName(@RequestParam Long renter_id,HttpSession s,RedirectAttributes r) {

    
        r.addFlashAttribute("msg","Renter Verified");
        EmployeeRepo.verifyRenter((Long)s.getAttribute("id"), renter_id);
        //TODO: process POST request
        
        return "redirect:/verifyrenters";
    }

    @GetMapping("/verifyrent")
    public String verifyrent(Model model) {
        List<Rent> r=RentCarDAO.getPendingRents();
        model.addAttribute("r", r);
        return "verifyrent";
    }
    
    @PostMapping("/verifyrent")
    public String verified(@RequestParam Long rent_id,HttpSession s,RedirectAttributes r) {
        r.addFlashAttribute("msg","Verified");
        //TODO: process POST request
        Long id=(Long)s.getAttribute("id");
        RentCarDAO.updateRentToActive(rent_id, id);
        return "redirect:/verifyrent";
    }
    
    @PostMapping("/rejectrent")
    public String reject(@RequestParam Long rent_id,HttpSession s,RedirectAttributes r) {
        r.addFlashAttribute("msg","Rejected");
        //TODO: process POST request
        Long id=(Long)s.getAttribute("id");
        RentCarDAO.updateRentToCancelled(rent_id, id);
        return "redirect:/verifyrent";
    }
    
   
    @GetMapping("/currentrenters")
    public String currentlist(Model model,HttpSession s) {
        Long id= (Long)s.getAttribute("id");
        List<Rent> r=RentCarDAO.getRentsByEmployeeId(id);
        model.addAttribute("r", r);
        return "currentrenters";
    }
    
    
}
