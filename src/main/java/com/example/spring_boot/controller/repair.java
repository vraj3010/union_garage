package com.example.spring_boot.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_boot.entity.Repair;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.RepairDAO;
import com.example.spring_boot.repository.UserDetailRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class repair {
    
    @GetMapping("/repair")
     public String repairst(Model model,@RequestParam Long carId,HttpSession s){

        System.out.println(carId+"&&&&&&");
        Repair m1=new Repair();
        boolean under=RepairDAO.carExistsWithDefaultRepairStatus(carId);
        if (under) {
            // If the car is under repair, redirect to a different template
            return "car_under_repair"; // Change this to your new template name
        }
        m1.setCarId(carId);
        Long id=(Long)s.getAttribute("id");
        model.addAttribute("car",m1);
        return "repair_det";
    }
    @PostMapping("/repair")
    public String postMethodName(@ModelAttribute("car") Repair m1,HttpSession s) {
        //TODO: process POST request
        RepairDAO.addRepairEntry(m1);
        return "redirect:/listcar";
    }
    
}
