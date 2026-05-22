package com.demo.repo;

// In src/main/java/com/demo/repo/OtpRepository.java

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Otp;

import jakarta.transaction.Transactional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    
    // Find record by email and OTP
    Otp findByEmailAndOtp(String email, Integer otp);

    // Delete record by email and OTP
    @Transactional
    void deleteByEmailAndOtp(String email, Integer otp);
}