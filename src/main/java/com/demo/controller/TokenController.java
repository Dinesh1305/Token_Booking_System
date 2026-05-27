package com.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.model.BookingRecord;
import com.demo.model.Student;
import com.demo.repo.BookingRecordRepository;
import com.demo.repo.StudentRepo;
import com.demo.service.ExcelService;
import com.demo.service.OtpService;

@Controller
public class TokenController {

    @Autowired private OtpService otpService; 
    @Autowired private ExcelService excelService; 
    @Autowired private StudentRepo studentRepository;
    @Autowired private BookingRecordRepository bookingRecordRepository;

    @GetMapping("/bookToken")
    public String generateOtp(@RequestParam("foodItem") String food,
                              @CookieValue(value = "email", required = false) String email,
                              RedirectAttributes redirectAttributes) {
        
        if (email == null) {
            return "redirect:/?error=true";
        }

        // --- NEW: ONE TOKEN PER DAY CHECK ---
        long alreadyBookedToday = bookingRecordRepository.countByEmailAndToday(email);
        
        if (alreadyBookedToday > 0) {
            // Redirect back to the booking page with a warning
            redirectAttributes.addFlashAttribute("errorMsg", "⚠️ You have already booked a meal token for today!");
            return "redirect:/book";
        }
        // ------------------------------------

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
            
            // 2. Save History to Database
            BookingRecord record = new BookingRecord();
            record.setEmail(email);
            record.setFoodItem(food);
            record.setBookingTime(new Date());
            
            // Determine cost
            int cost = 0;
            switch (food) {
                case "Chicken Briyani": cost = 120; break;
                case "Egg Gravy": cost = 100; break;
                case "Chicken Gravy": cost = 100; break;
                case "Cauliflower Curry": cost = 40; break;
                case "Chicken 65": cost = 90; break;
                case "Bread Omelet": cost = 30; break;
                case "Boiled Egg": cost = 20; break;
            }
            record.setCost(cost);
            bookingRecordRepository.save(record);

            // 3. Send Success Email
            otpService.sendBookingSuccessEmail(email, food);
            
            // 4. Redirect with success message (Keep cookies so they can view profile!)
            redirectAttributes.addFlashAttribute("bookingMessage", "Token booked successfully! Check your email.");
            return "redirect:/book"; 
            
        } else {
            redirectAttributes.addAttribute("foodItem", food);
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Invalid OTP. Please try again.");
            return "redirect:/enterOtp"; 
        }
    }

    // THE NEW PROFILE ENDPOINT THAT FIXES YOUR 405 ERROR
    @GetMapping("/profile")
    public String showProfile(@CookieValue(value = "email", required = false) String email, Model model) {
        if (email == null) {
            return "redirect:/"; // Go to login if cookie is missing
        }
        
        // Fetch student details
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            return "redirect:/";
        }
        
        // Fetch booking history
        List<BookingRecord> bookings = bookingRecordRepository.findByEmailOrderByBookingTimeDesc(email);
        
        model.addAttribute("student", student);
        model.addAttribute("bookings", bookings);
        
        return "profile";
    }
}