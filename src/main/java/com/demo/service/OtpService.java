package com.demo.service;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.model.DailyBill;
import com.demo.repo.DailyBillRepository;

@Service
public class OtpService {

    @Autowired
    private DailyBillRepository dailyBillRepository;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    // Generate OTP + Send Mail + Store DB
    public void generateAndStoreOtp(String email) {

        System.out.println("Generate OTP Started");

        Random r = new Random();

        int otpNo = r.nextInt(1000, 9999);

        System.out.println("Generated OTP : " + otpNo);

        // Send Mail
        sendEmail(email, otpNo);

        // Save in DB
        DailyBill bill = new DailyBill();

        bill.setEmail(email);

        bill.setCount(1);

        bill.setOtt(otpNo);

        dailyBillRepository.save(bill);

        System.out.println("OTP Saved In DB");
    }

    // Send Email
    private void sendEmail(String to, int otpNo) {

        System.out.println("Mail Method Started");

        System.out.println("Receiver Mail : " + to);

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
            props,
            new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(
                        emailUsername,
                        emailPassword
                    );
                }
            }
        );

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(emailUsername));

            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to)
            );

            message.setSubject("Your Mess Token OTP");

            message.setText(
                "Your OTP is : " + otpNo
            );

            System.out.println("Before Sending Mail");

            Transport.send(message);

            System.out.println("Mail Sent Successfully");

        } catch (Exception e) {

            System.out.println("Mail Sending Failed");

            e.printStackTrace();
        }
    }

    // Verify OTP
    public boolean verifyAndDeleteOtp(String email, int userOtp) {

        DailyBill bill =
            dailyBillRepository.findById(email).orElse(null);

        if (bill != null && bill.getOtt() == userOtp) {

            dailyBillRepository.delete(bill);

            System.out.println("OTP Verified");

            return true;
        }

        System.out.println("Invalid OTP");

        return false;
    }
 // Add this method to your existing OtpService.java
    public void sendBookingSuccessEmail(String to, String foodItem) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailUsername, emailPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Token Booked Successfully!");
            
            // Set the success text
            message.setText("Congratulations! Your token for " + foodItem + " has been booked successfully.\n\nEnjoy your meal!");

            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send success email: " + e.getMessage());
        }
    }
}