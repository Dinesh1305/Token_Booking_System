package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Calendar;

@Controller
public class TokenBookingController {

    @GetMapping("/book")
    public String showBookingPage(Model model) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String foodItem;
        String foodImage;

        switch (dayOfWeek) {
            case Calendar.TUESDAY:
                foodItem = "Chicken Briyani";
                foodImage = "/image/brr.jpg";
                break;
            case Calendar.MONDAY:
                foodItem = "Egg Gravy";
                foodImage = "/image/egggravy.jpg";
                break;
            case Calendar.WEDNESDAY:
                foodItem = "Chicken Gravy";
                foodImage = "/image/chickengravy.jpg";
                break;
            case Calendar.THURSDAY:
                foodItem = "Cauliflower Curry";
                foodImage = "/image/cauliflower.jpeg";
                break;
            case Calendar.FRIDAY:
                foodItem = "Chicken 65";
                foodImage = "/image/chicken65.webp";
                break;
            case Calendar.SATURDAY:
                foodItem = "Boiled Egg";
                foodImage = "/image/boiledegg.jpg";
                break;
            case Calendar.SUNDAY:
                foodItem = "Bread Omelet";
                foodImage = "/image/breadomlet.jpg";
                break;
            default:
                foodItem = "Delicious Meal";
                foodImage = "/image/Default.jpg";
                break;
        }

        model.addAttribute("foodItem", foodItem);
        model.addAttribute("foodImage", foodImage);
        
        return "book"; // Maps to src/main/resources/templates/book.html
    }
}