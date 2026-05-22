package com.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
}