package com.example.spring_boot.services;

import com.example.spring_boot.repository.RoleRepository;
import com.example.spring_boot.repository.UserRepository;
import com.example.spring_boot.entity.User;
// import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.entity.Role;
import com.example.spring_boot.entity.UserRegistrationDto;
import java.util.Optional;
// import org.apache.el.stream.Optional;
// import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
   public UserService(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository=roleRepository;
}
// public User login(String username, String password) throws UsernameNotFoundException {
//     System.out.println(username);
//     User user = userRepository.findByUsernameAndPassword(username, password);
//     if (user == null) {
//         System.out.println("hii");
//         throw new UsernameNotFoundException("User not found with username: " + username);
//     }
//     return user;


// }

public User findUserById(Long id) throws Exception {
    return userRepository.getUserById(id); // Fetch user by ID
}
public User registerUser(UserRegistrationDto userDto) throws Exception {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        // Assign default role USER
        System.out.println(userDto.getRole()+"######################################################");
        Role userRole = roleRepository.findByRoleName(userDto.getRole());
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.saveUser(user);
        user.setId(userRepository.getIdByUsername(user.getUsername()));
        return user;
    }
}