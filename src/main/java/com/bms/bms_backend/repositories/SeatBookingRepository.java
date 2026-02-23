package com.bms.bms_backend.repositories;

import com.bms.bms_backend.models.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatBookingRepository  extends JpaRepository<SeatBooking, UUID> {

    @Query(value = "select * from seat_bookings where show_id =:showId  and seat_id=:seatId", nativeQuery = true)
    SeatBooking getSeatBookingBySeatIdAndShowId(String seatId, UUID showId);
}
