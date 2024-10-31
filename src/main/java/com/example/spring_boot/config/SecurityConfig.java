package com.example.spring_boot.config;

import com.example.spring_boot.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.spring_boot.services.CustomUserService;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // causing infinite loop since CustomUserDetailsService implements UserDetailsService
    @Autowired
    private CustomUserService customLoginSuccessHandler;
    // () {
    //     return new CustomUserDetailsService();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    
    {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/error","/login", "/logout","/resources/**","/home","/css/**","/fonts/**","/images/**","/js/**","/register","/services/**","/userdetails","/employee/").permitAll()
                    .requestMatchers("/info/**","/buy").hasAuthority("USER")
                    .requestMatchers("/yy").hasAuthority("EMPLOYEE")
                    .requestMatchers("/admin","addemp").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .successHandler(customLoginSuccessHandler)
                    .permitAll()
            )
            .logout(logout ->
                logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            );
        System.out.println("Hiii");
        return http.build();
    }

    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    //     return (web) -> web.ignoring().requestMatchers("/resources/");
    // }
}