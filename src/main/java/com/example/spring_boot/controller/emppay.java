package com.example.spring_boot.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.repository.CustomerEmailDAO;
import com.example.spring_boot.repository.EmpPay;
import com.example.spring_boot.repository.PaymentDAO;
import com.example.spring_boot.repository.RequestInsuranceDAO;
import com.example.spring_boot.repository.SellCarDAO;
import com.example.spring_boot.repository.UserDetailRepository;

import jakarta.servlet.http.HttpSession;
import com.example.spring_boot.entity.*;

@Controller
public class emppay {
    @GetMapping("/emppay")
    public String getMethodName(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        List<Insurance> i=EmpPay.getInsurancesByEmployeeIdWhereDueDatePassed(id);
        List<Repair> r=EmpPay.getRepairsByEmployeeIdWhereStatusPaymentDue(id);
        List<SellBuffer> se=EmpPay.getPaymentDueSellBuffersByEmployeeId(id);
        model.addAttribute("r", r);
        model.addAttribute("i", i);
        model.addAttribute("s", se);
        return "emppay";
    }

    @PostMapping("/customerdetails2")
     public String getMethodName(@RequestParam long custId,Model model) {
        UserDetails u=UserDetailRepository.getCustomerById(custId);
        List<String> email=CustomerEmailDAO.getEmailsByCustomerId(custId);
        model.addAttribute("email", email);
        model.addAttribute("u",u);
         return "cust_det_car";
     }
     @PostMapping("cancel_sub_insure")
    public String cancel(@RequestParam Long carId,RedirectAttributes r){
        r.addFlashAttribute("msg","Insurance Cancelled Successfully");
        RequestInsuranceDAO.cancelSubscription(carId);
        return "redirect:/emppay";
    }
    
}
