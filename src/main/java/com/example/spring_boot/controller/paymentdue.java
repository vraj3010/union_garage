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
import com.example.spring_boot.repository.EmployeeEmailDAO;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.InsurancePlanDAO;
import com.example.spring_boot.repository.PaymentDAO;
import com.example.spring_boot.repository.RequestInsuranceDAO;
import com.example.spring_boot.repository.SellCarDAO;
@Controller
public class paymentdue {
    @GetMapping("/paymentdue")
    public String getMethodName(Model model,HttpSession s) {
        Long id=(Long)s.getAttribute("id");
        List<Insurance> i=RequestInsuranceDAO.getDueInsuranceByCustomerId(id);
        List<Repair> r=PaymentDAO.getRepairsWithPaymentDueByCustomerId(id);
        List<SellBuffer> se=SellCarDAO.getSellBufferEntriesByCustomerId(id);
        model.addAttribute("r", r);
        model.addAttribute("i", i);
        model.addAttribute("s", se);
        return "paymentdue";
    }
    @PostMapping("/emp_det")
    public String getMethodName(Model model,@RequestParam Long id) {
        Employee e=EmployeeRepo.getCustomerById(id);
        List<String> email=EmployeeEmailDAO.getEmailsByEmpId(id);
        model.addAttribute("e", e);
        model.addAttribute("email", email);
        return "emp_det";
    }
    @PostMapping("/car_det")
    public String cardescribe(Model model,@RequestParam Long id) {
       Cars car=CarsDAO.getCarById(id);
        model.addAttribute("car", car);
      return "car_det";
    }
    @PostMapping("/plan_det")
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
    @PostMapping("/paybuy")
    public String paybuy(Model model,@RequestParam("sellReqId") int sellReqid,@RequestParam("id") Long id,RedirectAttributes redirectAttributes,HttpSession s){
        System.out.println(sellReqid);
        PaymentDAO.sellCar(sellReqid,(Long)s.getAttribute("id"),id);
        redirectAttributes.addFlashAttribute("msg", "Payment Successful");
        return "redirect:/paymentdue";
    }
}
