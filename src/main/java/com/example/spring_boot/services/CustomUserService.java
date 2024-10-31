package com.example.spring_boot.services;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
// import org.springframework.web.bind.annotation.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.spring_boot.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;
import com.example.spring_boot.repository.*;
@Component
public class CustomUserService implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository U;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        
        String redirectUrl = "";
        User user=U.getuserByUsername(username);
        Long userId=user.getId();
        
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

        if (!redirectUrl.isEmpty()) {
            response.sendRedirect(redirectUrl);
        } else {
            throw new IllegalStateException();
        }
    }
}
