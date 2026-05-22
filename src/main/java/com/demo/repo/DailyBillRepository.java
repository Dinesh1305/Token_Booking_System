package com.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.demo.model.DailyBill;

@Repository
public interface DailyBillRepository extends JpaRepository<DailyBill, String> {
    
    // Automatically generates SQL: SELECT * FROM dailybill WHERE ott = ?
    DailyBill findByOtt(Integer ott);
    
    // Automatically generates SQL: DELETE FROM dailybill WHERE ott = ?
    @Transactional
    void deleteByOtt(Integer ott);
}