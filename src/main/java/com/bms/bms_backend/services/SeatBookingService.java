package com.bms.bms_backend.services;

import com.bms.bms_backend.models.SeatBooking;
import com.bms.bms_backend.repositories.SeatBookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SeatBookingService {

    SeatBookingRepository seatBookingRepository;

    @Autowired
    public SeatBookingService(SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }


    public boolean isSeatAvailableForShow(String seatId, UUID  showId) {

      //  log.info(String.format("select * from seatbookings where show_id =%s  and seat_id=%s", showId.toString(), seatId));
        SeatBooking booking = seatBookingRepository.getSeatBookingBySeatIdAndShowId(seatId, showId);
        if(booking == null){
            return true;
        }
        return false;
    }


    // method for generate ticket Id
    public String generateTicketId() {
        return "Ticket-" + seatBookingRepository.count();
    }


    // method for save or update seatbooking
    public SeatBooking saveOrUpdate(SeatBooking seatBooking) {
        return seatBookingRepository.save(seatBooking);
    }
}
