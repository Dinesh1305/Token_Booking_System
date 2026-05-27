package com.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.DailyBill;

import jakarta.transaction.Transactional;

@Repository
public interface DailyBillRepository extends JpaRepository<DailyBill, String> {
    
    // Automatically generates SQL: SELECT * FROM dailybill WHERE ott = ?
    DailyBill findByOtp(Integer otp);
    
    List<DailyBill> findByEmail(String email);
    
    // Automatically generates SQL: DELETE FROM dailybill WHERE ott = ?
    @Transactional
    void deleteByOtp(Integer otp);
}