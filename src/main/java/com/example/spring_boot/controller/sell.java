package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.entity.SellBuffer;
import com.example.spring_boot.repository.SellCarDAO;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class sell {
    
    @GetMapping("/sell")
    public String getMethodName(Model model) {
        List<SellBuffer> s=SellCarDAO.getAllInProgressEntries();
        model.addAttribute("s", s);
        return "sell_request";
    }
    @PostMapping("/accept")
    public String next(@RequestParam("request") int Id,@RequestParam("model_id") Long model_id,RedirectAttributes r,HttpSession s){
        boolean check= SellCarDAO.isSellQuantityAvailable(model_id);
        if(!check){
              r.addFlashAttribute("message", "Car not available");
        return "redirect:/sell";}
            SellCarDAO.updateSellBufferStatusToPaymentDue(Id, model_id,(Long)s.getAttribute("id"));
            r.addFlashAttribute("message", "Request Accepted");
        return "redirect:/sell";
    }
    @PostMapping("/reject")
    public String postMethodName(@RequestParam("request") int Id,RedirectAttributes r,HttpSession s) {
        //TODO: process POST request
        
        SellCarDAO.updateSellBufferStatusToRejected(Id, (Long)s.getAttribute("id"));
        r.addFlashAttribute("message", "Request Rejected");
        return "redirect:/sell";
    }
    
}
