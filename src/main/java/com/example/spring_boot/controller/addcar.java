package com.example.spring_boot.controller;

import org.springframework.stereotype.Controller;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring_boot.entity.Cars;
import com.example.spring_boot.entity.User;
import com.example.spring_boot.repository.CarsDAO;
import com.example.spring_boot.services.UserService;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
@Controller
public class addcar {
    @GetMapping("/addcar")
    public String addingcar(Model model,HttpSession session){
        Long id=(Long)session.getAttribute("id");
        Cars c=new Cars();
        c.setCustomerId(id);
        System.out.println(c.getCustomerId()+"&&");
        model.addAttribute("car", c);
        return "addcar";
    }
    @PostMapping("/addcar")
    public String gettingcar(@ModelAttribute("car") Cars c,HttpSession session, @RequestParam("proo") MultipartFile proof) throws Exception{
            Long id=(Long)session.getAttribute("id");
            // Check if the file is not empty and convert it to byte[] before setting it
            if (!proof.isEmpty()) {
                c.setProof(proof.getBytes());
            } else {
                throw new IllegalArgumentException("Proof document is required");
            }
            c.setCustomerId(id);
            // // Save car details including proof document
            CarsDAO.addNewCar(c);
            System.out.println(c.getCustomerId()+"^^^^^");
            return "redirect:/info"; // Redirect to another page on success
    }
}
