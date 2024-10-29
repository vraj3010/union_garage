package com.example.spring_boot.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.spring_boot.repository.*;
import com.example.spring_boot.entity.User;
import java.util.List;
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.getuserByUsername(username);
    System.out.println(user);
    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }
    
    // Convert Role to SimpleGrantedAuthority
    List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))  // Ensure role.getName() returns a String
            .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        authorities
    );
}
}
