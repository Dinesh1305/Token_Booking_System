package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	@RequestMapping("/aa")
	public String home()
	{
		return "index";
	}
	@GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin_login";
    }

    // Handles the form submission from admin_login.html
    @PostMapping("/admin/authenticate")
    public String adminAuthenticate(@RequestParam("email") String email, 
                                    @RequestParam("password") String password) {
        
        if ("admin@123gmail.com".equals(email) && "admin123".equals(password)) {
            return "redirect:/admin/dashboard"; // You will create an admin_dashboard.html next
        } 
        
        return "redirect:/admin/login?error=true";
    }

}
