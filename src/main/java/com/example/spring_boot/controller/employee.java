package com.example.spring_boot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import com.example.spring_boot.entity.Employee;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.EmployeeEmailDAO;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.UserDAO;
import com.example.spring_boot.repository.UserDetailRepository;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
public class employee {
     @GetMapping("/employee")
    public String infopage(Model model,HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        
        Long id=UserDAO.getUserIdByUsername(username);
        List<String> email=EmployeeEmailDAO.getEmailsByEmpId(id);
        session.setAttribute("id", id);
        Employee m1=EmployeeRepo.getCustomerById(id);
        m1.setEmpId(id);
        model.addAttribute("employee",m1);
        model.addAttribute("email", email);
        return "empdetails";
    }
    
    @GetMapping("/employee/edit")
    public String showEditForm(HttpSession session, Model model) {
        Long id=(Long)session.getAttribute("id");
        Employee employee = EmployeeRepo.getCustomerById(id);
        List<String> email=EmployeeEmailDAO.getEmailsByEmpId(id);
        String dept_id_str="";
        if (employee != null) {
            model.addAttribute("employee", employee);
            model.addAttribute("dept_id_str",dept_id_str);
            model.addAttribute("email",email);
            return "edit_employee";
        } else {
            return "error";  // If no employee found, redirect to an error page or handle the case
        }
    }

    @PostMapping("/employee/edit")
    public String updateEmployee(@RequestParam("emailList") List<String> emailList,HttpSession session, @ModelAttribute("employee") Employee employee) throws Exception{
        Long id=(Long)session.getAttribute("id");
        employee.setEmpId(id);  // Ensure the ID is set to update the correct employee
        EmployeeEmailDAO.deleteEmailsByEmployeeId(id);
        for (Field field : Employee.class.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            
            // Get field name and value
            String fieldName = field.getName();
            Object fieldValue = field.get(employee);
            
            // Print each field name and value
            if(fieldName=="dept_id") continue;
            if(fieldName=="phoneNo" || fieldName=="aadharNo")
            EmployeeRepo.upd_emp_detail(id, fieldName, new java.math.BigDecimal(fieldValue.toString()));
            else if(fieldName=="salary")
            continue;
            else if(fieldName=="firstName")
            EmployeeRepo.upd_emp_detail(id, "first_name", fieldValue.toString());
            else if(fieldName=="middleName")
            EmployeeRepo.upd_emp_detail(id, "middle_name", fieldValue.toString());
            else if(fieldName=="lastName")
            EmployeeRepo.upd_emp_detail(id, "last_name", fieldValue.toString());
            else
            EmployeeRepo.upd_emp_detail(id, fieldName, fieldValue.toString());
         
        }
        EmployeeEmailDAO.addEmployeeEmails(id, emailList);
        return "redirect:/employee";
    }
}
