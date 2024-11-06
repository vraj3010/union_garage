
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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.repository.CatalogueRepo;
import com.example.spring_boot.repository.EmployeeRepo;
import com.example.spring_boot.repository.InventoryDAO;
import com.example.spring_boot.repository.ManufacturerDAO;
import com.example.spring_boot.repository.UserDetailRepository;
import org.springframework.web.bind.annotation.RequestBody;

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
       
        model.addAttribute("m", new Manufacturer());
        List<String> emailList = new ArrayList<>();
        List<String> mobileList = new ArrayList<>();
        model.addAttribute("mobileList", mobileList);
        model.addAttribute("emailList", emailList);
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
    public String registerUsermanu(@RequestParam("emailList") List<String> e,@RequestParam("mobileList") List<Long> mo,@ModelAttribute("m") Manufacturer m) throws Exception{

        // System.out.println(userDto.getPassword()+"^^^##$$%#$%");
        long id=ManufacturerDAO.addManufacturer(m);

        ManufacturerDAO.addManufacturerMobileNumbers(id, mo);
        ManufacturerDAO.addManufacturerEmails(id, e);
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
    @GetMapping("/manufacturers")
    public String list(Model model){
        List<Manufacturer> m=ManufacturerDAO.getAllManufacturers();
        model.addAttribute("m", m);
        return "listmanu";
    }
    @PostMapping("/manufacturer/phoneno")
    public String getMethodName(Model model,@RequestParam Long manuId) {
        List<Long> p=ManufacturerDAO.getManufacturerMobileNumbers(manuId);
        model.addAttribute("p",p);
        return "manu_phone";
    }
    @PostMapping("/manufacturer/email")
    public String email(Model model,@RequestParam Long manuId){
        List<String> e=ManufacturerDAO.getManufacturerEmails(manuId);
        model.addAttribute("e", e);
        return "manu_email";
    }
    @GetMapping("addmanucar")
    public String addmanucar(Model model){
        return "addmanucar";
    }

    @GetMapping("/addnewcar")
    public String addnewcar(Model model) {
        catalogue c=new catalogue();
        Map<Long,String>m=ManufacturerDAO.getAllManufacturerDetails();
        model.addAttribute("c", c);
        model.addAttribute("m", m);
        return "addnewmodel";
    }
    @PostMapping("/addnewcar")
    public String addn(@ModelAttribute("c") catalogue c,@RequestParam Long manufacturer_id,@RequestParam("engine_type") String engine_type){
        c.setEngineType(engine_type);
        System.out.println(c.getModelName());
        c.setManufacturerId(manufacturer_id);
        ManufacturerDAO.addCatalogueAndInventory(c);
        return "redirect:/addmanucar";
    }

    @GetMapping("/addexistingcar")
    public String addexist(Model model) {
        List<Inventory> i=InventoryDAO.getAllInventory();
        model.addAttribute("i", i);
        return "addexistingcar";
    }
    
    @GetMapping("/addmore")
    public String getMethodName(@RequestParam("inventoryId") Long inventoryId,Model model) {
        model.addAttribute("inventoryId", inventoryId);
        return "addmoremodel";
    }
    
    @PostMapping("/addmore")
    public String postMethodName(@RequestParam("inventoryId") Long inventoryId,
                                  @RequestParam("action") String action, 
                                  @RequestParam("quantity") int quantity,
                                  Model model) {
        // Logic to handle adding more inventory based on action (rent/sell)
        if ("sell".equalsIgnoreCase(action)) {
            // Handle "Add More for Sell"
            // You can update the inventory with the new sell quantity, for example
            InventoryDAO.updateInventoryQuantities(inventoryId,quantity,0);
        } else if ("rent".equalsIgnoreCase(action)) {
          // You can update the inventory with the new sell quantity, for example
          InventoryDAO.updateInventoryQuantities(inventoryId,0,quantity);
        }
        
        // Pass the result back to the view or redirect to another page
        return "redirect:/addexistingcar";  // Assuming this is a confirmation page or updated inventory view
    }
    
}