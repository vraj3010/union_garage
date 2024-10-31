package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.InsurancePlanDAO;
import com.example.spring_boot.repository.PaymentDAO;
import com.example.spring_boot.repository.RequestInsuranceDAO;
@Controller
public class paymentdue {
    @GetMapping("/paymentdue")
    public String getMethodName(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        List<Insurance> i=RequestInsuranceDAO.getDueInsuranceByCustomerId(id);
        List<Repair> r=PaymentDAO.getRepairsWithPaymentDueByCustomerId(id);
        System.out.println(r.size()+"!@#$");
        model.addAttribute("r", r);
        model.addAttribute("i", i);
        return "paymentdue";
    }
    @GetMapping("/emp_det")
    public String getMethodName(Model model,@RequestParam Long id) {
        Employee e=EmployeeRepo.getCustomerById(id);
        model.addAttribute("e", e);
        return "emp_det";
    }
    @GetMapping("/car_det")
    public String cardescribe(Model model,@RequestParam Long id) {
       Cars car=CarsDAO.getCarById(id);
        model.addAttribute("car", car);
        return "car_det";
    }
    @GetMapping("/plan_det")
    public String plan(Model model,@RequestParam int id) {
       InsurancePlan i=InsurancePlanDAO.getInsurancePlanById(id);
        model.addAttribute("i", i);
        return "plandetails";
    }
    @PostMapping("/payinsurance")
    public String payinsurance(Model model,@RequestParam("policyNo") int policyNo,@RequestParam("id") Long id,RedirectAttributes redirectAttributes,HttpSession s){
        // System.out.println(id+"^^^");
        PaymentDAO.payInsurance(policyNo,id,(Long)s.getAttribute("id"));
        redirectAttributes.addFlashAttribute("msg", "Payment Successful");
        return "redirect:/paymentdue";
    }
    @PostMapping("/payrepair")
    public String payrepair(Model model,@RequestParam("repairId") Long repairId,@RequestParam("id") Long id,RedirectAttributes redirectAttributes,HttpSession s){
        // System.out.println(id+"^^^");
        PaymentDAO.processRepairPayment(repairId,(Long)s.getAttribute("id"),id);
        redirectAttributes.addFlashAttribute("msg", "Payment Successful");
        return "redirect:/paymentdue";
    }
}
