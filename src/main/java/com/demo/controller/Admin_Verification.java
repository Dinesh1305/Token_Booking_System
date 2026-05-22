package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.Otp;
import com.demo.repo.OtpRepository;

@Controller
public class Admin_Verification {

    @Autowired
    private OtpRepository otpRepository;

    @PostMapping("/admin_verify")
    public String verifyAdminToken(@RequestParam("email") String email, 
                                   @RequestParam("otp") String otpStr, 
                                   Model model) {
        
        boolean isVerified = false;
        
        try {
            Integer otp = Integer.parseInt(otpStr);
            
            // 1. Check if the specific email + otp pair exists in the table
            Otp foundOtp = otpRepository.findByEmailAndOtp(email, otp);
            System.out.println(foundOtp);
            System.out.println(otpRepository.findAll());
            if (foundOtp != null) {
                // 2. If found, delete it (consumes the token)
                otpRepository.deleteByEmailAndOtp(email, otp);
                isVerified = true;
            } else {
                // 3. Not found/invalid
                isVerified = false;
            }
        } catch (NumberFormatException e) {
            isVerified = false;
        }
        
        model.addAttribute("isVerified", isVerified);
        return "verification_result"; // Maps to templates/verification_result.html
    }
}