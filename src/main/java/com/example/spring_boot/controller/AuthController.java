package com.example.spring_boot.controller;

import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.example.spring_boot.entity.User;
import com.example.spring_boot.services.UserService;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {
    
    
                                   
    @GetMapping("/login")       
    public String login(Model model) {
        System.out.println("Hello From Shah");
        if (model.containsAttribute("error")) {
            System.out.println("HIi");
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        
        return "login";
    }

    // @PostMapping("/login")
    // public String logi(){
    //     System.out.println("you fool");
    //     return "redirect:/home";
    // }
    

//     @PostMapping("/login")
//     public String login(User user) {
//         System.out.println("hello from braj shah");
//         User oauthUser = userService.login(user.getUsername(), user.getPassword());
        
//         System.out.println(oauthUser+"^^^^");
//         if(Objects.nonNull(oauthUser)) 
//         {   
//             return "redirect:/home";            
//         } else {
//             return "redirect:/";
//         }
// }
    
    @PostMapping("/logout")
    public String logoutDo(HttpServletRequest request,HttpServletResponse response)
    {
        return "redirect:/login";
    }
}
