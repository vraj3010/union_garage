package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.lang.reflect.Field;
import com.example.spring_boot.entity.Employee;
import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.UserDetailRepository;

@Controller
public class employee {
     @GetMapping("/employee/{id}")
    public String infopage(Model model,@PathVariable Long id){
        System.out.println(id+"******");
        Employee m1=EmployeeRepo.getCustomerById(id);
        m1.setEmpId(id);
        model.addAttribute("employee",m1);
        return "empdetails";
    }
    
    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee employee = EmployeeRepo.getCustomerById(id);
        String dept_id_str="";
        if (employee != null) {
            model.addAttribute("employee", employee);
            model.addAttribute("dept_id_str",dept_id_str);
            return "edit_employee";
        } else {
            return "error";  // If no employee found, redirect to an error page or handle the case
        }
    }

    @PostMapping("/employee/edit/{id}")
    public String updateEmployee(@PathVariable("id") Long id, @ModelAttribute("employee") Employee employee) throws Exception{
        employee.setEmpId(id);  // Ensure the ID is set to update the correct employee
        
        for (Field field : Employee.class.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            
            // Get field name and value
            String fieldName = field.getName();
            Object fieldValue = field.get(employee);
            
            System.out.println(fieldName);
            System.out.println(fieldValue);
            // Print each field name and value
            if(fieldName=="dept_id") continue;
            if(fieldName=="phoneNo" || fieldName=="aadharNo")
            EmployeeRepo.upd_emp_detail(id, fieldName, new java.math.BigDecimal(fieldValue.toString()));
            else if(fieldName=="salary")
            EmployeeRepo.upd_emp_detail(id, fieldName, Integer.parseInt(fieldValue.toString()));
            else if(fieldName=="firstName")
            EmployeeRepo.upd_emp_detail(id, "first_name", fieldValue.toString());
            else if(fieldName=="middleName")
            EmployeeRepo.upd_emp_detail(id, "middle_name", fieldValue.toString());
            else if(fieldName=="lastName")
            EmployeeRepo.upd_emp_detail(id, "last_name", fieldValue.toString());
            else
            EmployeeRepo.upd_emp_detail(id, fieldName, fieldValue.toString());
         
        }
        return "redirect:/employee/"+id;
    }
}
