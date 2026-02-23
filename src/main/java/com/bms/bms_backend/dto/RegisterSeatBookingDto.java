package com.bms.bms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterSeatBookingDto {

    ArrayList<String> seatIds;
    String paymentId;
    String paymentSource;


    public ArrayList<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(ArrayList<String> seatIds) {
        this.seatIds = seatIds;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }
}
