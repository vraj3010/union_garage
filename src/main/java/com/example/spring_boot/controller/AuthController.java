package com.example.spring_boot.controller;

import java.util.Collection;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.example.spring_boot.entity.User;
import com.example.spring_boot.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
@Controller
public class AuthController {
    
    
                                   
    @GetMapping("/login")       
    public String login(Model model) {
          // Check if the user is already authenticated
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() 
            && !(authentication.getPrincipal() instanceof String)) { // checking if user is not "anonymousUser"
        // Redirect to profile page if already logged in
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl="";
         for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority()+"****");
            if (authority.getAuthority().equals("USER")) {
                redirectUrl = "/info";
                break;
            } else if (authority.getAuthority().equals("EMPLOYEE")) {
                redirectUrl = "/employee";
                break;
            }
            else if (authority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/admin";
                break;
            }
            else if (authority.getAuthority().equals("RENTER")) {
                redirectUrl = "/renter";
                break;
            }
        }
        return "redirect:"+redirectUrl;
    }
        
        if (model.containsAttribute("error")) {
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
