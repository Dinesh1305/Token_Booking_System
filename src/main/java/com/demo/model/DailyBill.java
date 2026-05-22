package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dailybill")
public class DailyBill {

    @Id
    private String email;
    
    private Integer count;
    
    private Integer otp;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public Integer getOtp() { return otp; }
    public void setOtp(Integer otp) { this.otp = otp; }
}