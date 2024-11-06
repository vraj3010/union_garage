
package com.example.spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring_boot.services.RoleService;
import com.example.spring_boot.services.UserService;

import java.util.*;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.ManufacturerRepo;
import com.example.spring_boot.repository.UserDetailRepository;
@Controller
public class admin{
    @Autowired
    private final UserService userService;
   
    public admin(UserService userService){
        this.userService=userService;
    }
 @GetMapping("/addemp")
    public String addemp(Model model) {
       
        model.addAttribute("user", new UserRegistrationDto());
        List<Role> k=new ArrayList<>();
        Role r1=new Role(1L,"EMPLOYEE");
        k.add(r1);
        model.addAttribute("roles", k);
        return "registeremployee"; // Name of the Thymeleaf template
    }
    @GetMapping("/addmanu")
    public String addmanu(Model model) {
       
        model.addAttribute("user", new UserRegistrationDto());
        List<Role> k=new ArrayList<>();
        Role r1=new Role(1L,"MANUFACTURER");
        k.add(r1);
        model.addAttribute("roles", k);
        return "registermanufacturer"; // Name of the Thymeleaf template
    }
    
    @PostMapping("/addemp")
    public String registerUseremp(@ModelAttribute("user") UserRegistrationDto userDto,RedirectAttributes r) throws Exception{
        // System.out.println(userDto.getPassword()+"^^^##$$%#$%");
        User Newuser=userService.registerUser(userDto);
        if(Newuser==null){
            r.addFlashAttribute("msg","username already exists");
            return "redirect:/addemp";
        }
        Long id=Newuser.getId();
        System.out.println(id);
        EmployeeRepo.add_new_emp(id);
        return "redirect:/admin"; // Redirect to login after registration
    }

    @PostMapping("/addmanu")
    public String registerUsermanu(@ModelAttribute("user") UserRegistrationDto userDto) throws Exception{
        // System.out.println(userDto.getPassword()+"^^^##$$%#$%");
        User Newuser=userService.registerUser(userDto);
        Long id=Newuser.getId();
        System.out.println(id);
        ManufacturerRepo.addNewManufacturer(id);
        return "redirect:/admin"; // Redirect to login after registration
    }
    
    @GetMapping("/employees")
    public String getMethodName(Model model) {
       List<Employee> e=EmployeeRepo.getAllEmployees();
       model.addAttribute("e",e);
       return "listemployee";
    }
    @PostMapping("/employees/edit")
    public String editEmployeeSalary(@RequestParam("empId") Long empId, Model model) {
        model.addAttribute("empId", empId);
        return "edit_salary";  // re    turns to the Thymeleaf template for editing salary
    }

    @PostMapping("/employees/update-salary")
    public String edit(@RequestParam("empId") Long empId,@RequestParam("salary") int salary,RedirectAttributes r){
        r.addFlashAttribute("msg","Salary Updated");
        EmployeeRepo.updateSalaryByEmpId(empId, salary);
        return "redirect:/employees";
    }
    @PostMapping("/employees/delete/{empId}")
    public String delete(@PathVariable("empId") Long empId,RedirectAttributes r){
        r.addFlashAttribute("msg","Employee Deleted");
        UserDetailRepository.deleteCustomerById(empId);
        System.out.println(empId);
        return "redirect:/employees";
    }
}