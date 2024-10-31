package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.spring_boot.entity.*;
import java.util.Arrays;
import java.sql.Date;
import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.repository.InsurancePlanDAO;
import com.example.spring_boot.repository.RequestInsuranceDAO;
import com.example.spring_boot.repository.UserDetailRepository;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.List;
@Controller
public class empinsurance {
    @GetMapping("/verifyinsurance")
    public String verify(Model model,HttpSession session){
        Long id=(Long)session.getAttribute("id");
        List<RequestInsurance> r=RequestInsuranceDAO.getAllRequestInsuranceEntries();
        model.addAttribute("r",r);
        return "verify_insurance";
    }
    @GetMapping("/customercar")
    public String customercar(Model model,@RequestParam("carId") long car_id){
        
        List<Cars> m1 = Arrays.asList(CarsDAO.getCarById(car_id));
        model.addAttribute("cars",m1);
        return "carlist";
    }
    @GetMapping("/plan")
    public String getMethodName(Model model,@RequestParam("plan_id") int plan_id) {
       
        InsurancePlan i=InsurancePlanDAO.getInsurancePlanById(plan_id);
        model.addAttribute("i",i);
        return "plandetails";
    }
     @GetMapping("/customer_det")
     public String getMethodName(@RequestParam long custId,Model model) {
        UserDetails u=UserDetailRepository.getCustomerById(custId);
        model.addAttribute("u",u);
         return "cust_det";
     }
      @GetMapping("/verifiedcust")
    public String verifyCustomer(RedirectAttributes redirectAttributes,@RequestParam("plan_id") int plan_id,@RequestParam("car_id") Long car_id,HttpSession s) {
        // Add a flash attribute with the message
        Insurance i=new Insurance();
        i.setCarId(car_id);
        i.setPlanId(plan_id);
        i.setEmployeeId((Long)s.getAttribute("id"));
        Date d = new Date(System.currentTimeMillis());
        i.setDueDate(d);
        i.setStartDate(d);
        redirectAttributes.addFlashAttribute("message", "Insurance Verified");
        RequestInsuranceDAO.addInsuranceEntry(i);
        // Redirect to /verifyinsurance
        return "redirect:/verifyinsurance";
    }
     
}
