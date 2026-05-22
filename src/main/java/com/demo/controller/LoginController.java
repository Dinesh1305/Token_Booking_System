package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.Student;
import com.demo.repo.StudentRepo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private StudentRepo studentRepository;

    // 1. Make the Login Page the very first screen
    @GetMapping("/")
    public String showLoginPage() {
        return "login"; // Maps to src/main/resources/templates/login.html
    }

    // 2. Handle the login submission
    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpServletResponse response) {
        
        Student student = studentRepository.findByEmail(email);

        // Verify credentials
        if (student != null && student.getPassword().equals(password)) {
            
            // Login successful: Store cookies
            Cookie emailCookie = new Cookie("email", email);
            Cookie passCookie = new Cookie("password", password); 
            response.addCookie(emailCookie);
            response.addCookie(passCookie);

            // Redirect directly to the booking page
            return "redirect:/book"; 
            
        } else {
            // Login failed: Redirect back to login page with an error flag
            return "redirect:/?error=true";
        }
    }
}