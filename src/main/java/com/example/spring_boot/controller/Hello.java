package com.example.spring_boot.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Hello
{
    @GetMapping("/admin")
    public String th(){
        return "admin";
    } 
    @GetMapping("/home")
    public String home() {
        return "main";
    }
    @GetMapping("/services")
    public String service(){
        return "services";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/car")
    public String car() {
        return "car";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @GetMapping("/news")
    public String news() {
        return "news";
    }
    @GetMapping("/portfolio")
    public String portfolio() {
        return "portfolio";
    }
}