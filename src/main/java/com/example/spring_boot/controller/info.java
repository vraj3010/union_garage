package com.example.spring_boot.controller;


import java.util.Map;
import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import com.example.spring_boot.entity.Employee;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.RepairDAO;
import com.example.spring_boot.repository.SellCarDAO;
import com.example.spring_boot.repository.UserDAO;
// import com.example.spring_boot.services.UserDetailService;
import com.example.spring_boot.repository.UserDetailRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Controller
public class info {

    
    @GetMapping("/info")
    public String infopage(Model model,HttpSession session){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Long id=UserDAO.getUserIdByUsername(username);
        session.setAttribute("id", id);
        UserDetails m1=UserDetailRepository.getCustomerById(id);
        model.addAttribute("userdetails",m1);
        return "info";
    }
    @GetMapping("/edit/info")
    public String editinfo(Model model,HttpSession session){
        Long id=(Long)session.getAttribute("id");
        UserDetails m1=UserDetailRepository.getCustomerById(id);
        model.addAttribute("userDetails",m1);
        return "edit_customer";
    }
    @PostMapping("/edit/info")
    public String edit(@ModelAttribute("userdetails") UserDetails m1,HttpSession session) throws Exception{

        Long id=(Long)session.getAttribute("id");
          for (Field field : UserDetails.class.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            
            // Get field name and value
            String fieldName = field.getName();
            Object fieldValue = field.get(m1);
            
           
            // Print each field name and value
            if(fieldName=="cust_id") continue;
            if(fieldName=="phoneNo")
            UserDetailRepository.upd_cust_detail(id, fieldName, new java.math.BigDecimal(fieldValue.toString()));
            else if(fieldName=="AadharNo"){
                UserDetailRepository.upd_cust_detail(id, "aadharNo", new java.math.BigDecimal(fieldValue.toString()));
            }
            else
            UserDetailRepository.upd_cust_detail(id, fieldName, fieldValue.toString());
         
        }
        return "redirect:/info";
    }
    @GetMapping("/delete/info")
    public String deleteCustomer(HttpSession session,RedirectAttributes r) {
        Long id=(Long)session.getAttribute("id");
        boolean check=SellCarDAO.isPaymentDueForCustomer(id);
        boolean check2=RepairDAO.isPaymentDueForCustomer(id);
        if(check || check2){
            r.addFlashAttribute("msg","First Clear your Dues");
        return "redirect:/info";}

        UserDetailRepository.deleteCustomerById(id);
        return "redirect:/logout"; // Redirect to a list of customers after deletion
    }
}
