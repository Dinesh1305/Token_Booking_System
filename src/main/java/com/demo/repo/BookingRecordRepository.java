package com.demo.repo;

import com.demo.model.BookingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {
    
    // For Student Profile
    List<BookingRecord> findByEmailOrderByBookingTimeDesc(String email);

    // Prevents double booking
    @Query("SELECT COUNT(b) FROM BookingRecord b WHERE b.email = :email AND DATE(b.bookingTime) = CURRENT_DATE")
    long countByEmailAndToday(@Param("email") String email);

    // NEW: Fetches ALL bookings for TODAY (For Admin Profile)
    @Query("SELECT b FROM BookingRecord b WHERE DATE(b.bookingTime) = CURRENT_DATE ORDER BY b.bookingTime DESC")
    List<BookingRecord> findAllTodayBookings();
}