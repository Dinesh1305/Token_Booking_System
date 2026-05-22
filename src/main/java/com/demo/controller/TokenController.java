package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.service.OtpService;
import com.demo.service.ExcelService;

@Controller
public class TokenController {

    @Autowired
    private OtpService otpService; 

    @Autowired
    private ExcelService excelService; 

    @GetMapping("/bookToken")
    public String generateOtp(@RequestParam("foodItem") String food,
                              @CookieValue(value = "email", required = false) String email,
                              RedirectAttributes redirectAttributes) {
        
        if (email == null) {
            return "redirect:/?error=true";
        }

        otpService.generateAndStoreOtp(email);
        redirectAttributes.addAttribute("foodItem", food);
        return "redirect:/enterOtp";
    }

    @GetMapping("/enterOtp")
    public String showOtpPage(@RequestParam("foodItem") String food, Model model) {
        model.addAttribute("foodItem", food);
        return "otp"; 
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("otp") String userOtp,
                            @RequestParam("foodItem") String food,
                            @CookieValue(value = "email", required = false) String email,
                            RedirectAttributes redirectAttributes) { // Added RedirectAttributes
        
        if (email == null) {
            // Use addFlashAttribute to safely pass messages across a redirect
            redirectAttributes.addFlashAttribute("bookingMessage", "Session expired. Please login again.");
            return "redirect:/book"; 
        }

        boolean isValid = otpService.verifyAndDeleteOtp(email, Integer.parseInt(userOtp));

        if (isValid) {
            excelService.addToExcel(food, email);
            redirectAttributes.addFlashAttribute("bookingMessage", "✅ TOKEN ADDED SUCCESSFULLY FOR " + food.toUpperCase());
        } else {
            redirectAttributes.addFlashAttribute("bookingMessage", "❌ Invalid OTP. Please try again.");
        }

        // FIXED: Use redirect instead of forward to prevent the 405 POST error
        return "redirect:/book"; 
    }
}