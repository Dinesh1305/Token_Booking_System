package com.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.model.BookingRecord;

@Repository
public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {
    // Fetches history for a specific student, newest first
    List<BookingRecord> findByEmailOrderByBookingTimeDesc(String email);
    
    
    @Query("SELECT COUNT(b) FROM BookingRecord b WHERE b.email = :email AND DATE(b.bookingTime) = CURRENT_DATE")
    long countByEmailAndToday(@Param("email") String email);
}