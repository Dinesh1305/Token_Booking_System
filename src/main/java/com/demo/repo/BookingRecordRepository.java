package com.demo.repo;

import com.demo.model.BookingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {
    // Fetches history for a specific student, newest first
    List<BookingRecord> findByEmailOrderByBookingTimeDesc(String email);
}