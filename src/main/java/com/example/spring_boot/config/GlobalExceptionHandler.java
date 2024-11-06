// package com.example.spring_boot.config;

// import org.springframework.dao.DataAccessException;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     // Handle SQL-related errors and logout
//     @ExceptionHandler(DataAccessException.class)
//     public String handleSQLException(DataAccessException ex, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
//         // Perform logout
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         if (auth != null) {
//             new SecurityContextLogoutHandler().logout(request, response, auth);
//         }
        
//         // Set a custom error message
//         String errorMessage = "A database error occurred: " + ex.getMessage();
//         redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        
//         // Redirect to the error page after logging out
//         return "redirect:/error";
//     }

//     // General exception handler with logout
//     @ExceptionHandler(Exception.class)
// public String handleException(Exception ex, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
//     // Check if the exception is a NoResourceFoundException
//     if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
//         // Set a simpler error message without logging out
//         redirectAttributes.addFlashAttribute("errorMessage", "Resource not found.");
//         return "redirect:/error";
//     }

//     // For other exceptions, perform logout
//     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     if (auth != null) {
//         new SecurityContextLogoutHandler().logout(request, response, auth);
//     }

//     String errorMessage = "An unexpected error occurred: " + ex.getMessage();
//     redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

//     return "redirect:/error";
// }
// }
