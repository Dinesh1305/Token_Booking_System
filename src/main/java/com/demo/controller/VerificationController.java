package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.DailyBill;
import com.demo.repo.DailyBillRepository;

@Controller
public class VerificationController {

    @Autowired
    private DailyBillRepository dailyBillRepository;

    // --- ADD THIS NEW METHOD ---
    // This displays the admin form when you visit http://localhost:9002/admin/dashboard
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin_dashboard"; // Maps to src/main/resources/templates/admin_dashboard.html
    }

    // This remains the same as before
  

    @Autowired
    private com.demo.repo.OtpRepository otpRepository; // Inject new repo

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam("otp") String otpStr, Model model) {
        boolean isVerified = false;
        try {
            Integer otp = Integer.parseInt(otpStr);
            DailyBill bill = dailyBillRepository.findByOtt(otp);
            
            if (bill != null) {
                // 1. Save to new 'otp_history' table
                com.demo.model.Otp history = new com.demo.model.Otp();
                history.setEmail(bill.getEmail());
                history.setOtp(otp);
                otpRepository.save(history);
                
                // 2. Delete from original
                dailyBillRepository.deleteByOtt(otp);
                isVerified = true;
            }
        } catch (NumberFormatException e) {
            isVerified = false;
        }
        model.addAttribute("isVerified", isVerified);
        return "verification_result"; 
    }
}