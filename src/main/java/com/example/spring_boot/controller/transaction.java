package com.example.spring_boot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_boot.entity.Insurance;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.PreviousTrxnDAO;

import jakarta.servlet.http.HttpSession;


@Controller
public class transaction {
    @GetMapping("/previoustransactions")
    public String getMethodName(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        Map<Insurance,Trxn> m=PreviousTrxnDAO.getInsuranceTransactionsByCustomerId(id);
        Map<Repair,Trxn> m2=PreviousTrxnDAO.getRepairTransactionsByCustomerId(id);
        Map<SellBuffer,Trxn> m3=PreviousTrxnDAO.getSellBufferTransactionsByCustomerId(id); 
        // policy 
        model.addAttribute("m", m);
        model.addAttribute("m2", m2);
        model.addAttribute("m3", m3);
        return "previous_trans";
    }
}
