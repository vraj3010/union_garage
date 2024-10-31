package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.entity.Repair;
import com.example.spring_boot.repository.RepairDAO;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
@Controller
public class carsforrepair {
    @GetMapping("/carsforrepair")
    public String getMethodName(Model model) {
        List<Repair> d=RepairDAO.getAllDefaultStatusRepairs();
        model.addAttribute("data", d);
        return "repair_service";
    }

    @GetMapping("/entercost")
    public String entercost(@RequestParam long repairId,Model model) {
        int c=0;
        model.addAttribute("repairId",repairId);
        return "submitcost";
    }


    @PostMapping("/entercost")
    public String submitcost(@RequestParam("repairId") long repairId,@RequestParam("cost") int c,HttpSession s,RedirectAttributes redirectAttributes ) {
        LocalDate localDate = LocalDate.now();

        // Convert LocalDate to java.sql.Date
        Date sqlDate = Date.valueOf(localDate);
        redirectAttributes.addFlashAttribute("message", "Changes Saved");
        RepairDAO.updateRepairDetails(repairId,sqlDate , (Long)s.getAttribute("id"), c);
        return "redirect:/carsforrepair";
    }
    
}
