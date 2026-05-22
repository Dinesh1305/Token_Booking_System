package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "otp_history")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Integer otp;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getOtp() { return otp; }
    public void setOtp(Integer otp) { this.otp = otp; }
	@Override
	public String toString() {
		return "Otp [id=" + id + ", email=" + email + ", otp=" + otp + "]";
	}
    
}