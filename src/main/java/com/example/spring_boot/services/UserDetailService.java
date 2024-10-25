// package com.example.spring_boot.services;

// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import com.example.spring_boot.entity.UserDetails;
// import com.example.spring_boot.repository.UserDetailRepository;
// @Service
// public class UserDetailService {
    
//     @Autowired
//     private UserDetailRepository UserDetailsRepository;

//     public UserDetails saveUserDetails(UserDetails userDetails) {
//         return UserDetailsRepository.save(userDetails); // Save the UserDetails entity
//     }

//     public UserDetails GetUserbyId(Long id){
//         return UserDetailsRepository.findById(id).orElse(null);
//     }
// }
