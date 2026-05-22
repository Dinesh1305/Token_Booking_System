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
                            jakarta.servlet.http.HttpServletResponse response,
                            RedirectAttributes redirectAttributes) { 
        
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired. Please login again.");
            return "redirect:/"; 
        }

        boolean isValid = otpService.verifyAndDeleteOtp(email, Integer.parseInt(userOtp));

        if (isValid) {
            // 1. Write to Excel
            excelService.addToExcel(food, email);
            
            // 2. Send the Success Email
            otpService.sendBookingSuccessEmail(email, food);
            
            // 3. Log the user out by deleting their cookies
            jakarta.servlet.http.Cookie emailCookie = new jakarta.servlet.http.Cookie("email", null);
            emailCookie.setMaxAge(0); // 0 deletes the cookie
            emailCookie.setPath("/");
            
            jakarta.servlet.http.Cookie passCookie = new jakarta.servlet.http.Cookie("password", null);
            passCookie.setMaxAge(0);
            passCookie.setPath("/");
            
            response.addCookie(emailCookie);
            response.addCookie(passCookie);

            // 4. Redirect to login with a success message
            redirectAttributes.addFlashAttribute("successMsg", "Token booked successfully! Check your email.");
            return "redirect:/"; 
            
        } else {
            // If OTP is wrong, send them back to the OTP page to try again
            redirectAttributes.addAttribute("foodItem", food);
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Invalid OTP. Please try again.");
            return "redirect:/enterOtp"; 
        }
    }
}