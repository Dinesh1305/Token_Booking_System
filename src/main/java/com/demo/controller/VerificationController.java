package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class VerificationController {

    
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin_dashboard"; // Maps to src/main/resources/templates/admin_dashboard.html
    }



}